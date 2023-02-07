package clontz.clientschedulingapp.Helpers;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class LocalizationService {
    private static ResourceBundle resourceBundle;

    public static void setResourceBundle(String resourceName) {
        resourceBundle = ResourceBundle.getBundle(resourceName);
    }

    public static String getString(String forInput) {
        return resourceBundle.getString(forInput);
    }

    public static void changeLocale(Locale local) {
        Locale.setDefault(local);
    }

}
