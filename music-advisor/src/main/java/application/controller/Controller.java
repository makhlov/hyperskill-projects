/* Class name: Controller
 * Date: 21.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.controller;

/**
 * Abstraction of a music-advisor application controller
 */
public interface Controller {

    /**
     * Authorization check method
     * @return <code>true</code> if authorization is done, otherwise <code>false</code>
     */
    boolean isSignedIn();

    /**
     * Method for checking if an application exit request is received
     * @return <code>true</code> if exit is requested and <code>false</code> otherwise
     */
    boolean isExitCommandReceived();

    /**
     * Method for passing commands to controlled modules (model and view).
     *
     * @param command user command
     * @param args    arguments passed with the command
     */
    void perform(String command, String[] args);
}
