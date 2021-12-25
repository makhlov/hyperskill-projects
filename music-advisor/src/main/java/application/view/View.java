/* Class name: View
 * Date: 21.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.view;

import java.util.List;

/**
 * The <code>View</code> is an abstraction for output of a requested information by the user.
 * Inspired by Martin Fowler's paper about Passive View MVC implementations
 * @see <a href="https://martinfowler.com/eaaDev/PassiveScreen.html">Passive View</a>
 */
public interface View {

    /**
     * Adds one unit to the View output variation
     * @param string some string to output
     */
    void addToOutput(final String string);

    /**
     * Adds a list of units to the View output variation
     * @param stringList list of string to output
     */
    void addToOutput(final List<String> stringList);

    /**
     * Displays the previous page with the requested
     * information according to the set pagination
     */
    void prev();

    /**
     * Displays the next page with the requested
     * information according to the set pagination
     */
    void next();
}