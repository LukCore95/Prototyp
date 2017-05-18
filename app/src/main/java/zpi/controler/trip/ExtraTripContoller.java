package zpi.controler.trip;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import zpi.data.model.Point;
import zpi.utils.DistanceCalculator;

/**
 * Created by Adrianna on 18/05/2017.
 */

public class ExtraTripContoller {
    public static final float MIN_DISTANCE=0.1f; //distance to notification - getting back to route
    Context ctx;
    Point current; //InterestingPoint or ResPoint
    TripNotificator tripNotificator;
    LatLng userLoc;

    public ExtraTripContoller(Context c)
    {
        ctx=c;
        tripNotificator=new TripNotificator();
    }

    public void setUserLoc(LatLng ul){ userLoc=ul; }

    public Point getCurrent() { return current; }

    public void setCurrent(Point p) { current=p; }

    public boolean checkIfPointReached()  {

        if(DistanceCalculator.distance(userLoc.latitude, userLoc.longitude, current.getLatitude(), current.getLongitude())<=MIN_DISTANCE)
        {
            tripNotificator.setNotification(ctx, current);

             //oddanie sterowania do tripControllera

            return true;
        }

        return false;
    }


}
