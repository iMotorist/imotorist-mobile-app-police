package com.madushanka.imotoristofficer.entities;

/**
 * Created by madushanka on 10/14/17.
 */

public class Driving_L {

    public Driving_L(String license_no) {
        this.license_no = license_no;
    }

    public Driving_L(String license_no, String issued_date, String expiry_date, String status) {
        this.license_no = license_no;
        this.issued_date = issued_date;
        this.expiry_date = expiry_date;
        this.status = status;
    }

    String license_no,issued_date,expiry_date,status;

    public String getLicense_no() {
        return license_no;
    }

    public void setLicense_no(String license_no) {
        this.license_no = license_no;
    }

    public String getIssued_date() {
        return issued_date;
    }

    public void setIssued_date(String issued_date) {
        this.issued_date = issued_date;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}