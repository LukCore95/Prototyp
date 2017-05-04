package zpi.prototyp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import zpi.data.db.MockDbHelper;
import zpi.data.db.dao.InterestingPlaceDAO;
import zpi.data.db.dao.InterestingPlaceDAOOptimized;
import zpi.data.model.InterestingPlace;

public class Interesting_Details extends AppCompatActivity {

    String interestingPlaceName;
    InterestingPlace interestingPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesting__details);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
     //   this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intentM=getIntent();
        interestingPlaceName=intentM.getStringExtra("nazwaPunktu");

        //pobranie z bazy
        MockDbHelper dbHelp = new MockDbHelper(this);
        SQLiteDatabase database = dbHelp.getReadableDatabase();
        InterestingPlaceDAO ipdao = new InterestingPlaceDAOOptimized(database, null);
        interestingPlace=ipdao.getInterestingPlace(interestingPlaceName);
        dbHelp.close();

        //actionBar text+font
        TextView actionBar_text =(TextView) findViewById(R.id.textPK);
        setFont((TextView) findViewById(R.id.textPK),"fonts/montserrat/Montserrat-Light.otf" );
        actionBar_text.setText("Ciekawe miejsce");

        //header Interesting_Place_Name
        TextView interesting_place_name = (TextView) findViewById(R.id.interesting_place_name);
        interesting_place_name.setText(interestingPlace.getName());

        //photo
        ImageView interesting_place_image=(ImageView) findViewById(R.id.interesting_place_image);
        interesting_place_image.setImageResource(interestingPlace.getOldPhotos().get(0));

        //description
        final WebView description = (WebView) findViewById(R.id.interesting_place_description_webview);
        description.getSettings().setDefaultTextEncodingName("UTF-8");
        description.setBackgroundColor(Color.TRANSPARENT);
        description.loadData(interestingPlace.getDescription(), "charset=utf-8" ,"UTF-8");

    }

    private void setFont(TextView tv, String font)
    {
        Typeface descriptionHeaderFont = Typeface.createFromAsset(getAssets(), font);
        tv.setTypeface(descriptionHeaderFont);
    }
}
