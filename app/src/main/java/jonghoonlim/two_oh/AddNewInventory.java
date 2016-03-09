package jonghoonlim.two_oh;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jonghoonlim.two_oh.OldDataStructures.DatabaseHelper;
import jonghoonlim.two_oh.dataStructures.JSONParser;


/**
 * Created by Jong Hoon Lim on 2/7/2016.
 */
public class AddNewInventory extends Activity implements View.OnClickListener {

    private Button mainMenu;
    private Button submit;
    private Button reset;

    // this is what will be inputted into the database
    private EditText uttagInput;
    private EditText checkInDateInput;
    private EditText machineTypeInput;
    private EditText operatingSystemInput;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser(); // for checking duplicate
    JSONParser jParserInsert = new JSONParser(); // for inserting

    // Progress Dialog
    private ProgressDialog pDialog;

    private static String url_duplicate_uttag = "http://www.jonghoonlim.me/android_connect/duplicate_uttag.php";
    private static String url_insert_inventory = "http://www.jonghoonlim.me/android_connect/create_inventory.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_inven);

        // for now disable landscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // open the check-in page
        mainMenu = (Button) findViewById(R.id.main_menu);
        mainMenu.setOnClickListener(this);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);

        // the fields to extract the data from
        uttagInput = (EditText) findViewById(R.id.uttag_editText);
        checkInDateInput = (EditText) findViewById(R.id.date_editText);

        checkInDateInput.setText(getDate());

        machineTypeInput = (EditText) findViewById(R.id.machineType_editText);
        operatingSystemInput = (EditText) findViewById(R.id.operatingSystem_editText);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu:
                Intent mainMenuIntent = new Intent(getApplication(), Main.class);
                startActivity(mainMenuIntent);
                break;
            case R.id.submit:
                String utTag = uttagInput.getText().toString();

                // get rest of the info
                String checkInDate = checkInDateInput.getText().toString();
                String machineType = machineTypeInput.getText().toString();
                String operatingSystem = operatingSystemInput.getText().toString();

                if(checkInDate.equals("") && machineType.equals("") && operatingSystem.equals("") && utTag.equals("")) {
                    new AlertDialog.Builder(this).setTitle("Failure!").setMessage("Please input the inventory information.")
                            .setNeutralButton("Close", null).show();
                    break;
                }

                // run the duplicate checking background task
                String output = "";
                try {
                    output = new CheckDuplicateUTTAG(utTag).execute().get();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catch (ExecutionException e) {
                    e.printStackTrace();
                }

                // the CheckDuplicate background task found a duplicate
                if (output.equals("Duplicate found."))
                    new AlertDialog.Builder(this).setTitle("Failure!").setMessage("Inventory with " +
                            "UTTAG number " + utTag + " already exists!")
                            .setNeutralButton("Close", null).show();

                // error checking complete. Insert inventory through background task
                else
                    new InsertInventory(utTag, checkInDate, machineType, operatingSystem).execute();

                break;
            case R.id.reset:
                resetFields();
                break;
            default:
                break;
        }
    }

    public void resetFields() {
        uttagInput.setText("");

        checkInDateInput.setText(getDate());

        machineTypeInput.setText("");
        operatingSystemInput.setText("");
    }

    public String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    // background task to insert new inventory
    class InsertInventory extends AsyncTask<String, String, String> {

        private String utTag;
        private String checkInDate;
        private String machineType;
        private String operatingSystem;
        private int added;

        public InsertInventory(String utTag, String checkInDate, String machineType, String operatingSystem) {
            this.utTag = utTag;
            this.checkInDate = checkInDate;
            this.machineType = machineType;
            this.operatingSystem = operatingSystem;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddNewInventory.this);
            pDialog.setMessage("Inserting new inventory. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(FeedReaderContract.FeedEntry.TAG_UT_TAG, this.utTag));
            params.add(new BasicNameValuePair(FeedReaderContract.FeedEntry.TAG_CHECK_IN_DATE, this.checkInDate));
            params.add(new BasicNameValuePair(FeedReaderContract.FeedEntry.TAG_CHECK_OUT_DATE, ""));
            params.add(new BasicNameValuePair(FeedReaderContract.FeedEntry.TAG_MACHINE_TYPE, this.machineType));
            params.add(new BasicNameValuePair(FeedReaderContract.FeedEntry.TAG_OPERATING_SYSTEM, this.operatingSystem));
            params.add(new BasicNameValuePair(FeedReaderContract.FeedEntry.TAG_CHECKED_IN, "Y"));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject jsonInsert = jParserInsert.makeHttpRequest(url_insert_inventory,
                    "POST", params);

            // check log cat for response
            Log.d("Create Response", jsonInsert.toString());

            try {
                added = jsonInsert.getInt("added");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
            if (added == 1) {
                new AlertDialog.Builder(AddNewInventory.this).setTitle("Sucess!").setMessage("Inventory added successfully.")
                        .setNeutralButton("Close", null).show();
            } else {
                new AlertDialog.Builder(AddNewInventory.this).setTitle("Failure!").setMessage("Inventory could not be added.")
                        .setNeutralButton("Close", null).show();
            }
        }

    }

    // background task to check whether inventory already exists in the database
    class CheckDuplicateUTTAG extends AsyncTask<String, String, String> {

        private final String utTag;
        private int success;

        public CheckDuplicateUTTAG(String utTag) {
            this.utTag = utTag;
        }

        protected String doInBackground(String... params) {
            List<NameValuePair> array = new ArrayList<>();
            array.add(new BasicNameValuePair(FeedReaderContract.FeedEntry.TAG_UT_TAG, this.utTag));
            JSONObject json = jParser.makeHttpRequest(
                    url_duplicate_uttag, "GET", array);
            // Check for success tag
            try {
                success = json.getInt(FeedReaderContract.FeedEntry.TAG_SUCCESS);
            }
            catch (JSONException e){
                    e.printStackTrace();
            }
            if (success == 1)
                return "Duplicate found.";
            else
                return "No duplicate found.";
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
