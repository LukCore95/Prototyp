package zpi.data.db;

import android.content.ContentValues;
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

        String selection = MockContract.RouteEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs1 = {name};


        Cursor cursor = readableDb.query(MockContract.RouteEntry.TABLE_NAME, null, selection, selectionArgs1, null, null, null);

        if(cursor.moveToFirst()){
            try {
                route = new Route(cursor.getString(1));
                routeId = cursor.getInt(0);
            } catch(DataException de){
                System.err.println(de);
            }
        }

        cursor.close();

        String[] selectionArgs2 = {"" + routeId};

        if(route != null){
            selection = MockContract.RoutePointEntry.COLUMN_NAME_ROUTE + " = ?";
            Map<Integer, ControlPoint> points = new HashMap<Integer, ControlPoint>();
            cursor = readableDb.query(MockContract.RoutePointEntry.TABLE_NAME, null, selection, selectionArgs2, null, null, null);
            ControlPointDBAgent cpAgent = new ControlPointDBOptimizedAgent(readableDb, null);

            while(cursor.moveToNext()){
                points.put(cursor.getInt(0), cpAgent.getControlPoint(cursor.getInt(1)));
            }
        }

        return route;
    }

    public int createRoute(Route route){
        ControlPointDBAgent cpAgent = new ControlPointDBOptimizedAgent(readableDb, writableDb);
        int id = -1;
        int currentCpId = -1;
        ControlPoint currentCp = null;

        if(route == null)
            return id;

        Map<Integer, ControlPoint> cps = route.getRoutePoints();
        String name = route.getName();

        ContentValues values = new ContentValues();
        values.put(MockContract.RouteEntry.COLUMN_NAME_NAME, name);
        writableDb.insert(MockContract.RouteEntry.TABLE_NAME, null, values);
        values.clear();
        id = getId(name);

        for(int cpInd: cps.keySet()){
            currentCp = cps.get(cpInd);
            cpAgent.createControlPoint(currentCp);
            currentCpId = cpAgent.getId(currentCp.getName());

            values.put(MockContract.RoutePointEntry.COLUMN_NAME_NUMBER, cpInd);
            values.put(MockContract.RoutePointEntry.COLUMN_NAME_POINT, currentCpId);
            values.put(MockContract.RoutePointEntry.COLUMN_NAME_ROUTE, id);
            writableDb.insert(MockContract.RoutePointEntry.TABLE_NAME, null, values);
            values.clear();
        }

        return id;
    }

    public int getId(String name){
        int id = -1;

        String[] projection = {MockContract.RouteEntry.COLUMN_NAME_ID};
        String selection = MockContract.RouteEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = {name};

        Cursor cursor = readableDb.query(MockContract.RouteEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst())
            id = cursor.getInt(0);

        return id;
    }
}
