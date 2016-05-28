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
public class DashBoardListArrayAdapter extends ArrayAdapter {

    private final Context context;
    private final String[] values;
    private final String[] notes;

    public DashBoardListArrayAdapter(Context context, String[] values, String[] notes) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.notes = notes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.entry_dashboard, parent, false);

        String[] result = values[position].split(",,");

        TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);
        TextView thirdLine = (TextView) rowView.findViewById(R.id.thirdLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        firstLine.setText(result[0]);
        secondLine.setText(result[1]);
        thirdLine.setText(result[2]);
        return rowView;
    }
}
