/* Class name: OperationNewReleases
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

import static application.model.UserRequestType.NEW_RELEASES;

/**
 * Operation of getting a selection of new releases
 */
public class OperationNewReleases implements Operation<List<String>> {

    /**
     * Gets list of published new releases
     *
     * @param model                  model with which the operation will interact
     * @param args                   none (empty array at version 1.0)
     *
     * @return                       list of new releases
     * @throws ClientServerException is related to the problems of obtaining information from the api spotify
     */
    @Override
    public List<String> execute(Model model, String[] args) throws ClientServerException {
        return parseNewReleases(model.get(NEW_RELEASES, args));
    }

    /**
     * Perform parse a raw JsonObject into a list of strings
     * @param object object for parse
     * @return       list with parsed objects
     */
    private static List<String> parseNewReleases(final JsonObject object) {
        JsonArray releases = object.getAsJsonObject("albums").getAsJsonArray("items");

        StringBuilder resultBuilder;
        List<String> result = new ArrayList<>();
        for (var item : releases) {
            JsonObject currentItem = item.getAsJsonObject();

            StringBuilder artistsStringBuilder = new StringBuilder("[");
            JsonArray artists = currentItem.getAsJsonArray("artists");
            for (var artist : artists) {
                artistsStringBuilder.append(artist.getAsJsonObject().get("name").getAsString()).append(", ");
            }
            artistsStringBuilder.replace(artistsStringBuilder.length()-2, artistsStringBuilder.length()-1, "")
                         .deleteCharAt(artistsStringBuilder.length()-1)
                         .append("]");

            resultBuilder = new StringBuilder(currentItem.get("name").getAsString());
            resultBuilder.append("\n")
                         .append(currentItem.getAsJsonObject("external_urls").get("spotify").getAsString())
                         .append("\n").append(artistsStringBuilder);

            result.add(resultBuilder.toString());
        }
        return result;
    }
}