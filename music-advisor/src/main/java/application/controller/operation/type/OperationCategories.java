/* Class name: OperationCategories
 * Date: 21.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.controller.operation.type;

import java.util.List;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import application.model.Model;
import application.model.exception.ClientServerException;

import application.controller.operation.Operation;

import static application.model.UserRequestType.CATEGORIES;

/**
 * Retrieves a selection of category names with spotify
 */
public class OperationCategories implements Operation<List<String>> {

    /**
     * Get a selection of category names from Spotify API.
     *
     * @param model                  model with which the operation will interact.
     * @param args                   arguments passed with the command.
     *
     * @return                       list of Spotify categories.
     * @throws ClientServerException if exists problems with connecting to the
     *                               server or working with categories api request.
     */
    @Override
    public List<String> execute(Model model, String[] args) throws ClientServerException {
        return parseCategories(model.get(CATEGORIES, args));
    }

    /**
     * Perform parse a raw JsonObject into a list of strings.
     * @param object object for parse.
     * @return       list with parsed objects.
     */
    private static List<String> parseCategories(final JsonObject object) {
        JsonObject main = object.getAsJsonObject().getAsJsonObject("categories");
        JsonArray items = main.getAsJsonArray("items");

        List<String> result = new ArrayList<>();
        for (var item : items) {
            JsonObject currentItem = item.getAsJsonObject();
            result.add(currentItem.get("name").getAsString());
        }
        return result;
    }
}