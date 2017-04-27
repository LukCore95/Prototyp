package zpi.data.db;

import zpi.data.model.RestPoint;

/**
 * @author Wojciech Michałowski
 * Rest Point DataBase Agent provides basic operations on database in RestType table.
 */
public interface RestPointDAO extends DAO {

    /**
     * Get the rest point of a given name from database.
     * @param name Name of needed Rest Point
     * @return Needed Rest Point
     */
    public RestPoint getRestPoint(String name);

    /**
     * Puts new row into RestPoint table in database.
     * @param newRestPoint Rest Point to be inserted into database
     * @return ID of a new Rest Point in database
     */
    public int createRestPoint(RestPoint newRestPoint);

}
