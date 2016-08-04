package com.ssj.prototype.prototype.model.Edmunds;

import com.ssj.prototype.prototype.utils.JSONHelper;

import org.json.JSONObject;

/**
 * Created by PAUL on 6/11/2016.
 */
public class Style implements EdmundsAttribute {

    private String id;
    private String name;
    private String trim;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTrim() {
        return trim;
    }

    public Style(String id, String name, String trim) {
        this.id = id;
        this.name = name;
        this.trim = trim;
    }

    public Style(JSONObject jsonObject) {
        this.id = JSONHelper.getString(jsonObject, "id");
        this.name = JSONHelper.getString(jsonObject, "name");
        this.trim = JSONHelper.getString(jsonObject, "trim");
    }

    @Override
    public String displayValue() {
        return trim;
    }

    @Override
    public String searchValue() {
        return name;
    }
}
