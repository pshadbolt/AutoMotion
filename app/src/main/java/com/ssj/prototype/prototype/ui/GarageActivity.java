package com.ssj.prototype.prototype.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ssj.prototype.prototype.adapters.GarageListArrayAdapter;
import com.ssj.prototype.prototype.model.Vehicle;
import com.ssj.prototype.prototype.R;
import com.ssj.prototype.prototype.database.GarageDataSource;

import java.util.ArrayList;

public class GarageActivity extends AppCompatActivity {

    private GarageDataSource garageDatasource;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<Vehicle> vehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Create the database connections
        garageDatasource = new GarageDataSource(this);
        garageDatasource.open();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        populateList();

        // Add the listView listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickOnVehicle(position);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3) {
                longClickOnVehicle(position);
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GarageActivity.this, AddVehicleActivity.class));
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        garageDatasource.open();
        populateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        garageDatasource.open();
        populateList();
    }

    /**
     *
     */
    private void populateList() {

        vehicles = garageDatasource.getAllEntries();
        String[] vehicleStrings = new String[vehicles.size()];

        for (int i = 0; i < vehicles.size(); i++) {
            vehicleStrings[i] = vehicles.get(i).toString();
        }

        adapter = new GarageListArrayAdapter(this, vehicleStrings, null);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    /**
     * @param position
     */
    private void clickOnVehicle(int position) {
        populateList();
    }

    /**
     *
     * @param position
     */
    private void longClickOnVehicle(int position) {
        garageDatasource.deleteVehicle(vehicles.get(position).getId());
        Toast.makeText(this, "Vehicle Deleted From Garage", Toast.LENGTH_LONG).show();
        populateList();
    }

    @Override
    protected void onStop() {
        garageDatasource.close();
        super.onStop();
    }
}
