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

import jonghoonlim.two_oh.OldDataStructures.DatabaseHelper;
import jonghoonlim.two_oh.dataStructures.JSONParser;


/**
 * Created by Jong Hoon Lim on 2/7/2016.
 */
public class AddNewInventory extends Activity implements View.OnClickListener {

    private Button mainMenu;
    private Button submit;
    private Button reset;
    private DatabaseHelper mDbHelper;

    // this is what will be inputted into the database
    private EditText uttagInput;
    private EditText checkInDateInput;
    private EditText machineTypeInput;
    private EditText operatingSystemInput;

    private HashSet<String> duplicates = new HashSet<>();

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    private static String url_duplicate_uttag = "http://www.jonghoonlim.me/android_connect/duplicate_uttag.php";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

        // interact with the database
        mDbHelper = new DatabaseHelper(this);

        // the fields to extract the data from
        uttagInput = (EditText) findViewById(R.id.uttag_editText);
        checkInDateInput = (EditText) findViewById(R.id.date_editText);

        checkInDateInput.setText(getDate());

        machineTypeInput = (EditText) findViewById(R.id.machineType_editText);
        operatingSystemInput = (EditText) findViewById(R.id.operatingSystem_editText);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu:
                Intent mainMenuIntent = new Intent(getApplication(), Main.class);
                startActivity(mainMenuIntent);
                mDbHelper.close();
                break;
            case R.id.submit:
                String utTag = uttagInput.getText().toString();
                new CheckDuplicateUTTAG(utTag).execute();
                if (duplicates.contains(utTag)) {
                    new AlertDialog.Builder(this).setTitle("Failure!").setMessage("Inventory with " +
                            "UTTAG number " + utTag + " already exists!")
                            .setNeutralButton("Close", null).show();
                    break;
                }
                
//                if (true) {
//                    new AlertDialog.Builder(this).setTitle("Sucess!").setMessage("Inventory added successfully.")
//                            .setNeutralButton("Close", null).show();
//                } else {
//                    new AlertDialog.Builder(this).setTitle("Failure!").setMessage("Inventory could not be added.")
//                            .setNeutralButton("Close", null).show();
//                }
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

    class CheckDuplicateUTTAG extends AsyncTask<String, String, String> {

        private final String utTag;

        public CheckDuplicateUTTAG(String utTag) {
            this.utTag = utTag;
        }

        protected String doInBackground(String... params) {
            // Check for success tag
            int success;
            List<NameValuePair> array = new ArrayList<>();
            array.add(new BasicNameValuePair(FeedReaderContract.FeedEntry.TAG_UT_TAG, this.utTag));
            JSONObject json = jParser.makeHttpRequest(
                    url_duplicate_uttag, "GET", array);

            try {
                success = json.getInt(FeedReaderContract.FeedEntry.TAG_SUCCESS);
                if (success == 1) {
                    duplicates.add(this.utTag);
                }
            }
            catch (JSONException e){
                    e.printStackTrace();
            }
            return null;
        }
    }
}
