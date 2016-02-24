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
public class CustomCursorAdapter extends SimpleCursorAdapter {

    private DatabaseHelper mDbHelper;
    private int layoutId;

    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.layoutId = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        final TextView uttag = (TextView) view.findViewById(R.id.text1);
        if (this.layoutId == R.layout.check_in_row) {
            final Button checkInButton = (Button) view.findViewById(R.id.row_check_in);
            checkInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int uttagNumber = -1;
                    // disable button and change text to notify user of change
                    checkInButton.setText("CHECKED-IN");
                    checkInButton.setEnabled(false);
                    mDbHelper = new DatabaseHelper(v.getContext());
                    try {
                        uttagNumber = Integer.parseInt(uttag.getText().toString());
                    } catch (NumberFormatException e) {

                    }
                    if (mDbHelper.checkIn(uttagNumber)) {
                        new AlertDialog.Builder(v.getContext()).setTitle("").setMessage("Inventory with UTTAG Number of " +
                                uttagNumber + " has been checked-in successfully.")
                                .setNeutralButton("Close", null).show();
                    } else {
                        new AlertDialog.Builder(v.getContext()).setTitle("").setMessage("Check-in unsuccessful!")
                                .setNeutralButton("Close", null).show();
                    }
                    mDbHelper.close();
                }
            });
        } else {
            final Button checkOutButton = (Button) view.findViewById(R.id.row_check_out);
            checkOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int uttagNumber = -1;
                    // disable button and change text to notify user of change
                    checkOutButton.setText("CHECKED-OUT");
                    checkOutButton.setEnabled(false);
                    mDbHelper = new DatabaseHelper(v.getContext());
                    try {
                        uttagNumber = Integer.parseInt(uttag.getText().toString());
                    } catch (NumberFormatException e) {

                    }
                    if (mDbHelper.checkOut(uttagNumber)) {
                        new AlertDialog.Builder(v.getContext()).setTitle("").setMessage("Inventory with UTTAG Number of " +
                                uttagNumber + " has been checked-out successfully.")
                                .setNeutralButton("Close", null).show();
                    } else {
                        new AlertDialog.Builder(v.getContext()).setTitle("").setMessage("Check-out unsuccessful!")
                                .setNeutralButton("Close", null).show();
                    }
                    mDbHelper.close();
                }
            });

        }
    }
}
