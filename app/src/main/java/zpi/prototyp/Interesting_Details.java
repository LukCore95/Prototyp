package zpi.prototyp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        setContentView(R.layout.activity_interesting_details);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
     //   this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

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
        setFont(actionBar_text,"fonts/grobe-deutschmeister/GrobeDeutschmeister.ttf" );
        actionBar_text.setText("Ciekawe miejsce");

        //header Interesting_Place_Name
        TextView interesting_place_name = (TextView) findViewById(R.id.interesting_place_name);
        interesting_place_name.setText(interestingPlace.getName());
        setFont(interesting_place_name, "fonts/montserrat/Montserrat-Light.otf");

        //photo
        ImageView interesting_place_image=(ImageView) findViewById(R.id.interesting_place_image);
        interesting_place_image.setImageResource(interestingPlace.getOldPhotos().get(0));

        //description header
        TextView interesting_place_description_header = (TextView) findViewById(R.id.interesting_place_description_header);
        setFont(interesting_place_description_header, "fonts/cambria/cambria_bold.ttf");
        //description
        final WebView description = (WebView) findViewById(R.id.interesting_place_description_webview);
        description.getSettings().setDefaultTextEncodingName("UTF-8");
        description.setBackgroundColor(Color.TRANSPARENT);
        description.loadData("<html><body>"
                + "<p align=\"justify\"; style=\"text-indent: 10%; \">" + interestingPlace.getDescription() +"</p>" + "</body></html>", "text/html; charset=utf-8", "utf-8");

        //close button
        ImageButton  closeImageButton = (ImageButton) findViewById(R.id.closeImageButton);
        closeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ImageButton navigationDetails=(ImageButton) findViewById(R.id.nawigujDetale);
    }

    private void setFont(TextView tv, String font)
    {
        Typeface descriptionHeaderFont = Typeface.createFromAsset(getAssets(), font);
        tv.setTypeface(descriptionHeaderFont);
    }

    public void nawiguj(View view)
    {
                final Context ctx = view.getContext();
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ctx, R.style.DialogStyle);
                alertBuilder.setMessage(ctx.getString(R.string.rp_navigate_alert_message) + " " + interestingPlace.getName() + "?");
                alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RestPointAdapter.tripController.setNavigation(interestingPlace);


                        Toast.makeText(ctx, "Teraz zmierzasz do: " + interestingPlace.getName(), Toast.LENGTH_SHORT).show();

                        MainActivity.navigationFromDetails=true;

                    }
                });
                alertBuilder.setNegativeButton(R.string.no, null);


                alertBuilder.create().show();

            }


}
