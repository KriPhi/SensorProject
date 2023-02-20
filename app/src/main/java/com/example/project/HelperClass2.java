package com.example.project;

public class HelperClass2 {

    String longi, lati, alti, device, algo, date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getAlti() {
        return alti;
    }

    public void setAlti(String alti) {
        this.alti = alti;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getAlgo() {
        return algo;
    }

    public void setAlgo(String algo) {
        this.algo = algo;
    }

    public HelperClass2(String longi, String lati, String alti, String device, String algo, String date) {
        this.date = date;
        this.longi = longi;
        this.lati = lati;
        this.alti = alti;
        this.device = device;
        this.algo = algo;
    }

    public HelperClass2() {
    }
}