/* Class name: PropertiesFileReader
 * Date: 22.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.controller.util.property;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.Properties;

/**
 * The <code>PropertiesFileReader</code> class encapsulates methods
 * for reading the core resource files used in the application
 */
class PropertiesFileReader {

    /* Paths to basic properties */
    private static final String AUTHORIZATION_PROPERTIES = "src\\main\\resources\\authorization.properties";
    private static final String CACHING_PROPERTIES = "src\\main\\resources\\caching.properties";
    private static final String INTERACTION_PROPERTIES = "src\\main\\resources\\interaction.properties";
    private static final String PAGINATION_PROPERTIES = "src\\main\\resources\\pagination.properties";

    /**
     * Reads properties at the specified path
     *
     * @param pathToProperties       the path along which the property is located, what should be considered
     * @return                       properties in the form of <code>Properties</code>
     * @throws FileNotFoundException if the file is missing for one reason or another, it throws an exception
     */
    public static Properties readProperties(String pathToProperties) throws FileNotFoundException {
        Properties config = new Properties();
        try (InputStream inputStream = new FileInputStream(pathToProperties)) {
            config.load(inputStream);
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
        return config;
    }

    /**
     * Reads properties for executing auth command
     *
     * @return properties in the form of <code>Properties</code>
     * @throws FileNotFoundException if the file is missing for one reason or another, it throws an exception
     */
    public static Properties readAuthProperties() throws FileNotFoundException {
        return readProperties(AUTHORIZATION_PROPERTIES);
    }

    /**
     * Reads properties for functioning of the cache executing auth command
     *
     * @return properties in the form of <code>Properties</code>
     * @throws FileNotFoundException if the file is missing for one reason or another, it throws an exception
     */
    public static Properties loadCachingProperties() throws FileNotFoundException {
        return readProperties(CACHING_PROPERTIES);
    }

    /**
     * Read properties related to interaction with api
     *
     * @return properties in the form of <code>Properties</code>
     * @throws FileNotFoundException if the file is missing for one reason or another, it throws an exception
     */
    public static Properties loadInteractionProperties() throws FileNotFoundException {
        return readProperties(INTERACTION_PROPERTIES);
    }

    /**
     * Reads properties required for pagination
     *
     * @return properties in the form of <code>Properties</code>
     * @throws FileNotFoundException if the file is missing for one reason or another, it throws an exception
     */
    public static Properties loadPaginationProperties() throws FileNotFoundException {
        return readProperties(PAGINATION_PROPERTIES);
    }
}