package zpi.view.slider;

/**
 * Created by Wojtek Michałowski on 2017-04-11.
 */

/**
 * Provides methods for transforming a surfaceView into a Before/After Slider of a 2 photos.
 * @author Wojciech Michałowski
 */
public interface BeforeAfterSlider {
    /**
     * Initialize the slider. It won't display anything if not initialized!
     */
    public void initSlider();

    /**
     * Perform the slide to X axis coordinate of surfaceView. It won't give any effect if slider is not initialized!
     * @param x Put MotionEvent X axis coordinate of a surfaceView here. (event.getAxisValue(MotionEvent.AXIS_X)).
     *          It represents the X axis coordinate inside a surfaceView.
     */
    public void slide(float x);

    /**
     * Stop the slider thread. Use this method while ceasing the activity. If not, app is gonna crash!
     * You need to reinitialize slider after stoping it if you want to slide.
     */
    public void stopSlider();

    /**
     * Check if the slider is ready to display.
     */
    public boolean isReady();

}
