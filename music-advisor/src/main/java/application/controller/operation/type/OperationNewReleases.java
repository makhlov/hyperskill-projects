package application.controller.operation.type;

import application.controller.operation.Operation;
import application.model.Model;
import application.model.UserRequestType;
import application.model.exception.ClientServerException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperationNewReleases implements Operation<List<String>> {

    @Override
    public List<String> execute(Model model, String[] args) throws ClientServerException {
        return parseNewReleases(model.get(UserRequestType.NEW_RELEASES, args));
    }

    private static List<String> parseNewReleases(final JsonObject object) {
        JsonArray releases = object.getAsJsonObject("albums").getAsJsonArray("items");

        List<String> result = new ArrayList<>();
        for (var item : releases) {
            JsonObject currentItem = item.getAsJsonObject();

            StringBuilder artistsString = new StringBuilder("[");
            JsonArray artists = currentItem.getAsJsonArray("artists");
            for (var artist : artists) {
                artistsString.append(artist.getAsJsonObject().get("name").getAsString()).append(", ");
            }
            artistsString.replace(artistsString.length()-2, artistsString.length()-1, "")
                         .deleteCharAt(artistsString.length()-1)
                         .append("]");

            result.add(currentItem.get("name").getAsString());
            result.add(currentItem.getAsJsonObject("external_urls").get("spotify").getAsString());
            result.add(artistsString.toString());
        }
        return result;
    }
}