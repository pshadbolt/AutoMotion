package com.ssj.prototype.prototype.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssj.prototype.prototype.R;

/**
 * Created by shadbolt on 5/14/2016.
 */
public class GarageListArrayAdapter extends ArrayAdapter {

    private final Context context;
    private final String[] values;
    private final String[] notes;

    public GarageListArrayAdapter(Context context, String[] values, String[] notes) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.notes = notes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.garage_entry, parent, false);

        String[] result = values[position].split(",");

        TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        firstLine.setText(result[0] + " " + result[1] + " " + result[2]);
        secondLine.setText(result[3]);

        return rowView;
    }
}
