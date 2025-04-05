package dashboard;

/**
 * Enum representing days of the week.
 * Provides display names and weekend status for each day.
 */
public enum DayOfWeek {
    MONDAY("Lunedì", false),
    TUESDAY("Martedì", false),
    WEDNESDAY("Mercoledì", false),
    THURSDAY("Giovedì", false),
    FRIDAY("Venerdì", false),
    SATURDAY("Sabato", true),
    SUNDAY("Domenica", true);
    
    private final String displayName;
    private final boolean weekend;
    
    /**
     * Constructor for DayOfWeek enum.
     * 
     * @param displayName The display name of the day
     * @param weekend Whether this day is a weekend day
     */
    DayOfWeek(String displayName, boolean weekend) {
        this.displayName = displayName;
        this.weekend = weekend;
    }
    
    /**
     * Gets the display name of the day.
     * 
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Checks if this day is a weekend day.
     * 
     * @return true if this is a weekend day, false otherwise
     */
    public boolean isWeekend() {
        return weekend;
    }
    
    /**
     * Converts a Calendar day of week to the corresponding DayOfWeek enum.
     * 
     * @param calendarDay The Calendar.DAY_OF_WEEK value
     * @return The corresponding DayOfWeek enum
     */
    public static DayOfWeek fromCalendarDay(int calendarDay) {
        switch (calendarDay) {
            case java.util.Calendar.MONDAY: return MONDAY;
            case java.util.Calendar.TUESDAY: return TUESDAY;
            case java.util.Calendar.WEDNESDAY: return WEDNESDAY;
            case java.util.Calendar.THURSDAY: return THURSDAY;
            case java.util.Calendar.FRIDAY: return FRIDAY;
            case java.util.Calendar.SATURDAY: return SATURDAY;
            case java.util.Calendar.SUNDAY: return SUNDAY;
            default: throw new IllegalArgumentException("Invalid calendar day: " + calendarDay);
        }
    }
}