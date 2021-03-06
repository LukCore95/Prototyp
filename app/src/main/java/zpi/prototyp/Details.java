package zpi.prototyp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import zpi.data.db.dao.ControlPointDAO;
import zpi.data.db.dao.ControlPointDAOOptimized;
import zpi.data.db.MockDbHelper;
import zpi.data.model.ControlPoint;
import zpi.view.slider.BeforeAfterSlider;
import zpi.view.slider.BeforeAfterSliderRed;

public class Details extends FragmentActivity implements View.OnTouchListener{
    private static final int SCROLL_MARGIN = 10;
    private static final int SCROLL_DURATION = 400;

    private SurfaceView surf;
    //private SurfaceHolder hold;
    //private SlideThread init;
   // private Thread slide;
    private int bNew;
    private int bOld;
    //private Bitmap slider_background;
    //private Bitmap slider_background2;
    //private Bitmap slider;
    private ImageButton closeIB;
    private MediaPlayer odtworzAudiobook;
    private TextView placeDescHeader;
    private RelativeLayout relLayout;
    private LockScrollView lsv;
    private TextView photoTitle;
    ControlPoint cp;

    private BeforeAfterSlider slider;

    //private static final float RATIO = 0.8124f;
    //private static final int x_margin = 20;

    // powiększanie obrazka i animacja
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    String controlPointName;
    String shortDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        //connection with a database
        Intent intentM=getIntent();
        controlPointName=intentM.getStringExtra("nazwaPunktu");
        MockDbHelper dbHelp = new MockDbHelper(this);
        SQLiteDatabase database = dbHelp.getReadableDatabase();
        ControlPointDAO cpdao = new ControlPointDAOOptimized(database, null);
        cp=cpdao.getControlPoint(controlPointName);
        dbHelp.close();

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        setContentView(R.layout.activity_details);
        lsv = (LockScrollView) findViewById(R.id.id_details_przewijanie);
        //description
        shortDescription=shortDescription();
        final WebView description = (WebView) findViewById(R.id.description_webview);
        description.getSettings().setDefaultTextEncodingName("UTF-8");
        description.setBackgroundColor(Color.TRANSPARENT);
        description.loadData(shortDescription, "charset=utf-8" ,"UTF-8");
        clickToReadMoreDetails();


        closeIB = (ImageButton) findViewById(R.id.closeImageButton);
        closeIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //setting font
        TextView germanNameHeader = (TextView) findViewById(R.id.de_name);
        photoTitle = (TextView) findViewById(R.id.textView_galeriaZdjec);
        photoTitle.setText(getString(R.string.string_fotografie) + " " + cp.getName());
        germanNameHeader.setText(cp.getGermanName());
        TextView polishNameHeader = (TextView) findViewById(R.id.pl_name);
        polishNameHeader.setText(cp.getName().toUpperCase());
        TextView date= (TextView) findViewById(R.id.date) ;
        //dodanie daty
        Calendar cal = Calendar.getInstance();
        cal.setTime(cp.getDate());
        SimpleDateFormat sdf=new SimpleDateFormat("dd MMMM yyyy", new Locale("pl"));
        Date d=new Date();
        d.setTime(cal.getTimeInMillis());
        date.setText(sdf.format(d));

        placeDescHeader = (TextView) findViewById(R.id.textView_opismiejscaHeader);
        relLayout = (RelativeLayout) findViewById(R.id.layout_naglowekTekstu);

        setFont(polishNameHeader,"fonts/montserrat/Montserrat-Light.otf");
        setFont((TextView) findViewById(R.id.textPK),"fonts/grobe-deutschmeister/GrobeDeutschmeister.ttf" );
        setFont(germanNameHeader, "fonts/grobe-deutschmeister/GrobeDeutschmeister.ttf");
        setFont(placeDescHeader,"fonts/cambria/cambria_bold.ttf");
        setFont((TextView) findViewById(R.id.textView_posluchajAudiobooka),"fonts/cambria/cambria_bold.ttf");
        setFont((TextView) findViewById(R.id.textView_galeriaZdjec),"fonts/cambria/cambria_bold.ttf");
        setFont(date, "fonts/cambria/cambria_regular.ttc");
        setFont((TextView) findViewById(R.id.textView_audiobook_authorname), "fonts/montserrat/Montserrat-Light.otf");
       // setFont((TextView) findViewById(R.id.marekkrajewski), "fonts/montserrat/Montserrat-Light.otf");

        //audiobook
        ImageButton imageButtonAudiobook = (ImageButton) findViewById(R.id.details_play_audiobook_icon);
        final MediaPlayer mediaPlayer = MediaPlayer.create(Details.this, cp.getAudiobook());
        odtworzAudiobook = mediaPlayer;
        try{
            mediaPlayer.prepare();
        }
        catch (Exception e){
            Log.e("AudioRecord", "prepare() failed");
        }

