package dashboard;

import javax.swing.*;
import javax.swing.border.Border;

import dashboard.i18n.Calendar_i18n;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.Calendar;

/**
 * Utility class for handling appointments in the Calendar application.
 * Contains methods for creating and managing appointment UI elements.
 */
public class Appuntamenti {
    
    // Constants for colors
    private static final Color TODAY_HIGHLIGHT_COLOR = Calendar_i18n.getColor("color.today_highlight_color");
    private static final Color EMPTY_DAY_COLOR_BG = Calendar_i18n.getColor("color.empty_day_color_bg");
    private static final Color DAY_SELECTED_COLOR_BG = Calendar_i18n.getColor("color.day_selected_color_bg");
    
    /**
     * Creates a border for appointment panels
     * 
     * @param color The base color for the appointment
     * @param isHovered Whether the appointment is being hovered over
     * @return A styled border for the appointment
     */
    public static Border createAppointmentBorder(Color color, boolean isHovered) {
        Color borderColor = isHovered ? color.darker().darker() : color.darker();
        return BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, borderColor),
            BorderFactory.createEmptyBorder(2, 3, 2, 3)
        );
    }
    
    /**
     * Creates a standard border for day panels
     */
    public static Border createStandardDayBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
    }
    
    /**
     * Creates a highlighted border for the current day.
     * Used to highlight today's date.
     * @return A compound border with the today highlight color
     */
    public static Border createTodayBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TODAY_HIGHLIGHT_COLOR),
            BorderFactory.createEmptyBorder(3, 3, 3, 3)
        );
    }
    
    /**
     * Creates a selected day border
     */
    public static Border createSelectedDayBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DAY_SELECTED_COLOR_BG, 2),
            BorderFactory.createEmptyBorder(3, 3, 3, 3)
        );
    }
    
    /**
     * Adds an appointment to a day panel
     * 
     * @param dayPanel The panel representing a day
     * @param time The time of the appointment
     * @param title The title of the appointment
     * @param color The color for the appointment
     * @param calendar The current calendar instance
     * @param appointmentDetails The text area to display appointment details
     */
    public static void addAppointment(JPanel dayPanel, String time, String title, Color color, 
                                    Calendar calendar, JTextArea appointmentDetails) {
        JPanel appointmentPanel = new JPanel();
        appointmentPanel.setLayout(new BoxLayout(appointmentPanel, BoxLayout.Y_AXIS));
        appointmentPanel.setBackground(color);
        appointmentPanel.setBorder(createAppointmentBorder(color, false));
        appointmentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 10));
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        appointmentPanel.add(timeLabel);
        appointmentPanel.add(titleLabel);
        
        // Create a MouseListener for all appointment components
        MouseAdapter appointmentListener = createAppointmentMouseListener(
            appointmentPanel, dayPanel, color, calendar, appointmentDetails, time, title);
        
        // Add the listener to the appointment panel
        appointmentPanel.addMouseListener(appointmentListener);
        
        // Add the same listener to the labels to ensure clicks are intercepted
        timeLabel.addMouseListener(appointmentListener);
        titleLabel.addMouseListener(appointmentListener);
        
        timeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        titleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        dayPanel.add(appointmentPanel);
        dayPanel.add(Box.createRigidArea(new Dimension(0, 2)));
    }
    
    /**
     * Creates a mouse listener for appointment interaction
     */
    private static MouseAdapter createAppointmentMouseListener(
            JPanel appointmentPanel, JPanel dayPanel, Color color, 
            Calendar calendar, JTextArea appointmentDetails, String time, String title) {
        
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Reset all day borders to their original state
                for (Component comp : appointmentPanel.getParent().getParent().getComponents()) {
                    if (comp instanceof JPanel && comp != dayPanel) {
                        if (comp.getBackground().equals(EMPTY_DAY_COLOR_BG)) {
                            // It's an empty day, keep its border
                            continue;
                        }
                        
                        // Restore the original border for non-selected days
                        Calendar today = Calendar.getInstance();
                        boolean isToday = false;
                        
                        // Check if the component is the current day
                        for (Component child : ((JPanel) comp).getComponents()) {
                            if (child instanceof JLabel) {
                                try {
                                    int day = Integer.parseInt(((JLabel) child).getText());
                                    isToday = calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                                            calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                                            day == today.get(Calendar.DAY_OF_MONTH);
                                    break;
                                } catch (NumberFormatException ex) {
                                    // Not a number, ignore
                                }
                            }
                        }
                        
                        if (isToday) {
                            // Keep the highlight for the current day
                            ((JPanel) comp).setBorder(createTodayBorder());
                        } else {
                            // Restore the standard border
                            ((JPanel) comp).setBorder(createStandardDayBorder());
                        }
                    }
                }
                
                // Highlight the selected day with a special border
                dayPanel.setBorder(createSelectedDayBorder());
                
                // Update appointment details
                String day = ((JLabel)dayPanel.getComponent(0)).getText();
                String month = getMonthName(calendar.get(Calendar.MONTH));
                String year = String.valueOf(calendar.get(Calendar.YEAR));
                
                appointmentDetails.setText(
                    MessageFormat.format(Calendar_i18n.getString("details.appointment"), title) + "\n" +
                    MessageFormat.format(Calendar_i18n.getString("details.day"), day, month, year) + "\n" +
                    MessageFormat.format(Calendar_i18n.getString("details.time"), time) + "\n" +
                    MessageFormat.format(Calendar_i18n.getString("details.description"), title)
                );
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                appointmentPanel.setBorder(createAppointmentBorder(color, true));
                appointmentPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                appointmentPanel.setBorder(createAppointmentBorder(color, false));
            }
        };
    }
    
    /**
     * Gets the localized month name
     */
    private static String getMonthName(int month) {
        String key = "month." + getMonthKey(month);
        return Calendar_i18n.getString(key);
    }

    /**
     * Gets the month key for localization
     */
    private static String getMonthKey(int month) {
        String[] monthKeys = {
            "january", "february", "march", "april", "may", "june",
            "july", "august", "september", "october", "november", "december"
        };
        return monthKeys[month];
    }
    
    /**
     * Adds an empty day to the appointment panel
     */
    public static void addEmptyDay(JPanel appointmentPanel) {
        JPanel panel = new JPanel();
        panel.setBackground(EMPTY_DAY_COLOR_BG);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        appointmentPanel.add(panel);
    }

    /**
     * Restituisce una stringa con gli appuntamenti per un determinato giorno
     * @param year Anno
     * @param month Mese (0-11)
     * @param day Giorno del mese
     * @return Stringa con gli appuntamenti, vuota se non ce ne sono
     */
    public static String getAppointmentsForDay(int year, int month, int day) {
        StringBuilder appointments = new StringBuilder();
        
        // Verifica se ci sono appuntamenti per questo giorno
        // Questo è un esempio, dovresti adattarlo alla tua logica di gestione degli appuntamenti
        if (day % 3 == 0) {
            appointments.append("10:00 - Riunione\n");
        }
        if (day % 5 == 0) {
            appointments.append("14:30 - Pranzo di lavoro\n");
        }
        if (day % 7 == 0) {
            appointments.append("16:00 - Conferenza\n");
        }
        
        return appointments.toString();
    }
}