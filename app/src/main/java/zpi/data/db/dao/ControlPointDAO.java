package zpi.data.db.dao;

import zpi.data.model.ControlPoint;

/**
 * @author Wojciech Michałowski
 * Control Point DataBase Agent provides basic operations on database in ControlPoint table.
 */
public interface ControlPointDAO extends DAO {

    /**
     * Get the control point of a given name from database.
     * @param name Name of needed Control Point
     * @return Needed Control Point
     */
    public ControlPoint getControlPoint(String name);

    /**
     * Puts new row into ControlPoint table in database.
     * @param newControlPoint Control Point to be inserted into database
     * @return ID of a new Control Point in database
     */
    public int createControlPoint(ControlPoint newControlPoint);

    /**
     * Get the contorl point of a given id from database.
     * @param id Id of needed Control Point
     * @return Needed Control Point
     */
    public ControlPoint getControlPoint(int id);
}
