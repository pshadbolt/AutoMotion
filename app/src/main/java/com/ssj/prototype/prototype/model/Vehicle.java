package com.ssj.prototype.prototype.model;

import android.database.Cursor;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by shadbolt on 5/14/2016.
 */
public class Vehicle {

    private long id;
    private String year;
    private String make;
    private String model;
    private String trim;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public Vehicle(int id, String year, String make, String model, String trim) {
        this.id = id;
        this.year = year;
        this.make = make;
        this.model = model;
        this.trim = trim;
    }

    public Vehicle(Cursor cursor) {
        this.id = cursor.getLong(0);
        this.year = cursor.getString(1);
        this.make = cursor.getString(2);
        this.model = cursor.getString(3);
        this.trim = cursor.getString(4);
    }

    public String toString() {
        return this.year + " " + this.make + " " + this.model + " " + this.trim;
    }
}
