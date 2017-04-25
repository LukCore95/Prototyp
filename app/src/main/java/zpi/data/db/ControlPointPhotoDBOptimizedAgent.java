package zpi.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zpi.data.model.ControlPoint;

/**
 * Created by Ania on 2017-04-24.
 */

public class ControlPointPhotoDBOptimizedAgent implements ControlPointPhotoDBAgent {
    SQLiteDatabase readableDb;
    SQLiteDatabase writableDb;

    public ControlPointPhotoDBOptimizedAgent(SQLiteDatabase read, SQLiteDatabase write){
        readableDb = read;
        writableDb = write;
    }

    public boolean insertPhotosFromControlPoint(ControlPoint cp){
        ControlPointDBAgent cpAgent = new ControlPointDBOptimizedAgent(readableDb, null);
        int cpId = cpAgent.getId(cp.getName());

        ContentValues values = new ContentValues();
        for(Integer photo: cp.getOldPhotos()){
            values.clear();
            values.put(MockContract.ControlPointPhotoEntry.COLUMN_NAME_ID, photo);
            values.put(MockContract.ControlPointPhotoEntry.COLUMN_NAME_POINT, cpId);
            writableDb.insert(MockContract.ControlPointPhotoEntry.TABLE_NAME, null, values);
        }

        return true;
    }

    public List<Integer> getControlPointsPhotos(String name){
        ControlPointDBAgent cpAgent = new ControlPointDBOptimizedAgent(readableDb, null);
        int cpId = cpAgent.getId(name);

        List<Integer> oldPhotos = new ArrayList<Integer>();

        String[] projection = {MockContract.ControlPointPhotoEntry.COLUMN_NAME_ID};
        String selection = MockContract.ControlPointPhotoEntry.COLUMN_NAME_POINT + " = ?";
        String[] selectionArgs = {"" + cpId};

        Cursor cursor = readableDb.query(MockContract.ControlPointPhotoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        while(cursor.moveToNext())
            oldPhotos.add(new Integer(cursor.getInt(0)));

        return oldPhotos;
    }

    public int getId(String name){
        return -1; //empty method
    }


}
