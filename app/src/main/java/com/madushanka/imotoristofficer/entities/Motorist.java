package com.madushanka.imotoristofficer.entities;

import java.util.List;

/**
 * Created by madushanka on 10/14/17.
 */

public class Motorist {

    String id;
    String name;
    String nic;
    String date_of_birth;
    String email;
    String phone;
    Driving_L motorist;
    List<Offence> offence_list;
    String vehicle_number;
    String location;
    String location_lat;
    String location_lon;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Driving_L getMotorist() {
        return motorist;
    }

    public void setMotorist(Driving_L motorist) {
        this.motorist = motorist;
    }

    public List<Offence> getOffence_list() {
        return offence_list;
    }

    public void setOffence_list(List<Offence> offence_list) {
        this.offence_list = offence_list;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(String location_lat) {
        this.location_lat = location_lat;
    }

    public String getLocation_lon() {
        return location_lon;
    }

    public void setLocation_lon(String location_lon) {
        this.location_lon = location_lon;
    }
}
