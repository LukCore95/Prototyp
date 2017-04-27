package zpi.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import zpi.data.model.ControlPoint;
import zpi.data.model.DataException;

/**
 * Created by Ania on 2017-04-24.
 */

public class ControlPointDAOOptimized implements ControlPointDAO {
    SQLiteDatabase readableDb;
    SQLiteDatabase writableDb;

    public ControlPointDAOOptimized(SQLiteDatabase read, SQLiteDatabase write){
        this.readableDb = read;
        this.writableDb = write;
    }

    public ControlPoint getControlPoint(String name){
        ControlPoint cp = null;
        int cpId = -1;

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

        if(cp != null) {
            cpId = getId(name);

            ControlPointPhotoDAO cppAgent = new ControlPointPhotoDAOOptimized(readableDb, null);
            cp.setOldPhotos(cppAgent.getControlPointsPhotos(name));
        }

        return cp;
    }

    public ControlPoint getControlPoint(int id){
        ControlPoint cp = null;
        int cpId = id;

        String selection = MockContract.ControlPointEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {"" + cpId};

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

        if(cp != null) {
            ControlPointPhotoDAO cppAgent = new ControlPointPhotoDAOOptimized(readableDb, null);
            cp.setOldPhotos(cppAgent.getControlPointsPhotos(cp.getName()));
        }

        return cp;
    }

    public int createControlPoint(ControlPoint cp){
        int id = -1;
        String name = cp.getName();
        ControlPointPhotoDAO photoAgent = new ControlPointPhotoDAOOptimized(readableDb, writableDb);

        ContentValues values = new ContentValues();
        values.put(MockContract.ControlPointEntry.COLUMN_NAME_NAME, name);
        values.put(MockContract.ControlPointEntry.COLUMN_NAME_DESC, cp.getDescription());
        values.put(MockContract.ControlPointEntry.COLUMN_NAME_LONG, cp.getLongitude());
        values.put(MockContract.ControlPointEntry.COLUMN_NAME_LAT, cp.getLatitude());
        values.put(MockContract.ControlPointEntry.COLUMN_NAME_GERMAN, cp.getGermanName());
        values.put(MockContract.ControlPointEntry.COLUMN_NAME_DATE, cp.getDate().getTime());
        values.put(MockContract.ControlPointEntry.COLUMN_NAME_ICON, cp.getIcon());
        values.put(MockContract.ControlPointEntry.COLUMN_NAME_SLNEW, cp.getSliderNewPhoto());
        values.put(MockContract.ControlPointEntry.COLUMN_NAME_SLOLD, cp.getSliderOldPhoto());
        values.put(MockContract.ControlPointEntry.COLUMN_NAME_AUDIO, cp.getAudiobook());

        writableDb.insert(MockContract.ControlPointEntry.TABLE_NAME, null, values);

        if(photoAgent.insertPhotosFromControlPoint(cp))
            id = getId(name);

        return id;
    }

    public int getId(String name){
        int cpId = -1;

        String[] projection = {MockContract.ControlPointEntry.COLUMN_NAME_ID};
        String selection = MockContract.ControlPointEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = {name};

        Cursor cursor = readableDb.query(MockContract.ControlPointEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst())
            cpId = cursor.getInt(0);

        return cpId;
    }
}
