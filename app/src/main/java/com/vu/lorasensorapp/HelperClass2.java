package com.vu.lorasensorapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelperClass2 {

    private Date timeStamp;
    private double longitude;
    private double latitude;
    private double altitude;
    private int deviceID;
    private String algorithm;

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            this.timeStamp = dateFormat.parse(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public HelperClass2(Date timeStamp, double longitude, double latitude, double altitude, int deviceID, String algorithm) {
        this.timeStamp = timeStamp;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.deviceID = deviceID;
        this.algorithm = algorithm;
    }

    public HelperClass2() {
    }

    public HelperClass2(String timeStamp, double longitude, double latitude, double altitude, int deviceID, String algorithm) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            this.timeStamp = dateFormat.parse(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.deviceID = deviceID;
        this.algorithm = algorithm;
    }
}
