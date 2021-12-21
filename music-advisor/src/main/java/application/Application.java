package application;

import application.controller.Controller;
import application.controller.ControllerDefault;

import java.io.IOException;

import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Pattern;

import static application.controller.Config.API_SERVER;
import static application.controller.Config.AUTH_SERVER;
import static application.controller.Config.CLIENT_ID;
import static application.controller.Config.CLIENT_SECRET;
import static application.controller.Config.ELEMENT_PER_PAGE;
import static application.controller.Config.REDIRECT_URI;
import static utils.PropertiesReader.*;

public final class Application {

    private static final String INPUT_PATTERN = "auth|new|featured|categories|playlists|exit|prev|next [\\w\\s]+";

    private static Application instance;
    private static Scanner scanner;
    private Controller controller;

    private Application() {
        scanner = new Scanner(System.in);
        controller = ControllerDefault.create();
    }

    public void run() {
        Pattern inputPattern = Pattern.compile(INPUT_PATTERN);

        while (true) {
            String input = scanner.nextLine();
            if (inputPattern.matcher(input).matches()) {

            }
        }
    }

    public static Application getInstance(String[] args) {
        defineConfigValues(args);
        return getInstance();
    }

    private static void defineConfigValues(String[] args) {
        Properties config = new Properties();
        try {
            config = readMainProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AUTH_SERVER = (args != null && args.length > 1 && args[0].equals("-access"))?
                args[1]:
                config.getProperty("authorization.authServer");

        API_SERVER = (args != null && args.length > 2 && args[2].equals("-resource"))?
                args[3]:
                config.getProperty("connection.apiServer");

        ELEMENT_PER_PAGE = (args != null && args.length > 4 && args[4].equals("-page"))?
                Integer.parseInt(args[5]):
                Integer.parseInt(config.getProperty("pagination.elementPerPage"));

        REDIRECT_URI = config.getProperty("authorization.redirectUri");
        CLIENT_ID = config.getProperty("authorization.clientID");
        CLIENT_SECRET = config.getProperty("authorization.clientSecret");
    }

    private static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }
}