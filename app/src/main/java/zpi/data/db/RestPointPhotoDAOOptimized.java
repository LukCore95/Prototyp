package zpi.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zpi.data.model.RestPoint;

/**
 * Created by Ania on 2017-04-25.
 */

public class RestPointPhotoDAOOptimized implements RestPointPhotoDAO {
    SQLiteDatabase readableDb;
    SQLiteDatabase writableDb;

    public RestPointPhotoDAOOptimized(SQLiteDatabase read, SQLiteDatabase write){
        readableDb = read;
        writableDb = write;
    }

    public List<Integer> getRestPointsPhotos(String name){
        List<Integer> oldPhotos = new ArrayList<Integer>();

        RestPointDAO rpAgent = new RestPointDAOOptimized(readableDb, null);
        int rpId = rpAgent.getId(name);

        String[] projection = {MockContract.RestPointPhotoEntry.COLUMN_NAME_ID};
        String selection = MockContract.RestPointPhotoEntry.COLUMN_NAME_POINT + " = ?";
        String[] selectionArgs = {"" + rpId};

        Cursor cursor = readableDb.query(MockContract.RestPointPhotoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        while(cursor.moveToNext()){
            oldPhotos.add(new Integer(cursor.getInt(0)));
        }

        return oldPhotos;
    }

    public boolean insertPhotosFromRestPoint(RestPoint rp){
        RestPointDAO rpAgent = new RestPointDAOOptimized(readableDb, null);

        int rpId = rpAgent.getId(rp.getName());

        ContentValues values = new ContentValues();
        for(Integer photo: rp.getOldPhotos()){
            values.clear();
            values.put(MockContract.RestPointPhotoEntry.COLUMN_NAME_ID, photo);
            values.put(MockContract.RestPointPhotoEntry.COLUMN_NAME_POINT, rpId);
            writableDb.insert(MockContract.RestPointPhotoEntry.TABLE_NAME, null, values);
        }

        return true;
    }

    public int getId(String name){
        return -1; //doesnt work and wont work
    }
}
