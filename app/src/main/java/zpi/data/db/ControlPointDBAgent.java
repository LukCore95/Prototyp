package zpi.data.db;

import zpi.data.model.ControlPoint;

/**
 * @author Wojciech Michałowski
 * Control Point DataBase Agent provides basic operations on database in ControlPoint table.
 */
public interface ControlPointDBAgent extends DBAgent {

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

    public ControlPoint getControlPoint(int id);
}
