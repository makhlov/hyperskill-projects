package application.controller.operation.type;

import application.controller.operation.Operation;
import application.model.Model;
import application.model.exception.ClientServerException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import static application.model.UserRequestType.CATEGORIES;
import static application.model.UserRequestType.FEATURED_PLAYLISTS;

public class OperationFeatured implements Operation<List<String>> {

    @Override
    public List<String> execute(Model model, String[] args) throws ClientServerException {
        return parseFeatured(model.get(FEATURED_PLAYLISTS, args));
    }

    private static List<String> parseFeatured(final JsonObject object) {
        JsonArray items = object.getAsJsonObject()
                                .getAsJsonObject("playlists")
                                .getAsJsonArray("items");

        List<String> result = new ArrayList<>();
        for (var item : items) {
            JsonObject current = item.getAsJsonObject();
            result.add(current.get("name").getAsString());
            result.add(current.getAsJsonObject("external_urls").get("spotify").getAsString());
        }
        return result;
    }
}