package application.controller.util.property;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileReader {

    private static final String AUTHORIZATION_PROPERTIES = "src\\main\\resources\\authorization.properties";
    private static final String CACHING_PROPERTIES = "src\\main\\resources\\caching.properties";
    private static final String INTERACTION_PROPERTIES = "src\\main\\resources\\interaction.properties";
    private static final String PAGINATION_PROPERTIES = "src\\main\\resources\\pagination.properties";

    public static Properties readProperties(String pathToProperties) throws FileNotFoundException {
        Properties config = new Properties();
        try (InputStream inputStream = new FileInputStream(pathToProperties)) {
            config.load(inputStream);
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
        return config;
    }

    public static Properties readAuthProperties() throws FileNotFoundException {
        return readProperties(AUTHORIZATION_PROPERTIES);
    }

    public static Properties loadCachingProperties() throws FileNotFoundException {
        return readProperties(CACHING_PROPERTIES);
    }

    public static Properties loadInteractionProperties() throws FileNotFoundException {
        return readProperties(INTERACTION_PROPERTIES);
    }

    public static Properties loadPaginationProperties() throws FileNotFoundException {
        return readProperties(PAGINATION_PROPERTIES);
    }
}