package com.ssj.prototype.prototype.model.Edmunds;

import com.ssj.prototype.prototype.utils.JSONHelper;

import org.json.JSONObject;

/**
 * Created by PAUL on 6/22/2016.
 */
public class MaintenanceAction {

    /**
     * 1	A-service schedule. This is the basic 1-year maintenance service that most cars get.
     * 2	B-service schedule. This is the extended maintenance service that comes after #1.
     * 3	This maintenance service takes place once at the exact value of the intervalMileage or intervalMonth, whichever comes first.
     * 4	This maintenance service takes place every intervalMileage or intervalMonth value, whichever comes first.
     * 5	This maintenance service takes place more frequently.
     * 6	This maintenance service takes place when the warning light indicates.
     * 7	Inspection I as recommended by the vehicle manufacturer.
     * 8	Inspection II as recommended by the vehicle manufacturer.
     * 9	**Second Inspection II **
     */

    private String id;
    private String engineCode;
    private String transmissionCode;
    private String intervalMileage;
    private String intervalMonth;
    private String frequency;
    private String action;
    private String item;
    private String itemDescription;
    private String laborUnits;
    private String partUnits;
    private String driveType;

    public String getEngineCode() {
        return engineCode;
    }

    public String getTransmissionCode() {
        return transmissionCode;
    }

    public String getIntervalMileage() {
        return intervalMileage;
    }

    public String getIntervalMonth() {
        return intervalMonth;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getAction() {
        return action;
    }

    public String getItem() {
        return item;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public MaintenanceAction(JSONObject jsonObject) {
        this.id = JSONHelper.getString(jsonObject, "id");
        this.engineCode = JSONHelper.getString(jsonObject, "engineCode");
        this.transmissionCode = JSONHelper.getString(jsonObject, "transmissionCode");
        this.intervalMileage = JSONHelper.getString(jsonObject, "intervalMileage");
        this.intervalMonth = JSONHelper.getString(jsonObject, "intervalMonth");
        this.frequency = JSONHelper.getString(jsonObject, "frequency");
        this.action = JSONHelper.getString(jsonObject, "action");
        this.item = JSONHelper.getString(jsonObject, "item");
        this.itemDescription = JSONHelper.getString(jsonObject, "itemDescription");
        this.laborUnits = JSONHelper.getString(jsonObject, "laborUnits");
        this.partUnits = JSONHelper.getString(jsonObject, "partUnits");
        this.driveType = JSONHelper.getString(jsonObject, "driveType");
    }


}
