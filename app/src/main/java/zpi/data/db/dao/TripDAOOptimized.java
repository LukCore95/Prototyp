package zpi.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import zpi.data.db.MockContract;
import zpi.data.model.ControlPoint;
import zpi.data.model.DataException;
import zpi.data.model.Trip;

/**
 * Created by Ania on 2017-04-25.
 */

public class TripDAOOptimized implements TripDAO {
    SQLiteDatabase readableDb;
    SQLiteDatabase writableDb;

    public TripDAOOptimized(SQLiteDatabase read, SQLiteDatabase write){
        readableDb = read;
        writableDb = write;
    }

    public int createTrip(Trip trip){
        int id = -1;
        int startId = -1;
        int lastId = -1;
        int rId = -1;

        ControlPointDAO cpAgent = new ControlPointDAOOptimized(readableDb, null);
        RouteDAO routeAgent = new RouteDAOOptimized(readableDb, null);

        if((startId = cpAgent.getId(trip.getStartPoint().getName())) == -1 ||
                (rId = routeAgent.getId(trip.getRoute().getName())) == -1)
            return id;

        ContentValues values = new ContentValues();
        values.put(MockContract.TripEntry.COLUMN_NAME_START, startId);

        if(trip.getLastVisitedPoint() != null)
            values.put(MockContract.TripEntry.COLUMN_NAME_LAST, lastId);

        values.put(MockContract.TripEntry.COLUMN_NAME_ROUTE, rId);
        id = (int)writableDb.insert(MockContract.TripEntry.TABLE_NAME, null, values);

        return id;
    }

    public Trip getTrip(int id){
        Trip trip = null;

        String selection = MockContract.TripEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {"" + id};

        ControlPointDAO cpAgent = new ControlPointDAOOptimized(readableDb, null);
        RouteDAO routeAgent = new RouteDAOOptimized(readableDb, null);

        Cursor cursor = readableDb.query(MockContract.TripEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst()){
            try {
                ControlPoint lastVisited = cpAgent.getControlPoint(cursor.getInt(2));
                if(lastVisited != null)
                    trip = new Trip(routeAgent.getRoute(cursor.getInt(3)), cpAgent.getControlPoint(cursor.getInt(1)), cursor.getInt(0));
                else
                    trip = new Trip(routeAgent.getRoute(cursor.getInt(3)), cpAgent.getControlPoint(cursor.getInt(1)), cpAgent.getControlPoint(cursor.getInt(2)), cursor.getInt(0));
            } catch(DataException de){
                System.err.println(de);
            }
        }
        cursor.close();

        return trip;
    }

    public int getId(String name){
        int id = -1;

        RouteDAO routeAgent = new RouteDAOOptimized(readableDb, null);

        String[] projection = {MockContract.TripEntry.COLUMN_NAME_ID};
        String selection = MockContract.TripEntry.COLUMN_NAME_ROUTE + " = ?";
        String[] selectionArgs = {"" + routeAgent.getId(name)};

        Cursor cursor = readableDb.query(MockContract.TripEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst()){
            id = cursor.getInt(0);
        }

        cursor.close();
        return id;
    }

    public boolean changeLastVisitedPoint(int id, ControlPoint cp){
        if(cp != null) {
            ControlPointDAO cpAgent = new ControlPointDAOOptimized(readableDb, null);

            String where = MockContract.TripEntry.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {"" + id};
            ContentValues values = new ContentValues();
            values.put(MockContract.TripEntry.COLUMN_NAME_LAST, cpAgent.getId(cp.getName()));

            return writableDb.update(MockContract.TripEntry.TABLE_NAME, values, where, whereArgs) > 0;
        }
        else
            return false;
    }

    public boolean deleteTrip(int ID){
        String where = MockContract.TripEntry.COLUMN_NAME_ID + " = ?";
        String[] whereArgs = {"" + ID};

        return writableDb.delete(MockContract.TripEntry.TABLE_NAME, where, whereArgs)>0;
    }

}
