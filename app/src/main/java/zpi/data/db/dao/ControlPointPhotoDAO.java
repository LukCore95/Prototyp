package zpi.data.db.dao;

import java.util.List;

import zpi.data.model.ControlPoint;

/**
 * @author Wojciech Michałowski
 * This interface provides basic database operations on ControlPointPhoto table in Mock database.
 */
public interface ControlPointPhotoDAO extends DAO {
    /**
     * Get the list of Integer representation of all oldPhotos connected with Control Point of a given name.
     * @param name Name of a Control Point
     * @return List of Integer representation of photos (in drawables)
     */
    public List<Integer> getControlPointsPhotos(String name);

    /**
     * Insert photos from a Control Point into ControlPointPhoto table in database.
     * @param cp ControlPoint object which contains a photos to be inserted.
     * @return Was insertion successful (at least one row added)
     */
    public boolean insertPhotosFromControlPoint(ControlPoint cp);
}
