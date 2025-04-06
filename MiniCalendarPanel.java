package dashboard;

import javax.swing.*;

import dashboard.resources.CalendarResources;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;

public class MiniCalendarPanel extends JPanel {
	private Calendar calendar;
    private JLabel monthLabel;
    private JTextArea appointmentDetails;
    private Runnable appointmentPanelUpdater;
    
    // Add color constants
    private static final Color DAY_HOVER_COLOR_BG = CalendarResources.getColor("color.day_hover_color_bg"); 
    private static final Color DAY_SELECTED_COLOR_BG = CalendarResources.getColor("color.day_selected_color_bg"); 
    
    public MiniCalendarPanel(Calendar calendar, JLabel monthLabel) {
        this.calendar = calendar;
        this.monthLabel = monthLabel;
        initialize();
    }
    
    public void setAppointmentDetails(JTextArea appointmentDetails) {
        this.appointmentDetails = appointmentDetails;
        System.out.println("Appointment details text area set: " + (appointmentDetails != null));
    }
    
    public void setAppointmentPanelUpdater(Runnable updater) {
        this.appointmentPanelUpdater = updater;
    }
    
    private void initialize() {
        setLayout(new GridLayout(7, 7, 1, 1));
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        setMinimumSize(new Dimension(224, 224));
        setPreferredSize(new Dimension(224, 224));
        setMaximumSize(new Dimension(224, 224));
    }
    
