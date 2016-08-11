package com.ssj.prototype.prototype.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ssj.prototype.prototype.R;
import com.ssj.prototype.prototype.database.GarageDataSource;
import com.ssj.prototype.prototype.model.Vehicle;

public class VehicleSpecsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_specs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set the title
        GarageDataSource garageDataSource = new GarageDataSource(this);
        garageDataSource.open();
        Vehicle vehicle = garageDataSource.getVehicle(this.getIntent().getExtras().getLong("id"));
        ((TextView) findViewById(R.id.textView1)).setText(vehicle.specs());
        garageDataSource.close();
    }

}
