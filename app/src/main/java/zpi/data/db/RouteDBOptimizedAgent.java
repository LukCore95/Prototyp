package zpi.data.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

import zpi.data.model.ControlPoint;
import zpi.data.model.DataException;
import zpi.data.model.Route;

/**
 * Created by Ania on 2017-04-24.
 */

public class RouteDBOptimizedAgent implements RouteDBAgent {
    SQLiteDatabase readableDb;
    SQLiteDatabase writableDb;

    public RouteDBOptimizedAgent(SQLiteDatabase read, SQLiteDatabase write){
        readableDb = read;
        writableDb = write;
    }

    public Route getRoute(String name){
        Route route = null;
        int routeId = -1;

        String[] projection = null;
        String selection = MockContract.RouteEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs1 = {name};


        Cursor cursor = readableDb.query(MockContract.RouteEntry.TABLE_NAME, projection, selection, selectionArgs1, null, null, null);

        if(cursor.moveToFirst()){
            try {
                route = new Route(cursor.getString(1));
                Map<Integer, ControlPoint> points = new HashMap<Integer, ControlPoint>();
                routeId = cursor.getInt(0);
            } catch(DataException de){
                System.err.println(de);
            }
        }

        cursor.close();

        String[] selectionArgs2 = {"" + routeId};

        if(route != null){
            projection = null;
            selection = MockContract.RoutePointEntry.COLUMN_NAME_ROUTE + " = ?";

            cursor = readableDb.query(MockContract.RoutePointEntry.TABLE_NAME, projection, selection, selectionArgs2, null, null, null);
        }

        return null;//TODO
    }

    public int createRoute(Route route){
        return -1;//TODO
    }

    public int getId(String name){
        return -1;//TODO
    }
}
