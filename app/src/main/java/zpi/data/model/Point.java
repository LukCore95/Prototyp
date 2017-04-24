package zpi.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wojciech Michałowski
 * Representation of any point on the map.
 */
public abstract class Point {
    private String name;
    private String description;
    private double longitude;
    private double latitude;
    private List<Integer> oldPhotos;

    public Point(String name, String description, double longitude, double latitude) throws DataException{
        setName(name);
        setDescription(description);
        this.longitude = longitude;
        this.latitude = latitude;
        oldPhotos = new ArrayList<Integer>();
    }

    public Point(String name, String description, double longitude, double latitude, List<Integer> oldPhotos) throws DataException{
        setName(name);
        setDescription(description);
        this.longitude = longitude;
        this.latitude = latitude;
        this.oldPhotos = oldPhotos;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return  description;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getLatitude(){
        return  latitude;
    }

    public List<Integer> getOldPhotos(){
        return oldPhotos;
    }

    private void setName(String name) throws DataException{
        if(name != null)
            this.name = name;
        else
            throw new DataException("name", "not null");
    }

    private void setDescription(String description) throws DataException{
        if(name != null)
            this.description = description;
        else
            throw new DataException("description", "not null");
    }

    public void setOldPhotos(List<Integer> oldPhotos){
        this.oldPhotos = oldPhotos;
    }
}
