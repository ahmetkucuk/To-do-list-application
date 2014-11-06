/**
 *
 */
package devgui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.prefs.Preferences;

import javax.swing.UIManager;

/**
 * @author Daniel S. McCain
 *
 */
public class VisualAppPreferences {

    private Preferences preferences;
    private String preferenceFile; // Location of the preference file
    private int width, height, xCoordinate, yCoordinated;
    private String language;
    private String theme;
    private final int DEFAULT_WIDTH = 400;
    private final int DEFAULT_HEIGHT = 300;
    private final int DEFAULT_X_COORD = 0;
    private final int DEFAULT_Y_COORD = 0;
    private final String DEFAULT_LANGUAGE = "English";
    private final String DEFAULT_THEME =
            UIManager.getSystemLookAndFeelClassName();


    /**
     * Class constructor
     */
    public VisualAppPreferences() {
        String homeDirectory = System.getProperty("user.home");
        preferenceFile = homeDirectory + "/.TODO-group4";
        try {
            // Import preferences from file
            Preferences.importPreferences(
                    new BufferedInputStream(new FileInputStream(preferenceFile)));
        } catch (Exception e) {
            // No error display
            // e.printStackTrace();
        }
        preferences = Preferences.userRoot().node(preferenceFile);

        // If the preference doesn't exist, the default value is used
        width = preferences.getInt("width", DEFAULT_WIDTH);
        height = preferences.getInt("height", DEFAULT_HEIGHT);
        xCoordinate = preferences.getInt("x", DEFAULT_X_COORD);
        yCoordinated = preferences.getInt("y", DEFAULT_Y_COORD);
        language = preferences.get("language", DEFAULT_LANGUAGE);
        theme = preferences.get("theme", DEFAULT_THEME);
    }

    /**
     * Sets the preferred window dimensions.
     *
     * @param width  window width
     * @param height window height
     */
    public void setWindowSizePreference(int width, int height) {
        preferences.putInt("width", width);
        preferences.putInt("height", height);
    }

    /**
     * Sets the preferred window location.
     *
     * @param xCoordinate
     * @param yCoordinate
     */
    public void setWindowLocationPreference(int xCoordinate, int yCoordinate) {
        preferences.putInt("x", xCoordinate);
        preferences.putInt("y", yCoordinate);
    }

    /**
     * Sets the preferred language.
     *
     * @param language string representing the language
     */
    public void setLanguage(String language) {
        preferences.put("language", language);
    }

    /**
     * Sets the theme.
     *
     * @param theme string representing the theme
     */
    public void setTheme(String theme) {
        preferences.put("theme", theme);
    }

    /**
     * Saves the current preferences.
     */
    public void savePreferences() {
        try {
            // Force changes to save, then save to preferrences file
            preferences.flush();
            preferences.exportSubtree(new BufferedOutputStream(
                    new FileOutputStream(preferenceFile)));
        } catch (Exception e1) {
            // e1.printStackTrace();
            // Currently a silent fail
        }
    }

    /**
     * Returns the current preferred window width.
     *
     * @return preferred width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the current preferred window height.
     *
     * @return preferred height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the current preferred X window coordinate.
     *
     * @return preferred X coordinate
     */
    public int getX() {
        return xCoordinate;
    }

    /**
     * Returns the current preferred Y window coordinate.
     *
     * @return preferred Y coordinate
     */
    public int getY() {
        return yCoordinated;
    }

    /**
     * Returns the current preferred language.
     *
     * @return preferred language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Returns the current preferred theme.
     *
     * @return preferred theme
     */
    public String getTheme() {
        return theme;
    }

}
