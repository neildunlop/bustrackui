package com.pathfinder.bustrack.model;

import org.slf4j.LoggerFactory;

import java.util.*;

public class VehicleDao {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(VehicleDao.class);
    private static final List<Vehicle> DATA = new ArrayList<>();

    public static void add(Vehicle vehicle) {

        log.debug("Adding Vehicle...");
        DATA.add(vehicle);
    }

    public static Vehicle find(String chassisNumber) {
        return DATA.stream().filter(t -> t.getChassisnumber().equals(chassisNumber)).findFirst().orElse(null);
    }

    public static void update(Vehicle vehicle) {
        if(find(vehicle.getChassisnumber())!=null) {
            remove(vehicle.getChassisnumber());
            add(vehicle);
        }
    }

//    public static List<Vehicle> ofStatus(String statusString) {
//        return (statusString == null || statusString.isEmpty()) ? DATA : ofStatus(Status.valueOf(statusString.toUpperCase()));
//    }
//
//    public static List<Vehicle> ofStatus(Status status) {
//        return DATA.stream().filter(t -> t.getStatus().equals(status)).collect(Collectors.toList());
//    }

    public static void remove(String chassisNumber) {
        DATA.remove(find(chassisNumber));
    }

//    public static void removeCompleted() {
//        ofStatus(Status.COMPLETE).forEach(t -> VehicleDao.remove(t.getId()));
//    }
//
//    public static void toggleStatus(String id) {
//        find(id).toggleStatus();
//    }
//
//    public static void toggleAll(boolean complete) {
//        VehicleDao.all().forEach(t -> t.setStatus(complete ? Status.COMPLETE : Status.ACTIVE));
//    }

    public static List<Vehicle> all() {
        return DATA;
    }
}
