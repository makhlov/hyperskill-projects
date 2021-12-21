package application.controller.operation.type;

import application.controller.operation.Operation;
import application.controller.operation.manager.OperationManager;
import application.model.Model;
import application.model.ModelDefault;
import application.model.UserRequestType;
import application.model.exception.ClientServerException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static application.controller.operation.manager.OperationManager.get;
import static application.model.UserRequestType.CATEGORIES;
import static application.model.UserRequestType.PLAYLISTS;

public class OperationPlaylists implements Operation<List<String>> {

    private static Model model;

    @Override
    public List<String> execute(Model model, String[] args) throws ClientServerException {
        this.model = model;
        return parsePlaylists(model.get(PLAYLISTS, new String[] {getCategoryIdByNameIfExists(args[0])}));
    }

    private static List<String> parsePlaylists(final JsonObject object) {
        JsonArray categories = object.getAsJsonObject("playlists")
                .getAsJsonArray("items");

        List<String> result = new ArrayList<>();
        for (var item : categories) {
            result.add(item.getAsJsonObject().get("name").getAsString());
            result.add(item.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString());
        }
        return result;
    }

    private static String deleteQuotes(final String original) {
        return original.replace("\"", "");
    }

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

    private static JsonArray getCategoriesArray() throws ClientServerException {
        JsonArray categories = model.get(CATEGORIES, null)
                    .getAsJsonObject("categories")
                    .getAsJsonArray("items");
        return categories;
    }
}