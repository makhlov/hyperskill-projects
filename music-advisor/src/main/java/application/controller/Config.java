/* Class name: Config
 * Date: 21.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.controller;

/**
 * The <code>Config</code> class is an access point to the loaded parameters of the application
 */
public final class Config {
    public static String API_SERVER, AUTH_SERVER, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, ACCESS_TOKEN;
    public static Integer ITEM_PER_PAGE, CACHE_EXPIRATION_SECONDS;
}