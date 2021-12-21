package application.model;

import application.model.exception.ClientServerException;
import com.google.gson.JsonObject;

public interface Model {
    JsonObject get(UserRequestType type, String[] args) throws ClientServerException;
}
