package application.controller.operation.type;

import application.controller.operation.Operation;
import application.model.Model;
import application.model.UserRequestType;
import application.model.exception.ClientServerException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import static application.model.UserRequestType.CATEGORIES;

public class OperationCategories implements Operation<List<String>> {

    @Override
    public List<String> execute(Model model, String[] args) throws ClientServerException {
        return parseCategories(model.get(CATEGORIES, args));
    }

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