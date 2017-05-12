package zpi.controler.trip;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import zpi.data.model.ControlPoint;
import zpi.data.model.DataException;
import zpi.prototyp.MainActivity;
import zpi.prototyp.R;

/**
 * Created by Adrianna on 11/05/2017.
 */

public class TripNotificator {
    int minDistance=50;

    public List<ControlPoint> controlPoints;

    public TripNotificator(List<ControlPoint> pointList) throws DataException
    {
        controlPoints=new ArrayList<ControlPoint>();
        for(int i=0; i<pointList.size();i++)
        {
            controlPoints.add(new ControlPoint(pointList.get(i)));
        }
    }

    public  void setNotification(Context ctx, Location loc)
    {
        int pointToNotification=pointToNotification(loc);
        if(pointToNotification!=-1)
        {
            long[] pattern1 = {0, 1000, 1000};
            NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(ctx)
                    .setSmallIcon(R.mipmap.actionbar_marker_icon) // notification icon
                    .setContentTitle("Doszedłeś do punktu " + controlPoints.get(pointToNotification).getName()+"!") // title for notification
                    .setContentText("Stuknij, aby wyświetlić detale!") // message for notification
                    .setAutoCancel(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setVibrate(pattern1); // clear notification after click
            Intent intent = new Intent(ctx, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(ctx,0,intent,PendingIntent.FLAG_ONE_SHOT);
            mBuilder.setContentIntent(pi);
            NotificationManager mNotificationManager =
                    (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());

        }
    }

    int pointToNotification(Location loc)
    {
       for(int i=0; i<controlPoints.size(); i++)
       {
           if(distance(loc.getLatitude(), loc.getLongitude(), controlPoints.get(i).getLatitude(), controlPoints.get(i).getLongitude())<=minDistance)
           {
               return i;
           }
       }
        return -1;
    }

    double distance(double lat1, double lon1, double lat2, double lon2)
    {
        // generally used geo measurement function
        double R = 6378.137; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000; // meters
    }



}
