package com.ssj.prototype.prototype.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PAUL on 6/18/2016.
 */
public class JSONHelper {

    public static String getString(JSONObject jsonObject, String attribute) {
        if (jsonObject != null && jsonObject.has(attribute))
            try {
                return jsonObject.getString(attribute);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return "";
    }

    public static int getInt(JSONObject jsonObject, String attribute) {
        if (jsonObject != null && jsonObject.has(attribute))
            try {
                return jsonObject.getInt(attribute);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return 0;
    }
}
