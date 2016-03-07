package jonghoonlim.two_oh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jonghoonlim.two_oh.dataStructures.CustomCursorAdapter;
import jonghoonlim.two_oh.dataStructures.DatabaseHelper;
import jonghoonlim.two_oh.dataStructures.FeedReaderContract;
import jonghoonlim.two_oh.dataStructures.JSONParser;


/**
 * Created by Jong Hoon Lim on 2/7/2016.
 */
public class CheckOut extends Activity implements View.OnClickListener {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, ArrayList<String>>> inventoryList;

    // url to get all products list
    private static String url_read_all_inventory = "http://192.168.1.6:80/android_connect/read_all_inventory.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String[] TAGS = new String[]{"ut_tag", "check_in_date",
            "check_out_date", "machine_type", "operating_system", "checked_in"} ;
    private static final String TAGS_STRING = "tags";
    private static final String TAG_INVENTORY = "inventory";
    private static final String TAG_ID = "id";

    // inventory JSONArray
    JSONArray inventory = null;

    private Button mainMenu;

    private EditText searchEditText;

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out);

        // for now disable landscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        inventoryList = new ArrayList<>();

        // Loading all inventory in Background Thread
        new LoadAllInventory().execute();

        // open the main menu
        mainMenu = (Button) findViewById(R.id.main_menu);
        mainMenu.setOnClickListener(this);

        lv = (ListView) findViewById(R.id.listView);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu :
                Intent mainMenuIntent = new Intent(getApplication(), Main.class);
                startActivity(mainMenuIntent);
                break;
            default :
                break;
        }
    }

    /**
     * Background Async Task to Load all inventory by making HTTP Request
     * */
    class LoadAllInventory extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CheckOut.this);
            pDialog.setMessage("Loading inventory currently checked-in. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_read_all_inventory, "GET", params);
            // Check your log cat for JSON reponse
            Log.d("All Inventory: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                // get all inventory and store in HashMap
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    inventory = json.getJSONArray(TAG_INVENTORY);

                    // looping through All Products
                    for (int i = 0; i < inventory.length(); i++) {
                        JSONObject item = inventory.getJSONObject(i);
                        // Storing each json item in variable
                        String id = item.getString(TAG_ID);
                        System.out.println("DEBUG *****: " + item.getString(TAG_ID));
                        ArrayList<String> ids = new ArrayList<>();
                        ids.add(id);

                        // get all other info... uttag, checked_in_date, etc.
                        ArrayList<String> allInfoList = new ArrayList<>();
                        for (String tag : TAGS) {
                            System.out.println("DEBUG *****: " + item.getString(tag));
                            allInfoList.add(item.getString(tag));
                        }

                        // creating new HashMap
                        HashMap<String, ArrayList<String>> map = new HashMap<>();
                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, ids);
                        map.put(TAGS_STRING, allInfoList);

                        inventoryList.add(map);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            CheckOut.this, inventoryList,
                            R.layout.row, new String[]{TAG_ID,
                            TAGS[0], TAGS[1], TAGS[3], TAGS[4]},
                            new int[]{R.id.id, R.id.text1, R.id.text2, R.id.text3, R.id.text4}); // populate the listview
                    // updating listview
                    lv.setAdapter(adapter);
                }
            });

        }
    }
}
