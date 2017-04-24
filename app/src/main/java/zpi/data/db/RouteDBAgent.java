package zpi.data.db;

import java.security.PermissionCollection;
import java.util.Collection;

import zpi.data.model.ControlPoint;
import zpi.data.model.Route;

/**
 * @author Wojciech Michałowski
 * Route DataBase Agent provides basic operations on SQLite database that manipulates on Route table.
 */
public interface RouteDBAgent extends DBAgent {
    /**
     * Get a route of a given name from the database.
     * @param name Name of needed route
     * @return Route of a given name
     */
    public Route getRoute(String name);

    /**
     * Creates new route in database.
     * @param newRoute A new route to be inserted into database.
     * @return ID of a new route in database
     */
    public int createRoute(Route newRoute);
}
