/* Class name: OperationPlaylists
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
import static application.model.UserRequestType.PLAYLISTS;

/**
 * Operation of getting a selection of playlists by category name
 */
public class OperationPlaylists implements Operation<List<String>> {

    private static Model model;

    /**
     * Gets a list of playlists by the specified category name
     *
     * @param model                  model with which the operation will interact
     * @param args                   arguments passed with the command with category name
     *
     * @return                       list with playlists by the requested category
     * @throws ClientServerException is related to the problems of obtaining information from the api spotify
     */
    @Override
    public List<String> execute(Model model, String[] args) throws ClientServerException {
        OperationPlaylists.model = model;
        return parsePlaylists(model.get(PLAYLISTS, new String[] {getCategoryIdByNameIfExists(args[0])}));
    }

    /**
     * Perform parse a raw JsonObject into a list of strings
     * @param object object for parse
     * @return       list with parsed objects
     */
    private static List<String> parsePlaylists(final JsonObject object) {
        JsonArray playlists = object.getAsJsonObject("playlists")
                .getAsJsonArray("items");

        List<String> result = new ArrayList<>();
        StringBuilder resultBuilder;
        for (var item : playlists) {
            resultBuilder = new StringBuilder(item.getAsJsonObject().get("name").getAsString());
            resultBuilder.append("\n")
                   .append(item.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString());

            result.add(resultBuilder.toString());
        }
        return result;
    }

    /**
     * Removes quotes from a string
     * @param original the string from which to remove quotes
     * @return         unquoted string
     */
    private static String deleteQuotes(final String original) {
        return original.replace("\"", "");
    }

    /**
     * Method for finding a category id by a user-specified category name.
     * If the user specified the exact name of the category and the id
     * exists, then it returns it, otherwise, it throws an exception
     *
     * @param name category name
     * @return id if exist and name correct
     * @throws ClientServerException throws in case of problems with the connection to the server and the absence of id
     */
    private static String getCategoryIdByNameIfExists(final String name) throws ClientServerException {
        String categoryID = null;

        JsonArray categories = getCategoriesArray();
        for (var item : categories) {
            JsonObject current = item.getAsJsonObject();

            if(name.equalsIgnoreCase(deleteQuotes(current.get("name").toString()))) {
                categoryID = deleteQuotes(current.get("id").toString());
            }
        }

        if (categoryID == null) {
            throw new ClientServerException("Unknown category name.");
        }

        return categoryID;
    }

    /**
     * Gets a list of category names
     * @return                       list of categories names
     * @throws ClientServerException throws in case of problems with the connection to the server and the absence of id
     */
    private static JsonArray getCategoriesArray() throws ClientServerException {
        return model.get(CATEGORIES, null)
                    .getAsJsonObject("categories")
                    .getAsJsonArray("items");
    }
}