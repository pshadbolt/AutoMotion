package com.ssj.prototype.prototype.model.Edmunds;

import com.ssj.prototype.prototype.utils.JSONHelper;

import org.json.JSONObject;

/**
 * Created by PAUL on 6/11/2016.
 */
public class Model implements EdmundsAttribute {

    private String name;
    private String niceName;

    public Model(String name, String niceName) {
        this.name = name;
        this.niceName = niceName;
    }

    public Model(JSONObject jsonObject) {
        this.name = JSONHelper.getString(jsonObject, "name");
        this.niceName = JSONHelper.getString(jsonObject, "niceName");
    }

    @Override
    public String displayValue() {
        return name;
    }

    @Override
    public String searchValue() {
        return niceName;
    }
}
