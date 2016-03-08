package jonghoonlim.two_oh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import jonghoonlim.two_oh.dataStructures.Item;

/**
 * Created by jhl2298 on 3/6/2016.
 */
public class CustomAdapter extends ArrayAdapter<Item>
{
    private final Context context;
    private final ArrayList<Item> itemsArrayList;
    private LayoutInflater mInflater;
    private int layout;

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

            if (!currentItem.getCheckInDate().equals("null"))
                txtCheckInDate.setText(currentItem.getCheckInDate());

        }
        // it must be check-in page
        else
        {
            txtCheckOutDate = (TextView) convertView.findViewById(R.id.txt_check_out_date);

            if (!currentItem.getCheckOutDate().equals("null"))
                txtCheckOutDate.setText(currentItem.getCheckOutDate());
        }

        if (!currentItem.getUtTag().equals("null"))
            txtUtTag.setText(currentItem.getUtTag());

        if (!currentItem.getMachineType().equals("null"))
            txtMachineType.setText(currentItem.getMachineType());

        if (!currentItem.getOperatingSystem().equals("null"))
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
            case R.layout.row :
                checkOutBtn = (Button) convertView.findViewById(R.id.row_check_out);
                checkOutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemsArrayList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                break;
            default :
                break;
        }

        return convertView;
    }

}
