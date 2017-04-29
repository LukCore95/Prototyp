package zpi.view.slider;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceHolder;

/**
 * Thread that needs to be started when the activity using Before/After Slider is resumed.
 * It draws new graphic on given surface when change of coordinate in X axis is noticed.
 * The coordinate can be changed by method setX which should be a handler for OnTouch
 * method of a surface host (eg. SurfaceView). The thread should be stopped on activity stop by
 * calling stopThread method.
 * @author Wojciech Michałowski
 */
public class SlideThread extends Thread {

    private Context ctx;

    // stop the thread?
    private boolean stop = false;

    //draw the view?
    private boolean drawn = false;

    //coordinate x (dirt)
    private float x;

    //coordinate to be used to computing
    private float xBis;
    //former coordinate
    private float xOld = -1;
    //is coordinate changed?
    private boolean zmiana = false;

    //needed graphics
    private Bitmap[] photos;
    private Integer[] iPhotos;
    private Bitmap background;
    private int iBackground;
    private Bitmap backgroundDeeper;
    private int iBackgroundDeeper;
    private Bitmap slider;
    private int iSlider;

    //Surface to draw on
    private SurfaceHolder hold;

    //constants
    private final int margin_top = 16;
    private final int chwytak_height = 98;
    private final int slider_half_width = 40;

    //dimensions to compute
    private int margin;

    private float width;
    private float height;

    private float im1Width;
    private float im1Height;
    private float im2Width;
    private float im2Height;
    private float backWidth;
    private float backHeight;
    private int sliderWidth;
    private int sliderHeight;
    private final float sliderBaseHeight = 765.0f;
    private float bck2size = 256.0f;
    private float multip;

    //rectangular dimensions to compute
    private Rect src1;
    private Rect src2;
    private RectF dst1;
    private RectF dst2;
    private Rect srcbck;
    private RectF dstbck;
    private RectF dstbck2;
    private Rect sliderSrc;
    private RectF sliderDst;
    private Rect sliderSrc2;
    private RectF sliderDst2;
    private Rect sliderSrc3;
    private RectF sliderDst3;

    //coordinates of source images to compute
    private int x1;
    private int x2;
    //painter
    private Paint paint;

    /**
     * Public constructor for SlideThread.
     * @param x Initial coordinate of slider bar position in a given surface.
     * @param photos An array of two bitmaps, where the first bitmap represents new photo, and the second - the old one
     * @param slider Bitmap that represents a slider bar
     * @param background Bitmap of a background of the photos
     * @param backgroundDeeper Bitmap of a background of all surface
     * @param hold SurfaceHolder needed to draw on a surface.
     */
    public SlideThread(float x, Integer[] photos, int slider, int background, int backgroundDeeper, SurfaceHolder hold, Context ctx){
        setX(x);
        this.iPhotos = photos;
        this.hold = hold;
        this.iBackground = background;
        this.iSlider = slider;
        this.iBackgroundDeeper = backgroundDeeper;
        this.ctx = ctx;

        this.photos = new Bitmap[2];

        paint = new Paint();

    }

    /**
     * Synchronized method - use this for setting current X coordinate and detect change of X axis coordinate.
     * @param x New X axis coordinate
     */
    synchronized public void setX(float x){
        //setting old coordinate
        this.xOld = this.x;

        //changing current coordinate
        this.x = x;

        //detecting change of coordinate
        zmiana = (x!=xOld);
    }

    /**
     * Synchronized method - use this for getting current X coordinate.
     * Alsoo cheks if the coordinate is in range - if it's not, method returns nearest extremum.
     * @return Effective X coordinate for slider bar. It does not represent coordinate of users touch, but nearest eligible
     * X coordinate for slider bar!
     */
    synchronized  public float getX(){
        //out of range - too small
        if (x < (margin+slider_half_width)){
           return margin+slider_half_width;
        }//out of range - too large
        else if (x > width - margin - slider_half_width){
            return width-margin-slider_half_width;
        }//fits in :)
        else
            return x;
    }

    /**
     * Exit the thread loop and stop thread. Use this method while ceasing the activity.
     */
    public void stopThread(){
        stop = true;
    }

    public synchronized boolean isDrawn(){
        return drawn;
    }

