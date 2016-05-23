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

public class VehicleActivity extends AppCompatActivity {

    GarageDataSource garageDataSource;

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
        garageDataSource = new GarageDataSource(this);
        garageDataSource.open();
        ((TextView) findViewById(R.id.textView)).setText(garageDataSource.getMaintenance(this.getIntent().getExtras().getLong("id")));
    }

    @Override
    protected void onResume() {
        super.onResume();
        garageDataSource.open();
    }

    @Override
    protected void onStop() {
        super.onStop();
        garageDataSource.close();
    }

}
