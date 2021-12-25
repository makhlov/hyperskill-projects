/* Class name: PropertiesApplicator
 * Date: 22.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.controller.util.property;

/* Common imports */
import java.io.FileNotFoundException;
import java.util.Properties;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

/* Static imports */
import static application.controller.Config.API_SERVER;
import static application.controller.Config.AUTH_SERVER;
import static application.controller.Config.CACHE_EXPIRATION_SECONDS;
import static application.controller.Config.CLIENT_ID;
import static application.controller.Config.CLIENT_SECRET;
import static application.controller.Config.ITEM_PER_PAGE;
import static application.controller.Config.REDIRECT_URI;

import static application.controller.util.property.PropertiesFileReader.loadCachingProperties;
import static application.controller.util.property.PropertiesFileReader.loadInteractionProperties;
import static application.controller.util.property.PropertiesFileReader.loadPaginationProperties;
import static application.controller.util.property.PropertiesFileReader.readAuthProperties;

public class PropertiesApplicator {

    public static final int DEFAULT_EXPIRATION_SECONDS = 120;
    public static final int DEFAULT_ITEMS_AMOUNT = 1;
    public static final String FAILED_READ = "Failed to read value from %s. Set to default: %d";

    public static void applyAuthorizationProperties(String[] args) throws FileNotFoundException {
        Properties config = readAuthProperties();

        AUTH_SERVER = (args != null && args.length > 1 && args[0].equals("-access"))?
                args[1]:
                config.getProperty("authorization.authServer");

        CLIENT_ID = config.getProperty("authorization.clientID");
        CLIENT_SECRET = config.getProperty("authorization.clientSecret");
        REDIRECT_URI = config.getProperty("authorization.redirectUri");
    }

    public static void applyInteractionProperties(String[] args) throws FileNotFoundException {
        API_SERVER = (args != null && args.length > 2 && args[2].equals("-resource"))?
                args[3]:
                loadInteractionProperties().getProperty("interaction.apiServer");
    }

    public static void applyPaginationProperties(String[] args) {
        int itemsAmount;

        try {
            itemsAmount = parseInt(loadPaginationProperties().getProperty("pagination.itemsPerPage"));
        } catch (FileNotFoundException e) {
            itemsAmount = DEFAULT_ITEMS_AMOUNT;
        }

        ITEM_PER_PAGE = args != null && args.length > 4 && args[4].equals("-page")?parseInt(args[5]):itemsAmount;
    }

    public static void applyCacheProperties() throws FileNotFoundException {
        int expirationSeconds;
        try {
            expirationSeconds = parseInt(loadCachingProperties().getProperty("cache.expirationSeconds"));
        } catch (FileNotFoundException e) {
            expirationSeconds = DEFAULT_EXPIRATION_SECONDS;
            String context = "Cache Properties";
            throw new FileNotFoundException(format(FAILED_READ, context, expirationSeconds));
        }
        CACHE_EXPIRATION_SECONDS = expirationSeconds;
    }
}