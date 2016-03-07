package jonghoonlim.two_oh.OldSQLite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import jonghoonlim.two_oh.Main;
import jonghoonlim.two_oh.R;
import jonghoonlim.two_oh.dataStructures.DatabaseHelper;


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

    // declare as String and int so that we can parseInt
    private String uttagString;
    private int uttag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_inven);

        // for now disable landscape orientation
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu :
                Intent mainMenuIntent = new Intent(getApplication(), Main.class);
                startActivity(mainMenuIntent);
                mDbHelper.close();
                break;
            case R.id.submit :
                uttagString = uttagInput.getText().toString();
                try {
                    uttag = Integer.parseInt(uttagString);
                } catch (NumberFormatException e){
                    // not an Integer
                }
                if (mDbHelper.duplicateInventory(uttag)) {
                    new AlertDialog.Builder(this).setTitle("Failure!").setMessage("Inventory with " +
                            "UTTAG number " + uttag + " already exists!")
                            .setNeutralButton("Close", null).show();
                    break;
                }
                if (mDbHelper.insertInventory(uttag, checkInDateInput.getText().toString(),
                        machineTypeInput.getText().toString(), operatingSystemInput.getText().toString()) != -1) {
                    new AlertDialog.Builder(this).setTitle("Sucess!").setMessage("Inventory added successfully.")
                            .setNeutralButton("Close", null).show();
                } else {
                    new AlertDialog.Builder(this).setTitle("Failure!").setMessage("Inventory could not be added.")
                            .setNeutralButton("Close", null).show();
                }
                break;
            case R.id.reset :
                resetFields();
                break;
            default :
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
}
