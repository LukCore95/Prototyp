package zpi.data.db;

import zpi.data.model.ControlPoint;
import zpi.data.model.Trip;

/**
 * @author Wojciech Michałowski
 * Trip DataBase Agent provides basic operations on database in Trip table.
 * WARNING! Don't update Trip row in database during the application running. Do it in onPause or onStop method of an activity.
 */
public interface TripDAO extends DAO {
    public Trip getTrip(int ID);
    public int createTrip(Trip newTrip);
    public boolean changeLastVisitedPoint(int id, ControlPoint newLastVisitedPoint);
    public boolean deleteTrip(int ID);
}
