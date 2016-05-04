package com.ssj.prototype.prototype;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ssj.prototype.prototype.db.VehicleDataSource;

public class MainActivity extends AppCompatActivity {

    private VehicleDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Create the database connection
        datasource = new VehicleDataSource(this);
        datasource.open();

        datasource.insertValue(2011,"CHEVROLET","CRUZE","ECO");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(datasource.getAllEntries());

        Spinner year = (Spinner)findViewById(R.id.spinner1);
        String[] years = new String[]{"2016", "2015", "2013"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, years);
        year.setAdapter(adapter);

        Spinner make = (Spinner)findViewById(R.id.spinner2);
        String[] makes = new String[]{"CHEVROLET", "FORD", "TESLA"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, makes);
        make.setAdapter(adapter);

        Spinner model = (Spinner)findViewById(R.id.spinner3);
        String[] models = new String[]{"CRUZE", "FOCUS", "MODEL 3"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, models);
        model.setAdapter(adapter);


    }


}
