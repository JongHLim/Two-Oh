package jonghoonlim.two_oh;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Main extends Activity implements View.OnClickListener {

    private Button addNew;
    private Button checkOut;
    private Button checkIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // for now, disable landscape orientation
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // open the add new page
        addNew = (Button) findViewById(R.id.add_new_main);
        addNew.setOnClickListener(this);

        // open the check-in page
        checkIn = (Button) this.findViewById(R.id.check_in_main);
        checkIn.setOnClickListener(this);

        // open the check-out page
        checkOut = (Button) this.findViewById(R.id.checkOut);
        checkOut.setOnClickListener(this);

    }

     /*
     * on click method to handle all button clicks within this layout
     */
    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            // if the user clicks the "Check-in inventory" button
            case R.id.add_new_main :
                Intent addNewIntent = new Intent(getApplication(), AddNewInventory.class);
                startActivity(addNewIntent);
                break;
            // if the user clicks the "Check-out inventory" button
            case R.id.checkOut :
                Intent checkOutIntent = new Intent(getApplication(), CheckOut.class);
                startActivity(checkOutIntent);
                break;
            case R.id.check_in_main :
                Intent checkInIntent = new Intent(getApplication(), CheckIn.class);
                startActivity(checkInIntent);
                break;
            default :
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
