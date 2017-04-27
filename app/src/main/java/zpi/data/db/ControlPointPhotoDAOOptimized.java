package zpi.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zpi.data.model.ControlPoint;

/**
 * @author Wojciech Michałowski
 * Optimized implementation od ControlPointPhotoDAO interface. Using this class requires injection of readable and/or writable database. Writable database can be null if
 * you're not planning to use writing method insertPhotosFromControlPoint. Using method insertPhotosFromControlPoint with a null-writableDb DAO object will result in Exception.
 * To get your instance of readable/writable database simply call getWritableDatabase()/getReadableDatabase() of your MockDbHelper object. After performing DB operations on this class
 * you need to close database connection by calling close() method of the database object.
 */
public class ControlPointPhotoDAOOptimized implements ControlPointPhotoDAO {
    SQLiteDatabase readableDb;
    SQLiteDatabase writableDb;

    public ControlPointPhotoDAOOptimized(SQLiteDatabase read, SQLiteDatabase write){
        readableDb = read;
        writableDb = write;
    }

    public boolean insertPhotosFromControlPoint(ControlPoint cp){
        ControlPointDAO cpAgent = new ControlPointDAOOptimized(readableDb, null);
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
        ControlPointDAO cpAgent = new ControlPointDAOOptimized(readableDb, null);
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

    /**
     * This method is disabled int this implementation and returns -1
     * @param name Name of object (e.g. control point or route)
     * @return Always returns -1
     */
    public int getId(String name){
        return -1; //empty method
    }


}
