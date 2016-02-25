package jonghoonlim.two_oh;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by jhl2298 on 2/24/2016.
 */
public class CheckIn extends Activity implements View.OnClickListener {

    private Button mainMenu;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;
    private final String[] projection = { FeedReaderContract.FeedEntry._ID,
            FeedReaderContract.FeedEntry.INVENTORY_COLUMN_UTTAG,
            FeedReaderContract.FeedEntry.INVENTORY_COLUMN_CHECKOUTDATE,
            FeedReaderContract.FeedEntry.INVENTORY_COLUMN_MACHINETYPE,
            FeedReaderContract.FeedEntry.INVENTORY_COLUMN_OPERATINGSYSTEM};
    private ListView list;
    private CustomCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_in);

        // for now disable landscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mainMenu = (Button) findViewById(R.id.main_menu_check_in);
        mainMenu.setOnClickListener(this);

        // readable database
        mDbHelper = new DatabaseHelper(this);
        db = mDbHelper.getReadableDatabase();

        String sortOrder = FeedReaderContract.FeedEntry.INVENTORY_COLUMN_UTTAG + " ASC";

        Cursor c = db.query(FeedReaderContract.FeedEntry.INVENTORY_TABLE_NAME, projection,
                "checkedIn=?", new String[]{"N"}, null, null, sortOrder);

        list = (ListView) findViewById(R.id.listView_check_in);
        String[] from = new String[]{FeedReaderContract.FeedEntry.INVENTORY_COLUMN_UTTAG,
                FeedReaderContract.FeedEntry.INVENTORY_COLUMN_CHECKOUTDATE,
                FeedReaderContract.FeedEntry.INVENTORY_COLUMN_MACHINETYPE,
                FeedReaderContract.FeedEntry.INVENTORY_COLUMN_OPERATINGSYSTEM};
        int[] to = new int[]{R.id.text1, R.id.text2, R.id.text3, R.id.text4};

        cursorAdapter = new CustomCursorAdapter(this, R.layout.check_in_row, c, from, to);
        list.setAdapter(cursorAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu_check_in :
                Intent mainMenuIntent = new Intent(getApplication(), Main.class);
                startActivity(mainMenuIntent);
                db.close();
                mDbHelper.close();
                break;
            default :
                break;
        }
    }
}