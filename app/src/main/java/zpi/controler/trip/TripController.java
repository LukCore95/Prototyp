package zpi.controler.trip;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

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

/**
 * Created by Ania on 2017-05-04.
 */

public final class TripController {
    private Trip currentTrip;
    private Context ctx;
    private LatLng userLoc = null;
    private static final int MIN_DISTANCE=50;
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
}
