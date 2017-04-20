package zpi.prototyp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Adrianna on 20/04/2017.
 */

public class Point {
    LatLng geoLoc;
    String polishName;
    String germanName;
    String description;
    int markerID;
    String[] imgSource;
    boolean slider;
    String sliderSource;

    Point(LatLng gl, String pN, String gN)
    {
        geoLoc=gl;
        polishName=pN;
        germanName=gN;
    }

    Point()
    {}

    public void setGeoLoc(LatLng l)
    {
        geoLoc=l;
    }

    public void setPolishName(String pl)
    {
        polishName=pl;
    }

    public void setGermanName(String ge)
    {
        germanName=ge;
    }

    public LatLng getGeoLoc()
    {
        return geoLoc;
    }

    public String getPolishName()
    {
        return polishName;
    }

    public String getGermanName()
    {
        return germanName;
    }
}
