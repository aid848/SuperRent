package ca.ubc.cs304.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Rental {
    private int rId;
    private int vLicense;
    private int dLicense;
    private Timestamp fromDateTime;
    private Timestamp toDateTime;
    private double odometer;
    private String cardName;
    private long cardNo;
    private Date expDate;
    private int confNo;

    public int getrId() {
        return rId;
    }

    public void setrId(int rId) {
        this.rId = rId;
    }

    public int getvLicense() {
        return vLicense;
    }

    public void setvLicense(int vLicense) {
        this.vLicense = vLicense;
    }

    public int getdLicense() {
        return dLicense;
    }

    public void setdLicense(int dLicense) {
        this.dLicense = dLicense;
    }

    public Timestamp getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(Timestamp fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public Timestamp getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(Timestamp toDateTime) {
        this.toDateTime = toDateTime;
    }

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public long getCardNo() {
        return cardNo;
    }

    public void setCardNo(long cardNo) {
        this.cardNo = cardNo;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public int getConfNo() {
        return confNo;
    }

    public void setConfNo(int confNo) {
        this.confNo = confNo;
    }
}
