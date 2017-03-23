package zpi.prototyp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.SurfaceHolder;

public class InitThread implements Runnable {
    private Bitmap photo1;
    private SurfaceHolder hold;

    public InitThread(Bitmap photo1, SurfaceHolder hold){
        this.photo1 = photo1;
        this.hold = hold;
    }

    public void run(){
        while(!hold.getSurface().isValid());

        Canvas canv = hold.lockCanvas();

        float width = canv.getWidth();
        float height = canv.getHeight();

        Paint paint = new Paint();
        RectF dstPlace = new RectF(0, 0, width, height);
        try {
            canv.drawBitmap(photo1, null, dstPlace, paint);
        }catch(Exception e){
            System.out.println(e);
        }

        hold.unlockCanvasAndPost(canv);
    }
}
