package zpi.view.slider;

import android.content.Context;
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
    //context of app
    private Context ctx;

    //needed bitmaps
    private int bOld;
    private int bNew;
    private static final int bBar = R.drawable.slider;
    private static final int bBackground = R.drawable.slider_background;
    private static final int bBackground2 = R.drawable.point_background;

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
    public BeforeAfterSliderRed(int bNew, int bOld, SurfaceView surfaceView, FragmentActivity fActivity, View.OnTouchListener listener, Context ctx) {
        this.bNew = bNew;
        this.bOld = bOld;
        this.surfaceView = surfaceView;
        this.fActivity = fActivity;
        this.listener = listener;
        this.ctx = ctx;

        int width = getWidth(x_margin);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.MarginLayoutParams(width, (int)(width*RATIO)));
        params.setMargins(x_margin, 0, x_margin, 0);
        surfaceView.setLayoutParams(params);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setFixedSize(width, (int)(width*RATIO));
        surfaceView.setOnTouchListener(listener);


        Integer[] bArray = new Integer[2];
        bArray[0] = bNew;
        bArray[1] = bOld;
        thread = new SlideThread(width/2, bArray, bBar, bBackground, bBackground2, surfaceHolder, ctx);
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

    @Override
    public boolean isReady(){
        return thread.isDrawn();
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
