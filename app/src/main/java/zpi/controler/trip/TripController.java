package zpi.controler.trip;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.List;

import zpi.data.db.MockDbHelper;
import zpi.data.db.dao.RouteDAO;
import zpi.data.db.dao.RouteDAOOptimized;
import zpi.data.db.dao.TripDAO;
import zpi.data.db.dao.TripDAOOptimized;
import zpi.data.model.ControlPoint;
import zpi.data.model.DataException;
import zpi.data.model.Point;
import zpi.data.model.Route;
import zpi.data.model.Trip;
import zpi.utils.DistanceCalculator;

/**
 * Created by Ania on 2017-05-04.
 */

public final class TripController {
    private Trip currentTrip;
    private Context ctx;
    private LatLng userLoc = null;
    private static final float MIN_DISTANCE=0.05f;
    private TripNotificator tripNotificator;

    public TripController(Context ctx, Route route){
        this.ctx = ctx;
        loadTripFromDatabase(route);
        tripNotificator=new TripNotificator();
    }

    public TripController(Context ctx, Trip trip){
        currentTrip = trip;
        this.ctx = ctx;
        tripNotificator=new TripNotificator();
    }

    private void loadTripFromDatabase(Route route){
        MockDbHelper dbHelper = new MockDbHelper(ctx);
        SQLiteDatabase readDb = dbHelper.getReadableDatabase();
        SQLiteDatabase writeDb = dbHelper.getWritableDatabase();
        TripDAO tripDAO = new TripDAOOptimized(readDb, writeDb);
        currentTrip = tripDAO.getTrip(tripDAO.getId(route.getName()));
        if(currentTrip == null){
            try {
                currentTrip = new Trip(route, route.getRoutePoints().getFirst(), 1); //TODO generate an index
            }catch(DataException de){
                System.err.println(de);
            }
            tripDAO.createTrip(currentTrip);
        }
        readDb.close();
        writeDb.close();
    }

    public List<ControlPoint> getRouteControlPoints(){
        return currentTrip.getModifiedRoute();
    }

    public Point getCurrentCP(){
        return currentTrip.getCurrentTarget();
    }

    public ControlPoint getStartCP(){
        return currentTrip.getStartPoint();
    }

    public void setNewTrip(Route route, ControlPoint startPoint){
        MockDbHelper dbHelper = new MockDbHelper(ctx);
        SQLiteDatabase readDb = dbHelper.getReadableDatabase();
        SQLiteDatabase writeDb = dbHelper.getWritableDatabase();
        TripDAO tripDAO = new TripDAOOptimized(readDb, writeDb);

        if(currentTrip != null && currentTrip.getRoute().getName() == route.getName()){
            tripDAO.deleteTrip(currentTrip.getID());
        }

        try {
            System.out.println("ZAPISUJĘ TRIP DO BD: " + route.getName() + "; " + startPoint.getName());
            currentTrip = new Trip(route, startPoint, 1); //TODO generate an index
            tripDAO.createTrip(currentTrip);
        }catch(DataException de){
            System.err.println(de);
        }

        readDb.close();
        writeDb.close();
    }

    public Trip getCurrentTrip(){
        return currentTrip;
    }

    public void setUserLoc(LatLng newLoc){
        userLoc = newLoc;
    }

    public LatLng getUserLoc(){
        return userLoc;
    }

    public void saveTripState(){
        MockDbHelper dbHelper = new MockDbHelper(ctx);
        SQLiteDatabase readDb = dbHelper.getReadableDatabase();
        SQLiteDatabase writeDb = dbHelper.getWritableDatabase();
        TripDAO tripDAO = new TripDAOOptimized(readDb, writeDb);

        tripDAO.changeLastVisitedPoint(currentTrip.getID(), currentTrip.getLastVisitedPoint());

        readDb.close();
        writeDb.close();
    }

    private int nextControlPoint(){
        int index = -1;
        List<ControlPoint> cpList= currentTrip.getModifiedRoute();

        if(currentTrip.getCurrentTarget() instanceof ControlPoint){
            index = cpList.indexOf((ControlPoint) currentTrip.getCurrentTarget());
        }
        else{
            index = cpList.indexOf(currentTrip.getLastVisitedPoint());
        }

        try {
            if (index != -1) {
                currentTrip.setCurrentTarget(cpList.get(++index));
            }
        }catch(DataException de){
            de.printStackTrace();
        }

        return index<0?-1: index< cpList.size()?0:1;
    }

    public void setNavigation(Point navigateTo){
        try {
            currentTrip.setCurrentTarget(navigateTo);
        }catch(DataException de){
            de.printStackTrace();
        }
    }

    public int checkIfPointReached() throws Exception {
        Point point=currentTrip.getCurrentTarget();
        if(DistanceCalculator.distance(userLoc.latitude, userLoc.longitude, point.getLatitude(), point.getLongitude())<=MIN_DISTANCE)
        {
            tripNotificator.setNotification(ctx, point);
            int nextControlPoint = nextControlPoint();
            if(nextControlPoint==0)
            {
                return 1;
            }
            if(nextControlPoint==1)
            {
                return 2;
            }
            else
            {
                throw new Exception("Następny punkt -1 / TripController, nextControlPoint");
            }
        }

        return 0;
    }
}
