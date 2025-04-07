package dashboard;

import java.util.ResourceBundle;

/**
 * Enum per i mesi dell'anno.
 */
public enum Month {
    JANUARY("month.january", 0),
    FEBRUARY("month.february", 1),
    MARCH("month.march", 2),
    APRIL("month.april", 3),
    MAY("month.may", 4),
    JUNE("month.june", 5),
    JULY("month.july", 6),
    AUGUST("month.august", 7),
    SEPTEMBER("month.september", 8),
    OCTOBER("month.october", 9),
    NOVEMBER("month.november", 10),
    DECEMBER("month.december", 11);
    
    private final String resourceKey;
    private final int calendarMonth;
    private String displayName;
    
    Month(String resourceKey, int calendarMonth) {
        this.resourceKey = resourceKey;
        this.calendarMonth = calendarMonth;
    }
    
    public String getDisplayName() {
        if (displayName == null) {
            // Use CalendarResources class instead of ResourceBundle
            displayName = dashboard.i18n.Calendar_i18n.getString(resourceKey);
        }
        return displayName;
    }
    
    public int getCalendarMonth() {
        return calendarMonth;
    }
    
    /**
     * Restituisce l'enum Month corrispondente al mese del calendario (0-11)
     * 
     * @param calendarMonth il mese del calendario (0-11)
     * @return l'enum Month corrispondente
     */
    public static Month fromCalendarMonth(int calendarMonth) {
        for (Month month : values()) {
            if (month.calendarMonth == calendarMonth) {
                return month;
            }
        }
        throw new IllegalArgumentException("Invalid calendar month: " + calendarMonth);
    }
}