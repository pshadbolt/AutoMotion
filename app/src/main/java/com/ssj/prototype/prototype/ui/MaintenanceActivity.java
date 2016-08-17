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
import com.ssj.prototype.prototype.model.Edmunds.MaintenanceAction;

public class MaintenanceActivity extends AppCompatActivity {

    private GarageDataSource garageDatasource;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = this.getIntent().getExtras().getLong("id");

        garageDatasource = new GarageDataSource(this);
        garageDatasource.open();
        MaintenanceAction maintenanceAction = garageDatasource.getMaintenanceAction(id);
        garageDatasource.close();

        ((TextView) findViewById(R.id.textView1)).setText(Integer.toString(maintenanceAction.getIntervalMileage()));
        ((TextView) findViewById(R.id.textView2)).setText(maintenanceAction.getAction());
        ((TextView) findViewById(R.id.textView3)).setText(maintenanceAction.getItem());
        ((TextView) findViewById(R.id.textView4)).setText(maintenanceAction.getItemDescription());
    }

    public void complete(View view) {
        garageDatasource.open();
        garageDatasource.completeMaintenance(id);
        garageDatasource.close();
        finish();
    }

}
