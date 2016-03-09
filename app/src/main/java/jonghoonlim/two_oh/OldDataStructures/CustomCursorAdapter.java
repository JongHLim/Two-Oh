package jonghoonlim.two_oh.OldDataStructures;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import jonghoonlim.two_oh.R;

/**
 * Created by jhl2298 on 2/23/2016.
 */
public class CustomCursorAdapter extends SimpleCursorAdapter {

    private jonghoonlim.two_oh.OldDataStructures.DatabaseHelper mDbHelper;
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
    public void bindView(View view, Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);
        final TextView uttag = (TextView) view.findViewById(R.id.txt_ut_tag);
        final Button checkInButton;
        final Button checkOutButton;
        switch (this.layoutId) {
            case R.layout.check_in_row :
                checkInButton = (Button) view.findViewById(R.id.row_check_in);
                checkInButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int uttagNumber = -1;
                        mDbHelper = new jonghoonlim.two_oh.OldDataStructures.DatabaseHelper(v.getContext());
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

                        // refresh the listview
                        cursor.requery();
                        notifyDataSetChanged();
                    }
                });
                break;
            case R.layout.row :
                checkOutButton = (Button) view.findViewById(R.id.row_check_out);
                checkOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int uttagNumber = -1;
                        mDbHelper = new jonghoonlim.two_oh.OldDataStructures.DatabaseHelper(v.getContext());
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

                        // refresh the listview
                        cursor.requery();
                        notifyDataSetChanged();
                    }
                });
                break;
            default :
                break;
        }
    }
}