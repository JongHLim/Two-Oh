package jonghoonlim.two_oh;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
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
        Button checkOut = (Button) view.findViewById(R.id.row_check_out);
        final TextView uttag = (TextView) view.findViewById(R.id.text1);
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext()).setTitle("YES!").setMessage("Check-out not ready... UTTAG Number: " +
                        uttag.getText().toString()).setNeutralButton("Close", null).show();
            }
        });
    }
}