    /**
     * (Re)draw Before/After screen when change of X axis coordinate is noticed. Call stopThread method to stop this thread.
     */
    public void run(){

        //decode bitmaps
        Resources res = ctx.getResources();
        photos[0] = BitmapFactory.decodeResource(res, iPhotos[0]);
        photos[1] = BitmapFactory.decodeResource(res, iPhotos[1]);
        background = BitmapFactory.decodeResource(res, iBackground);
        backgroundDeeper = BitmapFactory.decodeResource(res, iBackgroundDeeper);
        slider = BitmapFactory.decodeResource(res, iSlider);

        computeStaticDimensions();

        while(!stop) {
            if(!zmiana) //if coordinate is not changed - continue
                continue;

            while (!hold.getSurface().isValid()); //wait for surfaceHolder

            Canvas canv = hold.lockCanvas();

            //compute dimensions of canvas
            width = canv.getWidth();
            height = canv.getHeight();

            //get clean coordinate (without risk of desynchronization)
            xBis = getX();

            //translate given coordinate to coordinate within the photos
            x1 = (int) (((xBis - margin) * im1Width) / (width - 2 * margin));
            x2 = (int) (((xBis - margin) * im2Width) / (width - 2 * margin));

            //compute remaining dimensions
            computeNonStaticDimensions();

            //draw deeperBackground as tiles
            for(int act = 0; act < width; act += bck2size) {
                canv.drawBitmap(backgroundDeeper, null, new RectF(act, 0, act+bck2size, 0+bck2size), paint);
                canv.drawBitmap(backgroundDeeper, null, new RectF(act, height-bck2size, act+bck2size, height), paint);
            }
            //draw the rest of screen
            canv.drawBitmap(background, srcbck, dstbck, paint);
            canv.drawBitmap(photos[0], src2, dst2, paint);
            canv.drawBitmap(photos[1], src1, dst1, paint);
            canv.drawBitmap(slider, sliderSrc2, sliderDst2, paint);
            canv.drawBitmap(slider, sliderSrc, sliderDst, paint);
            canv.drawBitmap(slider, sliderSrc3, sliderDst3, paint);

            //post drawn screen
            hold.unlockCanvasAndPost(canv);

            setDrawn(true);

        }
    }

    /**
     * Computes all the static dimensions of images, that needs to be compute only once at slider initialization.
     * Use this in SlideThread constructor.
     */
    private void computeStaticDimensions(){
        im1Width = photos[1].getWidth();
        im1Height = photos[1].getHeight();
        im2Width = photos[0].getWidth();
        im2Height = photos[0].getHeight();
        backWidth = background.getWidth();
        backHeight = background.getHeight();

        srcbck = new Rect(0 + 20, 0 + 20, (int) backWidth - 20, (int) backHeight - 20);
        multip = slider.getHeight()/sliderBaseHeight;
        margin = (int)(multip*8);

        sliderHeight = slider.getHeight();
        sliderWidth = slider.getWidth();

        bck2size = bck2size*multip;
    }

    /**
     * Computes all the non-static dimensions of images, that needs to be compute in each redraw frame after canvas locking.
     * Use this in run method after computing width and height of the canvas.
     */
    private void computeNonStaticDimensions(){
        src1 = new Rect(0, 0, x1, (int) im1Height);
        src2 = new Rect(x2, 0, (int) im2Width, (int) im2Height);
        dst1 = new RectF(0 + margin, 0 + margin + margin_top, xBis, height - margin - chwytak_height - margin_top);
        dst2 = new RectF(xBis, 0 + margin + margin_top, width - margin, height - margin - chwytak_height - margin_top);
        dstbck = new RectF(0, 0 + margin_top, width, height - chwytak_height - margin_top);
        dstbck2 = new RectF(0, 0, width, height);
        sliderSrc = new Rect(0, Math.max((int)(sliderHeight/multip - (int) height), 0), sliderWidth, (int)((sliderHeight-chwytak_height)/multip));
        sliderSrc2 = new Rect(0, 0, sliderWidth, Math.max((int)((int)height-sliderHeight/multip), 0));
        sliderDst = new RectF(xBis - slider_half_width, Math.max(((int)height-sliderHeight/multip), 0), xBis + slider_half_width, height - margin_top - chwytak_height);
        sliderDst2 = new RectF(xBis - slider_half_width, 0, xBis + slider_half_width, Math.max(((int)height-sliderHeight/multip), 0));
        sliderSrc3 = new Rect(0, (int)((sliderHeight/multip-chwytak_height)*multip), sliderWidth, sliderHeight);
        sliderDst3 = new RectF(xBis - slider_half_width, height - chwytak_height - margin_top, xBis + slider_half_width, height - margin_top);
    }

    private synchronized void setDrawn(boolean drawn){
        this.drawn = drawn;
    }
}
