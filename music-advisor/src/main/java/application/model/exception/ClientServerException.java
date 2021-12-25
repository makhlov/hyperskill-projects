/* Class name: ClientServerException
 * Date: 15.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.model.exception;

/**
 * Interaction exceptions between music-advisor and the Spotify API
 */
public class ClientServerException extends Exception {

    public ClientServerException() {
        super();
    }

    public ClientServerException(String message) {
        super(message);
    }

    public ClientServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
