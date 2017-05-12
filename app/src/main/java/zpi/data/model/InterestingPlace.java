package zpi.data.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * @author Wojciech Michałowski
 * Representation of interesting place on the map.
 */
public class InterestingPlace extends Point {

    private String address;
    private InterestingPlaceType type;

    public InterestingPlace(String name, String description, double longitude, double latitude, String address, InterestingPlaceType type) throws DataException{
        super(name, description, longitude, latitude);
        this.address = address;
        this.type = type;
    }

    public InterestingPlace(String name, String description, double longitude, double latitude, String address, InterestingPlaceType type, List<Integer> oldPhotos) throws DataException{
        super(name, description, longitude, latitude, oldPhotos);
        this.address = address;
        this.type = type;
    }

    public LatLng getGeoLoc()
    {
        return new LatLng(getLatitude(), getLongitude());
    }

    public String getAddress(){
        return address;
    }

    public InterestingPlaceType getType(){
        return type;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setType(InterestingPlaceType type){
        this.type = type;
    }

    @Override
    public boolean equals(Object other){
        return (other instanceof InterestingPlace)&&this.getName().equals(((InterestingPlace) other).getName());
    }
}
