package zpi.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import zpi.data.model.DataException;
import zpi.data.model.InterestingPlace;

/**
 * Created by Ania on 2017-04-25.
 */

public class InterestingPlaceDBOptimizedAgent implements InterestingPlaceDBAgent {
    SQLiteDatabase readableDb;
    SQLiteDatabase writableDb;

    public InterestingPlaceDBOptimizedAgent(SQLiteDatabase read, SQLiteDatabase write){
        readableDb = read;
        writableDb = write;
    }

    public InterestingPlace getInterestingPlace(String name){
        InterestingPlace ip = null;
        InterestingPlacePhotoDBAgent photoAgent;
        int ipId = -1;
        String selection = MockContract.InterestingPlaceEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = {name};

        Cursor cursor = readableDb.query(MockContract.InterestingPlaceEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        int i = 1;

        if(cursor.moveToFirst()){
            try {
                ip = new InterestingPlace(cursor.getString(i++), cursor.getString(i++), cursor.getDouble(i++), cursor.getDouble(i));
                ipId = getId(name);
            }catch(DataException de){
                System.err.println(de);
            }
        }

        if(ip != null){
            photoAgent = new InterestingPlacePhotoDBOptimizedAgent(readableDb, null);
            ip.setOldPhotos(photoAgent.getInterestingPlacesPhotos(name));
        }

        return ip;
    }

    public int getId(String name){
        int id = -1;

        String[] projection = {MockContract.InterestingPlaceEntry.COLUMN_NAME_ID};
        String selection = MockContract.InterestingPlaceEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = {name};

        Cursor cursor = readableDb.query(MockContract.InterestingPlaceEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst())
            id = cursor.getInt(0);

        return id;
    }

    public int createInterestingPlace(InterestingPlace ip){
        int id = -1;
        String name = ip.getName();
        InterestingPlacePhotoDBAgent photoAgent = new InterestingPlacePhotoDBOptimizedAgent(readableDb, writableDb);

        ContentValues values = new ContentValues();
        values.put(MockContract.InterestingPlaceEntry.COLUMN_NAME_NAME, name);
        values.put(MockContract.InterestingPlaceEntry.COLUMN_NAME_DESC, ip.getDescription());
        values.put(MockContract.InterestingPlaceEntry.COLUMN_NAME_LONG, ip.getLongitude());
        values.put(MockContract.InterestingPlaceEntry.COLUMN_NAME_LAT, ip.getLatitude());

        writableDb.insert(MockContract.InterestingPlaceEntry.TABLE_NAME, null, values);

        if(photoAgent.insertPhotosFromInterestingPlace(ip))
            id = getId(name);

        return id;
    }
}
