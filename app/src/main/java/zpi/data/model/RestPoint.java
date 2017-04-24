package zpi.data.model;

import java.util.List;


/**
 * @author Wojciech Michałowski
 * Representation of a rest type (like pub, restaurant or cafe) on the map.
 */
public class RestPoint extends Point {
    private RestPointType type;

    public RestPoint(String name, String description, double longitude, double latitude, RestPointType type) throws DataException{
        super(name, description, longitude, latitude);
        this.type = type;
    }

    public RestPoint(String name, String description, double longitude, double latitude, List<Integer> oldPhotos, RestPointType type) throws DataException{
        super(name, description, longitude, latitude, oldPhotos);
        this.type = type;
    }

    public RestPointType getType(){
        return type;
    }
}
