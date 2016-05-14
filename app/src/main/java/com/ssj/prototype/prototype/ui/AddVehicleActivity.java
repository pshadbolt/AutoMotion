package com.ssj.prototype.prototype.ui;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ssj.prototype.prototype.R;
import com.ssj.prototype.prototype.database.GarageDataSource;
import com.ssj.prototype.prototype.database.VehicleDataSource;
import com.ssj.prototype.prototype.model.Vehicle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

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

        populateDatabase();

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
    }

    // Spinner Selector
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (parent.getId() == R.id.spinner1) {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vehicleDatasource.getMakes((String) year.getSelectedItem()));
            make.setAdapter(adapter);
        }

        if (parent.getId() == R.id.spinner2) {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vehicleDatasource.getModels((String) year.getSelectedItem(), (String) make.getSelectedItem()));
            model.setAdapter(adapter);
        }

        if (parent.getId() == R.id.spinner3) {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vehicleDatasource.getTrims((String) year.getSelectedItem(), (String) make.getSelectedItem(), (String) model.getSelectedItem()));
            trim.setAdapter(adapter);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void cancel(View view) {
        finish();
    }

    public void confirm(View view) {
        garageDatasource.insertValue(new Vehicle((String) year.getSelectedItem(), (String) make.getSelectedItem(), (String) model.getSelectedItem(), (String) trim.getSelectedItem()));
        finish();
    }

    public void populateDatabase() {

        //Delete and remake the database
        vehicleDatasource.recreate();

        //Load values from local csv file
        InputStream ins = getResources().openRawResource(getResources().getIdentifier("vehicledatabase", "raw", getPackageName()));
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(ins, Charset.forName("UTF-8")));

        String line = "";
        Vehicle vehicle = new Vehicle();
        StringTokenizer st = null;
        try {
            while ((line = reader.readLine()) != null) {
                st = new StringTokenizer(line, ",");
                vehicle.setYear(st.nextToken());
                vehicle.setMake(st.nextToken());
                vehicle.setModel(st.nextToken());
                if (st.hasMoreTokens())
                    vehicle.setTrim(st.nextToken());
                else
                    vehicle.setTrim("");
                vehicleDatasource.insertValue(vehicle);
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        // Execute some code after 3 seconds have passed
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                findViewById(R.id.spinner1).setVisibility(View.VISIBLE);
                findViewById(R.id.spinner2).setVisibility(View.VISIBLE);
                findViewById(R.id.spinner3).setVisibility(View.VISIBLE);
                findViewById(R.id.spinner4).setVisibility(View.VISIBLE);
                findViewById(R.id.cancel).setVisibility(View.VISIBLE);
                findViewById(R.id.confirm).setVisibility(View.VISIBLE);
            }
        }, 3000);
    }


}
