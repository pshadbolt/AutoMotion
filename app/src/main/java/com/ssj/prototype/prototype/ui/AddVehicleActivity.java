package com.ssj.prototype.prototype.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.ssj.prototype.prototype.model.Edmunds.Engine;
import com.ssj.prototype.prototype.model.Edmunds.MaintenanceAction;
import com.ssj.prototype.prototype.model.Edmunds.Make;
import com.ssj.prototype.prototype.model.Edmunds.Model;
import com.ssj.prototype.prototype.model.Edmunds.Style;
import com.ssj.prototype.prototype.model.Edmunds.Transmission;
import com.ssj.prototype.prototype.model.Edmunds.Year;
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
import java.util.HashSet;

public class AddVehicleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //UI Elements
    protected Spinner make;
    protected Spinner model;
    protected Spinner year;
    protected Spinner style;
    protected Spinner engine;
    protected Spinner transmission;

    protected EditText mileageTotalText;
    protected EditText mileageAnnualText;

    protected Button cancel;
    protected Button confirm;

    //Data Elements
    private int queryCounter = 0;
    private int state = 1;
    private HashMap<Spinner, HashMap<String, JSONObject>> lookupMap;
    private HashMap<String, HashSet<String>> styleList;
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

        lookupMap = new HashMap<>();
        lookupMap.put(make, new HashMap<String, JSONObject>());
        lookupMap.put(model, new HashMap<String, JSONObject>());
        lookupMap.put(year, new HashMap<String, JSONObject>());
        lookupMap.put(style, new HashMap<String, JSONObject>());
        lookupMap.put(engine, new HashMap<String, JSONObject>());
        lookupMap.put(transmission, new HashMap<String, JSONObject>());
        styleList = new HashMap<>();
        styleMap = new HashMap<>();
        styleMap.put(engine, new HashMap<String, String>());
        styleMap.put(transmission, new HashMap<String, String>());

        mileageTotalText = (EditText) findViewById(R.id.editText1);
        mileageAnnualText = (EditText) findViewById(R.id.editText2);

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
        } else if (parent.getId() == model.getId()) {
            year.setVisibility(View.GONE);
            style.setVisibility(View.GONE);
            engine.setVisibility(View.GONE);
            transmission.setVisibility(View.GONE);
            query(year);
        } else if (parent.getId() == year.getId()) {
            style.setVisibility(View.GONE);
            engine.setVisibility(View.GONE);
            transmission.setVisibility(View.GONE);
            query(style);
        } else if (parent.getId() == style.getId()) {
            engine.setVisibility(View.GONE);
            transmission.setVisibility(View.GONE);
            query(engine);
            query(transmission);
        } else if (parent.getId() == engine.getId())
            findViewById(R.id.confirm).setEnabled(true);
        else if (parent.getId() == transmission.getId())
            findViewById(R.id.confirm).setEnabled(true);
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

                    responses[0] = new Make(jsonObject.getJSONObject(EdmundsCodes.MAKE_BASE)).displayValue();
                    lookupMap.get(make).put(responses[0], jsonObject.getJSONObject(EdmundsCodes.MAKE_BASE));

                    responses[1] = new Model(jsonObject.getJSONObject(EdmundsCodes.MODEL_BASE)).displayValue();
                    lookupMap.get(model).put(responses[1], jsonObject.getJSONObject(EdmundsCodes.MODEL_BASE));

                    responses[2] = new Year(jsonObject.getJSONArray(EdmundsCodes.YEAR_ARRAY).getJSONObject(0)).displayValue();
                    lookupMap.get(year).put(responses[2], jsonObject.getJSONArray(EdmundsCodes.YEAR_ARRAY).getJSONObject(0));

                    if (jsonObject.getJSONArray(EdmundsCodes.YEAR_ARRAY).getJSONObject(0).getJSONArray(EdmundsCodes.STYLE_ARRAY).getJSONObject(0).has(EdmundsCodes.STYLE_DISPLAY) && jsonObject.getJSONArray(EdmundsCodes.YEAR_ARRAY).getJSONObject(0).getJSONArray("styles").length() == 1) {
                        responses[3] = jsonObject.getJSONArray(EdmundsCodes.YEAR_ARRAY).getJSONObject(0).getJSONArray(EdmundsCodes.STYLE_ARRAY).getJSONObject(0).getString(EdmundsCodes.STYLE_DISPLAY);
                        lookupMap.get(style).put(responses[3], jsonObject.getJSONArray(EdmundsCodes.YEAR_ARRAY).getJSONObject(0).getJSONArray(EdmundsCodes.STYLE_ARRAY).getJSONObject(0));
                    }
                    if (jsonObject.getJSONObject(EdmundsCodes.ENGINE_BASE).has(EdmundsCodes.ENGINE_DISPLAY) && jsonObject.getJSONObject(EdmundsCodes.ENGINE_BASE).has(EdmundsCodes.ENGINE_SEARCH)) {
                        responses[4] = jsonObject.getJSONObject(EdmundsCodes.ENGINE_BASE).getString(EdmundsCodes.ENGINE_DISPLAY);
                        lookupMap.get(engine).put(responses[4], jsonObject.getJSONObject(EdmundsCodes.ENGINE_BASE));
                        styleMap.get(engine).put(responses[4], jsonObject.getJSONArray(EdmundsCodes.YEAR_ARRAY).getJSONObject(0).getJSONArray(EdmundsCodes.STYLE_ARRAY).getJSONObject(0).getString(EdmundsCodes.STYLE_SEARCH));
                    }
                    if (jsonObject.getJSONObject(EdmundsCodes.TRANSMISSION_BASE).has(EdmundsCodes.TRANSMISSION_DISPLAY) && jsonObject.getJSONObject(EdmundsCodes.TRANSMISSION_BASE).has(EdmundsCodes.TRANSMISSION_SEARCH)) {
                        responses[5] = jsonObject.getJSONObject(EdmundsCodes.TRANSMISSION_BASE).getString(EdmundsCodes.TRANSMISSION_DISPLAY);
                        lookupMap.get(transmission).put(responses[5], jsonObject.getJSONObject(EdmundsCodes.TRANSMISSION_BASE));
                        styleMap.get(transmission).put(responses[5], jsonObject.getJSONArray(EdmundsCodes.YEAR_ARRAY).getJSONObject(0).getJSONArray(EdmundsCodes.STYLE_ARRAY).getJSONObject(0).getString(EdmundsCodes.STYLE_SEARCH));
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
            lookupMap.put(spinner, new HashMap<String, JSONObject>());
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

                                //Store the values to be used for this entry to look up further information
                                lookupMap.get(spinner).put(spinnerDisplay, jsonObject);

                                //Only display unique responses in the spinner
                                if (!responses.contains(spinnerDisplay))
                                    responses.add(spinnerDisplay);

                                //Store the mulitple style ids for each trim type
                                if (spinner == style) {
                                    if (!styleList.containsKey(spinnerDisplay))
                                        styleList.put(spinnerDisplay, new HashSet<String>());
                                    styleList.get(spinnerDisplay).add(spinnerSearch);
                                }

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
    private class VehicleMaintenanceQuery extends AsyncTask<String, Void, ArrayList<MaintenanceAction>> {

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
        protected ArrayList<MaintenanceAction> doInBackground(String... params) {
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
                    JSONArray responsesArray = jsonObject.getJSONArray(EdmundsCodes.STYLE_ARRAY);
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

                    ArrayList<MaintenanceAction> response = new ArrayList<MaintenanceAction>();
                    jsonObject = new JSONObject(stringBuilder.toString());
                    responsesArray = jsonObject.getJSONArray("actionHolder");
                    for (int i = 0; i < responsesArray.length(); i++) {
                        response.add(new MaintenanceAction(responsesArray.getJSONObject(i)));
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
        protected void onPostExecute(ArrayList<MaintenanceAction> response) {
            super.onPostExecute(response);
            garageDataSource.open();
            for (int i = 0; i < response.size(); i++) {
                MaintenanceAction maintenanceAction = response.get(i);
                garageDataSource.insertMaintenance(vehicle, maintenanceAction.getEngineCode(), maintenanceAction.getTransmissionCode(), maintenanceAction.getIntervalMileage(), maintenanceAction.getFrequency(), maintenanceAction.getAction(), maintenanceAction.getItem(), maintenanceAction.getItemDescription());
            }
            garageDataSource.close();
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            Toast.makeText(AddVehicleActivity.this, R.string.toast_vehicle_added, Toast.LENGTH_LONG).show();
            Log.i("COUNTER", "REST API CALLS: " + queryCounter);
            finish();
        }
    }

    /**
     * Return the niceName value used for searching REST API from display name value for a given spinner
     */
    public String lookup(Spinner spinner) {
        if (spinner == make)
            return new Make(lookupMap.get(spinner).get(spinner.getSelectedItem())).searchValue();
        else if (spinner == model)
            return new Model(lookupMap.get(spinner).get(spinner.getSelectedItem())).searchValue();
        else if (spinner == year)
            return new Year(lookupMap.get(spinner).get(spinner.getSelectedItem())).searchValue();
        else
            return TextUtils.join(",", styleList.get(spinner.getSelectedItem()));
    }

    /**
     * Find the first common value for style ID based on the engine and transmission selections
     */
    public String commonStyleID() {

        String styleID=null;

        if (styleMap.get(engine).get(engine.getSelectedItem()) != null) {
            ArrayList<String> styles_engine = new ArrayList<String>(Arrays.asList(styleMap.get(engine).get(engine.getSelectedItem()).split(",")));
            ArrayList<String> styles_transmission = new ArrayList<String>(Arrays.asList(styleMap.get(transmission).get(transmission.getSelectedItem()).split(",")));
            for (String id : styles_engine) {
                if (styles_transmission.contains(id)) {
                    styleID= id;
                    break;
                }
            }
        }
        styleID = styleMap.get(transmission).get(transmission.getSelectedItem()).split(",")[0];
        return styleID;
    }

    /**
     * Execute the query to populate a spinner value
     */
    public void query(Spinner spinner) {
        if (spinner == make)
            new SpinnerQuery(spinner).execute(new String[]{"", EdmundsCodes.MAKE_QUERY, "", EdmundsCodes.MAKE_ARRAY, EdmundsCodes.MAKE_DISPLAY, EdmundsCodes.MAKE_SEARCH});
        else if (spinner == model)
            new SpinnerQuery(spinner).execute(new String[]{lookup(make) + "/", EdmundsCodes.MODEL_QUERY, "", EdmundsCodes.MODEL_ARRAY, EdmundsCodes.MODEL_DISPLAY, EdmundsCodes.MODEL_SEARCH});
        else if (spinner == year)
            new SpinnerQuery(spinner).execute(new String[]{lookup(make) + "/" + lookup(model) + "/", EdmundsCodes.YEAR_QUERY, "", EdmundsCodes.YEAR_ARRAY, EdmundsCodes.YEAR_DISPLAY, EdmundsCodes.YEAR_SEARCH});
        else if (spinner == style)
            new SpinnerQuery(spinner).execute(new String[]{lookup(make) + "/" + lookup(model) + "/", lookup(year), "/" + EdmundsCodes.STYLE_QUERY, EdmundsCodes.STYLE_ARRAY, EdmundsCodes.STYLE_DISPLAY, EdmundsCodes.STYLE_SEARCH});
        else if (spinner == engine)
            new SpinnerQuery(spinner).execute(new String[]{"styles/", lookup(style), "/" + EdmundsCodes.ENGINE_QUERY, EdmundsCodes.ENGINE_ARRAY, EdmundsCodes.ENGINE_DISPLAY, EdmundsCodes.ENGINE_SEARCH});
        else if (spinner == transmission)
            new SpinnerQuery(spinner).execute(new String[]{"styles/", lookup(style), "/" + EdmundsCodes.TRANSMISSION_QUERY, EdmundsCodes.TRANSMISSION_ARRAY, EdmundsCodes.TRANSMISSION_DISPLAY, EdmundsCodes.TRANSMISSION_SEARCH});
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

            //Error catching for empty fields
            String mileageTotal = mileageTotalText.getText().toString();
            String mileageAnnual = mileageAnnualText.getText().toString();
            if (mileageTotal.length() == 0)
                mileageTotal = "0";
            if (mileageAnnual.length() == 0)
                mileageAnnual = "0";

            GarageDataSource garageDataSource = new GarageDataSource(this);
            garageDataSource.open();

            //TODO resolve Style JSONObject
            Vehicle vehicle = garageDataSource.insertVehicle(new Year(lookupMap.get(year).get(year.getSelectedItem())), new Make(lookupMap.get(make).get(make.getSelectedItem())), new Model(lookupMap.get(model).get(model.getSelectedItem())), new Style(lookupMap.get(style).get(style.getSelectedItem())), new Engine(lookupMap.get(engine).get(engine.getSelectedItem())), new Transmission(lookupMap.get(transmission).get(transmission.getSelectedItem())), mileageTotal, mileageAnnual);
            VehicleMaintenanceQuery vehicleMaintenanceQuery = new VehicleMaintenanceQuery(garageDataSource, vehicle);
            vehicleMaintenanceQuery.execute(new String[]{lookup(make) + "/" + lookup(model) + "/" + lookup(year) + "/" + EdmundsCodes.STYLE_QUERY, commonStyleID()});

            garageDataSource.close();
        }
    }
}