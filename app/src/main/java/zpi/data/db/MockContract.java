package zpi.data.db;


import android.provider.BaseColumns;

/**
 * @author Wojciech Michałowski
 * Contract that represents Mock database and contains SQL DDL scripts.
 */
public class MockContract {

    public static final String RouteCreation = "create table " + RouteEntry.TABLE_NAME + " (" + RouteEntry.COLUMN_NAME_ID + " integer primary key autoincrement, " + RouteEntry.COLUMN_NAME_NAME + " varchar(30) not null unique);";
    public static final String ControlPointCreation = "create table " + ControlPointEntry.TABLE_NAME + " (" +  ControlPointEntry.COLUMN_NAME_ID + " integer primary key autoincrement, " + ControlPointEntry.COLUMN_NAME_NAME + " varchar(50) not null unique, " + ControlPointEntry.COLUMN_NAME_GERMAN + " varchar(70) not null, " + ControlPointEntry.COLUMN_NAME_DESC + " varchar(1000) not null, \"" + ControlPointEntry.COLUMN_NAME_DATE + "\" date not null, " + ControlPointEntry.COLUMN_NAME_LONG + " double(8) not null, " + ControlPointEntry.COLUMN_NAME_LAT + " double(8) not null, " + ControlPointEntry.COLUMN_NAME_ICON + " integer(10) not null, " + ControlPointEntry.COLUMN_NAME_SLOLD + " integer(10) not null, " + ControlPointEntry.COLUMN_NAME_SLNEW + " integer(10) not null, " + ControlPointEntry.COLUMN_NAME_AUDIO + " integer(10) not null);";
    public static final String TripCreation = "create table " + TripEntry.TABLE_NAME + " (" + TripEntry.COLUMN_NAME_ID + " integer primary key, " + TripEntry.COLUMN_NAME_START + " integer(10) not null, " + TripEntry.COLUMN_NAME_LAST + " integer(10), " + TripEntry.COLUMN_NAME_ROUTE + " integer(10), foreign key(" + TripEntry.COLUMN_NAME_ROUTE + ") references " + RouteEntry.TABLE_NAME + "(" + RouteEntry.COLUMN_NAME_ID + "), foreign key(" + TripEntry.COLUMN_NAME_START + ") references " + ControlPointEntry.TABLE_NAME + "(" + ControlPointEntry.COLUMN_NAME_ID + "), foreign key(" + TripEntry.COLUMN_NAME_LAST + ") references " + ControlPointEntry.TABLE_NAME + "(" + ControlPointEntry.COLUMN_NAME_ID + "));";
    public static final String RoutePointCreation = "create table " + RoutePointEntry.TABLE_NAME + " (" + RoutePointEntry.COLUMN_NAME_NUMBER + " integer(10) not null unique, " + RoutePointEntry.COLUMN_NAME_POINT + " integer(10) not null unique, " + RoutePointEntry.COLUMN_NAME_ROUTE + " integer(10) not null, primary key (" + RoutePointEntry.COLUMN_NAME_NUMBER + ", " + RoutePointEntry.COLUMN_NAME_POINT + ", " + RoutePointEntry.COLUMN_NAME_ROUTE + "), foreign key(" + RoutePointEntry.COLUMN_NAME_POINT + ") references " + ControlPointEntry.TABLE_NAME + "(" +ControlPointEntry.COLUMN_NAME_ID + "), foreign key(" + RoutePointEntry.COLUMN_NAME_ROUTE + ") references " + RouteEntry.TABLE_NAME + "(" + RouteEntry.COLUMN_NAME_ID + "));";
    public static final String InterestingPlaceCreation = "create table " + InterestingPlaceEntry.TABLE_NAME +  "(" + InterestingPlaceEntry.COLUMN_NAME_ID + " integer primary key autoincrement, " + InterestingPlaceEntry.COLUMN_NAME_NAME + " varchar(50) not null unique, " + InterestingPlaceEntry.COLUMN_NAME_DESC + " varchar(1000) not null, " + InterestingPlaceEntry.COLUMN_NAME_LONG + " double(8) not null, " + InterestingPlaceEntry.COLUMN_NAME_LAT + " double(8) not null, " + InterestingPlaceEntry.COLUMN_NAME_ADDRESS + " varchar(100) not null, " + InterestingPlaceEntry.COLUMN_NAME_TYPE + " integer(2) not null);";
    public static final String RestPointCreation = "create table " + RestPointEntry.TABLE_NAME + " (" + RestPointEntry.COLUMN_NAME_ID + " integer primary key autoincrement, " + RestPointEntry.COLUMN_NAME_NAME + " varchar(50) not null unique, " + RestPointEntry.COLUMN_NAME_DESC + " varchar(1000) not null, " + RestPointEntry.COLUMN_NAME_LONG + " double(8) not null, " + RestPointEntry.COLUMN_NAME_LAT + " double(10) not null, " + RestPointEntry.COLUMN_NAME_TYPE + " integer(10) not null, " + RestPointEntry.COLUMN_NAME_ADDRESS + " varchar(100) not null, " + RestPointEntry.COLUMN_NAME_WEB + " varchar(50));";
    public static final String RestPointPhotoCreation = "create table " + RestPointPhotoEntry.TABLE_NAME + " (" + RestPointPhotoEntry.COLUMN_NAME_ID + " integer primary key, " + RestPointPhotoEntry.COLUMN_NAME_POINT + " integer(10) not null, foreign key(" + RestPointPhotoEntry.COLUMN_NAME_POINT + ") references " + RestPointEntry.TABLE_NAME + "(" + RestPointEntry.COLUMN_NAME_ID + "));";
    public static final String InterestingPlacePhotoCreation = "create table " + InterestingPlacePhotoEntry.TABLE_NAME + " (" + InterestingPlacePhotoEntry.COLUMN_NAME_ID + " integer not null primary key, " + InterestingPlacePhotoEntry.COLUMN_NAME_POINT + " integer(10) not null, foreign key(" + InterestingPlacePhotoEntry.COLUMN_NAME_POINT + ") references " + InterestingPlaceEntry.TABLE_NAME + "(" + InterestingPlaceEntry.COLUMN_NAME_ID + "));";
    public static final String ControlPointPhotoCreation = "create table " + ControlPointPhotoEntry.TABLE_NAME + " (" + ControlPointPhotoEntry.COLUMN_NAME_ID + " integer not null primary key, " + ControlPointPhotoEntry.COLUMN_NAME_POINT + " integer(10) not null, foreign key(" + ControlPointPhotoEntry.COLUMN_NAME_POINT + ") references " + ControlPointEntry.TABLE_NAME + "(" + ControlPointEntry.COLUMN_NAME_ID + "));";


