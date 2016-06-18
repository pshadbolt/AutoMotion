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
import com.ssj.prototype.prototype.model.Edmunds.EdmundsCodes;
import com.ssj.prototype.prototype.model.Vehicle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class AddVehicleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //UI Elements
    protected Spinner make;
    protected Spinner model;
    protected Spinner year;
    protected Spinner style;
    protected Spinner engine;
    protected Spinner transmission;

    protected EditText mileageTotal;
    protected EditText mileageAnnual;

    protected Button cancel;
    protected Button confirm;

    //Data Elements
    private int queryCounter = 0;
    private int state = 1;
    private HashMap<Spinner, HashMap<String, String>> lookupMap;
    private HashMap<Spinner, HashMap<String, String>> styleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        // Prepare Spinners
        make = (Spinner) findViewById(R.id.spinner1);
        model = (Spinner) findViewById(R.id.spinner2);
        year = (Spinner) findViewById(R.id.spinner3);
        style = (Spinner) findViewById(R.id.spinner4);
        engine = (Spinner) findViewById(R.id.spinner5);
        transmission = (Spinner) findViewById(R.id.spinner6);

        lookupMap = new HashMap<Spinner, HashMap<String, String>>();
        lookupMap.put(make, new HashMap<String, String>());
        lookupMap.put(model, new HashMap<String, String>());
        lookupMap.put(year, new HashMap<String, String>());
        lookupMap.put(style, new HashMap<String, String>());
        lookupMap.put(engine, new HashMap<String, String>());
        lookupMap.put(transmission, new HashMap<String, String>());
        styleMap = new HashMap<Spinner, HashMap<String, String>>();
        styleMap.put(engine, new HashMap<String, String>());
        styleMap.put(transmission, new HashMap<String, String>());

        mileageTotal = (EditText) findViewById(R.id.editText1);
        mileageAnnual = (EditText) findViewById(R.id.editText2);

        cancel = (Button) findViewById(R.id.cancel);
        confirm = (Button) findViewById(R.id.confirm);

        //Initiate populating the spinner values
        if (this.getIntent().hasExtra("VIN") && this.getIntent().getExtras().getString("VIN").length() >= 11) {
            toggleSpinners(false);
            String VIN = this.getIntent().getExtras().getString("VIN");
            new QueryVIN().execute(VIN.subSequence(0, 8).toString().toUpperCase() + VIN.subSequence(9, 11).toString().toUpperCase());
        } else {
            enableListeners();
            toggleSpinners(true);
            query(make);
        }
    }

    private void toggleSpinners(boolean enabled) {
        make.setEnabled(enabled);
        model.setEnabled(enabled);
        year.setEnabled(enabled);
        style.setEnabled(enabled);
        engine.setEnabled(enabled);
        transmission.setEnabled(enabled);
    }

    private void enableListeners() {
        make.setOnItemSelectedListener(this);
        model.setOnItemSelectedListener(this);
        year.setOnItemSelectedListener(this);
        style.setOnItemSelectedListener(this);
        engine.setOnItemSelectedListener(this);
        transmission.setOnItemSelectedListener(this);
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
            query(model);
        }
        if (parent.getId() == model.getId()) {
            year.setVisibility(View.GONE);
            style.setVisibility(View.GONE);
            engine.setVisibility(View.GONE);
            transmission.setVisibility(View.GONE);
            query(year);
        }
        if (parent.getId() == year.getId()) {
            style.setVisibility(View.GONE);
            engine.setVisibility(View.GONE);
            transmission.setVisibility(View.GONE);
            query(style);
        }
        if (parent.getId() == style.getId()) {
            engine.setVisibility(View.GONE);
            transmission.setVisibility(View.GONE);
            query(engine);
            query(transmission);
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

    /**
     * Perform the call to the REST API and update spinner with retrieved values
     */
    private class QueryVIN extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected String[] doInBackground(String... params) {
            try {
                URL url = new URL(EdmundsCodes.endpointVehicle + "squishvins/" + params[0] + "/?" + EdmundsCodes.format + EdmundsCodes.api_key);
                Log.d("REST", url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                queryCounter++;
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
                    String[] responses = new String[6];
                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());

                    if (jsonObject.getJSONObject("make").has(EdmundsCodes.MAKES_DISPLAY)) {
                        responses[0] = jsonObject.getJSONObject("make").getString(EdmundsCodes.MAKES_DISPLAY);
                        lookupMap.get(make).put(responses[0], jsonObject.getJSONObject("make").getString(EdmundsCodes.MAKES_ID));
                    }
                    if (jsonObject.getJSONObject("model").has(EdmundsCodes.MODELS_DISPLAY)) {
                        responses[1] = jsonObject.getJSONObject("model").getString(EdmundsCodes.MODELS_DISPLAY);
                        lookupMap.get(model).put(responses[1], jsonObject.getJSONObject("model").getString(EdmundsCodes.MODELS_ID));
                    }
                    if (jsonObject.getJSONArray("years").getJSONObject(0).has(EdmundsCodes.YEARS_DISPLAY)) {
                        responses[2] = jsonObject.getJSONArray("years").getJSONObject(0).getString(EdmundsCodes.YEARS_DISPLAY);
                        lookupMap.get(year).put(responses[2], jsonObject.getJSONArray("years").getJSONObject(0).getString(EdmundsCodes.YEARS_ID));
                    }
                    if (jsonObject.getJSONArray("years").getJSONObject(0).getJSONArray("styles").getJSONObject(0).has(EdmundsCodes.STYLES_DISPLAY) && jsonObject.getJSONArray("years").getJSONObject(0).getJSONArray("styles").length() == 1) {
                        responses[3] = jsonObject.getJSONArray("years").getJSONObject(0).getJSONArray("styles").getJSONObject(0).getString(EdmundsCodes.STYLES_DISPLAY);
                        lookupMap.get(style).put(responses[3], jsonObject.getJSONArray("years").getJSONObject(0).getJSONArray("styles").getJSONObject(0).getString(EdmundsCodes.STYLES_ID));
                    }
                    if (jsonObject.getJSONObject("engine").has(EdmundsCodes.ENGINES_DISPLAY) && jsonObject.getJSONObject("engine").has(EdmundsCodes.ENGINES_ID)) {
                        responses[4] = jsonObject.getJSONObject("engine").getString(EdmundsCodes.ENGINES_DISPLAY);
                        lookupMap.get(engine).put(responses[4], jsonObject.getJSONObject("engine").getString(EdmundsCodes.ENGINES_ID));
                        styleMap.get(engine).put(responses[4], jsonObject.getJSONArray("years").getJSONObject(0).getJSONArray("styles").getJSONObject(0).getString(EdmundsCodes.STYLES_ID));
                    }
                    if (jsonObject.getJSONObject("transmission").has(EdmundsCodes.TRANSMISSIONS_DISPLAY) && jsonObject.getJSONObject("transmission").has(EdmundsCodes.TRANSMISSIONS_ID)) {
                        responses[5] = jsonObject.getJSONObject("transmission").getString(EdmundsCodes.TRANSMISSIONS_DISPLAY);
                        lookupMap.get(transmission).put(responses[5], jsonObject.getJSONObject("transmission").getString(EdmundsCodes.TRANSMISSIONS_ID));
                        styleMap.get(transmission).put(responses[5], jsonObject.getJSONArray("years").getJSONObject(0).getJSONArray("styles").getJSONObject(0).getString(EdmundsCodes.STYLES_ID));
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
        protected void onPostExecute(String[] responses) {
            super.onPostExecute(responses);

            if (responses == null) {
                Toast.makeText(AddVehicleActivity.this, R.string.toast_vin_not_found, Toast.LENGTH_LONG).show();
                enableListeners();
                toggleSpinners(true);
                query(make);
            } else {
                make.setAdapter(new ArrayAdapter<String>(AddVehicleActivity.this, android.R.layout.simple_spinner_dropdown_item, new String[]{responses[0]}));
                make.setVisibility(View.VISIBLE);

                model.setAdapter(new ArrayAdapter<String>(AddVehicleActivity.this, android.R.layout.simple_spinner_dropdown_item, new String[]{responses[1]}));
                model.setVisibility(View.VISIBLE);

                year.setAdapter(new ArrayAdapter<String>(AddVehicleActivity.this, android.R.layout.simple_spinner_dropdown_item, new String[]{responses[2]}));
                year.setVisibility(View.VISIBLE);

                if (responses[3] == null) {
                    style.setOnItemSelectedListener(AddVehicleActivity.this);
                    engine.setOnItemSelectedListener(AddVehicleActivity.this);
                    transmission.setOnItemSelectedListener(AddVehicleActivity.this);
                    query(style);
                    style.setEnabled(true);
                } else {
                    style.setAdapter(new ArrayAdapter<String>(AddVehicleActivity.this, android.R.layout.simple_spinner_dropdown_item, new String[]{responses[3]}));
                    style.setVisibility(View.VISIBLE);

                    if (responses[4] == null) {
                        query(engine);
                    } else {
                        engine.setAdapter(new ArrayAdapter<String>(AddVehicleActivity.this, android.R.layout.simple_spinner_dropdown_item, new String[]{responses[4]}));
                        engine.setVisibility(View.VISIBLE);
                    }

                    if (responses[5] == null) {
                        query(transmission);
                    } else {
                        transmission.setAdapter(new ArrayAdapter<String>(AddVehicleActivity.this, android.R.layout.simple_spinner_dropdown_item, new String[]{responses[5]}));
                        transmission.setVisibility(View.VISIBLE);
                    }
                }

                confirm.setEnabled(true);
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }
        }
    }

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
            lookupMap.put(spinner, new HashMap<String, String>());
            styleMap.put(spinner, new HashMap<String, String>());

            if (findViewById(R.id.loadingPanel).getVisibility() == View.GONE)
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        /**
         * Execute the rest api call in a background thread
         *
         * @return Arraylist of string objects
         */
        protected ArrayList<String> doInBackground(String... params) {
            String queryPrefix = params[0];
            String queries = params[1];
            String queryPostfix = params[2];
            String array = params[3];
            String displayName = params[4];
            String searchName = params[5];
            try {
                ArrayList<String> responses = new ArrayList<String>();
                String[] queryArray = queries.split(",");
                for (int i = 0; i < queryArray.length; i++) {
                    String query = queryArray[i];
                    URL url = new URL(EdmundsCodes.endpointVehicle + queryPrefix + query + queryPostfix + "?" + EdmundsCodes.format + EdmundsCodes.api_key);
                    Log.d("REST", url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    queryCounter++;
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
                        JSONArray responsesArray = new JSONObject(stringBuilder.toString()).getJSONArray(array);

                        for (int j = 0; j < responsesArray.length(); j++) {
                            JSONObject jsonObject = responsesArray.getJSONObject(j);
                            if (jsonObject.has(displayName) && jsonObject.has(searchName)) {

                                String spinnerDisplay = jsonObject.getString(displayName);
                                String spinnerSearch = jsonObject.getString(searchName);

                                //Only display unique responses in the spinner
                                if (!responses.contains(spinnerDisplay))
                                    responses.add(spinnerDisplay);

                                //Store the values to be used for this entry to look up further information
                                String currentSearchValues = lookupMap.get(spinner).get(spinnerDisplay);
                                if (currentSearchValues == null)
                                    lookupMap.get(spinner).put(spinnerDisplay, spinnerSearch);
                                else
                                    lookupMap.get(spinner).put(spinnerDisplay, currentSearchValues + "," + spinnerSearch);

                                //Store the styleIDs that correspond to this selection
                                if (styleMap.containsKey(spinner)) {
                                    String styleIDs = styleMap.get(spinner).get(spinnerDisplay);
                                    if (styleIDs == null)
                                        styleMap.get(spinner).put(jsonObject.getString(displayName), query);
                                    else
                                        styleMap.get(spinner).put(jsonObject.getString(displayName), styleIDs + "," + query);
                                }
                            }
                        }
                    } finally {
                        urlConnection.disconnect();
                        //work around to throttle connections
                        Thread.sleep(200);
                    }
                }
                return responses;
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

            Collections.sort(responses);
            spinner.setAdapter(new ArrayAdapter<String>(AddVehicleActivity.this, android.R.layout.simple_spinner_dropdown_item, responses));
            if (responses.size() > 0) {
                spinner.setVisibility(View.VISIBLE);
                spinner.setEnabled(true);
            }
            if (spinner == transmission)
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
                URL url = new URL(EdmundsCodes.endpointVehicle + params[0] + "?" + EdmundsCodes.format + EdmundsCodes.api_key);
                Log.d("REST", url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                queryCounter++;
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
                        //Find the style entry with matching ID value
                        if (jsonObject1.getString("id").equals(params[1])) {
                            yearid = jsonObject1.getJSONObject("year").getString("id");
                        }
                    }
                    //Lookup the mainteance information using the year ID
                    url = new URL(EdmundsCodes.endpointMaintenance + "actionrepository/findbymodelyearid?modelyearid=" + yearid + "&" + EdmundsCodes.format + EdmundsCodes.api_key);
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
            Toast.makeText(AddVehicleActivity.this, R.string.toast_vehicle_added, Toast.LENGTH_LONG).show();
            //TODO Should finish be used?
            Log.i("COUNTER", "REST API CALLS: " + queryCounter);
            finish();
        }
    }

    /**
     * Return the niceName value used for searching REST API from display name value for a given spinner
     */
    public String lookup(Spinner spinner) {
        return lookupMap.get(spinner).get(spinner.getSelectedItem());
    }

    /**
     * Find the first common value for style ID based on the engine and transmission selections
     */
    public String commonStyleID() {
        if (styleMap.get(engine).get(engine.getSelectedItem()) != null) {
            ArrayList<String> styles_engine = new ArrayList<String>(Arrays.asList(styleMap.get(engine).get(engine.getSelectedItem()).split(",")));
            ArrayList<String> styles_transmission = new ArrayList<String>(Arrays.asList(styleMap.get(transmission).get(transmission.getSelectedItem()).split(",")));
            for (String id : styles_engine) {
                if (styles_transmission.contains(id)) {
                    return id;
                }
            }
        }
        return styleMap.get(transmission).get(transmission.getSelectedItem()).split(",")[0];
    }

    /**
     * Execute the query to populate a spinner value
     */
    public void query(Spinner spinner) {
        if (spinner == make)
            new SpinnerQuery(spinner).execute(new String[]{"", EdmundsCodes.MAKES_QUERY, "", EdmundsCodes.MAKES_ARRAY, EdmundsCodes.MAKES_DISPLAY, EdmundsCodes.MAKES_ID});
        else if (spinner == model)
            new SpinnerQuery(spinner).execute(new String[]{lookup(make) + "/", EdmundsCodes.MODELS_QUERY, "", EdmundsCodes.MODELS_ARRAY, EdmundsCodes.MODELS_DISPLAY, EdmundsCodes.MODELS_ID});
        else if (spinner == year)
            new SpinnerQuery(spinner).execute(new String[]{lookup(make) + "/" + lookup(model) + "/", EdmundsCodes.YEARS_QUERY, "", EdmundsCodes.YEARS_ARRAY, EdmundsCodes.YEARS_DISPLAY, EdmundsCodes.YEARS_ID});
        else if (spinner == style)
            new SpinnerQuery(spinner).execute(new String[]{lookup(make) + "/" + lookup(model) + "/", lookup(year), "/" + EdmundsCodes.STYLES_QUERY, EdmundsCodes.STYLES_ARRAY, EdmundsCodes.STYLES_DISPLAY, EdmundsCodes.STYLES_ID});
        else if (spinner == engine)
            new SpinnerQuery(spinner).execute(new String[]{"styles/", lookup(style), "/" + EdmundsCodes.ENGINES_QUERY, EdmundsCodes.ENGINES_ARRAY, EdmundsCodes.ENGINES_DISPLAY, EdmundsCodes.ENGINES_ID});
        else if (spinner == transmission)
            new SpinnerQuery(spinner).execute(new String[]{"styles/", lookup(style), "/" + EdmundsCodes.TRANSMISSIONS_QUERY, EdmundsCodes.TRANSMISSIONS_ARRAY, EdmundsCodes.TRANSMISSIONS_DISPLAY, EdmundsCodes.TRANSMISSIONS_ID});
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
            findViewById(R.id.layout_spinners).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_mileage).setVisibility(View.GONE);
            cancel.setText(R.string.button_cancel);
            confirm.setText(R.string.button_next);
        }
    }

    /**
     * Confirm Button
     */
    public void confirm(View view) {

        if (state == 1) {
            state = 2;
            findViewById(R.id.layout_spinners).setVisibility(View.GONE);
            findViewById(R.id.layout_mileage).setVisibility(View.VISIBLE);
            cancel.setText(R.string.button_back);
            confirm.setText(R.string.button_confirm);
        } else if (state == 2) {
            GarageDataSource garageDataSource = new GarageDataSource(this);
            garageDataSource.open();

            //TODO proper lookup and storage of vehicle attributes
            String engineText = "";
            if (lookup(engine) != null)
                engineText = lookup(engine).split(",")[0];

            Vehicle vehicle = garageDataSource.insertVehicle((String) year.getSelectedItem(), (String) make.getSelectedItem(), (String) model.getSelectedItem(), (String) style.getSelectedItem(), engineText, (String) transmission.getSelectedItem(), mileageTotal.getText().toString(), mileageAnnual.getText().toString());
            VehicleMaintenanceQuery vehicleMaintenanceQuery = new VehicleMaintenanceQuery(garageDataSource, vehicle);
            vehicleMaintenanceQuery.execute(new String[]{lookup(make) + "/" + lookup(model) + "/" + lookup(year) + "/" + EdmundsCodes.STYLES_QUERY, commonStyleID()});
        }
    }
}