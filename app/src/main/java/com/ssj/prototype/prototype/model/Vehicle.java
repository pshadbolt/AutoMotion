package com.ssj.prototype.prototype.model;

import android.database.Cursor;

/**
 * Created by shadbolt on 5/14/2016.
 */
public class Vehicle {

    private long id;
    private String make;
    private String model;
    private String year;
    private String style;
    private String engine;
    private String transmission;

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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public Vehicle() {

    }

    public Vehicle(long id, String year, String make, String model, String style, String engine, String transmission) {
        this.id = id;
        this.year = year;
        this.make = make;
        this.model = model;
        this.style = style;
        this.engine = engine;
        this.transmission = transmission;
    }

    public Vehicle(Cursor cursor) {
        this.id = cursor.getLong(0);
        this.year = cursor.getString(1);
        this.make = cursor.getString(2);
        this.model = cursor.getString(3);
        this.style = cursor.getString(4);
    }

    public String toString() {
        return this.year + "," + this.make + "," + this.model + "," + this.style;
    }
}
