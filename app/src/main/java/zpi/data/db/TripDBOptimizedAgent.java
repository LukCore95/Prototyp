package zpi.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import zpi.data.model.ControlPoint;
import zpi.data.model.Trip;

/**
 * Created by Ania on 2017-04-25.
 */

public class TripDBOptimizedAgent implements TripDBAgent {
    SQLiteDatabase readableDb;
    SQLiteDatabase writableDb;

    public TripDBOptimizedAgent(SQLiteDatabase read, SQLiteDatabase write){
        readableDb = read;
        writableDb = write;
    }

    public int createTrip(Trip trip){
        int id = -1;
        int startId = -1;
        int lastId = -1;
        int rId = -1;

        ControlPointDBAgent cpAgent = new ControlPointDBOptimizedAgent(readableDb, null);
        RouteDBAgent routeAgent = new RouteDBOptimizedAgent(readableDb, null);

        if((startId = cpAgent.getId(trip.getStartPoint().getName())) == -1 ||
                (lastId = cpAgent.getId(trip.getLastVisitedPoint().getName())) == -1 ||
                (rId = routeAgent.getId(trip.getRoute().getName())) == -1)
            return id;

        ContentValues values = new ContentValues();
        values.put(MockContract.TripEntry.COLUMN_NAME_START, startId);
        values.put(MockContract.TripEntry.COLUMN_NAME_LAST, lastId);
        values.put(MockContract.TripEntry.COLUMN_NAME_ROUTE, rId);
        id = (int)writableDb.insert(MockContract.TripEntry.TABLE_NAME, null, values);

        return id;
    }

    public Trip getTrip(int id){
        Trip trip = null;

        String selection = MockContract.TripEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {"" + id};

        ControlPointDBAgent cpAgent = new ControlPointDBOptimizedAgent(readableDb, null);
        RouteDBAgent routeAgent = new RouteDBOptimizedAgent(readableDb, null);

        Cursor cursor = readableDb.query(MockContract.TripEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst()){
          //  trip = new Trip(r)
        }

        return null;//TODO
    }

    public int getId(String name){
        return -1;//TODO
    }

    public boolean changeLastVisitedPoint(ControlPoint cp){
        return false;//TODO
    }
}
