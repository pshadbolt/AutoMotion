package com.ssj.prototype.prototype;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ssj.prototype.prototype.db.VehicleDataSource;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private VehicleDataSource datasource;

    ArrayAdapter<String> adapter;

    Spinner year;
    Spinner make;
    Spinner model;
    Spinner trim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Create the database connection
        datasource = new VehicleDataSource(this);
        datasource.open();

        populateDatabase();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextView textView = (TextView) findViewById(R.id.textView);
        //textView.setText();

        year = (Spinner) findViewById(R.id.spinner1);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, datasource.getAllYears());
        year.setAdapter(adapter);

        make = (Spinner) findViewById(R.id.spinner2);
        model = (Spinner) findViewById(R.id.spinner3);
        trim = (Spinner) findViewById(R.id.spinner4);

        // Add listeners
        year.setOnItemSelectedListener(this);
        make.setOnItemSelectedListener(this);
        model.setOnItemSelectedListener(this);
        trim.setOnItemSelectedListener(this);
    }

    // Spinner Selector
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (parent.getId() == R.id.spinner1) {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, datasource.getMakes((String) year.getSelectedItem()));
            make.setAdapter(adapter);
        }

        if (parent.getId() == R.id.spinner2) {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, datasource.getModels((String) year.getSelectedItem(), (String) make.getSelectedItem()));
            model.setAdapter(adapter);
        }

        if (parent.getId() == R.id.spinner3) {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, datasource.getTrims((String) year.getSelectedItem(), (String) make.getSelectedItem(), (String) model.getSelectedItem()));
            trim.setAdapter(adapter);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void populateDatabase() {
        //datasource.recreate();
        datasource.insertValue("2016", "TESLA", "MODEL 3", "");
        datasource.getAllEntries();
    }


}
