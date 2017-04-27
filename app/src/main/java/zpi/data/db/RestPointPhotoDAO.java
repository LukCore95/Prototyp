package zpi.data.db;

import java.util.List;

import zpi.data.model.RestPoint;

/**
 * Created by Ania on 2017-04-25.
 */

public interface RestPointPhotoDAO extends DAO {
    public List<Integer> getRestPointsPhotos(String name);
    public boolean insertPhotosFromRestPoint(RestPoint rp);
}