    /**
     * Constructor is private because this class is not to instatiate!
     */
    private MockContract(){}

    /**
     * @author Wojciech Michałowski
     * Part of contract that applies to the Route table
     */
    public class RouteEntry implements BaseColumns {
        public static final String TABLE_NAME = "Route";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_NAME = "Name";
    }

    /**
     * @author Wojciech Michałowski
     * Part of contract that applies to CotrolPoint table.
     */
    public class ControlPointEntry implements BaseColumns {
        public static final String TABLE_NAME = "ControlPoint";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_GERMAN = "GermanName";
        public static final String COLUMN_NAME_DESC = "Description";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_LONG = "Longitude";
        public static final String COLUMN_NAME_LAT = "Latitude";
        public static final String COLUMN_NAME_ICON = "Icon";
        public static final String COLUMN_NAME_SLOLD = "SliderOldPhoto";
        public static final String COLUMN_NAME_SLNEW = "SliderNewPhoto";
        public static final String COLUMN_NAME_AUDIO = "Audiobook";
    }

    /**
     * @author Wojciech Michałowski
     * Part of contract that applies to Trip table.
     */
    public class TripEntry implements BaseColumns {
        public static final String TABLE_NAME = "Trip";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_START = "StartPointID";
        public static final String COLUMN_NAME_LAST = "LastVisitedID";
        public static final String COLUMN_NAME_ROUTE = "RouteID";
    }

    /**
     * @author Wojciech Michałowski
     * Part of contract that applies to RoutePoint table.
     */
    public class RoutePointEntry implements BaseColumns {
        public static final String TABLE_NAME = "RoutePoint";
        public static final String COLUMN_NAME_NUMBER = "Number";
        public static final String COLUMN_NAME_POINT = "ControlPointID";
        public static final String COLUMN_NAME_ROUTE = "RouteID";
    }

    /**
     * @author Wojciech Michałowski
     * Part of contract that applies to InterestingPlace table.
     */
    public class InterestingPlaceEntry implements BaseColumns {
        public static final String TABLE_NAME = "InterestingPlace";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_DESC = "Description";
        public static final String COLUMN_NAME_LONG = "Longitude";
        public static final String COLUMN_NAME_LAT = "Latitude";
        public static final String COLUMN_NAME_ADDRESS = "Address";
        public static final String COLUMN_NAME_TYPE = "Type";
    }

    /**
     * @author Wojciech Michałowski
     * Part of contract that applies to RestPoint table.
     */
    public class RestPointEntry implements BaseColumns {
        public static final String TABLE_NAME = "RestPoint";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_DESC = "Description";
        public static final String COLUMN_NAME_LONG = "Longitude";
        public static final String COLUMN_NAME_LAT = "Latitude";
        public static final String COLUMN_NAME_TYPE = "Type";
        public static final String COLUMN_NAME_ADDRESS = "Address";
        public static final String COLUMN_NAME_WEB = "Web";
    }

    /**
     * @author Wojciech Michałowski
     * Part of contract that applies to RestPointPhoto table.
     */
    public class RestPointPhotoEntry implements BaseColumns {
        public static final String TABLE_NAME = "RestPointPhoto";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_POINT = "RestPointID";
    }

    /**
     * @author Wojciech Michałowski
     * Part of contract that applies to InterestingPlacePhoto table.
     */
    public class InterestingPlacePhotoEntry implements BaseColumns {
        public static final String TABLE_NAME = "InterestingPlacePhoto";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_POINT = "InterestingPlaceID";
    }

    /**
     * @author Wojciech Michałowski
     * Part of contract that applies to CotrolPointPhoto table.
     */
    public class ControlPointPhotoEntry implements BaseColumns {
        public static final String TABLE_NAME = "ControlPointPhoto";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_POINT = "ControlPointID";
    }
}
