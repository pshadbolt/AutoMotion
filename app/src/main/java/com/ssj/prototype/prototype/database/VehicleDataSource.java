package com.ssj.prototype.prototype.database;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created by shadbolt on 5/18/2016.
 */
public class VehicleDataSource extends AsyncTask<String, Void, String> {

    private String endpoint = "https://api.edmunds.com/api/vehicle/v2/";
    private String format = "?fmt=jsonl";
    private String api_key = "&api_key=m6vz5qajjyxbctbehqtnguz2";

    protected String doInBackground(String... urls) {
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
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    public List<String> getMakes() {
        List<String> makes = new ArrayList<String>();
        execute(new String[]{"makes?"});
        try {
            String response = get();
            Log.d("REST", response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray makesArray = jsonObject.getJSONArray("makes");
            for (int i = 0; i < makesArray.length(); i++) {
                makes.add(makesArray.getJSONObject(i).getString("niceName"));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return makes;
    }

    public List<String> getModels(String make) {
        List<String> makes = new ArrayList<String>();
        //RetrieveFeedClass retrieveFeedClass = new RetrieveFeedClass();
        execute(new String[]{make + "/models?"});
        try {
            String response = get();
            Log.d("REST", response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray modelsArray = jsonObject.getJSONArray("models");
            for (int i = 0; i < modelsArray.length(); i++) {
                makes.add(modelsArray.getJSONObject(i).getString("niceName"));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return makes;
    }

    public List<String> getYears(String make, String model) {
        List<String> years = new ArrayList<String>();
        //RetrieveFeedClass retrieveFeedClass = new RetrieveFeedClass();
        execute(new String[]{make + "/" + model + "/years?"});
        try {
            String response = get();
            if (response == null)
                return years;
            Log.d("REST", response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray yearsArray = jsonObject.getJSONArray("years");
            for (int i = 0; i < yearsArray.length(); i++) {
                years.add(yearsArray.getJSONObject(i).getString("year"));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return years;
    }

    public List<String> getStyles(String make, String model, String year) {
        List<String> styles = new ArrayList<String>();
        //RetrieveFeedClass retrieveFeedClass = new RetrieveFeedClass();
        execute(new String[]{make + "/" + model + "/" + year + "/styles?"});
        try {
            String response = get();
            if (response == null)
                return styles;
            Log.d("REST", response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray yearsArray = jsonObject.getJSONArray("styles");
            for (int i = 0; i < yearsArray.length(); i++) {
                styles.add(yearsArray.getJSONObject(i).getString("name"));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return styles;
    }
}
