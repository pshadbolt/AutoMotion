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

    public static final String MAKES_QUERY = "makes?";
    public static final String MAKES_ARRAY = "makes";
    public static final String MAKES_DISPLAY = "name";
    public static final String MAKES_ID = "niceName";

    public static final String MODELS_QUERY = "models?";
    public static final String MODELS_ARRAY = "models";
    public static final String MODELS_DISPLAY = "name";
    public static final String MODELS_ID = "niceName";

    public static final String YEARS_QUERY = "years?";
    public static final String YEARS_ARRAY = "years";
    public static final String YEARS_DISPLAY = "year";
    public static final String YEARS_ID = "year";

    public static final String STYLES_QUERY = "styles?";
    public static final String STYLES_ARRAY = "styles";
    public static final String STYLES_DISPLAY = "trim";
    public static final String STYLES_ID = "id";

    public static final String ENGINES_QUERY = "engines?";
    public static final String ENGINES_ARRAY = "engines";
    public static final String ENGINES_DISPLAY = "size";
    public static final String ENGINES_ID = "code";

    public static final String TRANSMISSIONS_QUERY = "transmissions?";
    public static final String TRANSMISSIONS_ARRAY = "transmissions";
    public static final String TRANSMISSIONS_DISPLAY = "transmissionType";
    public static final String TRANSMISSIONS_ID = "id";
}
