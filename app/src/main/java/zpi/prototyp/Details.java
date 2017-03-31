package zpi.prototyp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Details extends FragmentActivity implements View.OnTouchListener{

    private SurfaceView surf;
    private SurfaceHolder hold;
    private Thread init;
    private Thread slide;
    private Bitmap[] photos = new Bitmap[2];
    private ImageButton closeIB;

    // powiększanie obrazka i animacja
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        //opis obiektu
        final WebView renoma_description = (WebView) findViewById(R.id.renoma_decription_webview);
        renoma_description.getSettings().setDefaultTextEncodingName("UTF-8");
        renoma_description.setBackgroundColor(Color.TRANSPARENT);
        clickToReadMoreDetails();


        closeIB = (ImageButton) findViewById(R.id.closeImageButton);
        closeIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView myTextView = (TextView) findViewById(R.id.renoma_de_name);
        TextView myTextView1 = (TextView) findViewById(R.id.textPK);
        Typeface typeface=Typeface.createFromAsset(getAssets(), "fonts/grobe-deutschmeister/GrobeDeutschmeister.ttf");
        myTextView.setTypeface(typeface);
        myTextView1.setTypeface(typeface);

        ImageButton imageButtonAudiobook = (ImageButton) findViewById(R.id.details_play_audiobook_icon);
        // ZMIENIć TE SZAJSKIE IKONKI, BO TO JAKIEś Z NETA TYMCZASOWO WZIĄłEM ~W
        imageButtonAudiobook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton ib = (ImageButton)v;
                if (ib.getTag().equals("play")) {
                    ib.setImageDrawable(getDrawable(R.drawable.pause_icon));
                    ib.setTag("pause");
                }
                else {
                    ib.setImageDrawable(getDrawable(R.drawable.play_icon));
                    ib.setTag("play");
                }
            }
        });

//        final TextView renomaDesriptionDetails = (TextView)findViewById(R.id.textView_readMore);
//        renomaDesriptionDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                AlertDialog.Builder adbuilder = new AlertDialog.Builder(v.getContext());
////                adbuilder.setMessage(R.string.renoma_description_full).setTitle("Renoma - opis").
////                        setCancelable(true).setNeutralButton("Powrót", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.cancel();
////                    }
////                });
////                AlertDialog ad = adbuilder.create();
////                ad.show();
//                clickToReadLessDetails();
//
//
//            }
//        });

        //justowanie tekstu
//        TextView renomaDescription = (TextView)findViewById(R.id.renoma_description);
//        String textopisu = String.valueOf(Html.fromHtml("<![CDATA[<body style=\"text-align:justify;color:#222222; \">"
//                + getResources().getString(R.string.renoma_description)
//                + "</body>]]>"));
//        ((View)renomaDescription).load


        //Zabawa sliderem -> sekcja onResume

        //powiększanie
        final View renomaSmall1 = findViewById(R.id.imageView_renomaGallery1);
        renomaSmall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(renomaSmall1, R.drawable.renoma1,
                        (ImageView) findViewById(R.id.renoma1_zoom));
            }
        });
        final View renomaSmall2 = findViewById(R.id.imageView_renomaGallery2);
        renomaSmall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(renomaSmall2, R.drawable.renoma2,
                        (ImageView) findViewById(R.id.renoma2_zoom));
            }
        });
        final View renomaSmall3 = findViewById(R.id.imageView_renomaGallery3);
        renomaSmall3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(renomaSmall3, R.drawable.renoma3,
                        (ImageView) findViewById(R.id.renoma3_zoom));
            }
        });
        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);


    } //protected void onCreate(Bundle savedInstanceState)

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
    @Override
    protected void onResume()
    {
        super.onResume();
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

        final WebView renoma_description = (WebView)findViewById(R.id.renoma_decription_webview);
        final TextView renomaDesriptionDetails = (TextView)findViewById(R.id.textView_readMore);

//        renoma_description.reload();
        renoma_description.loadData("<html><body>"
                + "<p align=\"justify\">" + getString(R.string.renoma_description_full) +  "</p> "
                + "</body></html>", "text/html; charset=utf-8", "utf-8");

        renomaDesriptionDetails.setText("Zwiń");
        renomaDesriptionDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickToReadMoreDetails();
            }
        });

    }

    private void clickToReadMoreDetails(){
        final WebView renoma_description = (WebView)findViewById(R.id.renoma_decription_webview);
        final TextView renomaDesriptionDetails = (TextView)findViewById(R.id.textView_readMore);

//        renoma_description.setVisibility(View.GONE);
        String renoma_description_content = getString(R.string.renoma_description);
        renoma_description.loadData("<html><body>"
                + "<p align=\"justify\">" + renoma_description_content +  "...</p> "
                + "</body></html>", "text/html; charset=utf-8", "utf-8");
        renomaDesriptionDetails.setText("Rozwiń");

//        renoma_description.setVisibility(View.VISIBLE);
        renomaDesriptionDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickToReadLessDetails();
            }
        });
    }
}
