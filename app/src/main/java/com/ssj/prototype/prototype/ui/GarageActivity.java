package com.ssj.prototype.prototype.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ssj.prototype.prototype.R;
import com.ssj.prototype.prototype.adapters.GarageListArrayAdapter;
import com.ssj.prototype.prototype.database.GarageDataSource;
import com.ssj.prototype.prototype.model.Edmunds.EdmundsCodes;
import com.ssj.prototype.prototype.model.Vehicle;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickOnVehicle(position);
            }
        });

        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_add_vehicle);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.action_vin_lookup) {
                    showVINInputDialog();
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_garage,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId() == R.id.menu_item1){
            deleteVehicle(acmi.position);
            return true;
        }
        return false;
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
     * Verify the VIN entry prior to moving to Add Vehicle Activity
     */
    private class VerifyVIN extends AsyncTask<String, Void, Boolean> {

        AlertDialog dialog = null;
        RelativeLayout loadingPanel = null;
        String VIN = "";

        public VerifyVIN(String VIN, RelativeLayout loadingPanel, AlertDialog dialog) {
            this.dialog = dialog;
            this.loadingPanel = loadingPanel;
            this.VIN = VIN;
        }

        @Override
        protected void onPreExecute() {
            loadingPanel.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(String... params) {

            URL url = null;
            HttpURLConnection urlConnection = null;
            if (VIN.length() == 17) {
                try {
                    url = new URL(EdmundsCodes.endpointVehicle + "squishvins/" + VIN.subSequence(0, 8).toString().toUpperCase() + VIN.subSequence(9, 11).toString().toUpperCase() + "/?" + EdmundsCodes.format + EdmundsCodes.api_key);
                    Log.d("REST", url.toString());
                    try {
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        Log.d("REST", String.valueOf(urlConnection.getResponseCode()));
                        return (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK);
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);
            loadingPanel.setVisibility(View.INVISIBLE);
            Log.d("REST", response.toString());
            if (response.booleanValue()) {
                dialog.dismiss();
                Intent intent = new Intent(getBaseContext(), AddVehicleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("VIN", VIN);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                if (VIN.length() != 17)
                    Toast.makeText(GarageActivity.this, R.string.toast_insufficent_characters, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(GarageActivity.this, R.string.toast_vin_not_found, Toast.LENGTH_LONG).show();
            }
        }
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
    private void deleteVehicle(int position) {
        garageDatasource.open();
        garageDatasource.deleteVehicle(vehicles.get(position).getId());
        garageDatasource.close();
        Toast.makeText(this, R.string.toast_vehicle_deleted, Toast.LENGTH_LONG).show();
        populateList();
    }

    /**
     *
     */
    protected void showVINInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(GarageActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_input, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GarageActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.editText_vin);
        final RelativeLayout loadingPanel = (RelativeLayout) promptView.findViewById(R.id.loadingPanel);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(R.string.button_submit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.button_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new VerifyVIN(editText.getText().toString(), loadingPanel, alert).execute();
            }
        });
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
