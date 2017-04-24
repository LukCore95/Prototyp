package zpi.data.db;

import java.util.List;

import zpi.data.model.ControlPoint;

/**
 * Created by Ania on 2017-04-24.
 */

public interface ControlPointPhotoDBAgent extends DBAgent {
    public List<Integer> getControlPointsPhotos(String name);
    public boolean insertPhotosFromControlPoint(ControlPoint cp);
}
