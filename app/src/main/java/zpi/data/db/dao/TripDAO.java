package zpi.data.db.dao;

import zpi.data.model.ControlPoint;
import zpi.data.model.Trip;

/**
 * @author Wojciech Michałowski
 * Trip DataBase Agent provides basic operations on database in Trip table.
 * WARNING! Don't update Trip row in database during the application running. Do it in onPause or onStop method of an activity.
 */
public interface TripDAO extends DAO {
    /**
     * Returns the trip of a given id from database.
     * @param ID ID representing Trip in the database
     * @return The trip of a given id
     */
    public Trip getTrip(int ID);

    /**
     * Puts new trip into a database
     * @param newTrip Object representation of a trip.
     * @return Number of row inserted.
     */
    public int createTrip(Trip newTrip);

    /**
     * Updates row in database in Trip table, changing last visited point.
     * @param id Id of a trip
     * @param newLastVisitedPoint New lastVisitedPoint
     * @return Returns true if update was successful (at least one row was affected)
     */
    public boolean changeLastVisitedPoint(int id, ControlPoint newLastVisitedPoint);

    /**
     * Removes trip from a database
     * @param ID Id of a trip to be deleted
     * @return Returns true if deletion waas successful (at least one row was deleted)
     */
    public boolean deleteTrip(int ID);
}
