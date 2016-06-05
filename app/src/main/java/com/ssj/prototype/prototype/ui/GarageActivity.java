package com.ssj.prototype.prototype.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ssj.prototype.prototype.R;
import com.ssj.prototype.prototype.adapters.GarageListArrayAdapter;
import com.ssj.prototype.prototype.database.GarageDataSource;
import com.ssj.prototype.prototype.model.Vehicle;

import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class GarageActivity extends AppCompatActivity {

    private GarageDataSource garageDatasource;
    private ArrayList<Vehicle> vehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Create the database connections
        garageDatasource = new GarageDataSource(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateList();

        // Add the listView listener
        ListView listView = (ListView) findViewById(R.id.listView);
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

        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_add_vehicle);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.action_vin_lookup) {
                    showInputDialog();
                    return true;
                } else if (id == R.id.action_manual_lookup) {
                    startActivity(new Intent(getBaseContext(), AddVehicleActivity.class));
                    return true;
                }
                return true;
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

    /**
     * @param position
     */
    private void clickOnVehicle(int position) {
        Intent intent = new Intent(this, VehicleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", vehicles.get(position).toString());
        bundle.putLong("id", vehicles.get(position).getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * @param position
     */
    private void longClickOnVehicle(int position) {
        garageDatasource.open();
        garageDatasource.deleteVehicle(vehicles.get(position).getId());
        garageDatasource.close();
        Toast.makeText(this, "Vehicle Deleted From Garage", Toast.LENGTH_LONG).show();
        populateList();
    }

    //TODO minimum length of the string
    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(GarageActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_input, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GarageActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.editText_vin);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getBaseContext(), AddVehicleActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("VIN", editText.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    /**
     *
     */
    private void populateList() {
        garageDatasource.open();
        vehicles = garageDatasource.getAllVehicles();
        garageDatasource.close();
        String[] vehicleStrings = new String[vehicles.size()];

        for (int i = 0; i < vehicles.size(); i++) {
            vehicleStrings[i] = vehicles.get(i).toString();
        }
        ((ListView) findViewById(R.id.listView)).setAdapter(new GarageListArrayAdapter(this, vehicleStrings));
    }
}
