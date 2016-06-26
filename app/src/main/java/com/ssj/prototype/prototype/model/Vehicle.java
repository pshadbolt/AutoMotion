package com.ssj.prototype.prototype.model;

import android.database.Cursor;

import com.ssj.prototype.prototype.model.Edmunds.Engine;
import com.ssj.prototype.prototype.model.Edmunds.Make;
import com.ssj.prototype.prototype.model.Edmunds.Model;
import com.ssj.prototype.prototype.model.Edmunds.Style;
import com.ssj.prototype.prototype.model.Edmunds.Transmission;
import com.ssj.prototype.prototype.model.Edmunds.Year;

/**
 * Created by shadbolt on 5/14/2016.
 */
public class Vehicle {

    private long id;
    private Make make;
    private Model model;
    private Year year;
    private Style style;
    private Engine engine;
    private Transmission transmission;
    private int mileageTotal;
    private int mileageAnnual;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public Make getMake() {
        return make;
    }

    public void setMake(Make make) {
        this.make = make;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public int getMileageTotal() {
        return mileageTotal;
    }

    public void setMileageTotal(int mileageTotal) {
        this.mileageTotal = mileageTotal;
    }

    public int getMileageAnnual() {
        return mileageAnnual;
    }

    public void setMileageAnnual(int mileageAnnual) {
        this.mileageAnnual = mileageAnnual;
    }

    public Vehicle(long id, Year year, Make make, Model model, Style style, Engine engine, Transmission transmission) {
        this.id = id;
        this.year = year;
        this.make = make;
        this.model = model;
        this.style = style;
        this.engine = engine;
        this.transmission = transmission;
    }

    public Vehicle(long id, String year, String make, String model, String style, String engine, String transmission) {
        this.id = id;
        this.year = new Year(year);
        this.make = new Make(make, null);
        this.model = new Model(model, null);
        this.style = new Style(null, null, style);
        this.engine = new Engine(0, engine);
        this.transmission = new Transmission(transmission);
    }

    public Vehicle(Cursor cursor) {
        this.id = cursor.getLong(0);
        this.year = new Year(cursor.getString(1));
        this.make = new Make(cursor.getString(2), null);
        this.model = new Model(cursor.getString(3), null);
        this.style = new Style(null, null, cursor.getString(4));
        this.engine = new Engine(0, cursor.getString(5));
        this.transmission = new Transmission(cursor.getString(6));
        this.mileageTotal = cursor.getInt(7);
        this.mileageAnnual = cursor.getInt(8);
    }

    public String toString() {
        return this.year.displayValue() + "," + this.make.displayValue() + "," + this.model.displayValue() + "," + this.style.displayValue();
    }
}
