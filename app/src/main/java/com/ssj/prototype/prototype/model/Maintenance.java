package com.ssj.prototype.prototype.model;

import com.ssj.prototype.prototype.model.Edmunds.MaintenanceAction;

/**
 * Created by shadbolt on 8/13/16.
 */
public class Maintenance {

    private long id;
    private Vehicle vehicle;
    private MaintenanceAction maintenanceAction;
    private int dueIn;

    public long getId() {
        return id;
    }

    public int getDueIn(){
        return dueIn;
    }

    public Maintenance(long id, Vehicle vehicle, MaintenanceAction maintenanceAction){
        this.id = id;
        this.vehicle = vehicle;
        this.maintenanceAction = maintenanceAction;
        this.dueIn = this.maintenanceAction.getIntervalMileage() - this.vehicle.getMileageTotal();
    }

    public String toString(){
        //Build the response string with , delimiter
        String response = this.maintenanceAction.getAction() + ": " + this.maintenanceAction.getItem() + ",,";
        response += this.vehicle.getYear() + " " + this.vehicle.getMake() + " " + this.vehicle.getModel() + ",,";
        if (this.dueIn < 0)
            response += "OVERDUE:\t\t\t" + (this.dueIn * -1);
        else
            response += "DUE:\t\t\t\t\t\t" + this.dueIn;
        return response;
    }
}
