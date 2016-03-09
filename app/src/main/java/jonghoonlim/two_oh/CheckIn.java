package jonghoonlim.two_oh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jonghoonlim.two_oh.dataStructures.CustomAdapter;
import jonghoonlim.two_oh.dataStructures.CustomComparator;
import jonghoonlim.two_oh.dataStructures.Item;
import jonghoonlim.two_oh.dataStructures.JSONParser;

/**
 * Created by jhl2298 on 2/24/2016.
 */
public class CheckIn extends Activity implements View.OnClickListener {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<Item> inventoryList;

    // url to get all products list
    private static String url_read_all_inventory = "http://www.jonghoonlim.me/android_connect/read_all_inventory.php";

    // inventory JSONArray
    JSONArray inventory = null;

    private Button mainMenu;

    private EditText searchEditText;

    private ListView lv;

    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_in);

        // for now disable landscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        inventoryList = new ArrayList<>();

        // Loading all inventory in Background Thread
        new LoadAllInventory().execute();

        // open the main menu
        mainMenu = (Button) findViewById(R.id.main_menu_check_in);
        mainMenu.setOnClickListener(this);

        lv = (ListView) findViewById(R.id.listView_check_in);

        searchEditText = (EditText) findViewById(R.id.check_in_search);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence number, int start, int before, int count) {
                int searchLength = number.length();
                ArrayList<Item> temp = new ArrayList<>();
                for (Item item : inventoryList) {
                    String utTag = item.getUtTag();
                    if (searchLength <= utTag.length() && utTag.contains(number))
                        temp.add(item);
                }
                Collections.sort(temp, new CustomComparator());
                CustomAdapter filteredAdapter = new CustomAdapter(CheckIn.this, R.layout.check_in_row, temp);
                lv.setAdapter(filteredAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu_check_in :
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
            pDialog = new ProgressDialog(CheckIn.this);
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
                int success = json.getInt(FeedReaderContract.FeedEntry.TAG_SUCCESS);

                // get all inventory and store in HashMap
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    inventory = json.getJSONArray(FeedReaderContract.FeedEntry.TAG_INVENTORY);
                    Item current;
                    // looping through All Products
                    for (int i = 0; i < inventory.length(); i++) {
                        JSONObject item = inventory.getJSONObject(i);
                        current = new Item();
                        current.setId(item.getString(FeedReaderContract.FeedEntry.TAG_ID));
                        current.setUtTag(item.getString(FeedReaderContract.FeedEntry.TAG_UT_TAG));
                        current.setCheckInDate(item.getString(FeedReaderContract.FeedEntry.TAG_CHECK_IN_DATE));
                        current.setCheckOutDate(item.getString(FeedReaderContract.FeedEntry.TAG_CHECK_OUT_DATE));
                        current.setMachineType(item.getString(FeedReaderContract.FeedEntry.TAG_MACHINE_TYPE));
                        current.setOperatingSystem(item.getString(FeedReaderContract.FeedEntry.TAG_OPERATING_SYSTEM));

                        // determine whether to add to list of available check-outs
                        String checkedIn = item.getString(FeedReaderContract.FeedEntry.TAG_CHECKED_IN);
                        current.setCheckedIn(checkedIn);

                        if (checkedIn.equals("N"))
                            inventoryList.add(current);
                    }
                    System.out.println("DEBUG ***** " + inventoryList);
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

                    // First sort the array by UT TAG number
                    Collections.sort(inventoryList, new CustomComparator());

                    adapter = new CustomAdapter(
                            CheckIn.this, R.layout.check_in_row, inventoryList); // populate the listview

                    // updating listview
                    lv.setAdapter(adapter);
                }
            });

        }
    }
}
