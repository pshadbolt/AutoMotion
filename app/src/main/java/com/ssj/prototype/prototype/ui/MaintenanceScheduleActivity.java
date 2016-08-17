package com.ssj.prototype.prototype.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ssj.prototype.prototype.R;
import com.ssj.prototype.prototype.database.GarageDataSource;

public class MaintenanceScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set the title
        GarageDataSource garageDataSource = new GarageDataSource(this);
        garageDataSource.open();
        ((TextView) findViewById(R.id.textView1)).setText(garageDataSource.getMaintenanceSchedule(this.getIntent().getExtras().getLong("id")));
        garageDataSource.close();
    }

}
