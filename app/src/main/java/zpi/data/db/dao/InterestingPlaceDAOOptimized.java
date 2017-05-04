package zpi.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zpi.data.db.MockContract;
import zpi.data.model.DataException;
import zpi.data.model.InterestingPlace;

/**
 * @author Wojciech Michałowski
 * Optimized implementation od InterestingPlaceDAO interface. Using this class requires injection of readable and/or writable database. Writable database can be null if
 * you're not planning to use writing method createInterestingPlace. Using method createInterestingPlace with a null-writableDb DAO object will result in Exception.
 * To get your instance of readable/writable database simply call getWritableDatabase()/getReadableDatabase() of your MockDbHelper object. After performing DB operations on this class
 * you need to close database connection by calling close() method of the database object.
 */
public class InterestingPlaceDAOOptimized implements InterestingPlaceDAO {
    SQLiteDatabase readableDb;
    SQLiteDatabase writableDb;

    public InterestingPlaceDAOOptimized(SQLiteDatabase read, SQLiteDatabase write){
        readableDb = read;
        writableDb = write;
    }

    public InterestingPlace getInterestingPlace(String name){
        InterestingPlace ip = null;
        InterestingPlacePhotoDAO photoAgent;
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
            photoAgent = new InterestingPlacePhotoDAOOptimized(readableDb, null);
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
        InterestingPlacePhotoDAO photoAgent = new InterestingPlacePhotoDAOOptimized(readableDb, writableDb);

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

    public List<InterestingPlace> getAllInterestingPlaces(){
        InterestingPlacePhotoDAO ipPhotoDao = new InterestingPlacePhotoDAOOptimized(readableDb, null);
        List<InterestingPlace> ipList = new ArrayList<InterestingPlace>();
        List<Integer> photos;
        InterestingPlace curr;
        Cursor cursor = readableDb.query(MockContract.InterestingPlaceEntry.TABLE_NAME, null, null, null, null, null, null);

        while(cursor.moveToNext()){
            int i = 1;
            try {
                curr = new InterestingPlace(cursor.getString(i++), cursor.getString(i++), cursor.getDouble(i++), cursor.getDouble(i));
                photos = ipPhotoDao.getInterestingPlacesPhotos(curr.getName());
                curr.setOldPhotos(photos);
                ipList.add(curr);
            }catch (DataException de){
                System.err.println(de);
            }
        }

        cursor.close();

        return ipList;
    }
}
