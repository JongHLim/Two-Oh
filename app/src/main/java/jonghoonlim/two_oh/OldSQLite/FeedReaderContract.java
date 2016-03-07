package jonghoonlim.two_oh.OldSQLite;

import android.provider.BaseColumns;

/**
 * Created by jhl2298 on 2/22/2016.
 */
public final class FeedReaderContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String INVENTORY_TABLE_NAME = "inventory";
        public static final String INVENTORY_COLUMN_UTTAG = "utTag";
        public static final String INVENTORY_COLUMN_CHECKINDATE = "checkInDate";
        public static final String INVENTORY_COLUMN_CHECKOUTDATE ="checkOutDate";
        public static final String INVENTORY_COLUMN_MACHINETYPE = "machineType";
        public static final String INVENTORY_COLUMN_OPERATINGSYSTEM = "operatingSystem";
        public static final String INVENTORY_COLUMN_CHECKEDIN = "checkedIn";
    }

}
