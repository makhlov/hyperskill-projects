/* Class name: Model
 * Date: 21.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.model;

import com.google.gson.JsonObject;
import application.model.exception.ClientServerException;

/**
 * The <code>Model</code> is an abstraction for obtaining raw or preprocessed data from resources
 */
public interface Model {

    /**
     * Returns the requested data by a command <code>type</code> with
     * arguments <code>args</code> through updating the model state
     *
     * @param type                   the command that user is requesting to execute
     * @param args                   arguments passed along with the command
     *
     * @return                       data object in JSON format (GSON Object)
     *
     * @throws ClientServerException exception wrapper that describing a set of
     *                               Exception that related with receiving and
     *                               performing raw data and states
     */
    JsonObject get(UserRequestType type, String[] args) throws ClientServerException;
}
