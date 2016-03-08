package jonghoonlim.two_oh;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jonghoonlim.two_oh.dataStructures.Item;
import jonghoonlim.two_oh.dataStructures.JSONParser;

/**
 * Created by jhl2298 on 3/6/2016.
 */
public class CustomAdapter extends ArrayAdapter<Item>
{
    private final Context context;
    private final ArrayList<Item> itemsArrayList;
    private LayoutInflater mInflater;
    private int layout;

    private static final String TAG_ID = "id";
    private static final String TAG_UT_TAG = "ut_tag";
    private static final String TAG_CHECK_IN_DATE = "check_in_date";
    private static final String TAG_CHECK_OUT_DATE = "check_out_date";
    private static final String TAG_MACHINE_TYPE = "machine_type";
    private static final String TAG_OPERATING_SYSTEM = "operating_system";
    private static final String TAG_CHECKED_IN = "checked_in";
    private static final String TAG_SUCCESS = "success";

    private static final String NULL = "null";
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // url to update inventory
    private static final String url_update_inventory = "http://www.jonghoonlim.me/android_connect/update_inventory.php";

    public CustomAdapter(Context context, int layout, ArrayList<Item> itemsArrayList) {
        super(context, layout, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
        mInflater = LayoutInflater.from(context);
        this.layout = layout;
    }

    public int getCount() {
        return itemsArrayList.size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(layout, null);
        TextView txtUtTag = (TextView) convertView.findViewById(R.id.txt_ut_tag);
        TextView txtMachineType = (TextView) convertView.findViewById(R.id.txt_machine_type);
        TextView txtOperatingSystem = (TextView) convertView.findViewById(R.id.txt_operating_system);
        TextView txtCheckInDate;
        TextView txtCheckOutDate;

        Item currentItem = itemsArrayList.get(position);
        // if it's check-out page
        if (layout == R.layout.row)
        {
            txtCheckInDate = (TextView) convertView.findViewById(R.id.txt_check_in_date);

            if (!currentItem.getCheckInDate().equals(NULL))
                txtCheckInDate.setText(currentItem.getCheckInDate());

        }
        // it must be check-in page
        else
        {
            txtCheckOutDate = (TextView) convertView.findViewById(R.id.txt_check_out_date);

            if (!currentItem.getCheckOutDate().equals(NULL))
                txtCheckOutDate.setText(currentItem.getCheckOutDate());
        }

        if (!currentItem.getUtTag().equals(NULL))
            txtUtTag.setText(currentItem.getUtTag());

        if (!currentItem.getMachineType().equals(NULL))
            txtMachineType.setText(currentItem.getMachineType());

        if (!currentItem.getOperatingSystem().equals(NULL))
            txtOperatingSystem.setText(currentItem.getOperatingSystem());

        final Button checkOutBtn;
        final Button checkInBtn;
        switch (this.layout) {
            case R.layout.check_in_row :
                checkInBtn = (Button) convertView.findViewById(R.id.row_check_in);
                checkInBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemsArrayList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                break;
            // Check-out button
            case R.layout.row :
                checkOutBtn = (Button) convertView.findViewById(R.id.row_check_out);
                checkOutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Item checkedOut = itemsArrayList.remove(position);
                        notifyDataSetChanged();
                        new CheckOutInventory(checkedOut).execute();
                    }
                });
                break;
            default :
                break;
        }

        return convertView;
    }

    /**
     * Background Async Task to check out inventory by making HTTP request
     * */
    class CheckOutInventory extends AsyncTask<String, String, String> {

        private Item item;
        private int success;

        public CheckOutInventory(Item item) {
            this.item = item;
        }

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * updating inventory using url php file
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();

            // id will never be null
            params.add(new BasicNameValuePair(TAG_ID, item.getId()));

            params.add(new BasicNameValuePair(TAG_UT_TAG, item.getUtTag()));

            params.add(new BasicNameValuePair(TAG_CHECK_IN_DATE, item.getCheckInDate()));

            // check out date will never be null in check out
            params.add(new BasicNameValuePair(TAG_CHECK_OUT_DATE, getDate()));

            params.add(new BasicNameValuePair(TAG_MACHINE_TYPE, item.getMachineType()));

            params.add(new BasicNameValuePair(TAG_OPERATING_SYSTEM, item.getOperatingSystem()));

            params.add(new BasicNameValuePair(TAG_CHECKED_IN, "N"));

            JSONObject json = jsonParser.makeHttpRequest(url_update_inventory,
                    "POST", params);

            // check json success tag
            try {
                success = json.getInt(TAG_SUCCESS);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute() {
            Toast toast;
            if (success == 1) {
                toast = Toast.makeText(context, "Inventory with UTTAG Number of " +
                        item.getUtTag() + " has been checked-out successfully.", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                toast = Toast.makeText(context, "Check-out unsuccessful!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

}
