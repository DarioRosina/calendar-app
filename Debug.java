package dashboard;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

/**
 * Utility class for debugging purposes in the Calendar application.
 * Contains methods for logging panel sizes and applying debug colors.
 */
public class Debug {
    
    /**
     * Logs the sizes of various panels to the console.
     * 
     * @param frame The main application frame
     * @param mainPanel The main content panel
     * @param navigationPanel The navigation panel
     * @param miniCalendarPanel The mini calendar panel
     * @param appointmentPanel The appointment panel
     * @param controlsPanel The controls panel
     */
    public static void logPanelSizes(JFrame frame, JPanel mainPanel, JPanel navigationPanel, MiniCalendarPanel miniCalendarPanel,  
                                    JPanel appointmentPanel, JPanel controlsPanel) {
        System.out.println("--- Panel Sizes ---");
        System.out.println("Frame Size: " + frame.getSize());
        System.out.println("Main Panel (Light red): " + mainPanel.getSize());
        System.out.println("Navigation Panel (Blue): " + navigationPanel.getSize());
        System.out.println("Mini Calendar Panel (Light green): " + miniCalendarPanel.getSize());
        System.out.println("Appointment Panel (Green): " + appointmentPanel.getSize());
        System.out.println("Controls Panel (Light cyan)" + controlsPanel.getSize());
        System.out.println("------------------");
    }

    /**
     * Applies debug colors to panels to visualize their boundaries.
     * 
     * @param mainPanel The main content panel
     * @param navigationPanel The navigation panel
     * @param miniCalendarPanel The mini calendar panel
     * @param appointmentPanel The appointment panel
     * @param controlsPanel The controls panel
     */
    public static void applyDebugColors(JPanel mainPanel, JPanel navigationPanel, MiniCalendarPanel miniCalendarPanel, 
                                    JPanel appointmentPanel, JPanel controlsPanel) {
        mainPanel.setBackground(new Color(255, 220, 220));        // Light red
        navigationPanel.setBackground(new Color(0, 0, 255));        // Blue
        miniCalendarPanel.setBackground(new Color(220, 255, 220)); // Light green
        appointmentPanel.setBackground(new Color(0, 255, 0)); // Green
        controlsPanel.setBackground(new Color(220, 255, 255));    // Light cyan
        
        // Make panels opaque to show colors
        mainPanel.setOpaque(true);
        navigationPanel.setOpaque(true);
        miniCalendarPanel.setOpaque(true);
        appointmentPanel.setOpaque(true);
        controlsPanel.setOpaque(true);
    }

    /**
     * Logs information about calendar day selection
     * 
     * @param message A descriptive message
     * @param calendar The calendar instance
     */
    public static void logCalendarSelection(String message, Calendar calendar) {
        System.out.println("--- Calendar Selection Debug ---");
        System.out.println(message);
        System.out.println("Selected date: " + 
                        calendar.get(Calendar.YEAR) + "-" + 
                        (calendar.get(Calendar.MONTH) + 1) + "-" + 
                        calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("------------------------------");
    }
}