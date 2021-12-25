/* Class name: OperationManager
 * Date: 21.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.controller.operation.manager;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import application.controller.operation.Operation;
import application.controller.operation.type.OperationCategories;
import application.controller.operation.type.OperationFeatured;
import application.controller.operation.type.OperationNewReleases;
import application.controller.operation.type.OperationPlaylists;

import application.model.UserRequestType;

import static application.model.UserRequestType.CATEGORIES;
import static application.model.UserRequestType.FEATURED_PLAYLISTS;
import static application.model.UserRequestType.NEW_RELEASES;
import static application.model.UserRequestType.PLAYLISTS;

/**
 * Operations manager-pool providing a concrete implementation of an operation by the type of request
 */
public class OperationManager {
    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "rawtypes"})
    private static final Map<UserRequestType, Operation> ALLOCATED_OPERATION;
    static {
        ALLOCATED_OPERATION = new HashMap<>() {{
            put(FEATURED_PLAYLISTS, new OperationFeatured());
            put(CATEGORIES, new OperationCategories());
            put(NEW_RELEASES, new OperationNewReleases());
            put(PLAYLISTS, new OperationPlaylists());
        }};
    }

    @SuppressWarnings("unchecked")
    public static Operation<List<String>> get(final UserRequestType type) {
        return ALLOCATED_OPERATION.get(type);
    }
}