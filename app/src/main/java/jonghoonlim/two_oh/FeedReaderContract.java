package jonghoonlim.two_oh;

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
        // JSON Node names
        public static final String TAG_SUCCESS = "success";
        public static final String TAG_ID = "id";
        public static final String TAG_UT_TAG = "ut_tag";
        public static final String TAG_CHECK_IN_DATE = "check_in_date";
        public static final String TAG_CHECK_OUT_DATE = "check_out_date";
        public static final String TAG_MACHINE_TYPE = "machine_type";
        public static final String TAG_OPERATING_SYSTEM = "operating_system";
        public static final String TAG_CHECKED_IN = "checked_in";
        public static final String TAG_INVENTORY = "inventory";
    }

}
