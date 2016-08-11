package com.ssj.prototype.prototype.model.Edmunds;

import com.ssj.prototype.prototype.utils.JSONHelper;

import org.json.JSONObject;

/**
 * Created by PAUL on 6/11/2016.
 */
public class Transmission implements EdmundsAttribute {

    private String id;
    private String name;
    private String equipmentType;
    private String availability;
    private String automaticType;
    private String transmissionType;
    private int numberOfSpeeds;

    public String getTransmissionType(){
        return transmissionType;
    }

    public Transmission(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public Transmission(JSONObject jsonObject) {
        this.id = JSONHelper.getString(jsonObject, "id");
        this.name = JSONHelper.getString(jsonObject, "name");
        this.equipmentType = JSONHelper.getString(jsonObject, "equipmentType");
        this.availability = JSONHelper.getString(jsonObject, "availability");
        this.automaticType = JSONHelper.getString(jsonObject, "automaticType");
        this.transmissionType = JSONHelper.getString(jsonObject, "transmissionType");
        this.numberOfSpeeds = JSONHelper.getInt(jsonObject, "numberOfSpeeds");
    }

    @Override
    public String displayValue() {
        return transmissionType;
    }

    @Override
    public String searchValue() {
        return transmissionType;
    }
}
