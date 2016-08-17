package com.ssj.prototype.prototype.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ssj.prototype.prototype.R;
import com.ssj.prototype.prototype.database.GarageDataSource;

public class VehicleActivity extends AppCompatActivity {

    long vehicleID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set the title
        String[] title = this.getIntent().getExtras().getString("title").split(",");
        setTitle(title[0] + " " + title[1] + " " + title[2]);
        vehicleID = this.getIntent().getExtras().getLong("id");

        //((TextView) findViewById(R.id.textView0)).setText(garageDataSource.getVehicle(vehicleID));
        //((TextView) findViewById(R.id.textView1)).setText(garageDataSource.getMileage(vehicleID));
        //((TextView) findViewById(R.id.textView2)).setText(garageDataSource.getMaintenanceSchedule(vehicleID));
    }

    public void openMaintenanceSchedule(View view){
        Intent intent = new Intent(this, MaintenanceScheduleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("id", vehicleID);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openVehicleSpecs(View view){
        Intent intent = new Intent(this, VehicleSpecsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("id", vehicleID);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
