package com.ssj.prototype.prototype.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ssj.prototype.prototype.R;
import com.ssj.prototype.prototype.database.GarageDataSource;
import com.ssj.prototype.prototype.database.VehicleDataSource;
import com.ssj.prototype.prototype.model.Vehicle;

public class AddVehicleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private VehicleDataSource vehicleDatasource;
    private GarageDataSource garageDatasource;

    ArrayAdapter<String> adapter;

    Spinner year;
    Spinner make;
    Spinner model;
    Spinner trim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvehicle);

        // Create the database connections
        vehicleDatasource = new VehicleDataSource(this);
        vehicleDatasource.open();
        garageDatasource = new GarageDataSource(this);
        garageDatasource.open();

        //populateDatabase();

        year = (Spinner) findViewById(R.id.spinner1);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vehicleDatasource.getAllYears());
        year.setAdapter(adapter);

        make = (Spinner) findViewById(R.id.spinner2);
        model = (Spinner) findViewById(R.id.spinner3);
        trim = (Spinner) findViewById(R.id.spinner4);

        // Add listeners
        year.setOnItemSelectedListener(this);
        make.setOnItemSelectedListener(this);
        model.setOnItemSelectedListener(this);
        trim.setOnItemSelectedListener(this);

        findViewById(R.id.cancel).setVisibility(View.VISIBLE);
        findViewById(R.id.confirm).setVisibility(View.VISIBLE);
    }

    // Spinner Selector
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (parent.getId() == R.id.spinner1) {
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vehicleDatasource.getMakes((String) year.getSelectedItem()));
            make.setAdapter(adapter);
            findViewById(R.id.spinner1).setVisibility(View.VISIBLE);
            findViewById(R.id.spinner2).setVisibility(View.VISIBLE);
            findViewById(R.id.spinner3).setVisibility(View.GONE);
            findViewById(R.id.spinner4).setVisibility(View.GONE);
        }
        if (parent.getId() == R.id.spinner2) {
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vehicleDatasource.getModels((String) year.getSelectedItem(), (String) make.getSelectedItem()));
            model.setAdapter(adapter);
            findViewById(R.id.spinner3).setVisibility(View.VISIBLE);
        }
        if (parent.getId() == R.id.spinner3) {
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vehicleDatasource.getTrims((String) year.getSelectedItem(), (String) make.getSelectedItem(), (String) model.getSelectedItem()));
            trim.setAdapter(adapter);
            findViewById(R.id.spinner4).setVisibility(View.VISIBLE);
        }
        if (parent.getId() == R.id.spinner4) {
            if (((String) trim.getSelectedItem()).length() == 0)
                findViewById(R.id.spinner4).setVisibility(View.GONE);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void cancel(View view) {
        finish();
    }

    public void confirm(View view) {
        garageDatasource.insertVehicle(new Vehicle((String) year.getSelectedItem(), (String) make.getSelectedItem(), (String) model.getSelectedItem(), (String) trim.getSelectedItem()));
        finish();
    }

    public void populateDatabase() {

        //Remove progress bar and set UI visible
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        findViewById(R.id.spinner1).setVisibility(View.VISIBLE);
        findViewById(R.id.spinner2).setVisibility(View.VISIBLE);
        findViewById(R.id.spinner3).setVisibility(View.VISIBLE);
        findViewById(R.id.spinner4).setVisibility(View.VISIBLE);
        findViewById(R.id.cancel).setVisibility(View.VISIBLE);
        findViewById(R.id.confirm).setVisibility(View.VISIBLE);
    }


}
