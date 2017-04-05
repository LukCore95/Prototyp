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

public class SlideThread implements Runnable {
    private float x;
    private Bitmap[] photos;
    private Bitmap background;
    private Bitmap backgroundDeeper;
    private SurfaceHolder hold;
    private Bitmap slider;
    private Context ctx;
    private final int margin_top = 16;
    private final int margin = 32;
    private final int chwytak_height = 98;
    private final int slider_half_width = 40;


    public SlideThread(float x, Bitmap[] photos, Bitmap slider, Bitmap background, Bitmap backgroundDeeper, SurfaceHolder hold, Context ctx){
        this.x = x;
        this.photos = photos;
        this.hold = hold;
        this.background = background;
        this.slider = slider;
        this.backgroundDeeper = backgroundDeeper;
        this.ctx = ctx;
    }

    public void run(){
        while(!hold.getSurface().isValid());

        Canvas canv = hold.lockCanvas();

        float width = canv.getWidth();
        float height = canv.getHeight();

        if(x<margin||x>width-margin){
            hold.unlockCanvasAndPost(canv);
            return;}

        float im1Width = photos[1].getWidth();
        float im1Height = photos[1].getHeight();
        float im2Width = photos[0].getWidth();
        float im2Height = photos[0].getHeight();
        float backWidth = background.getWidth();
        float backHeight = background.getHeight();

        int x1 = (int)(((x-margin)*im1Width)/(width-2*margin));
        int x2 = (int)(((x-margin)*im2Width)/(width-2*margin));

        Paint paint = new Paint();

        Rect src1 = new Rect(0, 0, x1, (int)im1Height);
        Rect src2 = new Rect(x2, 0, (int)im2Width, (int)im2Height);
        RectF dst1 = new RectF(0+margin, 0+margin+margin_top, x, height-margin-chwytak_height);
        RectF dst2 = new RectF(x, 0+margin+margin_top, width-margin, height-margin-chwytak_height);
        Rect srcbck = new Rect(0+20, 0+20, (int)backWidth-20, (int)backHeight-20);
        RectF dstbck = new RectF(0, 0+margin_top, width, height-chwytak_height);
        RectF dstbck2 = new RectF(0, 0, width, height);
        Rect sliderSrc = new Rect(0, Math.max((slider.getHeight()-(int)height)/2, 0), slider.getWidth(), slider.getHeight());
        RectF sliderDst = new RectF(x-slider_half_width, 0, x+slider_half_width, height);

        //canv.drawColor(ctx.getResources().getColor(R.color.alpha));
        canv.drawBitmap(backgroundDeeper, null, dstbck2, paint);
        canv.drawBitmap(background, srcbck, dstbck, paint);
        canv.drawBitmap(photos[0], src2, dst2, paint);
        //paint.setMaskFilter(new BlurMaskFilter(30.0f, BlurMaskFilter.Blur.NORMAL));
        canv.drawBitmap(photos[1], src1, dst1, paint);
        canv.drawBitmap(slider, sliderSrc, sliderDst, paint);

        hold.unlockCanvasAndPost(canv);
    }
}
