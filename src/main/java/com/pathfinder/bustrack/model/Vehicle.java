package com.pathfinder.bustrack.model;

import lombok.NonNull;

public class Vehicle {

    @NonNull
    private String chassisnumber;
    @NonNull
    private String make;
    @NonNull
    private String model;
    @NonNull
    private String colour;
    @NonNull
    private int capacity;

    public String getChassisnumber() {
        return chassisnumber;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getColour() {
        return colour;
    }

    public int getCapacity() {
        return capacity;
    }


    public Vehicle() {
    }

    public Vehicle(String chassisnumber, String make, String model, String colour, int capacity) {
        this.chassisnumber = chassisnumber;
        this.make = make;
        this.model = model;
        this.colour = colour;
        this.capacity = capacity;
    }

    public Vehicle(String chassisnumber) {
        this.chassisnumber = chassisnumber;
    }
}
