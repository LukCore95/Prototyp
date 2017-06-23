package zpi.controler.trip;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.widget.Toast;

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
 * @author Wojciech Michałowski
 * @author Adrianna Łapucha
 * Main controller of app's work. Manages the route during app's running.
 */
public final class TripController {
    protected static final float MIN_DISTANCE=0.03f; //activation distance
    protected static final float MIN_DISTANCE_IP=0.1f;
    private Trip currentTrip;
    private Context ctx;
    private LatLng userLoc = null;
    private TripNotificator tripNotificator;

    /**
     * This constructor gets the Route parameter and extracts Trip related to the given route.
     * @param ctx Context of application
     * @param route Route to extract trip from. This trip will be managed during the controller work.
     */
    public TripController(Context ctx, Route route){
        this.ctx = ctx;
        loadTripFromDatabase(route);
        tripNotificator=new TripNotificator();
    }

    /**
     * @param ctx Context of application
     * @param trip Trip to manage
     */
    public TripController(Context ctx, Trip trip){
        currentTrip = trip;
        this.ctx = ctx;
        tripNotificator=new TripNotificator();
    }

    /**
     * @return The list of control points ordered from the startPoint of the trip.
     */
    public List<ControlPoint> getRouteControlPoints(){
        return currentTrip.getModifiedRoute();
    }

    /**
     * @return Current navigation target.
     */
    public Point getCurrentCP(){
        return currentTrip.getCurrentTarget();
    }

    /**
     * @return Start point of the trip.
     */
    public ControlPoint getStartCP(){
        return currentTrip.getStartPoint();
    }

    /**
     * @return Trip being managed by this controller.
     */
    public Trip getCurrentTrip(){
        return currentTrip;
    }

    /**
     * Updated user coordinates in this controller
     * @param newLoc User coordinates
     */
    public void setUserLoc(LatLng newLoc){
        userLoc = newLoc;
    }

    /**
     * @return User coordinates registered in this controller.
     */
    public LatLng getUserLoc(){
        return userLoc;
    }

    /**
     * Creates new trip, adds it to DB and to this controller. Deletes current trip if its related to the same route.
     * @param route New trip's route
     * @param startPoint New start point (must be contained in the route!)
     */
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

    /**
     * Makes changes to the DB that saves the lastVisitedPoint of managed trip.
     */
    public void saveTripState(){
        MockDbHelper dbHelper = new MockDbHelper(ctx);
        SQLiteDatabase readDb = dbHelper.getReadableDatabase();
        SQLiteDatabase writeDb = dbHelper.getWritableDatabase();
        TripDAO tripDAO = new TripDAOOptimized(readDb, writeDb);

        tripDAO.changeLastVisitedPoint(currentTrip.getID(), currentTrip.getLastVisitedPoint());

        readDb.close();
        writeDb.close();
    }

    /**
     * Set navigation to given point. Not working for Control Point!
     * Control Point may be set as current target only in order by calling private nextControlPoint method!
     * @param navigateTo Point to navigate to
     */
    public void setNavigation(Point navigateTo){
        if(navigateTo instanceof ControlPoint) {
            try {
                List<ControlPoint> cplist = currentTrip.getModifiedRoute();
                int currentIndex = cplist.indexOf(navigateTo);
                currentTrip.setCurrentTarget(navigateTo);
                currentTrip.setLastVisitedPoint(currentIndex>0?cplist.get(currentIndex-1):null);
            }catch(DataException de){
                de.printStackTrace();
            }
        }

        else {
            try {
                currentTrip.setCurrentTarget(navigateTo);
            } catch (DataException de) {
                de.printStackTrace();
            }
        }
    }

    /**
     * Check's if the current target is reached by user and navigates to next control point if available.
     * @return Code: 0 - target not reached yet; 1 - target reached; 2 - last target on list reached
     * @throws Exception Error in nextControlPoint private method implementation. Current target seems to be missing in the control point list!
     */
    public int checkIfPointReached() throws Exception {
        Point point=currentTrip.getCurrentTarget();

        //Toast.makeText(ctx, "Dystans: " + DistanceCalculator.distance(userLoc.latitude, userLoc.longitude, point.getLatitude(), point.getLongitude()), Toast.LENGTH_LONG).show();
        if((point instanceof ControlPoint)?DistanceCalculator.distance(userLoc.latitude, userLoc.longitude, point.getLatitude(), point.getLongitude())<=MIN_DISTANCE:DistanceCalculator.distance(userLoc.latitude, userLoc.longitude, point.getLatitude(), point.getLongitude())<=MIN_DISTANCE_IP)
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
                throw new Exception("Error in nextControlPoint private method implementation. Current target seems to be missing in the control point list!");
            }
        }

        return 0;
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

    //return -1 - ERROR; 0 - success; 1 - no more points on list (current target before this method is the last one)
    private int nextControlPoint(){
        int index = -1;
        List<ControlPoint> cpList= currentTrip.getModifiedRoute();

        if(currentTrip.getCurrentTarget() instanceof ControlPoint){
            index = cpList.indexOf((ControlPoint) currentTrip.getCurrentTarget());
            currentTrip.setLastVisitedPoint((ControlPoint) currentTrip.getCurrentTarget());
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

    /**
     * Gets the formatted string, that represents distance to current target.
     * @return Formatted string representing distance.
     */
    public String getDistanceToText()
    {
        int dist = (int)(DistanceCalculator.distance(userLoc.latitude, userLoc.longitude, getCurrentCP().getGeoLoc().latitude, getCurrentCP().getGeoLoc().longitude)*1000);
        String distance="";
        if(dist >= 2000)
            distance=("" + String.format("%.1f", ((float)dist)/1000) + "km");
        else
            distance="" + dist + "m";
        return distance;
    }
}
