/* Class name: OperationFeatured
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

import static application.model.UserRequestType.FEATURED_PLAYLISTS;

/**
 * Operation of getting a selection of featured playlists
 */
public class OperationFeatured implements Operation<List<String>> {

    /**
     * Gets lists of featured playlists
     *
     * @param model                  model with which the operation will interact
     * @param args                   none (empty array at version 1.0)
     *
     * @return                       list of new releases
     * @throws ClientServerException is related to the problems of obtaining information from the api spotify
     */
    @Override
    public List<String> execute(Model model, String[] args) throws ClientServerException {
        return parseFeatured(model.get(FEATURED_PLAYLISTS, args));
    }

    /**
     * Perform parse a raw JsonObject into a list of strings
     * @param object object for parse
     * @return       list with parsed objects
     */
    private static List<String> parseFeatured(final JsonObject object) {
        JsonArray items = object.getAsJsonObject()
                                .getAsJsonObject("playlists")
                                .getAsJsonArray("items");

        StringBuilder resultBuilder;
        List<String> result = new ArrayList<>();
        for (var item : items) {
            JsonObject current = item.getAsJsonObject();
            resultBuilder = new StringBuilder(current.get("name").getAsString());
            resultBuilder.append("\n").append(current.getAsJsonObject("external_urls").get("spotify").getAsString());
            result.add(resultBuilder.toString());
        }
        return result;
    }
}