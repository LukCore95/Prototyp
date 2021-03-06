package zpi.data.db;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import zpi.data.db.dao.InterestingPlaceDAO;
import zpi.data.db.dao.InterestingPlaceDAOOptimized;
import zpi.data.db.dao.RestPointDAO;
import zpi.data.db.dao.RestPointDAOOptimized;
import zpi.data.db.dao.RouteDAO;
import zpi.data.db.dao.RouteDAOOptimized;
import zpi.data.model.ControlPoint;
import zpi.data.model.DataException;
import zpi.data.model.InterestingPlace;
import zpi.data.model.InterestingPlaceType;
import zpi.data.model.RestPoint;
import zpi.data.model.RestPointType;
import zpi.data.model.Route;
import zpi.prototyp.R;

/**
 * @author Wojciech Michałowski
 * This static class should be use to insert data to the Mock database while initializing or updating the database.
 * During the updating application and adding new control points, routes or other points please modify ONLY the method InitializeData of this class!
 * Do so by adding new insertion method calls and new object that needs to be inserted.
 */
public final class DataInitializer {
    private static final int YEAR_1933 = 1933;
    private static final int YEAR_1934 = 1934;
    private static final int DAY_13 = 13;
    private static final int DAY_15 = 15;
    private static final int DAY_14 = 14;

    /**
     * You cannot instantiate this class.
     */
    private DataInitializer(){}

