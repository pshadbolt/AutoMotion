package com.ssj.prototype.prototype.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ssj.prototype.prototype.model.Edmunds.Engine;
import com.ssj.prototype.prototype.model.Edmunds.MaintenanceAction;
import com.ssj.prototype.prototype.model.Edmunds.Make;
import com.ssj.prototype.prototype.model.Edmunds.Model;
import com.ssj.prototype.prototype.model.Edmunds.Style;
import com.ssj.prototype.prototype.model.Edmunds.Transmission;
import com.ssj.prototype.prototype.model.Edmunds.Year;
import com.ssj.prototype.prototype.model.Maintenance;
import com.ssj.prototype.prototype.model.Vehicle;

import java.util.ArrayList;

/**
 * Created by shadbolt on 5/14/2016.
 */
public class GarageDataSource {

    private Context context;

    // Database fields
    private SQLiteDatabase database;
    private GarageDataOpenHelper dbHelper;
    private String[] allGarageColumns = {GarageDataOpenHelper.COLUMN_GARAGE_ID, GarageDataOpenHelper.COLUMN_GARAGE_YEAR, GarageDataOpenHelper.COLUMN_GARAGE_MAKE, GarageDataOpenHelper.COLUMN_GARAGE_MODEL, GarageDataOpenHelper.COLUMN_GARAGE_STYLE_TRIM, GarageDataOpenHelper.COLUMN_GARAGE_ENGINE_CODE, GarageDataOpenHelper.COLUMN_GARAGE_ENGINE_HORSEPOWER, GarageDataOpenHelper.COLUMN_GARAGE_ENGINE_SIZE, GarageDataOpenHelper.COLUMN_GARAGE_TRANSMISSION_TYPE, GarageDataOpenHelper.COLUMN_GARAGE_MILEAGE_TOTAL, GarageDataOpenHelper.COLUMN_GARAGE_MILEAGE_ANNUAL};
    private String[] allMaintenanceColumns = {GarageDataOpenHelper.COLUMN_MAINTENANCE_ID, GarageDataOpenHelper.COLUMN_MAINTENANCE_VEHICLE_ID, GarageDataOpenHelper.COLUMN_MAINTENANCE_ENGINE_CODE, GarageDataOpenHelper.COLUMN_MAINTENANCE_TRANSMISSION_CODE, GarageDataOpenHelper.COLUMN_MAINTENANCE_FREQUENCY, GarageDataOpenHelper.COLUMN_MAINTENANCE_INTERVAL_MILEAGE, GarageDataOpenHelper.COLUMN_MAINTENANCE_ACTION, GarageDataOpenHelper.COLUMN_MAINTENANCE_ITEM, GarageDataOpenHelper.COLUMN_MAINTENANCE_ITEM_DESCRIPTION, GarageDataOpenHelper.COLUMN_MAINTENANCE_COMPLETED};

