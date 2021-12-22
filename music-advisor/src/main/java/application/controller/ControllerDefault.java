package application.controller;

import application.controller.oauth.AuthCoordinator;
import application.controller.oauth.exception.AuthException;
import application.model.Model;
import application.model.ModelDefault;
import application.model.exception.ClientServerException;
import application.view.View;
import application.view.ViewConsole;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.List;

import static application.controller.Config.*;
import static application.controller.operation.manager.OperationManager.*;
import static application.controller.util.property.PropertiesApplicator.applyAuthorizationProperties;
import static application.controller.util.property.PropertiesApplicator.applyCacheProperties;
import static application.controller.util.property.PropertiesApplicator.applyInteractionProperties;
import static application.controller.util.property.PropertiesApplicator.applyPaginationProperties;
import static application.model.UserRequestType.CATEGORIES;
import static application.model.UserRequestType.FEATURED_PLAYLISTS;
import static application.model.UserRequestType.NEW_RELEASES;
import static application.model.UserRequestType.PLAYLISTS;

public class ControllerDefault implements Controller {

    private static final String
            UNKNOWN_OPERATION = "Unknown operation",
            PROVIDE_ACCESS = "Please, provide access for application.",
            SPECIFY_CATEGORY = "Please specify a category: \"playlists <category>\"",
            EXPIRED = "Access token expired",
            PROPERTY_LOADING_FAILED = "Specify parameters manually and restart the application";
    public static final String USE_AUTH_LINK = "use this link to request the access code:";

    private Model model;
    private View view;

    private boolean signedIn;
    private boolean exitCommandReceived;

    private ControllerDefault(String[] args) {
        signedIn = false;
        exitCommandReceived = false;
        initView(args);
        applyProperties(args);
    }

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

    private void initView(String[] args) {
        applyPaginationProperties(args);
        view = ViewConsole.create(ITEM_PER_PAGE);
    }

    public static Controller create(String[] args) {
        return new ControllerDefault(args);
    }

    @Override
    public boolean isSignedIn() {
        return signedIn;
    }

    @Override
    public boolean isExitCommandReceived() {
        return exitCommandReceived;
    }

    @Override
    public void perform(String command, String[] args) {
        try {
            rout(command, args);
        } catch (ClientServerException | InputMismatchException | AuthException e) {
            view.addToOutput(e.getMessage());
        } catch (NullPointerException e) {
            view.addToOutput(EXPIRED);
            view.addToOutput(PROVIDE_ACCESS);
            perform("auth", args);
        }
    }

    private void updateView(List<String> object) {
        view.addToOutput(object);
    }

    private void rout(String command, String[] args)
            throws ClientServerException, InputMismatchException, AuthException, NullPointerException
    {
        if (command.equalsIgnoreCase("exit")) {
            exitCommandReceived = true;
        }

        if (command.equalsIgnoreCase("auth")) {
            auth();
        } else if (signedIn) {
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

    private void auth() throws AuthException {
        AuthCoordinator coordinator = AuthCoordinator.create();
        view.addToOutput("use this link to request the access code:");
        view.addToOutput(coordinator.getAccessRequestLink());
        view.addToOutput("waiting for code...");
        ACCESS_TOKEN = coordinator.getAccessToken();
        /* In theory, the output should be performed at each stage, but I encapsulated all the work with OAuth
           in a separate class. I do not consider it expedient to add a view dependency to it (only view should output
           to the console, so I decide simulate the "phased" output
        */
        view.addToOutput("code received\nMaking http request for access_token...\nSuccess!");

        model = ModelDefault.create(API_SERVER, ACCESS_TOKEN, CACHE_EXPIRATION_SECONDS);
        signedIn = true;
    }
}