        imageButtonAudiobook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton ib = (ImageButton)v;
                if (ib.getTag().equals("play")) {
                    ib.setImageDrawable(getDrawable(R.mipmap.pause));
                    ib.setTag("pause");

                    mediaPlayer.start();
                }
                else {
                    ib.setImageDrawable(getDrawable(R.mipmap.play));
                    ib.setTag("play");

                    mediaPlayer.pause();
                }
            }
        });

        //Zabawa sliderem -> sekcja onResume

        //Gallery
        final HorizontalScrollView hsv=(HorizontalScrollView) findViewById(R.id.gallery_layout);
        final LinearLayout layout_inside_hsv= (LinearLayout) findViewById(R.id.gallery_inside_layout);
        final List<Integer> photosId=cp.getOldPhotos();
        for(int i=0; i<photosId.size(); i++)
        {
            final ImageView imageView = new ImageView (this);
            imageView.setTag(i);

            imageView.setImageResource(photosId.get(i));

            imageView.setAdjustViewBounds(true);
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(500,500);
            lp.setMargins(30,30,30,30);
            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //check if image needs to be rotated
            String name=getResources().getResourceName(photosId.get(i));
            final String n=name.charAt(name.length()-1)+"";
            if(n.equals("h"))
            {
                imageView.setRotation(270);
            }
            layout_inside_hsv.addView(imageView);
            final int temp = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    zoomImageFromThumb(imageView, photosId.get(temp),
                            (ImageView) findViewById(R.id.zoom));
                }
            });
        }

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);


    } //protected void onCreate(Bundle savedInstanceState)

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            ((LockScrollView) findViewById(R.id.id_details_przewijanie)).setScrollingEnabled(true);
        } else {
            ((LockScrollView) findViewById(R.id.id_details_przewijanie)).setScrollingEnabled(false);
        }
        float x = event.getAxisValue(MotionEvent.AXIS_X);
        slider.slide(x);
            /*slide = new Thread(new SlideThread(x, photos, slider, slider_background, slider_background2, hold, this));
            slide.start();*/
        //Toast.makeText(this, "Lol: " + x, Toast.LENGTH_SHORT).show();
        return true;
    }

//    @Override
//    public void onClick(View v) {
//        ImageButton imageButton = (ImageButton) findViewById(R.id.details_play_audiobook_icon);
//        imageButton.setImageBitmap();
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.close:
//
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
    /*private int getWidth(int margin){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x-(2*margin);
    }*/

    @Override
    protected void onResume()
    {
        long time = System.currentTimeMillis();
        super.onResume();

        surf = (SurfaceView) findViewById(R.id.surfaceView);

        //Toast.makeText(this, "szerokosc: " + width, Toast.LENGTH_SHORT).show();
        //surf.setLayoutParams(new LinearLayout.LayoutParams(width, (int)(width*RATIO)));
        /*int width = getWidth(x_margin);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.MarginLayoutParams(width, (int)(width*RATIO)));
        params.setMargins(x_margin, 0, x_margin, 0);
        surf.setLayoutParams(params);
        hold = surf.getHolder();
        hold.setFixedSize(width, (int)(width*RATIO));
        surf.setOnTouchListener(this);*/


        bNew = cp.getSliderNewPhoto();
        bOld = cp.getSliderOldPhoto();

        /*try {
            bNew = BitmapFactory.decodeResource(getResources(), R.drawable.foto_nowe);
            bOld = BitmapFactory.decodeResource(getResources(), R.drawable.foto_stare);
        }catch(Exception e){
            System.err.println(e);
        }*/
       /*     slider_background = BitmapFactory.decodeResource(getResources(), R.drawable.slider_background);
            slider_background2 = BitmapFactory.decodeResource(getResources(), R.drawable.point_background);
            slider = BitmapFactory.decodeResource(getResources(), R.drawable.slider);
        }catch(Exception e){
            System.out.println(e);
        }*/

        //init = new SlideThread(width/2, photos, slider, slider_background, slider_background2, hold);
        //init.start();
        slider = new BeforeAfterSliderRed(bNew, bOld, surf, this, this, this);
        slider.initSlider();
        //TODO hide slider while not loaded!!!!! USE isReady method

        time = System.currentTimeMillis() - time;
        //Toast.makeText(this, "Czas ładowania slidera: " + time + "ms", Toast.LENGTH_LONG).show();
    }

    // ze strony androida
    private void zoomImageFromThumb(final View thumbView, int imageResId, ImageView viewToZoom) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = viewToZoom;
        expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.details_container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    private void clickToReadLessDetails(){

        final WebView point_description = (WebView)findViewById(R.id.description_webview);
        final ImageView readMoreButton = (ImageView)findViewById(R.id.textView_readMore);

        point_description.loadData("<html><body>"
                + "<p align=\"justify\"; style=\"text-indent: 10%; \">" + cp.getDescription() +"</p>" + "</body></html>", "text/html; charset=utf-8", "utf-8");

//        readMoreButton.setText("Zwiń");
        readMoreButton.setRotation(180);
        readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickToReadMoreDetails();
            }
        });

        ObjectAnimator.ofInt(lsv, "scrollY",  (int)relLayout.getY()-SCROLL_MARGIN).setDuration(SCROLL_DURATION).start();

    }

    private void clickToReadMoreDetails(){
        final WebView description = (WebView)findViewById(R.id.description_webview);
        final ImageView buttonDesriptionDetails = (ImageView)findViewById(R.id.textView_readMore);

        description.loadData("<html><body>"
                + "<p align=\"justify\"; style=\"text-indent: 10%; \" >" + shortDescription+  "</p> "
                + "</body></html>", "text/html; charset=utf-8", "utf-8");
        
        buttonDesriptionDetails.setRotation(0);

        buttonDesriptionDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickToReadLessDetails();
            }
        });

        ObjectAnimator.ofInt(lsv, "scrollY",  0).setDuration(SCROLL_DURATION).start();

    }

    private void setFont(TextView tv, String font)
    {
        Typeface descriptionHeaderFont = Typeface.createFromAsset(getAssets(), font);
        tv.setTypeface(descriptionHeaderFont);
    }
    public void onPause(){
        super.onPause();
        slider.stopSlider();
        odtworzAudiobook.stop();
        //init = null;
    }

    private String shortDescription()
    {
        String sD=cp.getDescription();
        if(sD.length()>275)
        {
            return shortDescription=sD.substring(0,275)+ "...";
        }
        else {
            return sD;
        }

    }

}
