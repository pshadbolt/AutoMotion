package com.ssj.prototype.prototype.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ssj.prototype.prototype.R;
import com.ssj.prototype.prototype.database.GarageDataSource;
import com.ssj.prototype.prototype.model.Vehicle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class AddVehicleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int state = 1;

    protected Spinner make;
    protected Spinner model;
    protected Spinner year;
    protected Spinner style;
    protected Spinner engine;
    protected Spinner transmission;

    private HashMap<Spinner, HashMap<String, String>> spinnerMap;

    protected EditText mileageTotal;
    protected EditText mileageAnnual;

    protected Button cancel;
    protected Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvehicle);

        // Prepare Spinners
        make = (Spinner) findViewById(R.id.spinner1);
        model = (Spinner) findViewById(R.id.spinner2);
        year = (Spinner) findViewById(R.id.spinner3);
        style = (Spinner) findViewById(R.id.spinner4);
        engine = (Spinner) findViewById(R.id.spinner5);
        transmission = (Spinner) findViewById(R.id.spinner6);
        make.setOnItemSelectedListener(this);
        model.setOnItemSelectedListener(this);
        year.setOnItemSelectedListener(this);
        style.setOnItemSelectedListener(this);
        engine.setOnItemSelectedListener(this);
        transmission.setOnItemSelectedListener(this);

        spinnerMap = new HashMap<Spinner, HashMap<String, String>>();

        mileageTotal = (EditText) findViewById(R.id.editText1);
        mileageAnnual = (EditText) findViewById(R.id.editText2);

        cancel = (Button) findViewById(R.id.cancel);
        confirm = (Button) findViewById(R.id.confirm);

        //Initiate populating the spinner values
        query("makes?", "makes", "name", "niceName", make);
    }

    // Spinner Selector
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        findViewById(R.id.confirm).setEnabled(false);

        if (parent.getId() == make.getId()) {
            model.setVisibility(View.GONE);
            year.setVisibility(View.GONE);
            style.setVisibility(View.GONE);
            engine.setVisibility(View.GONE);
            transmission.setVisibility(View.GONE);
            query(lookup(make) + "/models?", "models", "name", "niceName", model);
        }
        if (parent.getId() == model.getId()) {
            year.setVisibility(View.GONE);
            style.setVisibility(View.GONE);
            engine.setVisibility(View.GONE);
            transmission.setVisibility(View.GONE);
            query(lookup(make) + "/" + lookup(model) + "/years?", "years", "year", "year", year);
        }
        if (parent.getId() == year.getId()) {
            style.setVisibility(View.GONE);
            engine.setVisibility(View.GONE);
            transmission.setVisibility(View.GONE);
            query(lookup(make) + "/" + lookup(model) + "/" + year.getSelectedItem() + "/styles?", "styles", "name", "id", style);
        }
        if (parent.getId() == style.getId()) {
            engine.setVisibility(View.GONE);
            transmission.setVisibility(View.GONE);
            query("styles/" + lookup(style) + "/engines?", "engines", "name", "id", engine);
            query("styles/" + lookup(style) + "/transmissions?", "transmissions", "transmissionType", "id", transmission);
        }
        if (parent.getId() == engine.getId()) {
            findViewById(R.id.confirm).setEnabled(true);
        }
        if (parent.getId() == transmission.getId()) {
            findViewById(R.id.confirm).setEnabled(true);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    //Query information
    private String endpointMaintenance = "https://api.edmunds.com/v1/api/maintenance/";
    private String endpointVehicle = "https://api.edmunds.com/api/vehicle/v2/";
    private String format = "fmt=json";
    private String api_key = "&api_key=m6vz5qajjyxbctbehqtnguz2";

    /**
     * Perform the call to the REST API and update spinner with retrieved values
     */
    private class SpinnerQuery extends AsyncTask<String, Void, ArrayList<String>> {

        private Spinner spinner;

        public SpinnerQuery(Spinner spinner) {
            this.spinner = spinner;
        }

        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.GONE);
            spinnerMap.put(spinner, new HashMap<String, String>());
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        /**
         * Execute the rest api call in a background thread
         *
         * @param params [0] - the query to be executed
         *               [1] - the expected json array value
         *               [2] - the attribute to parse for the display name
         *               [3] - the attribute to parse for the search name
         * @return Arraylist of string objects
         */
        protected ArrayList<String> doInBackground(String... params) {
            try {
                URL url = new URL(endpointVehicle + params[0] + "?" + format + api_key);
                Log.d("REST", url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    //Send request to REST API
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    Log.d("INFO", stringBuilder.toString());

                    //Parse JSON response
                    ArrayList<String> responses = new ArrayList<String>();
                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                    JSONArray responsesArray = jsonObject.getJSONArray(params[1]);
                    for (int i = 0; i < responsesArray.length(); i++) {
                        JSONObject jsonObject1 = responsesArray.getJSONObject(i);
                        responses.add(jsonObject1.getString(params[2]));
                        //Store the mapping of niceName to name for lookup on next search
                        spinnerMap.get(spinner).put(jsonObject1.getString(params[2]), jsonObject1.getString(params[3]));
                    }
                    return responses;
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @Override
        /**
         * Set the spinner values to the strings obtained from the REST API call
         */
        protected void onPostExecute(ArrayList<String> responses) {
            super.onPostExecute(responses);
            if (responses == null)
                responses = new ArrayList<String>();
            spinner.setAdapter(new ArrayAdapter<String>(AddVehicleActivity.this, android.R.layout.simple_spinner_dropdown_item, responses));
            if (responses.size() > 0)
                spinner.setVisibility(View.VISIBLE);
            if (responses.size() > 1)
                spinner.setEnabled(true);
            else
                spinner.setEnabled(false);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
    }

    /**
     *
     */
    private class VehicleMaintenanceQuery extends AsyncTask<String, Void, ArrayList<String[]>> {

        GarageDataSource garageDataSource;
        Vehicle vehicle;

        public VehicleMaintenanceQuery(GarageDataSource garageDataSource, Vehicle vehicle) {
            this.garageDataSource = garageDataSource;
            this.vehicle = vehicle;
        }

        @Override
        protected void onPreExecute() {
            findViewById(R.id.cancel).setEnabled(false);
            findViewById(R.id.confirm).setEnabled(false);
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        /**
         *
         */
        protected ArrayList<String[]> doInBackground(String... params) {
            try {
                URL url = new URL(endpointVehicle + params[0] + "?" + format + api_key);
                Log.d("REST", url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    //Send request to REST API
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    Log.d("INFO", stringBuilder.toString());

                    //Query style info to get yearid value to query maintenance info
                    String yearid = "";
                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                    JSONArray responsesArray = jsonObject.getJSONArray("styles");
                    for (int i = 0; i < responsesArray.length(); i++) {
                        JSONObject jsonObject1 = responsesArray.getJSONObject(i);
                        if (jsonObject1.getString("name").equals(params[1])) {
                            yearid = jsonObject1.getJSONObject("year").getString("id");
                        }
                    }
                    url = new URL(endpointMaintenance + "actionrepository/findbymodelyearid?modelyearid=" + yearid + "&" + format + api_key);
                    Log.d("REST", url.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    //Send request to REST API
                    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    stringBuilder = new StringBuilder();
                    line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    Log.d("INFO", stringBuilder.toString());

                    ArrayList<String[]> response = new ArrayList<String[]>();
                    jsonObject = new JSONObject(stringBuilder.toString());
                    responsesArray = jsonObject.getJSONArray("actionHolder");
                    for (int i = 0; i < responsesArray.length(); i++) {
                        JSONObject jsonObject1 = responsesArray.getJSONObject(i);
                        String engineCode = "";
                        String transmissionCode = "";
                        String intervalMileage = "0";
                        String frequency = "0";
                        String action = "";
                        String item = "";
                        String itemDescription = "";
                        if (jsonObject1.has("engineCode"))
                            engineCode = jsonObject1.getString("engineCode");
                        if (jsonObject1.has("transmissionCode"))
                            transmissionCode = jsonObject1.getString("transmissionCode");
                        if (jsonObject1.has("intervalMileage"))
                            intervalMileage = jsonObject1.getString("intervalMileage");
                        if (jsonObject1.has("frequency"))
                            frequency = jsonObject1.getString("frequency");
                        if (jsonObject1.has("action"))
                            action = jsonObject1.getString("action");
                        if (jsonObject1.has("item"))
                            item = jsonObject1.getString("item");
                        if (jsonObject1.has("itemDescription"))
                            itemDescription = jsonObject1.getString("itemDescription");
                        response.add(new String[]{engineCode, transmissionCode, intervalMileage, frequency, action, item, itemDescription});
                    }
                    return response;
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @Override
        /**
         */
        protected void onPostExecute(ArrayList<String[]> response) {
            super.onPostExecute(response);
            for (int i = 0; i < response.size(); i++) {
                String[] entry = response.get(i);
                garageDataSource.insertMaintenance(vehicle, entry[0], entry[1], entry[2], entry[3], entry[4], entry[5], entry[6]);
            }
            garageDataSource.close();
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            Toast.makeText(AddVehicleActivity.this, "Vehicle Added To Garage", Toast.LENGTH_LONG).show();
            //TODO Should finish be used?
            finish();
        }
    }

    /**
     * Return the niceName value used for searching REST API from display name value for a given spinner
     */
    public String lookup(Spinner spinner) {
        return spinnerMap.get(spinner).get(spinner.getSelectedItem());
    }

    /**
     * Execute the query to populate a spinner value
     */
    public void query(String query, String array, String name, String niceName, Spinner spinner) {
        SpinnerQuery spinnerQuery = new SpinnerQuery(spinner);
        spinnerQuery.execute(new String[]{query, array, name, niceName});
    }

    /**
     * Cancel Button
     */
    public void cancel(View view) {

        if (state == 1) {
            //TODO Should finish be used?
            finish();
        } else if (state == 2) {
            state = 1;
            make.setVisibility(View.VISIBLE);
            model.setVisibility(View.VISIBLE);
            year.setVisibility(View.VISIBLE);
            style.setVisibility(View.VISIBLE);
            engine.setVisibility(View.VISIBLE);
            transmission.setVisibility(View.VISIBLE);
            mileageTotal.setVisibility(View.GONE);
            mileageAnnual.setVisibility(View.GONE);
            cancel.setText("CANCEL");
            confirm.setText("NEXT");
        }
    }

    /**
     * Confirm Button
     */
    public void confirm(View view) {

        if (state == 1) {
            state = 2;
            make.setVisibility(View.GONE);
            model.setVisibility(View.GONE);
            year.setVisibility(View.GONE);
            style.setVisibility(View.GONE);
            engine.setVisibility(View.GONE);
            transmission.setVisibility(View.GONE);
            mileageTotal.setVisibility(View.VISIBLE);
            mileageAnnual.setVisibility(View.VISIBLE);
            cancel.setText("BACK");
            confirm.setText("CONFIRM");
        } else if (state == 2) {
            // Create the database connections
            GarageDataSource garageDataSource = new GarageDataSource(this);
            garageDataSource.open();

            Vehicle vehicle = garageDataSource.insertVehicle((String) year.getSelectedItem(), (String) make.getSelectedItem(), (String) model.getSelectedItem(), (String) style.getSelectedItem(), (String) engine.getSelectedItem(), (String) transmission.getSelectedItem(), mileageTotal.getText().toString(), mileageAnnual.getText().toString());
            VehicleMaintenanceQuery vehicleMaintenanceQuery = new VehicleMaintenanceQuery(garageDataSource, vehicle);
            vehicleMaintenanceQuery.execute(new String[]{lookup(make) + "/" + lookup(model) + "/" + year.getSelectedItem() + "/styles?", (String) style.getSelectedItem()});
        }
    }
}