package zpi.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import zpi.data.model.DataException;
import zpi.data.model.RestPoint;
import zpi.data.model.RestPointType;

/**
 * Created by Ania on 2017-04-25.
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
                rp = new RestPoint(cursor.getString(i++), cursor.getString(i++), cursor.getDouble(i++), cursor.getDouble(i++), RestPointType.fromIntToType(cursor.getInt(i)));
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

        writableDb.insert(MockContract.RestPointEntry.TABLE_NAME, null, values);

        if(photoAgent.insertPhotosFromRestPoint(rp))
            id = getId(name);

        return id;
    }
}
