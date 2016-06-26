package com.ssj.prototype.prototype.model.Edmunds;

/**
 * Created by shadbolt on 5/30/2016.
 */
public class EdmundsCodes {

    //Query information
    public static String endpointMaintenance = "https://api.edmunds.com/v1/api/maintenance/";
    public static String endpointVehicle = "https://api.edmunds.com/api/vehicle/v2/";
    public static String format = "fmt=json";
    public static String api_key = "&api_key=m6vz5qajjyxbctbehqtnguz2";

    public static final String MAKE_BASE = "make";
    public static final String MAKE_ARRAY = MAKE_BASE + "s";
    public static final String MAKE_QUERY = MAKE_ARRAY + "?";
    public static final String MAKE_DISPLAY = "name";
    public static final String MAKE_SEARCH = "niceName";

    public static final String MODEL_BASE = "model";
    public static final String MODEL_ARRAY = MODEL_BASE + "s";
    public static final String MODEL_QUERY = MODEL_ARRAY + "?";
    public static final String MODEL_DISPLAY = "name";
    public static final String MODEL_SEARCH = "niceName";

    public static final String YEAR_BASE = "year";
    public static final String YEAR_ARRAY = YEAR_BASE + "s";
    public static final String YEAR_QUERY = YEAR_ARRAY + "?";
    public static final String YEAR_DISPLAY = "year";
    public static final String YEAR_SEARCH = "year";

    public static final String STYLE_BASE = "style";
    public static final String STYLE_ARRAY = STYLE_BASE + "s";
    public static final String STYLE_QUERY = STYLE_ARRAY + "?";
    public static final String STYLE_DISPLAY = "trim";
    public static final String STYLE_SEARCH = "id";

    public static final String ENGINE_BASE = "engine";
    public static final String ENGINE_ARRAY = ENGINE_BASE+"s";
    public static final String ENGINE_QUERY = ENGINE_ARRAY +"?";
    public static final String ENGINE_DISPLAY = "size";
    public static final String ENGINE_SEARCH = "code";

    public static final String TRANSMISSION_BASE = "transmission";
    public static final String TRANSMISSION_ARRAY = TRANSMISSION_BASE+"s";
    public static final String TRANSMISSION_QUERY = TRANSMISSION_ARRAY+"?";
    public static final String TRANSMISSION_DISPLAY = "transmissionType";
    public static final String TRANSMISSION_SEARCH = "id";
}
