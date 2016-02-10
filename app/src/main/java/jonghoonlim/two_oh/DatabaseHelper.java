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

    public static final String DATABASE_NAME = "UTexasInventory.db";
    public static final String INVENTORY_TABLE_NAME = "inventory";
    public static final String INVENTORY_COLUMN_UTTAG = "utTag";
    public static final String INVENTORY_COLUMN_CHECKINDATE = "checkInDate";
    public static final String INVENTORY_COLUMN_MACHINETYPE = "machineType";
    public static final String INVENTORY_COLUMN_OPERATINGSYSTEM = "operatingSystem";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /*
     * Only runs when the database file did not exist and was just created
     * If returned successfully, the database is assumed to be created with the requested
     * version number
     *
     * ... do not catch SQLException here
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // "id integer primary key" auto increments
        db.execSQL(
                "create table inventory " +
                        "(id integer primary key, utTag integer, checkInDate text, " +
                        "machineType text, operatingSystem text)"
        );
    }

    /*
     * only called when the database file exists but the stored version number is lower
     * than requested in constructor
     * Should update the table schema to the requested version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS inventory");
        onCreate(db);
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
            uttags.add(res.getString(res.getColumnIndex(INVENTORY_COLUMN_UTTAG)));
            res.moveToNext();
        }
        return uttags;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, INVENTORY_TABLE_NAME);
        return numRows;
    }
}
