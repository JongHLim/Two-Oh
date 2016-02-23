package jonghoonlim.two_oh;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

/**
 * Created by Jong Hoon Lim on 2/7/2016.
 */
public class CheckOut extends Activity implements View.OnClickListener {

    private Button mainMenu;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;
    private final String[] projection = { FeedReaderContract.FeedEntry._ID,
            FeedReaderContract.FeedEntry.INVENTORY_COLUMN_UTTAG,
            FeedReaderContract.FeedEntry.INVENTORY_COLUMN_CHECKINDATE,
            FeedReaderContract.FeedEntry.INVENTORY_COLUMN_MACHINETYPE,
            FeedReaderContract.FeedEntry.INVENTORY_COLUMN_OPERATINGSYSTEM};
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out);

        // open the check-in page
        mainMenu = (Button) findViewById(R.id.main_menu);
        mainMenu.setOnClickListener(this);

        // readable database
        mDbHelper = new DatabaseHelper(this);
        db = mDbHelper.getReadableDatabase();

        String sortOrder = FeedReaderContract.FeedEntry.INVENTORY_COLUMN_UTTAG + " ASC";

        Cursor c = db.query(FeedReaderContract.FeedEntry.INVENTORY_TABLE_NAME, projection,
                null, null, null, null, sortOrder);

        list = (ListView) findViewById(R.id.listView);
        String[] from = new String[]{FeedReaderContract.FeedEntry.INVENTORY_COLUMN_UTTAG,
                FeedReaderContract.FeedEntry.INVENTORY_COLUMN_CHECKINDATE,
                FeedReaderContract.FeedEntry.INVENTORY_COLUMN_MACHINETYPE,
                FeedReaderContract.FeedEntry.INVENTORY_COLUMN_OPERATINGSYSTEM};
        int[] to = new int[]{R.id.text1, R.id.text2, R.id.text3, R.id.text4};

        displayInventory(c, from, to);

        mDbHelper.close();
    }

    public void displayInventory(Cursor c, String[] from, int[] to) {
        SimpleCursorAdapter cursorAdapter =
                new SimpleCursorAdapter(this, R.layout.row, c, from, to);
        list.setAdapter(cursorAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu :
                Intent mainMenuIntent = new Intent(getApplication(), Main.class);
                startActivity(mainMenuIntent);
                mDbHelper.close();
                break;
            default :
                break;
        }
    }

}
