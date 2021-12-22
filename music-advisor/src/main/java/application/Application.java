package application;

import application.controller.Controller;
import application.controller.ControllerDefault;

import java.util.Scanner;

import static java.util.regex.Pattern.*;

public final class Application {

    private static final String[] NO_ARGUMENTS = {};

    private static Application instance;
    private static Scanner scanner;
    private final Controller controller;

    private Application(String[] args) {
        scanner = new Scanner(System.in);
        controller = ControllerDefault.create(args);
    }

    public static Application getInstance(String[] args) {
        if (instance == null) {
            instance = new Application(args);
        }
        return instance;
    }

    public void run() {
        do {
            String command;
            String[] args;
            String userInput = scanner.nextLine();
            if (userInput.length() > 0) {
                if (matches("[\\w]+\\s[\\w\\s]+", userInput)) {
                    int spaceIndex = userInput.indexOf(" ");
                    command = userInput.substring(0, spaceIndex);
                    /* Not the most concise solution, but using an array instead of a string
                       leaves flexibility for many parameters in the future */
                    args = new String[] {userInput.substring(spaceIndex+1)};
                } else {
                    command = userInput;
                    args = NO_ARGUMENTS;
                }
                controller.perform(command, args);
            }
        } while (!controller.isExitCommandReceived());
    }
}