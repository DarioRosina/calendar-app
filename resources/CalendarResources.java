package dashboard.resources;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

/**
 * Resource class for calendar localization.
 * Provides a Java-based alternative to properties files.
 */
public class CalendarResources {
    private static final Map<String, String> resources = new HashMap<>();
    
    // Color constants
    private static final Map<String, Color> colorResources = new HashMap<>();
    
    // Dimension constants
    private static final Map<String, Dimension> dimensionResources = new HashMap<>();
    
    static {
        resources.put("app.title", "Calendario Appuntamenti");
        resources.put("dialog.appointments_title", "Appuntamenti");
        resources.put("dialog.appointments_for", "Appuntamenti del");
        resources.put("dialog.no_appointments", "Nessun appuntamento per questo giorno");
        resources.put("button.close", "Chiudi");
        // Day names
        resources.put("day.monday", "Lunedì");
        resources.put("day.tuesday", "Martedì");
        resources.put("day.wednesday", "Mercoledì");
        resources.put("day.thursday", "Giovedì");
        resources.put("day.friday", "Venerdì");
        resources.put("day.saturday", "Sabato");
        resources.put("day.sunday", "Domenica");
        
        // Month names
        resources.put("month.january", "Gennaio");
        resources.put("month.february", "Febbraio");
        resources.put("month.march", "Marzo");
        resources.put("month.april", "Aprile");
        resources.put("month.may", "Maggio");
        resources.put("month.june", "Giugno");
        resources.put("month.july", "Luglio");
        resources.put("month.august", "Agosto");
        resources.put("month.september", "Settembre");
        resources.put("month.october", "Ottobre");
        resources.put("month.november", "Novembre");
        resources.put("month.december", "Dicembre");
        
        // Add all other resources from your properties file here
        resources.put("button.today", "Oggi");
        resources.put("button.new_appointment", "Nuovo Appuntamento");
        resources.put("button.print_calendar", "Stampa Calendario");
        resources.put("button.your_mode", "Modalità");
        resources.put("button.compact_mode", "Modalità Compatta");
        resources.put("button.extended_mode", "Modalità Estesa");
        resources.put("label.filters", "Filtri");
        resources.put("label.details", "Dettagli");
        resources.put("label.select_appointment", "Seleziona un appuntamento per visualizzare i dettagli.");
        resources.put("appointment.meeting", "Riunione");
        resources.put("appointment.lunch", "Pranzo");
        resources.put("appointment.conference", "Conferenza");
        resources.put("details.appointment", "Appuntamento: {0}");
        resources.put("details.day", "Giorno: {0} {1} {2}");
        resources.put("details.time", "Ora: {0}");
        resources.put("details.description", "Descrizione: {0}");
        resources.put("info.feature_not_implemented", "La funzionalità di {0} non è ancora implementata.");
        resources.put("info.title", "Informazione");
        resources.put("error.icon_load", "Impossibile caricare l'icona: {0}");

        // Initialize color resources
        colorResources.put("color.weekend_color_bg", new Color(220, 20, 60));
        colorResources.put("color.day_hover_color_bg", new Color(240, 240, 240));
        colorResources.put("color.day_selected_color_bd", new Color(229, 243, 255));
        colorResources.put("color.today_highlight_color", new Color(0, 120, 215));
        colorResources.put("color.navigation_button_color_bg", new Color(240, 240, 240));
        colorResources.put("color.navigation_button_color", new Color(50, 50, 50));
        colorResources.put("color.navigation_button_color_bd", new Color(214, 217, 223));
        colorResources.put("color.navigation_month_color", new Color(50, 50, 50));
        colorResources.put("color.mini_calendar_color_bg", new Color(255, 255, 255));
        colorResources.put("color.day_selected_color_bg", new Color(229, 243, 255));
        colorResources.put("color.day_of_week_color_bg", new Color(255, 255, 255));
        colorResources.put("color.empty_day_color_bg", new Color(245, 245, 245));
        colorResources.put("color.meeting_color_bg", new Color(200, 230, 255));
        colorResources.put("color.lunch_color_bg", new Color(255, 230, 230));
        colorResources.put("color.conference_color_bg", new Color(230, 255, 230));
        colorResources.put("color.header_color_bd", new Color(130, 130, 130));
        
        // Initialize dimension resources
        dimensionResources.put("dimension.mini_calendar", new Dimension(200, 200));
    }

    /**
     * Gets a string from the resource bundle.
     * 
     * @param key the resource key
     * @return the string for the given key
     * @throws MissingResourceException if no object for the given key can be found
     */
    public static String getString(String key) {
        if (resources.containsKey(key)) {
            return resources.get(key);
        }
        throw new MissingResourceException("Can't find resource for key " + key, 
                                          CalendarResources.class.getName(), key);
    }
    
    /**
     * Gets a color from the resource bundle.
     * 
     * @param key the resource key
     * @return the color for the given key
     * @throws MissingResourceException if no object for the given key can be found
     */
    public static Color getColor(String key) {
        if (colorResources.containsKey(key)) {
            return colorResources.get(key);
        }
        throw new MissingResourceException("Can't find color resource for key " + key, 
                                          CalendarResources.class.getName(), key);
    }
    
    /**
     * Gets a dimension from the resource bundle.
     * 
     * @param key the resource key
     * @return the dimension for the given key
     * @throws MissingResourceException if no object for the given key can be found
     */
    public static Dimension getDimension(String key) {
        if (dimensionResources.containsKey(key)) {
            return dimensionResources.get(key);
        }
        throw new MissingResourceException("Can't find dimension resource for key " + key, 
                                          CalendarResources.class.getName(), key);
    }
}