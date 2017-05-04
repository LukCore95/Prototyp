package zpi.prototyp;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import java.util.LinkedList;
import java.util.List;

import zpi.data.db.MockDbHelper;
import zpi.data.db.dao.ControlPointDAO;
import zpi.data.db.dao.ControlPointDAOOptimized;
import zpi.data.db.dao.InterestingPlaceDAO;
import zpi.data.db.dao.InterestingPlaceDAOOptimized;
import zpi.data.db.dao.RouteDAO;
import zpi.data.db.dao.RouteDAOOptimized;
import zpi.data.db.dao.TripDAO;
import zpi.data.db.dao.TripDAOOptimized;
import zpi.data.model.ControlPoint;
import zpi.data.model.DataException;
import zpi.data.model.InterestingPlace;
import zpi.data.model.Route;
import zpi.data.model.Trip;

public class SplashScreen extends Activity implements Runnable {

    private static int SPLASH_TIME_OUT = 4500;
    //static String [] namesOfControlPoints = {"Dom handlowy Renoma", "Podwale", "Plac Teatralny", "Ulica Świdnicka", "Ulica Sądowa"};
    //static List<ControlPoint> controlPoints;
    static Trip currentTrip;
    static List<InterestingPlace> ipList;
    static boolean firstTrip = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);

        new Thread(this).start();


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        //controlPoints= new LinkedList<ControlPoint>();
        MockDbHelper dbHelp = new MockDbHelper(this);
        SQLiteDatabase readableDb = dbHelp.getReadableDatabase();
        SQLiteDatabase writableDb = dbHelp.getWritableDatabase();
        TripDAO tripdao = new TripDAOOptimized(readableDb, writableDb);
        InterestingPlaceDAO ipDao = new InterestingPlaceDAOOptimized(readableDb, null);

        /*for(int i=0; i<namesOfControlPoints.length; i++)
        {
            try {
                controlPoints[i]=new ControlPoint(cpdao.getControlPoint(namesOfControlPoints[i]));
            } catch (DataException e) {
                System.out.println("SplashScreen DataBase exception");
                e.printStackTrace();
            }
        }*/
        Trip trip = tripdao.getTrip(1);
        ipList = ipDao.getAllInterestingPlaces();
        if(trip != null) {
            System.out.println("WCZYTANO TRIP. PUNKT STARTOWY: " + trip.getStartPoint().getName());
            System.out.println("TRASA: ");
            for (ControlPoint cp : trip.getModifiedRoute())
                System.out.println("" + cp.getName());
        }
        if(trip == null){
            System.out.println("NIE WCZYTANO TRIPA");
            firstTrip = true;
            Route route = new RouteDAOOptimized(readableDb, null).getRoute("Trasa testowa");
            try {
                trip = new Trip(route, route.getRoutePoints().getFirst(), 1);
            }catch(DataException de){
                System.err.println(de);
            }

            tripdao.createTrip(trip);
        }

        //controlPoints = trip.getModifiedRoute();
        currentTrip = trip;

       /* for(ControlPoint cp: controlPoints)
            System.out.println("Wczytano punkt: " + cp.getName());*/

        dbHelp.close();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    public void run(){
        new MockDbHelper(this).getWritableDatabase().close();
    }

    public static Trip getTrip()
    {
        return currentTrip;
    }
}