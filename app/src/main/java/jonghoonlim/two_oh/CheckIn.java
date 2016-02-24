package jonghoonlim.two_oh;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by Jong Hoon Lim on 2/7/2016.
 */
public class CheckIn extends Activity implements View.OnClickListener {

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
        setContentView(R.layout.check_in);

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
        checkInDateInput.setText("");
        machineTypeInput.setText("");
        operatingSystemInput.setText("");
    }
}
