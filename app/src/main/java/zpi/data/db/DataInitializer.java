package zpi.data.db;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zpi.data.model.ControlPoint;
import zpi.data.model.DataException;
import zpi.prototyp.R;

/**
 * @author Wojciech Michałowski
 * This static class should be use to insert data to the Mock database while initializing or updating the database.
 * During the updating application and adding new control points, routes or other points please modify ONLY the method InitializeData of this class!
 * Do so by adding new insertion method calls and new object that needs to be inserted while checking the old and new version numbers.
 * oldVersion variable while 0 means that the database will be created, not upgraded.
 */
public final class DataInitializer {

    /**
     * You cannot instantiate this class.
     */
    private DataInitializer(){}

    /**
     * Use this method for inserting data into freshly created or updated database.
     * @param db Database that needs to be fulfilled with data
     * @param ctx Context of application
     * @param newVersion current data version
     * @param oldVersion former data version (if 0 - creation mode)
     */
    public static void InitializeData(SQLiteDatabase db, Context ctx, int newVersion, int oldVersion){
        //VERSION CHECK - validate if it's version 1. Now we have only 1 version, but we can expand the code during the app developing process.
        if(newVersion == 1) {
            ControlPoint podwale = null;
            Resources res = ctx.getResources();
            List<Integer> podwalePhotos = new ArrayList<Integer>();
            podwalePhotos.add(new Integer(R.drawable.oldpodwale));
            try {
                podwale = new ControlPoint(res.getString(R.string.podwale), res.getString(R.string.podwale_german), res.getString(R.string.podwale_description), new Date(), 17.030082, 51.104082, R.drawable.oldpodwale, R.drawable.oldpodwale, R.drawable.oldpodwale, R.raw.podwale_renoma_swidnicka_plac_teatralny, podwalePhotos);
            } catch (DataException de) {
                System.err.println("Nie udało się załadować danych do bazy: " + de);
            }

            ControlPointDAO cpdao = new ControlPointDAOOptimized(db, db);
            cpdao.createControlPoint(podwale);
        }
        //Toast.makeText(ctx, "Pomyślnie dodano", Toast.LENGTH_SHORT).show();
        //System.out.println("DODANE DANE");
    }
}
