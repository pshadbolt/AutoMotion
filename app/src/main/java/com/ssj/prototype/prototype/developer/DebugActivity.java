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
        garageDatasource.open();
    }

    public void deleteDatabaseVehicles(View view) {
        Toast.makeText(this, "Vehicle Database Deleted", Toast.LENGTH_LONG).show();
    }

    public void populateDatabaseVehicles(View view) {

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
                    vehicle.setStyle(st.nextToken());
                else
                    vehicle.setStyle("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Vehicle Database Loaded", Toast.LENGTH_LONG).show();
    }

    public void deleteDatabaseGarage(View view) {
        garageDatasource.drop();
        Toast.makeText(this, "Garage Database Deleted", Toast.LENGTH_LONG).show();
    }
}
