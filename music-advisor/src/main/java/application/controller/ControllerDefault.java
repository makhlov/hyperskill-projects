package application.controller;

import application.controller.oauth.AuthCoordinator;
import application.controller.oauth.exception.AuthException;
import application.model.Model;
import application.model.ModelDefault;
import application.model.exception.ClientServerException;
import application.view.View;
import application.view.ViewConsole;

import java.util.InputMismatchException;
import java.util.List;

import static application.controller.Config.*;
import static application.controller.operation.manager.OperationManager.*;
import static application.model.UserRequestType.CATEGORIES;
import static application.model.UserRequestType.FEATURED_PLAYLISTS;
import static application.model.UserRequestType.NEW_RELEASES;
import static application.model.UserRequestType.PLAYLISTS;

public class ControllerDefault implements Controller {

    private static final String
            UNKNOWN_OPERATION = "Unknown operation",
            PROVIDE_ACCESS = "Please, provide access for application.",
            SPECIFY_CATEGORY = "Please specify a category: \"playlists <category>\"",
            EXPIRED = "Access token expired";

    private static final String[] NO_ARGUMENTS = {};

    private Model model;
    private View view;

    private boolean signedIn;
    private boolean exitCommandReceived;

    private ControllerDefault() {
        signedIn = false;
        exitCommandReceived = false;
        view = new ViewConsole();
    }

    public static Controller create() {
        return new ControllerDefault();
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
            view.addToOutputQueue(e.getMessage());
        } catch (NullPointerException e) {
            view.addToOutputQueue(EXPIRED);
            view.addToOutputQueue(PROVIDE_ACCESS);
            perform("auth", NO_ARGUMENTS);
        }
    }

    private void updateView(List<String> object) {
        view.addToOutputQueue(object);
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
                case "new" -> updateView(get(NEW_RELEASES).execute(model, NO_ARGUMENTS));
                case "featured" -> updateView(get(FEATURED_PLAYLISTS).execute(model, NO_ARGUMENTS));
                case "categories" -> updateView(get(CATEGORIES).execute(model, NO_ARGUMENTS));
                case "playlists" -> {
                    if (args==null || args.length != 1) {
                        throw new InputMismatchException(SPECIFY_CATEGORY);
                    }
                    updateView(get(PLAYLISTS).execute(model, args));
                }

                /* Application */
                default -> throw new InputMismatchException(UNKNOWN_OPERATION);
            }
        } else view.addToOutputQueue(PROVIDE_ACCESS);
    }

    private void auth() throws AuthException {
        AuthCoordinator coordinator = AuthCoordinator.create();
        view.addToOutputQueue(coordinator.getAccessRequestLink());
        ACCESS_TOKEN = coordinator.getAccessToken();
        model = ModelDefault.create(API_SERVER, ACCESS_TOKEN);
        signedIn = true;
    }
}