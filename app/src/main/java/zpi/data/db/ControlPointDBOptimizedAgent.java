package zpi.data.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import zpi.data.model.ControlPoint;
import zpi.data.model.DataException;

/**
 * Created by Ania on 2017-04-24.
 */

public class ControlPointDBOptimizedAgent implements ControlPointDBAgent {
    SQLiteDatabase readableDb;
    SQLiteDatabase writableDb;

    public ControlPointDBOptimizedAgent(SQLiteDatabase read, SQLiteDatabase write){
        this.readableDb = read;
        this.writableDb = write;
    }

    public ControlPoint getControlPoint(String name){
        ControlPoint cp = null;

        String selection = MockContract.ControlPointEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = {name};

        int i;

        Cursor cursor = readableDb.query(MockContract.ControlPointEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()){
            try {
                i = 1;
                cp = new ControlPoint(cursor.getString(i++), cursor.getString(i++), cursor.getDouble(i++), cursor.getDouble(i++), cursor.getString(i++),
                        new Date(cursor.getLong(i++)), cursor.getInt(i++), cursor.getInt(i++), cursor.getInt(i++), cursor.getInt(i++));
            }catch(DataException de){
                System.err.println(de);
            }
        }
        return null; //TODO

    }

    public int createControlPoint(ControlPoint cp){
        return -1; //TODO
    }

    public int getId(String name){
        return -1; //TODO
    }
}