    public GarageDataSource(Context context) {
        this.context = context;
        dbHelper = new GarageDataOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void drop() throws SQLException {
        dbHelper.drop(database);
    }

    /**
     *
     */
    public Vehicle insertVehicle(Year year, Make make, Model model, Style style, Engine engine, Transmission transmission, String mileageTotal, String mileageAnnual) {
        ContentValues values = new ContentValues();
        values.put(GarageDataOpenHelper.COLUMN_GARAGE_YEAR, year.getYear());
        values.put(GarageDataOpenHelper.COLUMN_GARAGE_MAKE, make.getName());
        values.put(GarageDataOpenHelper.COLUMN_GARAGE_MODEL, model.getName());
        values.put(GarageDataOpenHelper.COLUMN_GARAGE_STYLE_TRIM, style.getTrim());
        values.put(GarageDataOpenHelper.COLUMN_GARAGE_ENGINE_CODE, engine.getCode());
        values.put(GarageDataOpenHelper.COLUMN_GARAGE_ENGINE_HORSEPOWER, engine.getHorsepower());
        values.put(GarageDataOpenHelper.COLUMN_GARAGE_ENGINE_SIZE, engine.getSize());
        values.put(GarageDataOpenHelper.COLUMN_GARAGE_TRANSMISSION_TYPE, transmission.getTransmissionType());
        values.put(GarageDataOpenHelper.COLUMN_GARAGE_MILEAGE_TOTAL, Integer.parseInt(mileageTotal));
        values.put(GarageDataOpenHelper.COLUMN_GARAGE_MILEAGE_ANNUAL, Integer.parseInt(mileageAnnual));
        Log.d("INSERT", values.toString());
        long id = database.insert(GarageDataOpenHelper.TABLE_NAME_GARAGE, null, values);
        return new Vehicle(id, year, make, model, style, engine, transmission);
    }

    /**
     *
     */
    public void insertMaintenance(Vehicle vehicle, String engineCode, String transmissionCode, int mileage, String frequency, String action, String item, String itemDescription) {
        ContentValues values = new ContentValues();
        values.put(GarageDataOpenHelper.COLUMN_MAINTENANCE_VEHICLE_ID, vehicle.getId());
        values.put(GarageDataOpenHelper.COLUMN_MAINTENANCE_ENGINE_CODE, engineCode);
        values.put(GarageDataOpenHelper.COLUMN_MAINTENANCE_TRANSMISSION_CODE, transmissionCode);
        values.put(GarageDataOpenHelper.COLUMN_MAINTENANCE_INTERVAL_MILEAGE, mileage);
        values.put(GarageDataOpenHelper.COLUMN_MAINTENANCE_FREQUENCY, Integer.parseInt(frequency));
        values.put(GarageDataOpenHelper.COLUMN_MAINTENANCE_ACTION, action);
        values.put(GarageDataOpenHelper.COLUMN_MAINTENANCE_ITEM, item);
        values.put(GarageDataOpenHelper.COLUMN_MAINTENANCE_ITEM_DESCRIPTION, itemDescription);
        database.insert(GarageDataOpenHelper.TABLE_NAME_MAINTENANCE, null, values);
    }

    public void completeMaintenance(long id) {
        ContentValues values = new ContentValues();
        values.put(GarageDataOpenHelper.COLUMN_MAINTENANCE_COMPLETED, "done");
        database.update(GarageDataOpenHelper.TABLE_NAME_MAINTENANCE, values, GarageDataOpenHelper.COLUMN_MAINTENANCE_ID + " = " + id, null);
    }

    /**
     * @param id
     */
    public void deleteVehicle(long id) {
        database.delete(GarageDataOpenHelper.TABLE_NAME_GARAGE, GarageDataOpenHelper.COLUMN_GARAGE_ID + " = " + id, null);
    }

    /**
     * @param id
     * @return
     */
    public MaintenanceAction getMaintenanceAction(long id) {
        MaintenanceAction maintenanceAction = null;
        Cursor cursor = database.query(GarageDataOpenHelper.TABLE_NAME_MAINTENANCE, allMaintenanceColumns, GarageDataOpenHelper.COLUMN_MAINTENANCE_ID + "=\'" + id + "\'", null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            maintenanceAction = new MaintenanceAction(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return maintenanceAction;
    }

    /**
     * @param id
     * @return
     */

    public Vehicle getVehicle(long id) {
        Vehicle vehicle = null;
        Cursor cursor = database.query(GarageDataOpenHelper.TABLE_NAME_GARAGE, allGarageColumns, GarageDataOpenHelper.COLUMN_GARAGE_ID + "=\'" + id + "\'", null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            vehicle = new Vehicle(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return vehicle;
    }

    /**
     * @param id
     * @return
     */
    public String getMileage(long id) {
        String response = "";

        //query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        Cursor cursor = database.query(GarageDataOpenHelper.TABLE_NAME_GARAGE, new String[]{GarageDataOpenHelper.COLUMN_GARAGE_MILEAGE_TOTAL, GarageDataOpenHelper.COLUMN_GARAGE_MILEAGE_ANNUAL}, GarageDataOpenHelper.COLUMN_MAINTENANCE_VEHICLE_ID + "=\'" + id + "\'", null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            response += "MILEAGE: " + cursor.getInt(0) + System.getProperty("line.separator");
            response += "ANNUAL: " + cursor.getInt(1) + System.getProperty("line.separator");
            cursor.moveToNext();
        }
        cursor.close();
        return response;
    }

    /**
     * @param id
     * @return
     */
    public String getMaintenanceSchedule(long id) {
        String response = "";

        //query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        Cursor cursor = database.query(GarageDataOpenHelper.TABLE_NAME_MAINTENANCE, allMaintenanceColumns, GarageDataOpenHelper.COLUMN_MAINTENANCE_VEHICLE_ID + " =\'" + id + "\'", null, null, null, GarageDataOpenHelper.COLUMN_MAINTENANCE_INTERVAL_MILEAGE + " ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            response += "MILEAGE:\t\t\t\t\t\t" + cursor.getString(4) + System.getProperty("line.separator");
            response += "FREQUENCY:\t\t\t\t" + cursor.getString(3) + System.getProperty("line.separator");
            response += "ENGINE:\t\t\t\t\t\t\t\t" + cursor.getString(1) + System.getProperty("line.separator");
            response += "TRANSMISSION:\t" + cursor.getString(2) + System.getProperty("line.separator");
            response += cursor.getString(6) + ": " + cursor.getString(7) + System.getProperty("line.separator");
            response += cursor.getString(8) + System.getProperty("line.separator") + System.getProperty("line.separator");
            cursor.moveToNext();
        }
        cursor.close();
        return response;
    }

    /**
     * @return
     */
    public ArrayList<Vehicle> getAllVehicle() {
        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

        Cursor cursor = database.query(GarageDataOpenHelper.TABLE_NAME_GARAGE, allGarageColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Vehicle vehicle = new Vehicle(cursor);
            vehicles.add(vehicle);
            cursor.moveToNext();
        }
        cursor.close();
        return vehicles;
    }

    /**
     * Return all maintenance that has not been marked completed
     */
    public ArrayList<Maintenance> getAllMaintenance() {
        ArrayList<Maintenance> maintenances = new ArrayList<>();

        Cursor cursor = database.query(GarageDataOpenHelper.TABLE_NAME_MAINTENANCE, allMaintenanceColumns, GarageDataOpenHelper.COLUMN_MAINTENANCE_COMPLETED + " is null", null, null, null, null);
        //Cursor cursor = database.query(GarageDataOpenHelper.TABLE_NAME_MAINTENANCE, allMaintenanceColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Maintenance maintenance = new Maintenance(cursor.getLong(0), getVehicle(cursor.getLong(1)), new MaintenanceAction(cursor));
            maintenances.add(maintenance);
            cursor.moveToNext();
        }
        cursor.close();
        return maintenances;
    }

    /**
     * @return
     */
    public String[] getAllMaintenanceActivities() {
        ArrayList<String> responses = new ArrayList<>();
        ArrayList<Integer> sort = new ArrayList<>();

        ArrayList<Vehicle> vehicles = getAllVehicle();
        for (Vehicle vehicle : vehicles) {

            //Set the mileage threshold to search
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            int threshold = settings.getInt("threshold", 10000);
            int low = vehicle.getMileageTotal() - threshold;
            int high = vehicle.getMileageTotal() + threshold;

            Log.d("QUERY", GarageDataOpenHelper.COLUMN_MAINTENANCE_INTERVAL_MILEAGE + " between " + low + " and " + high + " and " + GarageDataOpenHelper.COLUMN_MAINTENANCE_VEHICLE_ID + "=\'" + vehicle.getId() + "\'" + " and " + GarageDataOpenHelper.COLUMN_MAINTENANCE_ENGINE_CODE + "=\'" + vehicle.getEngine().getCode() + "\'" + " and (" + GarageDataOpenHelper.COLUMN_MAINTENANCE_TRANSMISSION_CODE + "=\'" + vehicle.getTransmission().getTransmissionType() + "\' OR " + GarageDataOpenHelper.COLUMN_MAINTENANCE_TRANSMISSION_CODE + "=\'ALL\'" + ")");
            Cursor cursor = database.query(GarageDataOpenHelper.TABLE_NAME_MAINTENANCE, allMaintenanceColumns, GarageDataOpenHelper.COLUMN_MAINTENANCE_INTERVAL_MILEAGE + " between " + low + " and " + high + " and " + GarageDataOpenHelper.COLUMN_MAINTENANCE_VEHICLE_ID + "=\'" + vehicle.getId() + "\'" + " and " + GarageDataOpenHelper.COLUMN_MAINTENANCE_ENGINE_CODE + "=\'" + vehicle.getEngine().getCode() + "\'" + " and (" + GarageDataOpenHelper.COLUMN_MAINTENANCE_TRANSMISSION_CODE + "=\'" + vehicle.getTransmission().getTransmissionType() + "\' OR " + GarageDataOpenHelper.COLUMN_MAINTENANCE_TRANSMISSION_CODE + "=\'ALL\'" + ")", null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                int intervalMileage = cursor.getInt(4);
                int dueIn = intervalMileage - vehicle.getMileageTotal();
                String action = cursor.getString(5);
                String item = cursor.getString(6);

                //Build the response string with , delimiter
                String response = action + ": " + item + ",,";
                response += vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel() + ",,";
                if (dueIn < 0)
                    response += "OVERDUE:\t\t\t" + (dueIn * -1);
                else
                    response += "DUE:\t\t\t\t\t\t" + dueIn;
                cursor.moveToNext();

                //Sort based on dueIn metric
                boolean added = false;
                for (int i = 0; i < responses.size(); i++) {
                    if (sort.get(i) < dueIn) continue;
                    sort.add(i, dueIn);
                    responses.add(i, response);
                    added = true;
                    break;
                }
                if (!added) {
                    sort.add(dueIn);
                    responses.add(response);
                }
            }
            cursor.close();
        }
        return responses.toArray(new String[responses.size()]);
    }
}