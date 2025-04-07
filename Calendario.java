package dashboard;

import javax.swing.*;
import javax.swing.border.Border;

import dashboard.i18n.Calendar_i18n;

import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;
import java.util.Calendar;

/**
 * Applicazione calendario per la visualizzazione e gestione degli appuntamenti.
 * Fornisce un'interfaccia grafica con un mini calendario per la navigazione
 * e un pannello principale per visualizzare gli appuntamenti del mese.
 */
public class Calendario extends JFrame {
    // Calendar components
    private Calendar calendar;
    private JLabel monthLabel;
    private JButton prevButton;
    private JButton nextButton;
    private JButton todayButton;
    
    // Left panel components
    private MiniCalendarPanel miniCalendarPanel;
    private JPanel navigationPanel;
    private JPanel controlsPanel;
    
    // Checkbox filters
    private JCheckBox meetingsCheckbox;
    private JCheckBox lunchCheckbox;
    private JCheckBox conferenceCheckbox;
    
    // Main panel components
    private JPanel mainPanel;
    private JPanel appointmentPanel;
    
    /**
     * Text area that displays details about the selected appointment.
     */
    private JTextArea appointmentDetails;
    
    // Costanti per i colori
    private static final Color WEEKEND_COLOR_BG = Calendar_i18n.getColor("color.weekend_color_bg");
    private static final Color NAVIGATION_BUTTON_COLOR_BG = Calendar_i18n.getColor("color.navigation_button_color_bg");
    private static final Color NAVIGATION_BUTTON_COLOR = Calendar_i18n.getColor("color.navigation_button_color");
    private static final Color NAVIGATION_BUTTON_COLOR_BD = Calendar_i18n.getColor("color.navigation_button_color_bd");
    private static final Color NAVIGATION_MONTH_COLOR = Calendar_i18n.getColor("color.navigation_month_color");
    private static final Color MINI_CALENDAR_COLOR_BG = Calendar_i18n.getColor("color.mini_calendar_color_bg");
    private static final Color DAY_SELECTED_COLOR_BD = Calendar_i18n.getColor("color.day_selected_color_bd");
    private static final Color DAY_OF_WEEK_COLOR_BG = Calendar_i18n.getColor("color.day_of_week_color_bg");
    
    private static final Color MEETING_COLOR_BG = Calendar_i18n.getColor("color.meeting_color_bg");
    private static final Color LUNCH_COLOR_BG = Calendar_i18n.getColor("color.lunch_color_bg");
    private static final Color CONFERENCE_COLOR_BG = Calendar_i18n.getColor("color.conference_color_bg");
    private static final Color HEADER_COLOR_BD = Calendar_i18n.getColor("color.header_color_bd");
    // Costanti per dimensioni
    private static final Dimension MINI_CALENDAR_SIZE = Calendar_i18n.getDimension("dimension.mini_calendar");
    
    // Flag per tracciare la modalità di visualizzazione corrente
    private boolean compactMode = false;

    /**
     * Creates a standard border for day panels
     */
    private Border createStandardDayBorder() {
        return Appuntamenti.createStandardDayBorder();
    }
    
    /**
     * Creates a highlighted border for the current day.
     * Used by updateAppointmentPanel() to highlight today's date.
     * @return A compound border with the today highlight color
     */
    private Border createTodayBorder() {
        return Appuntamenti.createTodayBorder();
    }

