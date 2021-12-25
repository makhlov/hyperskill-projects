/* Class name: Operation
 * Date: 21.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.controller.operation;

import java.util.Collection;

import application.model.Model;
import application.model.exception.ClientServerException;

/**
 * Operation abstraction, which is a unit of user work with an application
 * @param <T> some collection returned as a result of processed data from api
 */
@SuppressWarnings("rawtypes")
public interface Operation <T extends Collection> {

    /**
     * Method of launching the execution of the implemented operation with the specified parameters
     *
     * @param model                  model with which the operation will interact
     * @param args                   arguments passed with the command
     *
     * @return                       a result of the command as a subtype of the <code>Collection</code>
     * @throws ClientServerException exceptions related to data exchange problems with api spotify
     */
    T execute(Model model, String[] args) throws ClientServerException;
}