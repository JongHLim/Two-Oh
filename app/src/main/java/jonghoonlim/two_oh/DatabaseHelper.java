package jonghoonlim.two_oh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by jhl2298 on 2/10/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UTexasInventory.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.INVENTORY_TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.INVENTORY_COLUMN_UTTAG + " integer" + COMMA +
                    FeedReaderContract.FeedEntry.INVENTORY_COLUMN_CHECKINDATE + TEXT_TYPE + COMMA +
                    FeedReaderContract.FeedEntry.INVENTORY_COLUMN_MACHINETYPE + TEXT_TYPE + COMMA +
                    FeedReaderContract.FeedEntry.INVENTORY_COLUMN_OPERATINGSYSTEM + TEXT_TYPE +
            ")";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.INVENTORY_TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /*
     * Insert a new inventory onto the SQLite database file / device
     */
    public boolean insertInventory(Integer utTag, String checkInDate,
                                   String machineType, String operatingSystem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("utTag", utTag);
        contentValues.put("checkInDate", checkInDate);
        contentValues.put("machineType", machineType);
        contentValues.put("operatingSystem", operatingSystem);
        db.insert("inventory", null, contentValues);
        return true;
    }

    /*
     * Get data with specific id
     */
    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from inventory where id="+id+"", null );
        return res;
    }

    /*
     * Update data at specific id
     */
    public boolean updateInventory(Integer id, Integer utTag, String checkInDate,
                                   String machineType, String operatingSystem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("utTag", utTag);
        contentValues.put("checkInDate", checkInDate);
        contentValues.put("machineType", machineType);
        contentValues.put("operatingSystem", operatingSystem);
        db.update("inventory", contentValues, "id = ? ",
                new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteInventory (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("inventory",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllInventory()
    {
        ArrayList<String> uttags = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from inventory", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            uttags.add(res.getString(res.getColumnIndex(FeedReaderContract.FeedEntry.INVENTORY_COLUMN_UTTAG)));
            res.moveToNext();
        }
        return uttags;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, FeedReaderContract.FeedEntry.INVENTORY_TABLE_NAME);
        return numRows;
    }
}
