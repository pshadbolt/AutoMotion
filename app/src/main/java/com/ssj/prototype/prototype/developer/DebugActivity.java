package com.ssj.prototype.prototype.developer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ssj.prototype.prototype.R;
import com.ssj.prototype.prototype.database.GarageDataSource;
import com.ssj.prototype.prototype.model.Vehicle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

public class DebugActivity extends AppCompatActivity {

    private GarageDataSource garageDatasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        // Create the database connections
        garageDatasource = new GarageDataSource(this);
    }

    public void deleteDatabaseVehicles(View view) {
        Toast.makeText(this, "Vehicle Database Deleted", Toast.LENGTH_LONG).show();
    }

    public void deleteDatabaseGarage(View view) {
        garageDatasource.open();
        garageDatasource.drop();
        garageDatasource.close();
        Toast.makeText(this, "Garage Database Deleted", Toast.LENGTH_LONG).show();
    }
}
