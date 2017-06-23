package zpi.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zpi.data.db.MockContract;
import zpi.data.model.DataException;
import zpi.data.model.InterestingPlace;
import zpi.data.model.InterestingPlaceType;
import zpi.data.model.RestPoint;
import zpi.data.model.RestPointType;

/**
 * @author Wojciech Michałowski
 * Optimized implementation od RestPointDAO interface. Using this class requires injection of readable and/or writable database. Writable database can be null if
 * you're not planning to use writing method createInterestingPlace. Using method createInterestingPlace with a null-writableDb DAO object will result in Exception.
 * To get your instance of readable/writable database simply call getWritableDatabase()/getReadableDatabase() of your MockDbHelper object. After performing DB operations on this class
 * you need to close database connection by calling close() method of the database object.
 */
public class RestPointDAOOptimized implements RestPointDAO {
    SQLiteDatabase readableDb;
    SQLiteDatabase writableDb;

    public RestPointDAOOptimized(SQLiteDatabase read, SQLiteDatabase write){
        readableDb = read;
        writableDb = write;
    }

    public RestPoint getRestPoint(String name){
        RestPoint rp = null;
        RestPointPhotoDAO photoAgent;
        int rpId = -1;
        String selection = MockContract.RestPointEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = {name};

        RestPointType type;
        Cursor cursor = readableDb.query(MockContract.RestPointEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        int i = 1;

        if(cursor.moveToFirst()){
            try {
                rp = new RestPoint(cursor.getString(i++), cursor.getString(i++), cursor.getDouble(i++), cursor.getDouble(i++), RestPointType.fromIntToType(cursor.getInt(i++)), cursor.getString(i++), cursor.getString(i));
                rpId = getId(name);
            }catch(DataException de){
                System.err.println(de);
            }
        }

        if(rp != null){
            photoAgent = new RestPointPhotoDAOOptimized(readableDb, null);
            rp.setOldPhotos(photoAgent.getRestPointsPhotos(name));
        }

        return rp;
    }

    public int getId(String name){
        int id = -1;

        String[] projection = {MockContract.RestPointEntry.COLUMN_NAME_ID};
        String selection = MockContract.RestPointEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = {name};

        Cursor cursor = readableDb.query(MockContract.RestPointEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst())
            id = cursor.getInt(0);

        return id;
    }

    public int createRestPoint(RestPoint rp){
        int id = -1;
        String name = rp.getName();
        RestPointPhotoDAO photoAgent = new RestPointPhotoDAOOptimized(readableDb, writableDb);

        ContentValues values = new ContentValues();
        values.put(MockContract.RestPointEntry.COLUMN_NAME_NAME, name);
        values.put(MockContract.RestPointEntry.COLUMN_NAME_DESC, rp.getDescription());
        values.put(MockContract.RestPointEntry.COLUMN_NAME_LONG, rp.getLongitude());
        values.put(MockContract.RestPointEntry.COLUMN_NAME_LAT, rp.getLatitude());
        values.put(MockContract.RestPointEntry.COLUMN_NAME_TYPE, RestPointType.fromTypeToInt(rp.getType()));
        values.put(MockContract.RestPointEntry.COLUMN_NAME_ADDRESS, rp.getAddress());
        values.put(MockContract.RestPointEntry.COLUMN_NAME_WEB, rp.getWeb());

        writableDb.insert(MockContract.RestPointEntry.TABLE_NAME, null, values);

        if(photoAgent.insertPhotosFromRestPoint(rp))
            id = getId(name);

        //System.out.println("DODANO RP: " + id);
        return id;
    }

    public List<RestPoint> getAllRestPoints(){
        RestPointPhotoDAO rpPhotoDao = new RestPointPhotoDAOOptimized(readableDb, null);
        List<RestPoint> rpList = new ArrayList<RestPoint>();
        List<Integer> photos;
        RestPoint curr;
        Cursor cursor = readableDb.query(MockContract.RestPointEntry.TABLE_NAME, null, null, null, null, null, null);

        while(cursor.moveToNext()){
            int i = 1;
            try {
                curr = new RestPoint(cursor.getString(i++), cursor.getString(i++), cursor.getDouble(i++), cursor.getDouble(i++), RestPointType.fromIntToType(cursor.getInt(i++)), cursor.getString(i++), cursor.getString(i));
                photos = rpPhotoDao.getRestPointsPhotos(curr.getName());
                curr.setOldPhotos(photos);
                rpList.add(curr);
            }catch (DataException de){
                System.err.println(de);
            }
        }

        cursor.close();

        return rpList;
    }
}
