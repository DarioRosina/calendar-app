package dashboard;

import dashboard.i18n.Calendar_i18n;

/**
 * Enum representing days of the week.
 * Provides display names and weekend status for each day.
 */
public enum DayOfWeek {
    MONDAY("day.monday", false),
    TUESDAY("day.tuesday", false),
    WEDNESDAY("day.wednesday", false),
    THURSDAY("day.thursday", false),
    FRIDAY("day.friday", false),
    SATURDAY("day.saturday", true),
    SUNDAY("day.sunday", true);
    
    private final String resourceKey;
    private final boolean weekend;
    private String displayName;
    
    /**
     * Constructor for DayOfWeek enum.
     * 
     * @param resourceKey The resource key for the display name
     * @param weekend Whether this day is a weekend day
     */
    DayOfWeek(String resourceKey, boolean weekend) {
        this.resourceKey = resourceKey;
        this.weekend = weekend;
    }
    
    /**
     * Gets the display name of the day.
     * 
     * @return The display name
     */
    public String getDisplayName() {
        if (displayName == null) {
            displayName = Calendar_i18n.getString(resourceKey);
        }
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