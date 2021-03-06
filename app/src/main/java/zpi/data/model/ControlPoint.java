package zpi.data.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Ania on 2017-04-24.
 */

public class ControlPoint extends Point {
    private String germanName;
    private Date date;
    private int icon;
    private int sliderOldPhoto;
    private int sliderNewPhoto;
    private int audiobook;

    public ControlPoint(String name, String germanName, String description, Date date, double longitude, double latitude, int icon, int sliderOldPhoto, int sliderNewPhoto, int audiobook) throws DataException{
        super(name, description, longitude, latitude);
        setGermanName(germanName);
        this.date = date;
        this.sliderOldPhoto = sliderOldPhoto;
        this.sliderNewPhoto = sliderNewPhoto;
        this.audiobook = audiobook;
        this.icon=icon; //brakowalo
    }

    public ControlPoint(String name, String germanName, String description, Date date, double longitude, double latitude, int icon, int sliderOldPhoto, int sliderNewPhoto, int audiobook, List<Integer> oldPhotos) throws DataException{
        super(name, description, longitude, latitude, oldPhotos);
        setGermanName(germanName);
        this.date = date;
        this.sliderOldPhoto = sliderOldPhoto;
        this.sliderNewPhoto = sliderNewPhoto;
        this.audiobook = audiobook;
        this.icon=icon; //brakowało
    }
    //Ada
    public ControlPoint(ControlPoint controlPoint) throws DataException{
        super(controlPoint);
        setGermanName(controlPoint.getGermanName());
        this.date=controlPoint.getDate();
        this.sliderNewPhoto=controlPoint.getSliderNewPhoto();
        this.sliderOldPhoto=controlPoint.getSliderOldPhoto();
        this.audiobook=controlPoint.getAudiobook();
        this.icon= controlPoint.getIcon();
    }

    public String getGermanName(){
        return germanName;
    }

    public Date getDate(){
        return date;
    }

    public int getIcon(){
        return icon;
    }

    public int getSliderOldPhoto(){
        return sliderOldPhoto;
    }

    public int getSliderNewPhoto(){
        return sliderNewPhoto;
    }

    public int getAudiobook(){
        return audiobook;
    }

    private void setGermanName(String germanName) throws DataException{
        if(germanName != null)
            this.germanName = germanName;
        else
            throw new DataException("germanName", "not null");
    }

    @Override
    public boolean equals(Object other){
        return (other instanceof ControlPoint)&&this.getName().equals(((ControlPoint) other).getName());
    }

}
