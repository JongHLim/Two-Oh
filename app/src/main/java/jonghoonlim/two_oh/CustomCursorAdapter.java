package jonghoonlim.two_oh;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
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

    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        final Button checkOut = (Button) view.findViewById(R.id.row_check_out);
        final TextView uttag = (TextView) view.findViewById(R.id.text1);
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDbHelper = new DatabaseHelper(v.getContext());
                int uttagNumber = -1;
                try {
                    uttagNumber = Integer.parseInt(uttag.getText().toString());
                } catch (NumberFormatException e) {

                }
                if ( mDbHelper.checkOut(uttagNumber)) {
                    new AlertDialog.Builder(v.getContext()).setTitle("").setMessage("Inventory with UTTAG Number of " +
                            uttagNumber + " has been checked-out successfully.")
                            .setNeutralButton("Close", null).show();
                } else {
                    new AlertDialog.Builder(v.getContext()).setTitle("").setMessage("Check-out unsuccessful!")
                            .setNeutralButton("Close", null).show();
                }

                // disable button and change text to notify user
                checkOut.setText("CHECKED-OUT");
                checkOut.setEnabled(false);

            }
        });
    }
}
