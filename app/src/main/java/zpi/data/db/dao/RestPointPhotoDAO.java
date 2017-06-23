package zpi.data.db.dao;

import java.util.List;

import zpi.data.model.RestPoint;

/**
 * @author Wojciech Michałowski
 * Rest point photo DataBase Agent provides basic operations on SQLite database that manipulates on RestPointPhoto table.
 */
public interface RestPointPhotoDAO extends DAO {
    /**
     * Gets a list of Integer representation of photos from rest point of a given name.
     * @param name Name of rest point
     * @return List of Integer representation of photos (in drawables)
     */
    public List<Integer> getRestPointsPhotos(String name);

    /**
     * Inserts all the photos from given rest point into database's table.
     * @param rp Rest point with photos listed within.
     * @return Returns true if insertion was successful and at least 1 photo was inserted, and false if no row was inserted.
     */
    public boolean insertPhotosFromRestPoint(RestPoint rp);
}
