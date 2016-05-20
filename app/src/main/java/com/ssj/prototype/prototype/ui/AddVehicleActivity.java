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
import java.util.List;

public class AddVehicleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private GarageDataSource garageDatasource;

    Spinner make;
    Spinner model;
    Spinner year;
    Spinner style;

    public class VehicleDataSource extends AsyncTask<String, Void, ArrayList<String>> {

        private String endpoint = "https://api.edmunds.com/api/vehicle/v2/";
        private String format = "?fmt=jsonl";
        private String api_key = "&api_key=m6vz5qajjyxbctbehqtnguz2";

        private Spinner spinner;

        public VehicleDataSource(Spinner spinner) {
            this.spinner = spinner;
        }

        @Override
        protected void onPreExecute() {
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected ArrayList<String> doInBackground(String... urls) {
            try {
                URL url = new URL(endpoint + urls[0] + format + api_key);
                Log.d("REST", url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    ArrayList<String> responses = new ArrayList<String>();
                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                    JSONArray responsesArray = jsonObject.getJSONArray(urls[1]);
                    for (int i = 0; i < responsesArray.length(); i++) {
                        responses.add(responsesArray.getJSONObject(i).getString(urls[2]));

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
        // Set the spinner values to the strings obtained from the REST API call
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            if (strings == null)
                strings = new ArrayList<>();
            spinner.setAdapter(new ArrayAdapter<String>(AddVehicleActivity.this, android.R.layout.simple_spinner_dropdown_item, strings));
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

        findViewById(R.id.cancel).setVisibility(View.VISIBLE);
        findViewById(R.id.confirm).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.spinner1).setVisibility(View.VISIBLE);
        query("makes?", "makes", "niceName", make);
    }

    @Override
    protected void onStop() {
        garageDatasource.close();
        super.onStop();
    }

    // Spinner Selector
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        if (parent.getId() == R.id.spinner1) {
            findViewById(R.id.spinner3).setVisibility(View.GONE);
            findViewById(R.id.spinner4).setVisibility(View.GONE);
            query(make.getSelectedItem() + "/models?", "models", "niceName", model);
            findViewById(R.id.spinner2).setVisibility(View.VISIBLE);
        }
        if (parent.getId() == R.id.spinner2) {
            findViewById(R.id.spinner4).setVisibility(View.GONE);
            query(make.getSelectedItem() + "/" + model.getSelectedItem() + "/years?", "years", "year", year);
            findViewById(R.id.spinner3).setVisibility(View.VISIBLE);
        }
        if (parent.getId() == R.id.spinner3) {
            query(make.getSelectedItem() + "/" + model.getSelectedItem() + "/" + year.getSelectedItem() + "/styles?", "styles", "name", style);
            findViewById(R.id.spinner4).setVisibility(View.VISIBLE);
        }
        if (parent.getId() == R.id.spinner4) {
            if (((String) style.getSelectedItem()).length() == 0)
                findViewById(R.id.spinner4).setVisibility(View.GONE);
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void cancel(View view) {
        finish();
    }

    public void confirm(View view) {
        Toast.makeText(this, "Vehicle Added To Garage", Toast.LENGTH_LONG).show();
        garageDatasource.insertVehicle(new Vehicle((String) year.getSelectedItem(), (String) make.getSelectedItem(), (String) model.getSelectedItem(), (String) style.getSelectedItem()));
        finish();
    }

    // Async call to refresh the spinner value
    public void query(String query, String array, String name, Spinner spinner) {
        VehicleDataSource vehicleDataSource = new VehicleDataSource(spinner);
        vehicleDataSource.execute(new String[]{query, array, name});
    }
}
