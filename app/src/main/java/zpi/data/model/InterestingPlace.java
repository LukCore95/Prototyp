package zpi.data.model;

import java.util.List;

/**
 * @author Wojciech Michałowski
 * Representation of interesting place on the map.
 */
public class InterestingPlace extends Point {

    public InterestingPlace(String name, String description, double longitude, double latitude) throws DataException{
        super(name, description, longitude, latitude);
    }

    public InterestingPlace(String name, String description, double longitude, double latitude, List<Integer> oldPhotos) throws DataException{
        super(name, description, longitude, latitude, oldPhotos);
    }
}
