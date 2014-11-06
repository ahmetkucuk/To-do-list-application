/**
 *
 */
package devgui;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Daniel S. McCain
 *
 */
public class LanguageManager {

    // Enum type to easily display available languages
    private enum AvailableLanguage {
        ENGLISH,
        SPANISH,
        SWEDISH;

        public Locale toLocale() {
            Locale locale = null;
            switch (this) {
                case ENGLISH:
                    locale = new Locale("en");
                    break;
                case SPANISH:
                    locale = new Locale("es", "ES");
                    break;
                case SWEDISH:
                    locale = new Locale("sv", "SE");
                    break;
            }
            return locale;
        }
    }

    private static LanguageManager instance = null;
    private ResourceBundle resourceBundle;
    private String currentLanguage;

    /**
     * Class constructor
     */
    private LanguageManager() {
        resourceBundle = ResourceBundle.getBundle("language.support.Language");
        currentLanguage = resourceBundle.getLocale().getDisplayLanguage();
    }

    public static LanguageManager getInstance() {
        if(instance == null) {
           instance = new LanguageManager();
        }
        return instance;
     }

    /**
     * Returns the currently set language in the LanguageManager.
     *
     * @return the current language
     */
    public String getLanguage() {
        return currentLanguage;
    }

    /**
     * Changes the language that the LanguageManager will use.
     *
     * @param language a string with the new language to set the LanguageManager
     */
    public void setLanguage(String language) {
        // The display language will depend on the user's computer's default locale
        String english = AvailableLanguage.ENGLISH.toLocale().getDisplayLanguage();
        String spanish = AvailableLanguage.SPANISH.toLocale().getDisplayLanguage();
        String swedish = AvailableLanguage.SWEDISH.toLocale().getDisplayLanguage();

        Locale locale = null;
        if (language.equalsIgnoreCase(english))
            locale = AvailableLanguage.ENGLISH.toLocale();
        else if (language.equalsIgnoreCase(spanish))
            locale = AvailableLanguage.SPANISH.toLocale();
        else if (language.equalsIgnoreCase(swedish))
            locale = AvailableLanguage.SWEDISH.toLocale();
        else
            locale = Locale.getDefault();

        currentLanguage = language;
        resourceBundle = ResourceBundle.getBundle("language.support.Language",
                locale);
    }

    /**
     * Returns a list of available languages in the form of strings.
     *
     * @return a list of strings, each one being a possible language to use
     */
    public String[] getAvailableLanguages() {
        ArrayList<String> languageList = new ArrayList<String>();
        for (AvailableLanguage lang: AvailableLanguage.values()) {
            Locale locale = lang.toLocale();
            languageList.add(locale.getDisplayLanguage());
        }
        String[] languageStringList =
                languageList.toArray(new String[languageList.size()]);
        // Sort the list so it shows in alphabetic order
        java.util.Arrays.sort(languageStringList);
        return languageStringList;
    }

    /**
     * Returns the string of a resource string in the current language that is
     * set in the Language Manager.
     *
     * @param string
     * @return string associated to the input string in the current language
     */
    public String getString(String string) {
        return resourceBundle.getString(string);
    }

}
