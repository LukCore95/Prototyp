package zpi.data.db.dao;

import java.util.List;

import zpi.data.model.InterestingPlace;

/**
 * @author Wojciech Michałowski
 * DAO interface which provides basic database operations on InterestingPlacePhoto table in Mock database.
 */
public interface InterestingPlacePhotoDAO extends DAO {

    /**
     * Gets a list of Integer representation of photos from interesting place of a givn name.
     * @param name Name of interesting place
     * @return List of Integer representation of photos (in drawables)
     */
    public List<Integer> getInterestingPlacesPhotos(String name);

    /**
     * Inserts all the photos from given interesting place into database's table.
     * @param ip Interesting place with photos listed within.
     * @return Returns true if insertion was successful and at least 1 photo was inserted, and false if no row was inserted.
     */
    public boolean insertPhotosFromInterestingPlace(InterestingPlace ip);
}
