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
import zpi.data.model.Point;
import zpi.prototyp.MainActivity;
import zpi.prototyp.R;
import zpi.utils.DistanceCalculator;

/**
 * @author Adrianna Łapucha
 * Controller that manages notificator behaviour.
 */
public class TripNotificator {
    int minDistance=50;
    private Context context;

    public List<ControlPoint> controlPoints;

    /**
     * Main public, empty constructor.
     */
    public TripNotificator()
    {

    }

    /**
     * Displays notification about reached current target.
     * @param ctx Context of application
     * @param point Reached target point
     */
    public  void setNotification(Context ctx, Point point) {


        long[] pattern1 = {0, 1000, 1000};
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.mipmap.actionbar_marker_icon) // notification icon
                .setContentTitle("Doszedłeś do punktu " + point.getName() + "!") // title for notification
                .setContentText("Stuknij, aby wyświetlić detale!") // message for notification
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setVibrate(pattern1); // clear notification after click
        Intent intent = new Intent(ctx, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

    }
}
