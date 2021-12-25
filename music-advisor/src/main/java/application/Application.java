/* Class name: Application
 * Date: 22.12.21
 * Version 1.0
 * Author: makhlov
 */
package application;

import java.util.Scanner;

import application.controller.Controller;
import application.controller.ControllerDefault;

import static java.util.regex.Pattern.matches;

/**
 * The <code>Application</code> is a central class for coordinating Spotify Music Advisor.
 * Class preprocesses user input to pass it to the controller.
 *
 * The <code>Application</code> is implemented as a singleton since, in fact, it is a single
 * access point to the application,which is what this pattern provides. Since there is no
 * multithreading in the formulation of the project and the list of topics, this singleton
 * is intentionally not thread-safe.
 */
public final class Application {

    private static final String[] NO_ARGUMENTS = {};

    private static Application instance;
    private static Scanner scanner;

    private final Controller controller;

    /**
     * Private constructor for class initialization.
     *
     * @param args arguments passed at startup
     */
    private Application(String[] args) {
        scanner = new Scanner(System.in);
        controller = ControllerDefault.create(args);
    }

    /**
     * Returns an instance of <code>Application</code> with passed <code>args</code>,
     * or creates a new one if it has not been created before.
     *
     * @param  args arguments passed at startup
     * @return instance of class
     */
    public static Application getInstance(String[] args) {
        if (instance == null) {
            instance = new Application(args);
        }
        return instance;
    }

    /**
     * Runs read logic from the console to pass in commands to the controller.
     */
    public void run() {
        String command;
        String[] args;
        do {
            String userInput = scanner.nextLine();
            if (userInput.length() > 0) {
                if (matches("[\\w]+\\s[\\w\\s]+", userInput)) {
                    int separatorIndex = userInput.indexOf(" ");
                    command = userInput.substring(0, separatorIndex);
                    /* Not the most concise solution, but using an array instead of a string
                       leaves flexibility for many parameters in the future */
                    args = new String[] {userInput.substring(separatorIndex+1)};
                } else {
                    command = userInput;
                    args = NO_ARGUMENTS;
                }
                controller.perform(command, args);
            }
        } while (!controller.isExitCommandReceived());
    }
}