    public void updateDisplay() {
        this.removeAll();
        
        // Aggiorna l'etichetta del mese
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        
        // Usa l'enum Month per ottenere il nome del mese
        String monthName = Month.fromCalendarMonth(month).getDisplayName();
        monthLabel.setText(monthName + " " + year);
        
        // Use populateDaysPanel to add day headers
        populateDaysPanel();
        
        // Get first day of month and total days
        Calendar temp = (Calendar) calendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int firstDay = temp.get(Calendar.DAY_OF_WEEK);
        // Adjust for Monday as first day of week
        firstDay = firstDay == Calendar.SUNDAY ? 6 : firstDay - 2;
        int daysInMonth = temp.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        // Get current day for highlighting
        Calendar today = Calendar.getInstance();
        
        // Add empty labels before first day
        for (int i = 0; i < firstDay; i++) {
            this.add(new JLabel(""));
        }
        
        // Add day buttons
        for (int day = 1; day <= daysInMonth; day++) {
            final int currentDay = day;
            JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.PLAIN, 9));
            dayLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            
            // Calculate day of week
            temp.set(Calendar.DAY_OF_MONTH, day);
            int dayOfWeek = temp.get(Calendar.DAY_OF_WEEK);
            boolean isWeekend = (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
            
            // Highlight current day
            if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                day == today.get(Calendar.DAY_OF_MONTH)) {
                dayLabel.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 1, true));
                dayLabel.setBackground(DAY_SELECTED_COLOR_BG);
                dayLabel.setOpaque(true);
            }
            
            // Set weekend days in red
            if (isWeekend) {
                dayLabel.setForeground(new Color(220, 20, 60));
            }
            
            // Add click listener to select day
            dayLabel.addMouseListener(new MouseAdapter() {
                // Nel metodo mouseClicked della classe MiniCalendarPanel
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Mini calendar day clicked: " + currentDay);
                    
                    // Update the calendar to the selected day
                    calendar.set(Calendar.DAY_OF_MONTH, currentDay);
                    System.out.println("Calendar updated to: " + calendar.get(Calendar.YEAR) + "-" + 
                                    (calendar.get(Calendar.MONTH) + 1) + "-" + 
                                    calendar.get(Calendar.DAY_OF_MONTH));
                    
                    // Log the calendar selection
                    Debug.logCalendarSelection("Day selected in mini calendar", calendar);
                    
                    // Update the mini calendar display
                    updateDisplay();
                    
                    // Run the appointment panel updater
                    System.out.println("Running appointment panel updater");
                    if (appointmentPanelUpdater != null) {
                        appointmentPanelUpdater.run();
                        System.out.println("Appointment panel updater completed");
                    }
                    
                    // Se siamo in modalità compatta, mostra la finestra di dialogo degli appuntamenti
                    Container parent = getParent();
                    while (parent != null && !(parent instanceof Calendario)) {
                        parent = parent.getParent();
                    }
                    
                    if (parent instanceof Calendario) {
                        Calendario parentCalendar = (Calendario) parent;
                        if (parentCalendar.isCompactMode()) {
                            parentCalendar.showAppointmentsInDialog();
                        }
                    }
                    
                    // Update the appointment details
                    System.out.println("Updating appointment details text area");
                    if (appointmentDetails != null) {
                        appointmentDetails.setText(CalendarResources.getString("label.select_appointment"));
                        
                        // Get the parent container and revalidate/repaint
                        Container parent2 = appointmentDetails.getParent();
                        if (parent2 != null) {
                            parent2.revalidate();
                            parent2.repaint();
                            System.out.println("Appointment details parent revalidated and repainted");
                        }
                    }
                    
                    // Revalidate and repaint the mini calendar panel
                    revalidate();
                    repaint();
                    System.out.println("Mini calendar panel revalidated and repainted");
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    // Get the day label that triggered the event
                    if (e.getSource() instanceof JLabel) {
                        JLabel dayLabel = (JLabel) e.getSource();
                        String dayText = dayLabel.getText();
                        
                        // Only change cursor for actual day numbers (not empty cells)
                        if (!dayText.isEmpty()) {
                            // Set hand cursor to indicate clickable element
                            dayLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            
                            // Highlight the day
                            dayLabel.setBackground(DAY_HOVER_COLOR_BG);
                            dayLabel.setOpaque(true);
                        }
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    // Get the day label that triggered the event
                    if (e.getSource() instanceof JLabel) {
                        JLabel dayLabel = (JLabel) e.getSource();
                        String dayText = dayLabel.getText();
                        
                        // Only process for actual day numbers
                        if (!dayText.isEmpty()) {
                            // Reset cursor
                            dayLabel.setCursor(Cursor.getDefaultCursor());
                            
                            // Reset background color
                            int day = Integer.parseInt(dayText);
                            
                            // Get current day for highlighting
                            Calendar today = Calendar.getInstance();
                            boolean isToday = (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                                calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                                day == today.get(Calendar.DAY_OF_MONTH));
                            
                            // Check if this is the selected day
                            boolean isSelectedDay = (day == calendar.get(Calendar.DAY_OF_MONTH));
                            
                            if (isToday) {
                                // Keep today's highlight
                                dayLabel.setBackground(new Color(229, 243, 255));
                                dayLabel.setOpaque(true);
                            } else if (isSelectedDay) {
                                dayLabel.setBackground(DAY_SELECTED_COLOR_BG);
                                dayLabel.setOpaque(true);
                            } else {
                                dayLabel.setBackground(null);
                                dayLabel.setOpaque(false);
                            }
                        }
                    }
                }
            });
            
            this.add(dayLabel);
        }
        
        // Add remaining empty cells to complete the grid
        int remainingCells = 42 - (firstDay + daysInMonth); // 42 = 6 rows × 7 columns
        for (int i = 0; i < remainingCells; i++) {
            this.add(new JLabel(""));
        }
        
        // Refresh the panel
        this.revalidate();
        this.repaint();
    }

    // Quando devi visualizzare i giorni nel mini calendario
    private void populateDaysPanel() {
        // Usa l'enum DayOfWeek per le intestazioni dei giorni
        for (DayOfWeek day : DayOfWeek.values()) {
            JLabel dayLabel = new JLabel(day.getDisplayName().substring(0, 3), SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 9));
            if (day.isWeekend()) {
                dayLabel.setForeground(new Color(220, 20, 60));
            }
            this.add(dayLabel);
        }
    }
}