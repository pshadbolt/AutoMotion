package com.ssj.prototype.prototype.model.Edmunds;

import com.ssj.prototype.prototype.utils.JSONHelper;

import org.json.JSONObject;

/**
 * Created by PAUL on 6/11/2016.
 */
public class Year implements EdmundsAttribute {

    private String year;

    public Year(String year) {
        this.year = year;
    }

    public Year(JSONObject jsonObject) {
        this.year = JSONHelper.getString(jsonObject, "year");
    }

    @Override
    public String displayValue() {
        return year;
    }

    @Override
    public String searchValue() {
        return year;
    }
}
