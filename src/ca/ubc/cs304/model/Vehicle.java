package ca.ubc.cs304.model;

public class Vehicle {
    private int vId;
    private int vLicense;
    private String make;
    private String model;
    private int year;
    private String color;
    private double odometer;
    private String status;
    private String vTName;
    private String location;
    private String city;

    public int getvId() {
        return vId;
    }

    public void setvId(int vId) {
        this.vId = vId;
    }

    public int getvLicense() {
        return vLicense;
    }

    public void setvLicense(int vLicense) {
        this.vLicense = vLicense;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVTName() {
        return vTName;
    }

    public void setVTName(String vTName) {
        this.vTName = vTName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
