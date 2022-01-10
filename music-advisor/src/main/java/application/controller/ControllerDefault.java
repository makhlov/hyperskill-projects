/* Class name: ControllerDefault
 * Date: 21.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.controller;

/* Common imports */
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.List;

import application.controller.oauth.AuthCoordinator;
import application.controller.oauth.exception.AuthException;

import application.model.Model;
import application.model.ModelDefault;
import application.model.exception.ClientServerException;

import application.view.View;
import application.view.ViewConsole;

/* Static imports */
import static application.controller.Config.ACCESS_TOKEN;
import static application.controller.Config.API_SERVER;
import static application.controller.Config.CACHE_EXPIRATION_SECONDS;
import static application.controller.Config.ITEM_PER_PAGE;

import static application.model.UserRequestType.CATEGORIES;
import static application.model.UserRequestType.FEATURED_PLAYLISTS;
import static application.model.UserRequestType.NEW_RELEASES;
import static application.model.UserRequestType.PLAYLISTS;

import static application.controller.operation.manager.OperationManager.get;

import static application.controller.util.property.PropertiesApplicator.applyAuthorizationProperties;
import static application.controller.util.property.PropertiesApplicator.applyCacheProperties;
import static application.controller.util.property.PropertiesApplicator.applyInteractionProperties;
import static application.controller.util.property.PropertiesApplicator.applyPaginationProperties;

/**
 * The default implementation of <code>Controller</code> interface in specific
 * and fully consistent with the description task from hyperskill
 */
public class ControllerDefault implements Controller {

    public static final String UNKNOWN_OPERATION = "Unknown operation";
    public static final String PROVIDE_ACCESS = "Please, provide access for application.";
    public static final String SPECIFY_CATEGORY = "Please specify a category: \"playlists <category>\"";
    public static final String EXPIRED = "Access token expired";
    public static final String PROPERTY_LOADING_FAILED = "Specify parameters manually and restart the application";
    public static final String USE_AUTH_LINK = "use this link to request the access code:";
    public static final String WAITING_FOR_CODE = "waiting for code...";
    public static final String SUCCESS = "code received\nMaking http request for access_token...\nSuccess!";

    private Model model;
    private View view;

    private boolean signedIn;
    private boolean exitCommandReceived;

    /**
     * Private constructor
     * @param args arguments passed at application startup
     */
    private ControllerDefault(String[] args) {
        signedIn = false;
        exitCommandReceived = false;
        initView(args);
        applyProperties(args);
    }

    /**
     *  Method for getting new class instances
     *
     * @param args arguments passed at application startup
     * @return a new <code>Controller</code> instance
     */
    public static Controller create(String[] args) {
        return new ControllerDefault(args);
    }

    /* Interface implementation methods */

    /** {@inheritDoc} */
    @Override
    public boolean isSignedIn() {
        return signedIn;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExitCommandReceived() {
        return exitCommandReceived;
    }

    /** {@inheritDoc} */
    @Override
    public void perform(String command, String[] args) {
        try {
            rout(command, args);
        } catch (NullPointerException e) {
            view.addToOutput(EXPIRED);
            view.addToOutput(PROVIDE_ACCESS);
            perform("auth", args);
        } catch (ClientServerException | AuthException | RuntimeException e) {
            view.addToOutput(e.getMessage());
        }
    }

    /**
     * Method for updating the view state (sends the data passed as parameters to imprint)
     * @param object object to send to view
     */
    private void updateView(List<String> object) {
        view.addToOutput(object);
    }

    /**
     * Routes a custom command to its execution methods
     *
     * @param command                 user command
     * @param args                    arguments passed with the command
     *
     * @throws ClientServerException  @see {@link ClientServerException}
     * @throws AuthException          @see {@link ClientServerException}

     */
    private void rout(String command, String[] args) throws ClientServerException, AuthException
    {
        if (command.equalsIgnoreCase("exit")) {
            exitCommandReceived = true;
        }
        if (command.equalsIgnoreCase("auth")) {
            auth();
        } else if (signedIn && !exitCommandReceived) {
            switch (command) {
                /* Pagination */
                case "next" -> view.next();
                case "prev" -> view.prev();

                /* Request to API */
                case "new" -> updateView(get(NEW_RELEASES).execute(model, args));
                case "featured" -> updateView(get(FEATURED_PLAYLISTS).execute(model, args));
                case "categories" -> updateView(get(CATEGORIES).execute(model, args));
                case "playlists" -> {
                    if (args==null || args.length != 1) {
                        throw new InputMismatchException(SPECIFY_CATEGORY);
                    }
                    updateView(get(PLAYLISTS).execute(model, args));
                }
                default -> throw new InputMismatchException(UNKNOWN_OPERATION);
            }
        } else if (!exitCommandReceived) view.addToOutput(PROVIDE_ACCESS);
    }

    /**
     * Method performing auth on the Spotify API server
     *
     * @throws AuthException occurs when there are errors in connecting to the server or inability to receive a token
     */
    private void auth() throws AuthException {
        AuthCoordinator coordinator = AuthCoordinator.create();
        view.addToOutput(USE_AUTH_LINK);
        view.addToOutput(coordinator.getAccessRequestLink());
        view.addToOutput(WAITING_FOR_CODE);
        ACCESS_TOKEN = coordinator.getAccessToken();
        /* In theory, the output should be performed at each stage, but I encapsulated all the work with OAuth
           in a separate class. I do not consider it expedient to add a view dependency to it (only view should output
           to the console, so I decide simulate the "phased" output
        */
        view.addToOutput(SUCCESS);

        model = ModelDefault.create(API_SERVER, ACCESS_TOKEN, CACHE_EXPIRATION_SECONDS);
        signedIn = true;
    }

    /* Below are helper methods for initializing values */

    /**
     * The method performs the installation of the arguments values that loaded from the
     * properties by default or passed as arguments when starting the application
     *
     * @param args arguments passed at application startup
     */
    private void applyProperties(String[] args) {
        try {
            applyInteractionProperties(args);
            applyCacheProperties();
            applyAuthorizationProperties(args);
        } catch (FileNotFoundException e) {
            view.addToOutput(e.getMessage());
            view.addToOutput(PROPERTY_LOADING_FAILED);
        }
    }

    /**
     * Initializes the view
     * @param args arguments passed at application startup
     */
    private void initView(String[] args) {
        applyPaginationProperties(args);
        view = ViewConsole.create(ITEM_PER_PAGE);
    }
}