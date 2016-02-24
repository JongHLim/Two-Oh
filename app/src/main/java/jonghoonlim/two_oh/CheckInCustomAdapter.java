package jonghoonlim.two_oh;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by jhl2298 on 2/23/2016.
 */
public class CheckInCustomAdapter extends SimpleCursorAdapter {

    private DatabaseHelper mDbHelper;

    public CheckInCustomAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        final Button checkIn = (Button) view.findViewById(R.id.row_check_in);
        final TextView uttag = (TextView) view.findViewById(R.id.text1);
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDbHelper = new DatabaseHelper(v.getContext());
                int uttagNumber = -1;
                try {
                    uttagNumber = Integer.parseInt(uttag.getText().toString());
                } catch (NumberFormatException e) {

                }
                if ( mDbHelper.checkIn(uttagNumber)) {
                    new AlertDialog.Builder(v.getContext()).setTitle("").setMessage("Inventory with UTTAG Number of " +
                            uttagNumber + " has been checked-in successfully.")
                            .setNeutralButton("Close", null).show();
                } else {
                    new AlertDialog.Builder(v.getContext()).setTitle("").setMessage("Check-in unsuccessful!")
                            .setNeutralButton("Close", null).show();
                }

                // disable button and change text to notify user of change
                checkIn.setText("CHECKED-IN");
                checkIn.setEnabled(false);

                mDbHelper.close();

            }
        });
    }
}
