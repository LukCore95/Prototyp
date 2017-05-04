package zpi.controler.trip;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import zpi.data.db.MockDbHelper;
import zpi.data.db.dao.RouteDAO;
import zpi.data.db.dao.RouteDAOOptimized;
import zpi.data.db.dao.TripDAO;
import zpi.data.db.dao.TripDAOOptimized;
import zpi.data.model.ControlPoint;
import zpi.data.model.DataException;
import zpi.data.model.Route;
import zpi.data.model.Trip;

/**
 * Created by Ania on 2017-05-04.
 */

public final class TripControler {
    private Trip currentTrip;
    private Context ctx;

    public TripControler(Context ctx, Route route){
        this.ctx = ctx;
        loadTripFromDatabase(route);
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
    }

    public List<ControlPoint> getRouteControlPoints(){
        return currentTrip.getRoute().getRoutePoints();
    }

    public void setNewTrip(Route route, ControlPoint startPoint){
        try {
            currentTrip = new Trip(route, startPoint, 2); //TODO generate an index
        }catch(DataException de){
            System.err.println(de);
        }
    }
}
