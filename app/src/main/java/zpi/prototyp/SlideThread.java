package zpi.prototyp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceHolder;

public class SlideThread extends Thread {

    private boolean stop = false;

    //współrzędna - do modyfikacji
    private float x;

    //współrzędna do obliczeń
    private float xBis;
    private float xOld = -1;
    private boolean zmiana = false;

    //potrzebne grafiki
    private Bitmap[] photos;
    private Bitmap background;
    private Bitmap backgroundDeeper;
    private Bitmap slider;

    //Potrzebna powierzchnia do rysowania
    private SurfaceHolder hold;

    //potrzebny context
    private Context ctx;

    //stałe wartości liczbowe
    private final int margin_top = 16;
    private final int chwytak_height = 98;
    private final int slider_half_width = 40;
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

    int x1;
    int x2;

    Paint paint;

    public SlideThread(float x, Bitmap[] photos, Bitmap slider, Bitmap background, Bitmap backgroundDeeper, SurfaceHolder hold, Context ctx){
        setX(x);
        this.photos = photos;
        this.hold = hold;
        this.background = background;
        this.slider = slider;
        this.backgroundDeeper = backgroundDeeper;
        this.ctx = ctx;

        im1Width = photos[1].getWidth();
        im1Height = photos[1].getHeight();
        im2Width = photos[0].getWidth();
        im2Height = photos[0].getHeight();
        backWidth = background.getWidth();
        backHeight = background.getHeight();

        paint = new Paint();

        srcbck = new Rect(0 + 20, 0 + 20, (int) backWidth - 20, (int) backHeight - 20);
        multip = slider.getHeight()/sliderBaseHeight;
        margin = (int)(multip*8);

        sliderHeight = slider.getHeight();
        sliderWidth = slider.getWidth();

        bck2size = bck2size*multip;
    }

    synchronized public void setX(float x){
        this.xOld = this.x;
        this.x = x;

        zmiana = (x!=xOld);
    }

    public void stopThread(){
        stop = true;
    }

    synchronized  public float getX(){
        if (x < (margin+slider_half_width)){
           return margin+slider_half_width;
        }
        else if (x > width - margin - slider_half_width){
            return width-margin-slider_half_width;
        }
        else
            return x;
    }


    public void run(){
        while(!stop) {
            if(!zmiana)
                continue;

            while (!hold.getSurface().isValid()) ;

            Canvas canv = hold.lockCanvas();

            width = canv.getWidth();
            height = canv.getHeight();

            xBis = getX();

            /*if (xBis < (margin+slider_half_width) || xBis > (width - margin - slider_half_width)) {
                hold.unlockCanvasAndPost(canv);
                continue;
            }*/

            x1 = (int) (((xBis - margin) * im1Width) / (width - 2 * margin));
            x2 = (int) (((xBis - margin) * im2Width) / (width - 2 * margin));

            src1 = new Rect(0, 0, x1, (int) im1Height);
            src2 = new Rect(x2, 0, (int) im2Width, (int) im2Height);
            dst1 = new RectF(0 + margin, 0 + margin + margin_top, xBis, height - margin - chwytak_height - margin_top);
            dst2 = new RectF(xBis, 0 + margin + margin_top, width - margin, height - margin - chwytak_height - margin_top);
            dstbck = new RectF(0, 0 + margin_top, width, height - chwytak_height - margin_top);
            dstbck2 = new RectF(0, 0, width, height);
            sliderSrc = new Rect(0, Math.max((int)(sliderHeight/multip - (int) height), 0), sliderWidth, sliderHeight);
            sliderSrc2 = new Rect(0, 0, sliderWidth, Math.max((int)((int)height-sliderHeight/multip), 0));
            sliderDst = new RectF(xBis - slider_half_width, Math.max(((int)height-sliderHeight/multip), 0), xBis + slider_half_width, height - margin_top);
            sliderDst2 = new RectF(xBis - slider_half_width, 0, xBis + slider_half_width, Math.max(((int)height-sliderHeight/multip), 0));
            //canv.drawColor(ctx.getResources().getColor(R.color.alpha));
            for(int act = 0; act < width; act += bck2size) {
                canv.drawBitmap(backgroundDeeper, null, new RectF(act, 0, act+bck2size, 0+bck2size), paint);
                canv.drawBitmap(backgroundDeeper, null, new RectF(act, height-bck2size, act+bck2size, height), paint);
            }
            canv.drawBitmap(background, srcbck, dstbck, paint);
            canv.drawBitmap(photos[0], src2, dst2, paint);
            //paint.setMaskFilter(new BlurMaskFilter(30.0f, BlurMaskFilter.Blur.NORMAL));
            canv.drawBitmap(photos[1], src1, dst1, paint);
            canv.drawBitmap(slider, sliderSrc2, sliderDst2, paint);
            canv.drawBitmap(slider, sliderSrc, sliderDst, paint);

            hold.unlockCanvasAndPost(canv);

        }
    }
}