    /**
     * Creates a header border for day of week labels
     */
    private Border createDayOfWeekHeaderBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, HEADER_COLOR_BD),
            BorderFactory.createEmptyBorder(5, 2, 5, 2)
        );
    }

    // Abilita log per stampare a console la dimensione dei pannelli
    private boolean debug = false;
    
    /**
     * Costruttore della classe Calendario.
     * Inizializza l'interfaccia grafica e configura tutti i componenti necessari.
     */
    public Calendario() {
        initializeFrame();
        initializeCalendar();
        createMainLayout();
        setupEventListeners();
        finalizeSetup();
    }
    
    private void initializeFrame() {
        setTitle(dashboard.i18n.Calendar_i18n.getString("app.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupAccelerator();
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        setMinimumSize(new Dimension(224, 224));
        setLocationRelativeTo(null);
        
        // Set application icon
        try {
            // Load the icon image
            ImageIcon icon = new ImageIcon(getClass().getResource("/dashboard/img/calendar.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            // If icon loading fails, log the error but continue
            System.err.println(MessageFormat.format(
                Calendar_i18n.getString("error.icon_load"), e.getMessage()));
        }
    }

    private void initializeCalendar() {
        // Initialize calendar instance
        calendar = Calendar.getInstance();
        
        // Create main panel
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    } 

    private void createNavigationPanel() {
        navigationPanel = new JPanel(new BorderLayout(5, 0));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Create month label
        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("Arial", Font.BOLD, 16));
        monthLabel.setForeground(NAVIGATION_MONTH_COLOR);
        monthLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        // Create navigation buttons panel
        JPanel navButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        
        // Style the navigation buttons
        prevButton = createStyledButton("<<", NAVIGATION_BUTTON_COLOR_BG);
        todayButton = createStyledButton(Calendar_i18n.getString("button.today"), NAVIGATION_BUTTON_COLOR_BG);
        nextButton = createStyledButton(">>", NAVIGATION_BUTTON_COLOR_BG);
        
        // Add buttons to panel
        navButtonsPanel.add(prevButton);
        navButtonsPanel.add(todayButton);
        navButtonsPanel.add(nextButton);
        
        // Add month label at the top and navigation buttons at the bottom
        navigationPanel.add(monthLabel, BorderLayout.NORTH);
        navigationPanel.add(navButtonsPanel, BorderLayout.CENTER);
    }
    
    /**
     * Creates a styled button with custom colors and rounded borders
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(NAVIGATION_BUTTON_COLOR);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NAVIGATION_BUTTON_COLOR_BD, 1, true),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void createMiniCalendarPanel() {
        // Create the mini calendar panel using the dedicated class
        miniCalendarPanel = new MiniCalendarPanel(calendar, monthLabel);
        miniCalendarPanel.setBackground(MINI_CALENDAR_COLOR_BG);
        
        // Set references for appointment updates
        if (appointmentDetails == null) {
            System.out.println("ERROR: appointmentDetails is null when setting up mini calendar");
        } else {
            System.out.println("Setting appointment details to mini calendar");
        }
        miniCalendarPanel.setAppointmentDetails(appointmentDetails);
        miniCalendarPanel.setAppointmentPanelUpdater(() -> updateAppointmentPanel());
    }

    private void createMainLayout() {
        // Create left panel for mini calendar and controls
        JPanel leftPanel = new JPanel(new BorderLayout(5, 10));
        leftPanel.setPreferredSize(new Dimension(250, 250));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 10)));

        // Create and setup navigation panel
        createNavigationPanel();
        
        // Create details panel first
        createDetailsPanel();
        
        // Create mini calendar section
        createMiniCalendarPanel();
        
        // Create controls section
        createControlsPanel();
        
        // Add components to left panel
        leftPanel.add(navigationPanel, BorderLayout.NORTH);
        leftPanel.add(miniCalendarPanel, BorderLayout.CENTER);
        leftPanel.add(controlsPanel, BorderLayout.SOUTH);
        
        // Create and setup main appointment panel
        createAppointmentPanel();
        
        // Add panels to frame
        add(leftPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventListeners() {
        // Add button listeners
        prevButton.addActionListener(e -> {
            calendar.add(Calendar.MONTH, -1);
            miniCalendarPanel.updateDisplay();
            updateAppointmentPanel();
        });
        
        nextButton.addActionListener(e -> {
            calendar.add(Calendar.MONTH, 1);
            miniCalendarPanel.updateDisplay();
            updateAppointmentPanel();
        });
        
        todayButton.addActionListener(e -> {
            // Instead of creating a new Calendar instance, update the current one
            Calendar today = Calendar.getInstance();
            calendar.set(Calendar.YEAR, today.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, today.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH));
            
            // Update displays
            miniCalendarPanel.updateDisplay();
            updateAppointmentPanel();
        });

        // Add component size logger
        ComponentListener sizeLogger = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (debug) {
                    Debug.logPanelSizes(Calendario.this, mainPanel, navigationPanel, miniCalendarPanel, 
                                    appointmentPanel, controlsPanel);
                }
            }
        };
        
        miniCalendarPanel.addComponentListener(sizeLogger);
        appointmentPanel.addComponentListener(sizeLogger);
        controlsPanel.addComponentListener(sizeLogger);
        mainPanel.addComponentListener(sizeLogger);
    }

    private void finalizeSetup() {
        // Initial updates
        miniCalendarPanel.updateDisplay();
        updateAppointmentPanel();
        
        // Apply debug colors if debug mode is on
        if (debug) {
            Debug.applyDebugColors(mainPanel, navigationPanel, miniCalendarPanel, appointmentPanel, controlsPanel);
        }
        
        // Pack the frame to respect component sizes
        pack();

        // Reset to desired size after packing
        setSize(1020, 885);
        setLocationRelativeTo(null);
    }
    
    private void setupAccelerator() {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_X, 
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        getRootPane().registerKeyboardAction(
            e -> System.exit(0),
            keyStroke,
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    private void updateAppointmentPanel() {
        appointmentPanel.removeAll();
        appointmentPanel.setLayout(new GridLayout(0, 7, 1, 1)); // Forza 7 colonne
        
        // Intestazioni dei giorni usando l'enum
        for (DayOfWeek day : DayOfWeek.values()) {
            JLabel label = new JLabel(day.getDisplayName(), SwingConstants.CENTER);
            label.setBorder(createDayOfWeekHeaderBorder());
            label.setFont(new Font("Arial", Font.BOLD, 12));
            label.setForeground(day.isWeekend() ? WEEKEND_COLOR_BG : Color.BLACK);
            label.setBackground(DAY_OF_WEEK_COLOR_BG);
            label.setOpaque(true);
            appointmentPanel.add(label);
        }
        
        // Calcola il primo giorno del mese (0=Lunedì, 6=Domenica)
        Calendar temp = (Calendar) calendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = temp.get(Calendar.DAY_OF_WEEK);
        // Converti in base Lunedì=0
        int firstDayOffset = (firstDayOfWeek + 5) % 7;
    
        // Celle vuote iniziali
        for (int i = 0; i < firstDayOffset; i++) {
            addEmptyDay();
        }
    
        // Giorni del mese
        int daysInMonth = temp.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar today = Calendar.getInstance();
        
        for (int day = 1; day <= daysInMonth; day++) {
            JPanel dayPanel = new JPanel();
            dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
            dayPanel.setBorder(createStandardDayBorder());
            
            // Verifica se questo è il giorno selezionato
            boolean isSelectedDay = (day == calendar.get(Calendar.DAY_OF_MONTH));
            
            // Evidenzia il giorno corrente
            boolean isToday = calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                            calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                            day == today.get(Calendar.DAY_OF_MONTH);
            
            if (isSelectedDay) {
                Debug.logCalendarSelection("Found selected day in appointment panel", calendar);
                // Highlight the selected day with a special border
                dayPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(DAY_SELECTED_COLOR_BD, 2, true),
                    BorderFactory.createEmptyBorder(2, 2, 2, 2)
                ));
                // Also set a background color to make it more visible
                dayPanel.setBackground(new Color(229, 243, 255));
                dayPanel.setOpaque(true);
            } else if (isToday) {
                dayPanel.setBorder(createTodayBorder());
            } else {
                dayPanel.setBorder(createStandardDayBorder());
            }
            
            // Aggiungi numero del giorno
            JLabel dayLabel = new JLabel(String.valueOf(day));
            dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            // Set weekend days in red
            temp.set(Calendar.DAY_OF_MONTH, day);
            int dayOfWeek = temp.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                dayLabel.setForeground(new Color(220, 20, 60));
            }
            
            dayPanel.add(dayLabel);
    
            // Aggiungi appuntamenti di esempio
            addSampleAppointments(dayPanel, day);
    
            appointmentPanel.add(dayPanel);
        }
    
        // Completa la griglia fino a 6 righe complete (42 celle totali)
        int totalCells = firstDayOffset + daysInMonth;
        int remainingCells = 42 - totalCells;
        for (int i = 0; i < remainingCells; i++) {
            addEmptyDay();
        }
    
        appointmentPanel.revalidate();
        appointmentPanel.repaint();
    }

    private void addEmptyDay() {
        Appuntamenti.addEmptyDay(appointmentPanel);
    }

    private void addSampleAppointments(JPanel dayPanel, int day) {
        // Aggiungi appuntamenti di esempio in base ai filtri selezionati
        if (day % 3 == 0 && meetingsCheckbox.isSelected()) {
            Appuntamenti.addAppointment(dayPanel, "10:00", Calendar_i18n.getString("appointment.meeting"), MEETING_COLOR_BG, calendar, appointmentDetails);
        }
        if (day % 5 == 0 && lunchCheckbox.isSelected()) {
            Appuntamenti.addAppointment(dayPanel, "14:30", Calendar_i18n.getString("appointment.lunch"), LUNCH_COLOR_BG, calendar, appointmentDetails);
        }
        if (day % 7 == 0 && conferenceCheckbox.isSelected()) {
            Appuntamenti.addAppointment(dayPanel, "16:00", Calendar_i18n.getString("appointment.conference"), CONFERENCE_COLOR_BG, calendar, appointmentDetails);
        }
    }
    
    /**
     * Toggles between compact and extended view modes
     */
    private void toggleViewMode() {
        compactMode = !compactMode;
        
        // Find the toggle button and update its text
        for (Component comp : controlsPanel.getComponents()) {
            if (comp instanceof JPanel) {
                for (Component button : ((JPanel) comp).getComponents()) {
                    if (button instanceof JButton) {
                        JButton btn = (JButton) button;
                        if (btn.getText().contains(Calendar_i18n.getString("button.your_mode"))) {
                            // Update button text based on current mode
                            btn.setText(compactMode ? Calendar_i18n.getString("button.extended_mode") : Calendar_i18n.getString("button.compact_mode"));
                            break;
                        }
                    }
                }
            }
        }
        
        // Find and toggle visibility of filter panel
        for (Component comp : controlsPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                // Check if this is the filter panel (has a label with "Filtri")
                for (Component child : panel.getComponents()) {
                    if (child instanceof JLabel && 
                        ((JLabel) child).getText().equals(Calendar_i18n.getString("label.filters"))) {
                        // This is the filter panel, toggle its visibility
                        panel.setVisible(!compactMode);
                        break;
                    }
                }
            }
        }
        
        if (compactMode) {
            // Switch to compact mode - show only mini calendar
            mainPanel.setVisible(false);
            // Resize the window to fit only the left panel
            setSize(250, 400);
        } else {
            // Switch back to extended mode
            mainPanel.setVisible(true);
            // Restore original size
            setSize(1020, 885);
        }
        
        // Update the UI
        revalidate();
        repaint();
    }
    
    /**
     * Restituisce lo stato della modalità compatta
     * @return true se il calendario è in modalità compatta, false altrimenti
     */
    public boolean isCompactMode() {
        return compactMode;
    }
    
    /**
     * Mostra gli appuntamenti del giorno selezionato in una finestra di dialogo
     * quando il calendario è in modalità compatta.
     */
    public void showAppointmentsInDialog() {
        // Crea una nuova finestra di dialogo
        JDialog appointmentsDialog = new JDialog(this, dashboard.i18n.Calendar_i18n.getString("dialog.appointments_title"), false);
        appointmentsDialog.setLayout(new BorderLayout(10, 10));
        appointmentsDialog.setSize(400, 300);
        appointmentsDialog.setLocationRelativeTo(this);
        
        // Crea un pannello per il titolo
        JPanel titlePanel = new JPanel(new BorderLayout());
        String dateText = calendar.get(Calendar.DAY_OF_MONTH) + " " + 
                        Month.fromCalendarMonth(calendar.get(Calendar.MONTH)).getDisplayName() + " " + 
                        calendar.get(Calendar.YEAR);
        JLabel titleLabel = new JLabel(Calendar_i18n.getString("dialog.appointments_for") + " " + dateText);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // Crea un pannello per gli appuntamenti
        JPanel appointmentsPanel = new JPanel();
        appointmentsPanel.setLayout(new BoxLayout(appointmentsPanel, BoxLayout.Y_AXIS));
        appointmentsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Aggiungi gli appuntamenti del giorno selezionato
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        boolean hasAppointments = false;
        
        if (day % 3 == 0 && meetingsCheckbox.isSelected()) {
            addAppointmentToPanel(appointmentsPanel, "10:00", 
                                Calendar_i18n.getString("appointment.meeting"), MEETING_COLOR_BG);
            hasAppointments = true;
        }
        if (day % 5 == 0 && lunchCheckbox.isSelected()) {
            addAppointmentToPanel(appointmentsPanel, "14:30", 
                                Calendar_i18n.getString("appointment.lunch"), LUNCH_COLOR_BG);
            hasAppointments = true;
        }
        if (day % 7 == 0 && conferenceCheckbox.isSelected()) {
            addAppointmentToPanel(appointmentsPanel, "16:00", 
                                Calendar_i18n.getString("appointment.conference"), CONFERENCE_COLOR_BG);
            hasAppointments = true;
        }
        
        // Se non ci sono appuntamenti, mostra un messaggio
        if (!hasAppointments) {
            JLabel noAppointmentsLabel = new JLabel(Calendar_i18n.getString("dialog.no_appointments"));
            noAppointmentsLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            appointmentsPanel.add(noAppointmentsLabel);
        }
        
        // Crea un pannello per i pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton(dashboard.i18n.Calendar_i18n.getString("button.close"));
        closeButton.addActionListener(e -> appointmentsDialog.dispose());
        buttonPanel.add(closeButton);
        
        // Aggiungi i pannelli alla finestra di dialogo
        appointmentsDialog.add(titlePanel, BorderLayout.NORTH);
        appointmentsDialog.add(new JScrollPane(appointmentsPanel), BorderLayout.CENTER);
        appointmentsDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Mostra la finestra di dialogo
        appointmentsDialog.setVisible(true);
    }
    
    /**
     * Aggiunge un appuntamento al pannello degli appuntamenti nella finestra di dialogo.
     */
    private void addAppointmentToPanel(JPanel panel, String time, String description, Color color) {
        JPanel appointmentPanel = new JPanel(new BorderLayout(5, 0));
        appointmentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, color),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        appointmentPanel.add(timeLabel, BorderLayout.WEST);
        appointmentPanel.add(descriptionLabel, BorderLayout.CENTER);
        
        panel.add(appointmentPanel);
        panel.add(Box.createVerticalStrut(5)); // Spazio tra gli appuntamenti
    }

    private void createControlsPanel() {
        // Create controls panel with vertical box layout
        controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        
        // Create filter section
        JPanel filterPanel = new JPanel(new BorderLayout(5, 0));
        JLabel filterLabel = new JLabel(Calendar_i18n.getString("label.filters"));
        filterLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        
        // Add checkboxes for different appointment types
        meetingsCheckbox = new JCheckBox(Calendar_i18n.getString("appointment.meeting"));
        lunchCheckbox = new JCheckBox(Calendar_i18n.getString("appointment.lunch"));
        conferenceCheckbox = new JCheckBox(Calendar_i18n.getString("appointment.conference"));
        
        // Set all checkboxes selected by default
        meetingsCheckbox.setSelected(true);
        lunchCheckbox.setSelected(true);
        conferenceCheckbox.setSelected(true);
        
        // Add action listeners to checkboxes
        ActionListener filterListener = e -> updateAppointmentPanel();
        meetingsCheckbox.addActionListener(filterListener);
        lunchCheckbox.addActionListener(filterListener);
        conferenceCheckbox.addActionListener(filterListener);
        
        // Add checkboxes to panel
        checkboxPanel.add(meetingsCheckbox);
        checkboxPanel.add(lunchCheckbox);
        checkboxPanel.add(conferenceCheckbox);
        
        filterPanel.add(filterLabel, BorderLayout.NORTH);
        filterPanel.add(checkboxPanel, BorderLayout.CENTER);
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 0, 5)); // Changed from 2 to 3 rows
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton addButton = new JButton(Calendar_i18n.getString("button.new_appointment"));
        JButton printButton = new JButton(Calendar_i18n.getString("button.print_calendar"));
        
        // Add toggle view button
        JButton toggleViewButton = new JButton(Calendar_i18n.getString("button.compact_mode"));
        toggleViewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleViewButton.addActionListener(e -> toggleViewMode());
        
        // Set hand cursor for buttons
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        printButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add action listeners (placeholder functionality)
        addButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                MessageFormat.format(Calendar_i18n.getString("info.feature_not_implemented"), 
                    Calendar_i18n.getString("button.new_appointment").toLowerCase()),
                Calendar_i18n.getString("info.title"), 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        printButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                MessageFormat.format(Calendar_i18n.getString("info.feature_not_implemented"), 
                    Calendar_i18n.getString("button.print_calendar").toLowerCase()),
                Calendar_i18n.getString("info.title"), 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        buttonsPanel.add(addButton);
        buttonsPanel.add(printButton);
        buttonsPanel.add(toggleViewButton); // Add the toggle button
        
        // Add components to controls panel
        controlsPanel.add(filterPanel);
        controlsPanel.add(buttonsPanel);
    }

    private void createAppointmentPanel() {
        // Create appointment panel with grid layout
        appointmentPanel = new JPanel();
        appointmentPanel.setLayout(new GridLayout(0, 7, 1, 1)); // Force 7 columns
        appointmentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Create a scroll pane for the appointment panel
        JScrollPane scrollPane = new JScrollPane(appointmentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Add the scroll pane to the main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Initial update of the appointment panel
        updateAppointmentPanel();
    }

    private void createDetailsPanel() {
        // Create details panel for showing appointment information
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 0, 0, 0)
        ));
        
        // Create label for the details section
        JLabel detailsLabel = new JLabel(Calendar_i18n.getString("label.details"));
        detailsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Create text area for appointment details
        appointmentDetails = new JTextArea(5, 20);
        appointmentDetails.setEditable(false);
        appointmentDetails.setFont(new Font("Arial", Font.PLAIN, 12));
        appointmentDetails.setLineWrap(true);
        appointmentDetails.setWrapStyleWord(true);
        appointmentDetails.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        appointmentDetails.setText(Calendar_i18n.getString("label.select_appointment"));
        
        // Create scroll pane for the text area
        JScrollPane scrollPane = new JScrollPane(appointmentDetails);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Add components to details panel
        detailsPanel.add(detailsLabel, BorderLayout.NORTH);
        detailsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add details panel to main panel
        mainPanel.add(detailsPanel, BorderLayout.SOUTH);
    }

    /**
     * Metodo principale che avvia l'applicazione Calendario.
     * Imposta il look and feel Nimbus e crea l'istanza del calendario.
     * 
     * @param args argomenti da linea di comando (non utilizzati)
     */
    public static void main(String[] args) {
        try {
            // Set Nimbus Look and Feel
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            Calendario calendar = new Calendario();
            calendar.setVisible(true);
        });
    }
}
