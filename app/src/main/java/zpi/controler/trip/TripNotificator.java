package zpi.controler.trip;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

    public List<ControlPoint> controlPoints;

    public TripNotificator(List<ControlPoint> pointList) throws DataException
    {
        controlPoints=new ArrayList<ControlPoint>();
        for(int i=0; i<pointList.size();i++)
        {
            controlPoints.add(new ControlPoint(pointList.get(i)));
        }
    }

    public  void setNotification(Context ctx)
    {
        int pointToNotification=pointToNotification();
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

    int pointToNotification()
    {
        return 0;
    }

}
