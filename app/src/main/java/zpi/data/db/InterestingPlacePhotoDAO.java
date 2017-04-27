package zpi.data.db;

import java.util.List;

import zpi.data.model.InterestingPlace;

/**
 * Created by Ania on 2017-04-25.
 */

public interface InterestingPlacePhotoDAO extends DAO {
    public List<Integer> getInterestingPlacesPhotos(String name);
    public boolean insertPhotosFromInterestingPlace(InterestingPlace ip);
}
