package provider;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceProvider {
    /**
     * Gets user Locale from the request and returns the ResourceBundle instance
     *
     * @param request HttpServletRequest
     * @return ResourceBundle object with User's Locale setting.
     */
    public static ResourceBundle getResourceBundle(HttpServletRequest request) {
        ResourceBundle rb = null;
        if (request.getSession().getAttribute("CurrentLocale") != null) {
            try {
                rb = ResourceBundle.getBundle("language", (Locale) request.getSession().getAttribute("CurrentLocale"));
            } catch (MissingResourceException mre) {
                mre.printStackTrace();
                try {
                    rb = ResourceBundle.getBundle("language", new Locale("tr"));
                } catch (MissingResourceException re) {
                    re.printStackTrace();
                }
            }
        } else {
            rb = ResourceBundle.getBundle("language", Locale.getDefault());
        }

        return rb;
    }
}
