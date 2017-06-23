package zpi.data.db.dao;

import zpi.data.model.Route;

/**
 * @author Wojciech Michałowski
 * Route DataBase Agent provides basic operations on SQLite database that manipulates on Route table.
 */
public interface RouteDAO extends DAO {
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

    /**
     * Returns a route that has given id
     * @param id Id of a needed Route
     * @return Requested route
     */
    public Route getRoute(int id);
}
