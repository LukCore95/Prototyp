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
 * Created by Ania on 2017-04-27.
 */

public final class DataInitializer {
    private DataInitializer(){}

    public static void InitializeData(SQLiteDatabase read, SQLiteDatabase write, Context ctx){
        ControlPoint podwale = null;
        Resources res = ctx.getResources();
        List<Integer> podwalePhotos = new ArrayList<Integer>();
        podwalePhotos.add(new Integer(R.drawable.oldpodwale));
        try {
            podwale = new ControlPoint(res.getString(R.string.podwale), res.getString(R.string.podwale_german), res.getString(R.string.podwale_description), new Date(), 17.030082, 51.104082, R.drawable.oldpodwale, R.drawable.oldpodwale, R.drawable.oldpodwale, R.raw.podwale_renoma_swidnicka_plac_teatralny, podwalePhotos);
        }catch(DataException de){
            System.err.println("Nie udało się załadować danych do bazy: " + de);
        }

        ControlPointDAO cpdao = new ControlPointDAOOptimized(read, write);
        cpdao.createControlPoint(podwale);
        //Toast.makeText(ctx, "Pomyślnie dodano", Toast.LENGTH_SHORT).show();
        //System.out.println("DODANE DANE");
    }
}
