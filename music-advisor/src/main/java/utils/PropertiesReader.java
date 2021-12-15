package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    public final static Properties readProperties(String pathToProperties) throws FileNotFoundException {
        Properties config = new Properties();
        try (InputStream inputStream = new FileInputStream(pathToProperties)) {
            config.load(inputStream);
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
        return config;
    }

    public final static Properties readMainProperties() throws FileNotFoundException {
        return readProperties("src\\main\\resources\\config.properties");
    }
}