package jonghoonlim.two_oh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    public CustomAdapter(Context context, ArrayList<Item> itemsArrayList) {
        super(context, R.layout.row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return itemsArrayList.size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.row, null);
        TextView txtUtTag = (TextView) convertView.findViewById(R.id.txt_ut_tag);
        TextView txtCheckInDate = (TextView) convertView.findViewById(R.id.txt_check_in_date);
        TextView txtMachineType = (TextView) convertView.findViewById(R.id.txt_machine_type);
        TextView txtOperatingSystem = (TextView) convertView.findViewById(R.id.txt_operating_system);

        Item currentItem = itemsArrayList.get(position);

        if (!currentItem.getUtTag().equals("null"))
            txtUtTag.setText(currentItem.getUtTag());

        if (!currentItem.getCheckInDate().equals("null"))
            txtCheckInDate.setText(currentItem.getCheckInDate());

        if (!currentItem.getMachineType().equals("null"))
            txtMachineType.setText(currentItem.getMachineType());

        if (!currentItem.getOperatingSystem().equals("null"))
            txtOperatingSystem.setText(currentItem.getOperatingSystem());

        return convertView;
    }

}
