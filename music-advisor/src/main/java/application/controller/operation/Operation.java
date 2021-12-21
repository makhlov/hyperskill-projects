package application.controller.operation;

import application.model.Model;
import application.model.exception.ClientServerException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Collection;

public interface Operation <T extends Collection> {
    T execute(Model model, String[] args) throws ClientServerException;
}