package zpi.data.db;

import zpi.data.model.InterestingPlace;

/**
 * @author Wojciech Michałowski
 * Interesting Place DataBase Agent provides basic operations on database in InterestingPlace table.
 */
public interface InterestingPlaceDBAgent extends DBAgent {

    /**
     * Get the interesting place of a given name from database.
     * @param name Name of needed Interesting Place
     * @return Needed Interesting Place
     */
    public InterestingPlace getInterestingPlace(String name);

    /**
     * Puts new row into InterestingPlace table in database.
     * @param newInterestingPlace Interesting Place to be inserted into database
     * @return ID of a new Interesting Place in database
     */
    public int createInterestingPlace(InterestingPlace newInterestingPlace);
}
