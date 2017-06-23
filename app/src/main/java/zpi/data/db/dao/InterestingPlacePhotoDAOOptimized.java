package zpi.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zpi.data.db.MockContract;
import zpi.data.model.InterestingPlace;

/**
 * @author Wojciech Michałowski
 * Optimized implementation od InterestingPlacePhotoDAO interface. Using this class requires injection of readable and/or writable database. Writable database can be null if
 * you're not planning to use writing method createInterestingPlace. Using method createInterestingPlace with a null-writableDb DAO object will result in Exception.
 * To get your instance of readable/writable database simply call getWritableDatabase()/getReadableDatabase() of your MockDbHelper object. After performing DB operations on this class
 * you need to close database connection by calling close() method of the database object.
 */
public class InterestingPlacePhotoDAOOptimized implements InterestingPlacePhotoDAO {
    SQLiteDatabase readableDb;
    SQLiteDatabase writableDb;

    public InterestingPlacePhotoDAOOptimized(SQLiteDatabase read, SQLiteDatabase write){
        readableDb = read;
        writableDb = write;
    }

    public List<Integer> getInterestingPlacesPhotos(String name){
        List<Integer> oldPhotos = new ArrayList<Integer>();

        InterestingPlaceDAO ipAgent = new InterestingPlaceDAOOptimized(readableDb, null);
        int ipId = ipAgent.getId(name);

        String[] projection = {MockContract.InterestingPlacePhotoEntry.COLUMN_NAME_ID};
        String selection = MockContract.InterestingPlacePhotoEntry.COLUMN_NAME_POINT + " = ?";
        String[] selectionArgs = {"" + ipId};

        Cursor cursor = readableDb.query(MockContract.InterestingPlacePhotoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        while(cursor.moveToNext()){
            oldPhotos.add(new Integer(cursor.getInt(0)));
        }

        return oldPhotos;
    }

    public boolean insertPhotosFromInterestingPlace(InterestingPlace ip){
        InterestingPlaceDAO ipAgent = new InterestingPlaceDAOOptimized(readableDb, null);

        int ipId = ipAgent.getId(ip.getName());

        ContentValues values = new ContentValues();
        for(Integer photo: ip.getOldPhotos()){
            values.clear();
            values.put(MockContract.InterestingPlacePhotoEntry.COLUMN_NAME_ID, photo);
            values.put(MockContract.InterestingPlacePhotoEntry.COLUMN_NAME_POINT, ipId);
            writableDb.insert(MockContract.InterestingPlacePhotoEntry.TABLE_NAME, null, values);
        }

        return true;
    }

    public int getId(String name){
        return -1; //doesnt work and wont work
    }
}

