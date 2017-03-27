package zpi.prototyp;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceHolder;

public class SlideThread implements Runnable {
    private float x;
    private Bitmap[] photos;
    private SurfaceHolder hold;

    public SlideThread(float x, Bitmap[] photos, SurfaceHolder hold){
        this.x = x;
        this.photos = photos;
        this.hold = hold;
    }

    public void run(){
        while(!hold.getSurface().isValid());

        Canvas canv = hold.lockCanvas();

        float width = canv.getWidth();
        float height = canv.getHeight();
        float im1Width = photos[1].getWidth();
        float im1Height = photos[1].getHeight();
        float im2Width = photos[0].getWidth();
        float im2Height = photos[0].getHeight();

        int x1 = (int)((x*im1Width)/width);
        int x2 = (int)((x*im2Width)/width);

        Paint paint = new Paint();

        Rect src1 = new Rect(0, 0, x1, (int)im1Height);
        Rect src2 = new Rect(x2, 0, (int)im2Width, (int)im2Height);
        RectF dst1 = new RectF(0, 0, x, height);
        RectF dst2 = new RectF(x, 0, width, height);

        canv.drawBitmap(photos[0], src2, dst2, paint);
        //paint.setMaskFilter(new BlurMaskFilter(30.0f, BlurMaskFilter.Blur.NORMAL));
        canv.drawBitmap(photos[1], src1, dst1, paint);

        hold.unlockCanvasAndPost(canv);
    }
}
