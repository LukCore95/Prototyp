package zpi.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * @author Wojciech Michałowski
 * This class performs SQL operations on Mock database.
 */
public class MockDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 20;
    public static final String DATABASE_NAME = "Mock.db";
    Context ctx;

    /**
     * Public constructor which constructDbHelper using current Context.
     * @param context Current context
     */
    public MockDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }

    /**
     * Handles DB creating event.
     * @param db Created database
     */
    public void onCreate(SQLiteDatabase db){
        //System.out.println("Rozpoczynam tworzenie bazy danych...");
        //long time = System.currentTimeMillis();
       // Toast.makeText(ctx, "Rozpoczynam tworzenie bazy!", Toast.LENGTH_SHORT).show();
        //System.out.println("Rozpoczynam tworzenie bazy");
        createTables(db);
        //Toast.makeText(ctx, "Baza danych zainicjalizowana", Toast.LENGTH_SHORT).show();
        //System.out.println("Baza danych zainicjalizowana");
        DataInitializer.InitializeData(db, ctx);
        //time = System.currentTimeMillis()-time;
        //System.out.println("Baza danych utworzona w " + time + "ms");
        //Toast.makeText(ctx, "Baza danych pomyślnie utworzona!", Toast.LENGTH_SHORT).show();
        //System.out.println("Baza danych pomyślnie utworzona!");
    }

    /**
     * Handles database upgrade event (from external server)
     * @param db Upgraded database
     * @param oldVersion Old version number
     * @param newVersion New (current) version number
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.delete(MockContract.RoutePointEntry.TABLE_NAME, null, null);
        db.delete(MockContract.TripEntry.TABLE_NAME, null, null);
        db.delete(MockContract.RouteEntry.TABLE_NAME, null, null);
        db.delete(MockContract.ControlPointEntry.TABLE_NAME, null, null);
        db.delete(MockContract.ControlPointPhotoEntry.TABLE_NAME, null, null);
        db.delete(MockContract.InterestingPlaceEntry.TABLE_NAME, null, null);
        db.delete(MockContract.InterestingPlacePhotoEntry.TABLE_NAME, null, null);
        db.delete(MockContract.RestPointEntry.TABLE_NAME, null, null);
        db.delete(MockContract.RestPointPhotoEntry.TABLE_NAME, null, null);
        DataInitializer.InitializeData(db, ctx);
    }

    /**
     * Handles database downgrade event (from external server)
     * @param db Upgraded database
     * @param oldVersion Old version number
     * @param newVersion New (current) version number
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    /*public SQLiteDatabase enableReading(){
        return getReadableDatabase();
    }

    public SQLiteDatabase enableWriting(){
        return getWritableDatabase();
    }

    public void closeDb(SQLiteDatabase db){
        if(db.isOpen())
            db.close();
    }*/

    private void createTables(SQLiteDatabase db){
        db.execSQL(MockContract.ControlPointPhotoCreation);
        db.execSQL(MockContract.InterestingPlacePhotoCreation);
        db.execSQL(MockContract.RestPointPhotoCreation);
        db.execSQL(MockContract.RestPointCreation);
        db.execSQL(MockContract.InterestingPlaceCreation);
        db.execSQL(MockContract.ControlPointCreation);
        db.execSQL(MockContract.RouteCreation);
        db.execSQL(MockContract.RoutePointCreation);
        db.execSQL(MockContract.TripCreation);
    }
}
