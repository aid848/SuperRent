package ca.ubc.cs304.model;

import java.sql.Timestamp;

public class Reservation {
    private int confNum;
    private String vTName;
    private int dLicense;
    private Timestamp pickupDate;
    private Timestamp returnDate;

    public int getConfNum() {
        return confNum;
    }

    public void setConfNum(int confNum) {
        this.confNum = confNum;
    }

    public String getvTName() {
        return vTName;
    }

    public void setvTName(String vTName) {
        this.vTName = vTName;
    }

    public int getdLicense() {
        return dLicense;
    }

    public void setdLicense(int dLicense) {
        this.dLicense = dLicense;
    }

    public Timestamp getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Timestamp pickupDate) {
        this.pickupDate = pickupDate;
    }

    public Timestamp getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }
}
