package application.controller.operation.manager;

import application.controller.operation.Operation;
import application.controller.operation.type.OperationCategories;
import application.controller.operation.type.OperationFeatured;
import application.controller.operation.type.OperationNewReleases;
import application.controller.operation.type.OperationPlaylists;
import application.model.UserRequestType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static application.model.UserRequestType.CATEGORIES;
import static application.model.UserRequestType.FEATURED_PLAYLISTS;
import static application.model.UserRequestType.NEW_RELEASES;
import static application.model.UserRequestType.PLAYLISTS;

public class OperationManager {
    private static final Map<UserRequestType, Operation> ALLOCATED_OPERATION;
    static {
        ALLOCATED_OPERATION = new HashMap<>() {{
            put(FEATURED_PLAYLISTS, new OperationFeatured());
            put(CATEGORIES, new OperationCategories());
            put(NEW_RELEASES, new OperationNewReleases());
            put(PLAYLISTS, new OperationPlaylists());
        }};
    }

    public static Operation<List<String>> get(UserRequestType type) {
        return ALLOCATED_OPERATION.get(type);
    }
}