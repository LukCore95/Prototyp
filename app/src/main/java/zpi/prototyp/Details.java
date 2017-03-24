package zpi.prototyp;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Locale;

public class Details extends AppCompatActivity implements View.OnTouchListener {

    private SurfaceView surf;
    private SurfaceHolder hold;
    private Thread init;
    private Thread slide;
    private Bitmap[] photos = new Bitmap[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);

        //ustawienie czcionki nagłówka
//        AssetManager am = getApplicationContext().getAssets();
//        Typeface typeface = Typeface.createFromAsset(am,
//                String.format(Locale.US, "fonts/%s", "abc.ttf"));
//        (typeface);
//        TextView myTextView = (TextView) findViewById(R.id.renoma_de_name);
//        Typeface typeface=Typeface.createFromAsset(getAssets(), "fonts/grobe-deutschmeister/GrobeDeutschmeister.ttf");
//        myTextView.setTypeface(typeface);

//        TextView tx = (TextView)findViewById(R.id.renoma_de_name);
//        Typeface custom_font = Typeface.createFromAsset(getAssets(),
//                "fonts/grobe-deutschmeister/GrobeDeutschmeister.ttf");
//        tx.setTypeface(custom_font);



        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_details);

        TextView myTextView = (TextView) findViewById(R.id.renoma_de_name);
        Typeface typeface=Typeface.createFromAsset(getAssets(), "fonts/grobe-deutschmeister/GrobeDeutschmeister.ttf");
        myTextView.setTypeface(typeface);


        surf = (SurfaceView) findViewById(R.id.surfaceView);
        hold = surf.getHolder();
        surf.setOnTouchListener(this);

        try {
            photos[0] = BitmapFactory.decodeResource(getResources(), R.drawable.zpi2);
            photos[1] = BitmapFactory.decodeResource(getResources(), R.drawable.zpi1);
        }catch(Exception e){
            System.out.println(e);
        }

        init = new Thread(new InitThread(photos[0], hold));
        init.start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getAxisValue(MotionEvent.AXIS_X);
        slide = new Thread(new SlideThread(x, photos, hold));
        slide.start();
        //Toast.makeText(this, "Lol: " + x, Toast.LENGTH_SHORT).show();
        return true;
    }

    public void setPhotos(Bitmap[] _photos){
        photos = _photos;
    }
}
