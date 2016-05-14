package com.ssj.prototype.prototype.model;

import android.database.Cursor;

/**
 * Created by shadbolt on 5/14/2016.
 */
public class Vehicle {
    private String year;
    private String make;
    private String model;
    private String trim;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTrim() {
        return trim;
    }

    public void setTrim(String trim) {
        this.trim = trim;
    }

    public Vehicle() {

    }

    public Vehicle(String year, String make, String model, String trim) {
        this.year = year;
        this.make = make;
        this.model = model;
        this.trim = trim;
    }

    public Vehicle(Cursor cursor) {
        this.year = cursor.getString(0);
        this.make = cursor.getString(1);
        this.model = cursor.getString(2);
        this.trim = cursor.getString(3);
    }

    public String toString() {
        return this.year + " " + this.make + " " + this.model + " " + this.trim;
    }
}
