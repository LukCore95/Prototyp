package zpi.data.db;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
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
    private static final int YEAR_1933 = 1933;
    private static final int DAY_13 = 13;
    private static final int DAY_15 = 15;

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
            ControlPoint renoma = null;
            ControlPoint swidnicka = null;
            ControlPoint plteatralny = null;

            Resources res = ctx.getResources();

            List<Integer> podwalePhotos = new ArrayList<Integer>();
            List<Integer> renomaPhotos = new ArrayList<Integer>();
            List<Integer> swidnickaPhotos = new ArrayList<Integer>();
            List<Integer> plteatralnyPhotos = new ArrayList<Integer>();

            podwalePhotos.add(new Integer(R.drawable.oldpodwale));
            renomaPhotos.add(new Integer(R.drawable.renoma1));
            renomaPhotos.add(new Integer(R.drawable.renoma2));
            renomaPhotos.add(new Integer(R.drawable.renoma3));
            renomaPhotos.add(new Integer(R.drawable.renoma4));
            swidnickaPhotos.add(new Integer(R.drawable.oldswidnicka1));
            swidnickaPhotos.add(new Integer(R.drawable.oldswidnicka2));
            swidnickaPhotos.add(new Integer(R.drawable.oldswidnicka3));
            swidnickaPhotos.add(new Integer(R.drawable.oldswidnicka4));
            swidnickaPhotos.add(new Integer(R.drawable.oldswidnicka5));
            swidnickaPhotos.add(new Integer(R.drawable.oldswidnicka6));
            swidnickaPhotos.add(new Integer(R.drawable.oldswidnicka7));
            plteatralnyPhotos.add(new Integer(R.drawable.oldplacteatralny1));
            plteatralnyPhotos.add(new Integer(R.drawable.oldplacteatralny2));

            Calendar maj131933 = Calendar.getInstance();
            Calendar maj151933 = Calendar.getInstance();
            maj131933.set(YEAR_1933, Calendar.MAY, DAY_13);
            maj151933.set(YEAR_1933, Calendar.MAY, DAY_15);

            try {
                podwale = new ControlPoint(res.getString(R.string.podwale), res.getString(R.string.podwale_german), res.getString(R.string.podwale_description), maj131933.getTime(), 17.030082, 51.104082, R.drawable.oldpodwale, R.drawable.oldpodwale, R.drawable.oldpodwale, R.raw.podwale_renoma_swidnicka_plac_teatralny, podwalePhotos);
                renoma = new ControlPoint(res.getString(R.string.renoma), res.getString(R.string.renoma_german), res.getString(R.string.renoma_description), maj131933.getTime(), 17.031064, 51.103851, R.drawable.renoma1, R.drawable.foto_stare, R.drawable.foto_nowe, R.raw.podwale_renoma_swidnicka_plac_teatralny, renomaPhotos);
                swidnicka = new ControlPoint(res.getString(R.string.swidnicka), res.getString(R.string.swidnicka_german), res.getString(R.string.swidnicka_description), maj131933.getTime(), 17.031117, 51.105059, R.drawable.oldswidnicka1, R.drawable.oldswidnicka2, R.drawable.oldswidnicka2, R.raw.podwale_renoma_swidnicka_plac_teatralny, swidnickaPhotos);
                plteatralny = new ControlPoint(res.getString(R.string.placTeatralny), res.getString(R.string.placTeatralny_german), res.getString(R.string.placTeatralny_description), maj131933.getTime(), 17.031921, 51.105483, R.drawable.oldplacteatralny1, R.drawable.oldplacteatralny2, R.drawable.oldplacteatralny2, R.raw.podwale_renoma_swidnicka_plac_teatralny, plteatralnyPhotos);

            } catch (DataException de) {
                System.err.println("Nie udało się załadować danych do bazy: " + de);
            }

            ControlPointDAO cpdao = new ControlPointDAOOptimized(db, db);
            cpdao.createControlPoint(podwale);
            cpdao.createControlPoint(renoma);
            cpdao.createControlPoint(swidnicka);
            cpdao.createControlPoint(plteatralny);
        }
        //Toast.makeText(ctx, "Pomyślnie dodano", Toast.LENGTH_SHORT).show();
        //System.out.println("DODANE DANE");
    }
}
