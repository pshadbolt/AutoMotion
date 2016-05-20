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

    private GarageDataSource garageDatasource;
    protected Spinner make;
    protected Spinner model;
    protected Spinner year;
    protected Spinner style;

    private HashMap<Spinner, HashMap<String, String>> spinnerMap;

    /**
     * Perform the call to the REST API and update spinner with retrieved values
     */
    private class VehicleDataSource extends AsyncTask<String, Void, ArrayList<String>> {

        private String endpoint = "https://api.edmunds.com/api/vehicle/v2/";
        private String format = "?fmt=jsonl";
        private String api_key = "&api_key=m6vz5qajjyxbctbehqtnguz2";
        private Spinner spinner;

        public VehicleDataSource(Spinner spinner) {
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
                URL url = new URL(endpoint + params[0] + format + api_key);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvehicle);

        // Create the database connections
        garageDatasource = new GarageDataSource(this);
        garageDatasource.open();

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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        garageDatasource.open();
    }


    @Override
    protected void onStop() {
        super.onStop();
        garageDatasource.close();
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
        finish();
    }

    /**
     * Confirm Button
     */
    public void confirm(View view) {
        Toast.makeText(this, "Vehicle Added To Garage", Toast.LENGTH_LONG).show();
        garageDatasource.insertVehicle(new Vehicle((String) year.getSelectedItem(), (String) make.getSelectedItem(), (String) model.getSelectedItem(), (String) style.getSelectedItem()));
        finish();
    }

    /**
     * Return the niceName value used for searching REST API from display name value for a given spinner
     */
    public String lookup(Spinner spinner) {
        String lookup = spinnerMap.get(spinner).get(spinner.getSelectedItem());
        Log.d("LOOKUP", (String) spinner.getSelectedItem() + " returned:" + lookup);
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
        Log.d("QUERY", query + " " + array + " " + name + " " + niceName);
        VehicleDataSource vehicleDataSource = new VehicleDataSource(spinner);
        vehicleDataSource.execute(new String[]{query, array, name, niceName});
    }
}