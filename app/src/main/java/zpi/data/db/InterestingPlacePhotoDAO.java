package zpi.data.db;

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

    public boolean insertPhotosFromInterestingPlace(InterestingPlace ip);
}
