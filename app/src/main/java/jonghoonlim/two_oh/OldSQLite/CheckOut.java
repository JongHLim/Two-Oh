package jonghoonlim.two_oh.OldSQLite;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import jonghoonlim.two_oh.Main;
import jonghoonlim.two_oh.R;
import jonghoonlim.two_oh.OldDataStructures.DatabaseHelper;
import jonghoonlim.two_oh.OldDataStructures.CustomCursorAdapter;
import jonghoonlim.two_oh.OldDataStructures.FeedReaderContract;


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
            FeedReaderContract.FeedEntry.INVENTORY_COLUMN_CHECKOUTDATE,
            FeedReaderContract.FeedEntry.INVENTORY_COLUMN_MACHINETYPE,
            FeedReaderContract.FeedEntry.INVENTORY_COLUMN_OPERATINGSYSTEM};
    private ListView list;
    private CustomCursorAdapter cursorAdapter;

    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out);

        // for now disable landscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // open the main menu
        mainMenu = (Button) findViewById(R.id.main_menu);
        mainMenu.setOnClickListener(this);

        // readable database
        mDbHelper = new DatabaseHelper(this);
        db = mDbHelper.getReadableDatabase();

        String sortOrder = FeedReaderContract.FeedEntry.INVENTORY_COLUMN_UTTAG + " ASC";

        final Cursor c = db.query(FeedReaderContract.FeedEntry.INVENTORY_TABLE_NAME, projection,
                "checkedIn=?", new String[]{"Y"}, null, null, sortOrder);

        list = (ListView) findViewById(R.id.listView);
        String[] from = new String[]{FeedReaderContract.FeedEntry.INVENTORY_COLUMN_UTTAG,
                FeedReaderContract.FeedEntry.INVENTORY_COLUMN_CHECKINDATE,
                FeedReaderContract.FeedEntry.INVENTORY_COLUMN_MACHINETYPE,
                FeedReaderContract.FeedEntry.INVENTORY_COLUMN_OPERATINGSYSTEM};
        int[] to = new int[]{R.id.txt_ut_tag, R.id.txt_check_in_date, R.id.txt_machine_type, R.id.txt_operating_system};

        cursorAdapter = new CustomCursorAdapter(this, R.layout.row, c, from, to);
        list.setAdapter(cursorAdapter);

        searchEditText = (EditText) findViewById(R.id.check_out_search);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Cursor cursor;
                if (searchEditText.getText().toString().equals("")) {
                    cursorAdapter.swapCursor(c);
                    cursorAdapter.notifyDataSetChanged();
                } else {
                    int search;
                    try {
                        search = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        return;
                    }
                    cursor = mDbHelper.selectDataWithConstrain(search, true);
                    cursorAdapter.swapCursor(cursor);
                    cursorAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu :
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
