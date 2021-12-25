/* Class name: AuthException
 * Date: 21.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.controller.oauth.exception;

/**
 * Auth exception. Occurs when there are problems connecting
 * to the authorization server, receiving a code or access token
 */
public class AuthException extends Exception {

    public AuthException() {
        super();
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
