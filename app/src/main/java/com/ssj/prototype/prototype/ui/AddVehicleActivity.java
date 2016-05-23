package com.ssj.prototype.prototype.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    private GarageDataSource garageDatasSource;
    protected Spinner make;
    protected Spinner model;
    protected Spinner year;
    protected Spinner style;
    private HashMap<Spinner, HashMap<String, String>> spinnerMap;

    //Query information
    private String endpointMaintenance = "https://api.edmunds.com/v1/api/maintenance/";
    private String endpointVehicle = "https://api.edmunds.com/api/vehicle/v2/";
    private String format = "fmt=json";
    private String api_key = "&api_key=m6vz5qajjyxbctbehqtnguz2";

    /**
     * Perform the call to the REST API and update spinner with retrieved values
     */
    private class SpinnerLoader extends AsyncTask<String, Void, ArrayList<String>> {

        private Spinner spinner;

        public SpinnerLoader(Spinner spinner) {
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
                        Log.d("MAP", "key:" + jsonObject1.getString(params[2]) + " value:" + jsonObject1.getString(params[3]));
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
            spinner.setVisibility(View.VISIBLE);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
    }

    /**
     *
     */
    private class MaintenanceQuery extends AsyncTask<String, Void, ArrayList<String[]>> {

        Vehicle vehicle;

        public MaintenanceQuery(Vehicle vehicle) {
            this.vehicle = vehicle;
        }

        @Override
        protected void onPreExecute() {
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

                    //Parse JSON response
                    String yearid = "";
                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                    JSONArray responsesArray = jsonObject.getJSONArray("styles");
                    for (int i = 0; i < responsesArray.length(); i++) {
                        JSONObject jsonObject1 = responsesArray.getJSONObject(i);
                        if (jsonObject1.getString("name").equals(params[1])) {
                            yearid = jsonObject1.getJSONObject("year").getString("id");
                            Log.d("INFO", "year id:" + yearid);
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
                        String[] entry = new String[]{jsonObject1.getString("intervalMileage"), jsonObject1.getString("action"), jsonObject1.getString("item")};
                        response.add(entry);
                        Log.d("INFO", jsonObject1.getString("intervalMileage") + " " + jsonObject1.getString("action") + " " + jsonObject1.getString("item"));
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
                garageDatasSource.insertMaintenance(vehicle, entry[0], entry[1], entry[2]);
            }
            findViewById(R.id.textView).setVisibility(View.VISIBLE);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            Toast.makeText(AddVehicleActivity.this, "Vehicle Added To Garage", Toast.LENGTH_LONG).show();
            //TODO Should finish be used?
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvehicle);

        // Create the database connections
        garageDatasSource = new GarageDataSource(this);
        garageDatasSource.open();

        // Prepare Spinners
        make = (Spinner) findViewById(R.id.spinner1);
        model = (Spinner) findViewById(R.id.spinner2);
        year = (Spinner) findViewById(R.id.spinner3);
        style = (Spinner) findViewById(R.id.spinner4);
        make.setOnItemSelectedListener(this);
        model.setOnItemSelectedListener(this);
        year.setOnItemSelectedListener(this);
        style.setOnItemSelectedListener(this);

        spinnerMap = new HashMap<Spinner, HashMap<String, String>>();

        findViewById(R.id.confirm).setEnabled(false);
        //Initiate populating the spinner values
        query("makes?", "makes", "name", "niceName", make);
    }

    @Override
    protected void onResume() {
        super.onResume();
        garageDatasSource.open();
    }

    @Override
    protected void onStop() {
        super.onStop();
        garageDatasSource.close();
    }

    // Spinner Selector
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        findViewById(R.id.confirm).setEnabled(false);

        if (parent.getId() == R.id.spinner1) {
            findViewById(R.id.spinner2).setVisibility(View.GONE);
            findViewById(R.id.spinner3).setVisibility(View.GONE);
            findViewById(R.id.spinner4).setVisibility(View.GONE);
            query(lookup(make) + "/models?", "models", "name", "niceName", model);
        }
        if (parent.getId() == R.id.spinner2) {
            findViewById(R.id.spinner3).setVisibility(View.GONE);
            findViewById(R.id.spinner4).setVisibility(View.GONE);
            query(lookup(make) + "/" + lookup(model) + "/years?", "years", "year", "year", year);
        }
        if (parent.getId() == R.id.spinner3) {
            findViewById(R.id.spinner4).setVisibility(View.GONE);
            query(lookup(make) + "/" + lookup(model) + "/" + year.getSelectedItem() + "/styles?", "styles", "name", "name", style);
        }
        if (parent.getId() == R.id.spinner4) {
            findViewById(R.id.confirm).setEnabled(true);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    /**
     * Cancel Button
     */
    public void cancel(View view) {
        //TODO Should finish be used?
        finish();
    }

    /**
     * Confirm Button
     */
    public void confirm(View view) {
        Vehicle vehicle = garageDatasSource.insertVehicle((String) year.getSelectedItem(), (String) make.getSelectedItem(), (String) model.getSelectedItem(), (String) style.getSelectedItem());
        MaintenanceQuery maintenanceQuery = new MaintenanceQuery(vehicle);
        maintenanceQuery.execute(new String[]{lookup(make) + "/" + lookup(model) + "/" + year.getSelectedItem() + "/styles?", lookup(style)});
    }

    /**
     * Return the niceName value used for searching REST API from display name value for a given spinner
     */
    public String lookup(Spinner spinner) {
        String lookup = spinnerMap.get(spinner).get(spinner.getSelectedItem());
        //Log.d("LOOKUP", (String) spinner.getSelectedItem() + " returned:" + lookup);
        return lookup;
    }

    /**
     * @param query
     * @param array
     * @param name
     * @param niceName
     * @param spinner
     */
    public void query(String query, String array, String name, String niceName, Spinner spinner) {
        findViewById(R.id.textView).setVisibility(View.GONE);
        //Log.d("QUERY", query + " " + array + " " + name + " " + niceName);
        SpinnerLoader spinnerLoader = new SpinnerLoader(spinner);
        spinnerLoader.execute(new String[]{query, array, name, niceName});
    }
}