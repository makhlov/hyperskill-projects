/* Class name: Application
 * Date: 15.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.model;

/**
 * Enumeration of the available commands that requests to the Spotify API:
 *
 * {@link #CATEGORIES}
 * {@link #FEATURED_PLAYLISTS}
 * {@link #NEW_RELEASES}
 * {@link #PLAYLISTS}
 */
public enum UserRequestType {
    /**
     * Gets a list with the names of Spotify categories.
     * @see <a href="https://developer.spotify.com/console/get-browse-categories/">Get Several Browse Categories</a>
     */
    CATEGORIES,

    /**
     * Gets a list of featured playlists with title and link to the release.
     * @see <a href="https://developer.spotify.com/console/get-featured-playlists/">Get Featured Playlists</a>
     */
    FEATURED_PLAYLISTS,

    /**
     * Gets a list of new releases with title, artists, and link to the release.
     * @see <a href="https://developer.spotify.com/console/get-new-releases/">Get New Releases</a>
     */
    NEW_RELEASES,

    /**
     * Gets a Spotify playlists by category
     * @see <a href="https://developer.spotify.com/console/get-category-playlists/">Get Category's Playlists</a>
     */
    PLAYLISTS
}