    /**
     * Use this method for inserting data into freshly created or updated database.
     * @param db Database that needs to be fulfilled with data
     * @param ctx Context of application
     */
    public static void InitializeData(SQLiteDatabase db, Context ctx){
        //NO VERSION CHECK NOW. All DB is being reloaded.

            ControlPoint podwale = null;
            ControlPoint renoma = null;
            ControlPoint swidnicka = null;
            ControlPoint plteatralny = null;
            ControlPoint sadowa = null;
            ControlPoint pomnikamora=null;
            ControlPoint biskupia=null;
            ControlPoint szewska=null;
            ControlPoint ofiarOswiecimskich=null;
            ControlPoint placWolnosci= null;
            ControlPoint kameleon = null;
            //ControlPoint punktTestowy=null;

            InterestingPlace sad=null;
            InterestingPlace promenada=null;
            InterestingPlace kosciol_bc=null;
            InterestingPlace opera=null;
            InterestingPlace pomnik_bc=null;
            InterestingPlace teatrlalek =null;
            //InterestingPlace wat = null;

            RestPoint cafeBarMonopol=null;
            RestPoint costacoffee=null;
            RestPoint dinette = null;
            RestPoint haggisPub = null;
            RestPoint hanaSushi =null;
            RestPoint polishLody=null;
            RestPoint niezlyDym=null;
            RestPoint pubWedrowki = null;
            RestPoint staraPaczkarnia = null;
            RestPoint tuttiFrutti=null;
            RestPoint wloszczyzna=null;
            RestPoint nespressoBoutique=null;



            Resources res = ctx.getResources();

            List<Integer> podwalePhotos = new ArrayList<Integer>();
            List<Integer> renomaPhotos = new ArrayList<Integer>();
            List<Integer> swidnickaPhotos = new ArrayList<Integer>();
            List<Integer> plteatralnyPhotos = new ArrayList<Integer>();
            List<Integer> sadowaPhotos = new ArrayList<Integer>();
            List<Integer> pomnikamoraPhotos=new ArrayList<Integer>();
            List<Integer> biskupiaPhotos=new ArrayList<Integer>();
            List<Integer> szewskaPhotos=new ArrayList<Integer>();
            List<Integer> ofiarOswiecimskichPhotos=new ArrayList<Integer>();
            List<Integer> placWolnosciPhotos=new ArrayList<Integer>();
            List<Integer> kameleonPhotos = new ArrayList<Integer>();
            List<Integer> sadPhoto=new ArrayList<Integer>();
            List<Integer> promenadaPhoto=new ArrayList<Integer>();
            List<Integer> operaPhoto = new ArrayList<Integer>();
            List<Integer> kosciol_bcPhoto=new ArrayList<Integer>();
            List<Integer> pomnik_bcPhoto=new ArrayList<Integer>();
            List<Integer> teatrLalekPhoto=new ArrayList<Integer>();


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
            pomnikamoraPhotos.add(new Integer(R.drawable.oldpomnikamora1h));
            pomnikamoraPhotos.add(new Integer(R.drawable.oldpomnikamora2));
            pomnikamoraPhotos.add(new Integer(R.drawable.oldpomnikamora3));
            pomnikamoraPhotos.add(new Integer(R.drawable.oldpomnikamora4h));
            pomnikamoraPhotos.add(new Integer(R.drawable.oldpomnikamora5h));
            pomnikamoraPhotos.add(new Integer(R.drawable.oldpomnikamora6h));
            ofiarOswiecimskichPhotos.add(new Integer(R.drawable.oldofiaroswiecimskich1));
            ofiarOswiecimskichPhotos.add(new Integer(R.drawable.oldofiaroswiecimskich2h));
            ofiarOswiecimskichPhotos.add(new Integer(R.drawable.oldofiaroswiecimskich3h));
            ofiarOswiecimskichPhotos.add(new Integer(R.drawable.oldofiaroswiecimskich4h));
            ofiarOswiecimskichPhotos.add(new Integer(R.drawable.oldofiaroswiecimskich5h));
            szewskaPhotos.add(new Integer(R.drawable.oldszewska491h));
            biskupiaPhotos.add(new Integer(R.drawable.oldbiskupia1));
            biskupiaPhotos.add(new Integer(R.drawable.oldbiskupia1));
            biskupiaPhotos.add(new Integer(R.drawable.oldbiskupia2));
            biskupiaPhotos.add(new Integer(R.drawable.oldbiskupia3h));
            biskupiaPhotos.add(new Integer(R.drawable.oldbiskupia4));
            biskupiaPhotos.add(new Integer(R.drawable.oldbiskupia5h));
            placWolnosciPhotos.add(new Integer(R.drawable.oldplacwolnosci1h));
            placWolnosciPhotos.add(new Integer(R.drawable.oldplacwolnosci2h));
            placWolnosciPhotos.add(new Integer(R.drawable.oldplacwolnosci3h));
//            placWolnosciPhotos.add(new Integer(R.drawable.oldplacwolnosci6h));
//            placWolnosciPhotos.add(new Integer(R.drawable.oldplacwolnosci5h));
//            placWolnosciPhotos.add(new Integer(R.drawable.oldplacwolnosci7h));
//            placWolnosciPhotos.add(new Integer(R.drawable.oldplacwolnosci8h));
//            placWolnosciPhotos.add(new Integer(R.drawable.oldplacwolnosci9h));
//            placWolnosciPhotos.add(new Integer(R.drawable.oldplacwolnosci10h));
            //kameleonPhotos.add(new Integer(R.drawable.oldkameleon1));
          //  kameleonPhotos.add(new Integer(R.drawable.oldkameleon2));
           kameleonPhotos.add(new Integer(R.drawable.oldkameleon3));
           kameleonPhotos.add(new Integer(R.drawable.oldkameleon4));
         //   kameleonPhotos.add(new Integer(R.drawable.oldkameleon5h));
          //  kameleonPhotos.add(new Integer(R.drawable.oldkameleon6h));
        //    kameleonPhotos.add(new Integer(R.drawable.oldkameleon7h));
         //   kameleonPhotos.add(new Integer(R.drawable.oldkameleon8));




            //interesting place photos
            operaPhoto.add(new Integer(R.drawable.ip_opera));
            kosciol_bcPhoto.add(new Integer(R.drawable.ip_kosciol_bc));
            teatrLalekPhoto.add(new Integer(R.drawable.ip_teatrlalek));
            sadPhoto.add(new Integer(R.drawable.ip_sad));
            promenadaPhoto.add(new Integer(R.drawable.ip_promenada));
            pomnik_bcPhoto.add(new Integer(R.drawable.ip_pomnik_bc));

            Calendar maj131933 = Calendar.getInstance();
            Calendar maj151933 = Calendar.getInstance();
            Calendar lipiec141934 =Calendar.getInstance();
            maj131933.set(YEAR_1933, Calendar.MAY, DAY_13);
            maj151933.set(YEAR_1933, Calendar.MAY, DAY_15);
            lipiec141934.set(YEAR_1934, Calendar.JULY, DAY_14);


            try {
                podwale = new ControlPoint(res.getString(R.string.podwale), res.getString(R.string.podwale_german), res.getString(R.string.podwale_description), maj131933.getTime(), 17.030082, 51.104082, R.mipmap.marker_podwale, R.drawable.oldpodwale, R.drawable.newpodwale, R.raw.podwale_renoma_swidnicka_plac_teatralny, podwalePhotos);
                renoma = new ControlPoint(res.getString(R.string.renoma), res.getString(R.string.renoma_german), res.getString(R.string.renoma_description), maj131933.getTime(), 17.031064, 51.103851, R.mipmap.marker_renoma, R.drawable.foto_stare, R.drawable.foto_nowe, R.raw.podwale_renoma_swidnicka_plac_teatralny, renomaPhotos);
                swidnicka = new ControlPoint(res.getString(R.string.swidnicka), res.getString(R.string.swidnicka_german), res.getString(R.string.swidnicka_description), maj131933.getTime(), 17.031117, 51.105059, R.mipmap.marker_swidnicka, R.drawable.oldswidnicka, R.drawable.newswidnicka, R.raw.podwale_renoma_swidnicka_plac_teatralny, swidnickaPhotos);
                plteatralny = new ControlPoint(res.getString(R.string.placTeatralny), res.getString(R.string.placTeatralny_german), res.getString(R.string.placTeatralny_description), maj131933.getTime(), 17.031921, 51.105483, R.mipmap.marker_plteatralny, R.drawable.oldplteatralny, R.drawable.newplteatralny, R.raw.podwale_renoma_swidnicka_plac_teatralny, plteatralnyPhotos);
                sadowa = new ControlPoint(res.getString(R.string.sadowa), res.getString(R.string.sadowa_german), res.getString(R.string.sadowa_description), maj151933.getTime(), 17.027947, 51.105328, R.mipmap.marker_sadowa, R.drawable.oldsadowa1, R.drawable.newsadowa, R.raw.sadowa, sadowaPhotos );
                pomnikamora=new ControlPoint(res.getString(R.string.pomnikamora), res.getString(R.string.pomnikamora_german), res.getString(R.string.pomnikamora_description), maj151933.getTime(), 17.0352593, 51.104268, R.mipmap.marker_amor, R.drawable.oldpomnikamora, R.drawable.newpomnikamora, R.raw.pomnik_amora, pomnikamoraPhotos );
                szewska=new ControlPoint(res.getString(R.string.szewska), res.getString(R.string.szewska_german),res.getString(R.string.szewska_description), maj131933.getTime(), 17.035566, 51.112735, R.mipmap.marker_szewska, R.drawable.oldszewska49, R.drawable.newszewska49, R.raw.szewska, szewskaPhotos);
                ofiarOswiecimskich=new ControlPoint(res.getString(R.string.ofiaroswiecimskich), res.getString(R.string.ofiaroswiecimskich_german), res.getString(R.string.ofiaroswiecimskich_description), lipiec141934.getTime(),17.0323972, 51.1083705, R.mipmap.marker_ofiaroswiecimskich, R.drawable.oldofiaroswiecimskich, R.drawable.newofiaroswiecimskich, R.raw.ofiaroswiecimskich, ofiarOswiecimskichPhotos);
                biskupia=new ControlPoint(res.getString(R.string.biskupia), res.getString(R.string.biskupia_german), res.getString(R.string.biskupia_description), maj131933.getTime(), 17.0359827,51.1087502, R.mipmap.marker_biskupia, R.drawable.oldbiskupia, R.drawable.newbiskupia, R.raw.biskupia, biskupiaPhotos);
                placWolnosci=new ControlPoint(res.getString(R.string.placWolnosci), res.getString(R.string.placWolnosci_german), res.getString(R.string.placWolnosci_description), maj131933.getTime(), 17.0276188, 51.1068292, R.mipmap.marker_placwolnosci, R.drawable.oldplacwolnosci, R.drawable.newplacwolnosci, R.raw.placwolnosci, placWolnosciPhotos);
                kameleon=new ControlPoint(res.getString(R.string.kameleon), res.getString(R.string.kameleon_german), res.getString(R.string.kameleon_description), lipiec141934.getTime(), 17.033954, 51.108502, R.mipmap.marker_kameleon, R.drawable.oldkameleon, R.drawable.newkameleon, R.raw.placwolnosci, kameleonPhotos);
                //punktTestowy=new ControlPoint("kakakaka", "hihiihihihih", "ameba ameba", maj131933.getTime(), 16.845005, 51.389996, R.mipmap.marker_plteatralny,R.drawable.oldpodwale, R.drawable.oldplacteatralny1h, R.raw.podwale_renoma_swidnicka_plac_teatralny, sadowaPhotos);

                sad=new InterestingPlace(res.getString(R.string.ip_sad), res.getString(R.string.ip_sad_description),17.025995, 51.105936, res.getString(R.string.ip_sad_address), InterestingPlaceType.kultury, sadPhoto);
                promenada = new InterestingPlace(res.getString(R.string.ip_promenada), res.getString(R.string.ip_promenada_description), 17.025415, 51.107015, "", InterestingPlaceType.kultury, promenadaPhoto );
                kosciol_bc=new InterestingPlace(res.getString(R.string.ip_kosciol_bc), res.getString(R.string.ip_kosciol_bc_description), 17.031296, 51.105006, res.getString(R.string.ip_kosciol_bc_address), InterestingPlaceType.sakralny, kosciol_bcPhoto);
                opera=new InterestingPlace(res.getString(R.string.ip_opera), res.getString(R.string.ip_opera_description), 17.031189, 51.105582, res.getString(R.string.ip_opera_address), InterestingPlaceType.kultury, operaPhoto);
                pomnik_bc=new InterestingPlace(res.getString(R.string.ip_pomnik_bc), res.getString(R.string.ip_pomnik_bc_description), 17.030988, 51.104368, "", InterestingPlaceType.kultury, pomnik_bcPhoto);
                teatrlalek =new InterestingPlace(res.getString(R.string.ip_teatrlalek), res.getString(R.string.ip_teatrlalek_description), 17.033139, 51.105328, res.getString(R.string.ip_teatrlalek_address), InterestingPlaceType.kultury, teatrLalekPhoto);
                //wat = new InterestingPlace("Wojteks house", "Wojteguuu hałsuu", 16.845156, 51.389567, "Skokowa, ul. Żmigrodzka 11b", InterestingPlaceType.kultury, sadowaPhotos);

                cafeBarMonopol = new RestPoint(res.getString(R.string.rp_cafeBarMonopol),
                        res.getString(R.string.rp_cafeBarMonopol_description), 17.0306989, 51.1060844,
                        RestPointType.cafe, res.getString(R.string.rp_cafeBarMonopol_address),
                        res.getString(R.string.rp_cafeBarMonopol_web));
                costacoffee= new RestPoint(res.getString(R.string.rp_costaCoffee),
                        res.getString(R.string.rp_costaCoffee_description), 17.0321131, 51.103495,
                        RestPointType.cafe, res.getString(R.string.rp_costaCoffee_address),
                        res.getString(R.string.rp_costaCoffee_web));
                dinette= new RestPoint(res.getString(R.string.rp_dinette), res.getString(R.string.rp_dinette_description), 17.031725,51.105790, RestPointType.cafe, res.getString(R.string.rp_dinette_address),
                        res.getString(R.string.rp_dinette_web));
                haggisPub = new RestPoint(res.getString(R.string.rp_haggisPub), res.getString(R.string.rp_haggisPub_description),
                        17.030317, 51.103787, RestPointType.pub, res.getString(R.string.rp_haggisPub_address),
                        res.getString(R.string.rp_haggisPub_web));
                hanaSushi=new RestPoint(res.getString(R.string.rp_hanaSushi), res.getString(R.string.rp_hanaSushi_description),
                        17.0310187, 51.1036146, RestPointType.restaurant, res.getString(R.string.rp_haggisPub_address),
                        res.getString(R.string.rp_hanaSushi_web));
                polishLody=new RestPoint(res.getString(R.string.rp_polishLody),
                        res.getString(R.string.rp_polishLody_description), 17.030716, 51.102106,
                        RestPointType.cafe, res.getString(R.string.rp_polishLody_address),
                        res.getString(R.string.rp_polishLody_web));
                niezlyDym=new RestPoint(res.getString(R.string.rp_niezlyDym), res.getString(R.string.rp_niezlyDym_description),
                        17.03181, 51.105474, RestPointType.restaurant, res.getString(R.string.rp_niezlyDym_address),
                        res.getString(R.string.rp_niezlyDym_web));
                pubWedrowki=new RestPoint(res.getString(R.string.rp_pubWedrowki),
                        res.getString(R.string.rp_pubWedrowki_description), 17.0299611, 51.104158,
                        RestPointType.pub, res.getString(R.string.rp_pubWedrowki_address),
                        res.getString(R.string.rp_pubWedrowki_web));//17.031265, 51.105982,
                staraPaczkarnia=new RestPoint(res.getString(R.string.rp_staraPaczkarnia),
                        res.getString(R.string.rp_staraPaczkarnia_description),  17.031846,51.106474,
                        RestPointType.cafe, res.getString(R.string.rp_staraPaczkarnia_address),
                        res.getString(R.string.rp_staraPaczkarnia_web));
                tuttiFrutti = new RestPoint(res.getString(R.string.rp_tuttiFrutti),
                        res.getString(R.string.rp_tuttiFrutti_description), 17.0301108, 51.1035695,
                        RestPointType.cafe, res.getString(R.string.rp_tuttiFrutti_address),
                        res.getString(R.string.rp_tuttiFrutti_web));
                wloszczyzna=new RestPoint(res.getString(R.string.rp_wloszczyzna),
                        res.getString(R.string.rp_wloszczyzna_description), 17.0320742, 51.1036891,
                        RestPointType.restaurant, res.getString(R.string.rp_wloszczyzna_address),
                        res.getString(R.string.rp_wloszczyzna_web));
                nespressoBoutique =new RestPoint(res.getString(R.string.rp_nespressoBoutique),
                        res.getString(R.string.rp_nespressoBoutique_description), 17.031265, 51.105982,
                        RestPointType.cafe, res.getString(R.string.rp_nespressoBoutique_address),
                        res.getString(R.string.rp_nespressoBoutique_web));

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
            testTrasa.add(podwale);
            testTrasa.add(renoma);
            testTrasa.add(swidnicka);
            testTrasa.add(plteatralny);
            testTrasa.add(pomnikamora);
            testTrasa.add(biskupia);
            testTrasa.add(szewska);
            testTrasa.add(kameleon);
            testTrasa.add(ofiarOswiecimskich);
            testTrasa.add(placWolnosci);




            //testTrasa.add(punktTestowy);

           /* for(ControlPoint cp: testTrasa)
                System.out.println("Dodany punkt: " + cp.getName());*/

            rTest.setRoutePoints(testTrasa);

            RouteDAO routeDAO = new RouteDAOOptimized(db, db);
            routeDAO.createRoute(rTest);

            InterestingPlaceDAO ipDao = new InterestingPlaceDAOOptimized(db, db);

            ipDao.createInterestingPlace(sad);
            ipDao.createInterestingPlace(promenada);
            ipDao.createInterestingPlace(kosciol_bc);
            ipDao.createInterestingPlace(opera);
            ipDao.createInterestingPlace(pomnik_bc);
            ipDao.createInterestingPlace(teatrlalek);
            //ipDao.createInterestingPlace(wat);

            RestPointDAO rpDao=new RestPointDAOOptimized(db, db);

            rpDao.createRestPoint(cafeBarMonopol);
            rpDao.createRestPoint(costacoffee);
            rpDao.createRestPoint(dinette);
            rpDao.createRestPoint(haggisPub);
            rpDao.createRestPoint(hanaSushi);
            rpDao.createRestPoint(polishLody);
            rpDao.createRestPoint(niezlyDym);
            rpDao.createRestPoint(pubWedrowki);
            rpDao.createRestPoint(staraPaczkarnia);
            rpDao.createRestPoint(tuttiFrutti);
            rpDao.createRestPoint(wloszczyzna);
            rpDao.createRestPoint(nespressoBoutique);




        //Toast.makeText(ctx, "Pomyślnie dodano", Toast.LENGTH_SHORT).show();
        //System.out.println("DODANE DANE");
    }
}
