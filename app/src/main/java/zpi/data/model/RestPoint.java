package zpi.data.model;

import java.util.List;


/**
 * @author Wojciech Michałowski
 * Representation of a rest type (like pub, restaurant or cafe) on the map.
 */
public class RestPoint extends Point {
    private RestPointType type;
    private String address;
    private String web;

    public RestPoint(String name, String description, double longitude, double latitude, RestPointType type, String address, String web) throws DataException{
        super(name, description, longitude, latitude);
        this.type = type;
        this.address = address;
        this.web = web;
    }

    public RestPoint(String name, String description, double longitude, double latitude, List<Integer> oldPhotos, RestPointType type, String address, String web) throws DataException{
        super(name, description, longitude, latitude, oldPhotos);
        this.type = type;
        this.address = address;
        this.web = web;
    }

    public RestPointType getType(){
        return type;
    }

    public String getAddress(){
        return address;
    }

    public String getWeb(){
        return web;
    }

    @Override
    public boolean equals(Object other){
        return (other instanceof RestPoint)&&this.getName().equals(((RestPoint) other).getName());
    }
}
