package zpi.data.db;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import zpi.data.db.dao.ControlPointDAO;
import zpi.data.db.dao.ControlPointDAOOptimized;
import zpi.data.db.dao.RouteDAO;
import zpi.data.db.dao.RouteDAOOptimized;
import zpi.data.model.ControlPoint;
import zpi.data.model.DataException;
import zpi.data.model.Route;
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
            ControlPoint sadowa = null;

            Resources res = ctx.getResources();

            List<Integer> podwalePhotos = new ArrayList<Integer>();
            List<Integer> renomaPhotos = new ArrayList<Integer>();
            List<Integer> swidnickaPhotos = new ArrayList<Integer>();
            List<Integer> plteatralnyPhotos = new ArrayList<Integer>();
            List<Integer> sadowaPhotos = new ArrayList<Integer>();

            podwalePhotos.add(new Integer(R.drawable.oldpodwaleh));
            podwalePhotos.add(new Integer(R.drawable.oldpodwale2h));
            renomaPhotos.add(new Integer(R.drawable.renoma1h));
            renomaPhotos.add(new Integer(R.drawable.renoma2h));
            renomaPhotos.add(new Integer(R.drawable.renoma3h));
            renomaPhotos.add(new Integer(R.drawable.renoma4));
            swidnickaPhotos.add(new Integer(R.drawable.oldswidnicka1));
            swidnickaPhotos.add(new Integer(R.drawable.oldswidnicka2h));
            swidnickaPhotos.add(new Integer(R.drawable.oldswidnicka3h));
            swidnickaPhotos.add(new Integer(R.drawable.oldswidnicka4h));
            swidnickaPhotos.add(new Integer(R.drawable.oldswidnicka5h));
            swidnickaPhotos.add(new Integer(R.drawable.oldswidnicka6h));
            swidnickaPhotos.add(new Integer(R.drawable.oldswidnicka7h));
            plteatralnyPhotos.add(new Integer(R.drawable.oldplacteatralny1h));
            plteatralnyPhotos.add(new Integer(R.drawable.oldplacteatralny2h));
            sadowaPhotos.add(new Integer(R.drawable.oldsadowa1h));
            sadowaPhotos.add(new Integer(R.drawable.oldsadowa2h));


            Calendar maj131933 = Calendar.getInstance();
            Calendar maj151933 = Calendar.getInstance();
            maj131933.set(YEAR_1933, Calendar.MAY, DAY_13);
            maj151933.set(YEAR_1933, Calendar.MAY, DAY_15);

            try {
                podwale = new ControlPoint(res.getString(R.string.podwale), res.getString(R.string.podwale_german), res.getString(R.string.podwale_description), maj131933.getTime(), 17.030082, 51.104082, R.mipmap.marker_podwale, R.drawable.oldpodwale, R.drawable.newpodwale, R.raw.podwale_renoma_swidnicka_plac_teatralny, podwalePhotos);
                renoma = new ControlPoint(res.getString(R.string.renoma), res.getString(R.string.renoma_german), res.getString(R.string.renoma_description), maj131933.getTime(), 17.031064, 51.103851, R.mipmap.marker_renoma, R.drawable.foto_stare, R.drawable.foto_nowe, R.raw.podwale_renoma_swidnicka_plac_teatralny, renomaPhotos);
                swidnicka = new ControlPoint(res.getString(R.string.swidnicka), res.getString(R.string.swidnicka_german), res.getString(R.string.swidnicka_description), maj131933.getTime(), 17.031117, 51.105059, R.mipmap.marker_swidnicka, R.drawable.oldswidnicka, R.drawable.newswidnicka, R.raw.podwale_renoma_swidnicka_plac_teatralny, swidnickaPhotos);
                plteatralny = new ControlPoint(res.getString(R.string.placTeatralny), res.getString(R.string.placTeatralny_german), res.getString(R.string.placTeatralny_description), maj131933.getTime(), 17.031921, 51.105483, R.mipmap.marker_plteatralny, R.drawable.oldplteatralny, R.drawable.newplteatralny, R.raw.podwale_renoma_swidnicka_plac_teatralny, plteatralnyPhotos);
                sadowa = new ControlPoint(res.getString(R.string.sadowa), res.getString(R.string.sadowa_german), res.getString(R.string.sadowa_description), maj151933.getTime(), 17.027947, 51.105328, R.mipmap.marker_sadowa, R.drawable.oldsadowa, R.drawable.oldsadowa, R.raw.sadowa, sadowaPhotos );

            } catch (DataException de) {
                System.err.println("Nie udało się załadować danych do bazy: " + de);
            }

         /*   ControlPointDAO cpdao = new ControlPointDAOOptimized(db, db);
            cpdao.createControlPoint(podwale);
            cpdao.createControlPoint(renoma);
            cpdao.createControlPoint(swidnicka);
            cpdao.createControlPoint(plteatralny);
            cpdao.createControlPoint(sadowa);*/

            //ADDING A ROUTE
            Route rTest = null;
            try {
                rTest = new Route("Trasa testowa");
            }catch(DataException de){
                System.err.println(de);
            }
            LinkedList<ControlPoint> testTrasa = new LinkedList<ControlPoint>();
            testTrasa.add(sadowa);
            testTrasa.add(plteatralny);
            testTrasa.add(swidnicka);
            testTrasa.add(renoma);
            testTrasa.add(podwale);

            for(ControlPoint cp: testTrasa)
                System.out.println("Dodany punkt: " + cp.getName());

            rTest.setRoutePoints(testTrasa);

            RouteDAO routeDAO = new RouteDAOOptimized(db, db);
            routeDAO.createRoute(rTest);




        }
        //Toast.makeText(ctx, "Pomyślnie dodano", Toast.LENGTH_SHORT).show();
        //System.out.println("DODANE DANE");
    }
}
