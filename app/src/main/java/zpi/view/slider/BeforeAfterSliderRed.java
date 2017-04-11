package zpi.view.slider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import zpi.prototyp.R;

/**
 * Created by Ania on 2017-04-11.
 */

/**
 * Implementation of Before/After Slider in Red Mock Style
 * IMPORTANT!
 * SurfaceView must be nested in LinearLayout!
 * The SurfaceView will be automatically expanded screen-wide!
 * The app must provide in folder drawables: slider_background, point_background (which is the deeper background) and slider!!!
 * @author Wojciech Michałowski
 */
public class BeforeAfterSliderRed implements BeforeAfterSlider {
    //needed bitmaps
    private Bitmap bOld;
    private Bitmap bNew;
    private Bitmap bBar;
    private Bitmap bBackground;
    private Bitmap bBackground2;

    //slider thread
    private SlideThread thread;

    //needed surface
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    //activity which hosts the slider
    private FragmentActivity fActivity;

    //on touch listener for surfaceView
    private View.OnTouchListener listener;


    private static final float RATIO = 0.8124f; // represents (height/width) of the screen
    private static final int x_margin = 20; //left and rightm margin in px for the surfaceView


    /**
     * Public constructor for a red slider.
     * @param bNew A nowadays photo
     * @param bOld An old photo
     * @param surfaceView SurfaceView on which the slider will be displayed
     * @param fActivity An activity which hosts the slider
     * @param listener  OnTouch listener for the SurfaceView - must use this class's slide() method to work properly!
     */
    public BeforeAfterSliderRed(Bitmap bNew, Bitmap bOld, SurfaceView surfaceView, FragmentActivity fActivity, View.OnTouchListener listener) {
        this.bNew = bNew;
        this.bOld = bOld;
        this.surfaceView = surfaceView;
        this.fActivity = fActivity;
        this.listener = listener;

        int width = getWidth(x_margin);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.MarginLayoutParams(width, (int)(width*RATIO)));
        params.setMargins(x_margin, 0, x_margin, 0);
        surfaceView.setLayoutParams(params);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setFixedSize(width, (int)(width*RATIO));
        surfaceView.setOnTouchListener(listener);

        try {
            bNew = BitmapFactory.decodeResource(fActivity.getResources(), R.drawable.foto_nowe);
            bOld = BitmapFactory.decodeResource(fActivity.getResources(), R.drawable.foto_stare);
            bBackground = BitmapFactory.decodeResource(fActivity.getResources(), R.drawable.slider_background);
            bBackground2 = BitmapFactory.decodeResource(fActivity.getResources(), R.drawable.point_background);
            bBar = BitmapFactory.decodeResource(fActivity.getResources(), R.drawable.slider);
        }catch(Exception e){
            System.err.println(e);
        }

        Bitmap[] bArray = new Bitmap[2];
        bArray[0] = bNew;
        bArray[1] = bOld;
        thread = new SlideThread(width/2, bArray, bBar, bBackground, bBackground2, surfaceHolder);
    }

    /**
     * Initialize the red mock style slider
     */
    @Override
    public void initSlider(){
        if(!thread.isAlive())
            thread.start();
    }

    /**
     * Perform a slide on the slider
     * @param x Put MotionEvent X axis coordinate of a surfaceView here. (event.getAxisValue(MotionEvent.AXIS_X)).
     */
    @Override
    public void slide(float x){
        thread.setX(x);
    }

    /**
     * Stop the slider thread. Once this method is call, you must reinitialize the slider before using again!
     */
    @Override
    public void stopSlider()
    {
        if(thread.isAlive())
            thread.stopThread();
    }

    /**
     * Internal method which gives needed width of a surfaceView.
     * @param margin Left and right margin in px.
     * @return total needed width od a surfaceView
     */
    private int getWidth(int margin){
        Display display = fActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x-(2*margin);
    }
}
