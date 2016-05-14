package com.ssj.prototype.prototype.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ssj.prototype.prototype.R;
import com.ssj.prototype.prototype.adapters.GarageListArrayAdapter;
import com.ssj.prototype.prototype.database.GarageDataSource;

import java.util.ArrayList;

public class GarageActivity extends AppCompatActivity {

    private GarageDataSource garageDatasource;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GarageActivity.this, AddVehicleActivity.class));
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        populateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateList();
    }

    private void populateList() {

        ArrayList<String> vehicles = garageDatasource.getAllEntries();

        ArrayAdapter<String> adapter = new GarageListArrayAdapter(this, vehicles.toArray(new String[vehicles.size()]), null);
        ((ListView) findViewById(R.id.listView)).setAdapter(adapter);

    }
}
