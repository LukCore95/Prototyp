package zpi.data.db.dao;

/**
 * @author Wojciech Michałowski
 * Basic DAO interface which is extended by any other DAO interface working on Mock database.
 */
public interface DAO {

    /**
     * Get ID (which is usually a primary key) of an object of a given name from the database
     * @param name Name of object (e.g. control point or route)
     * @return Needed ID
     */
    public int getId(String name);
}
