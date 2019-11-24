package ca.ubc.cs304.model;

import java.sql.Timestamp;

// this class contains information that a clerk would input upon vehicle return
public class Return {
    private int rID;
    private Timestamp returnDateTime;
    private float odometer;
    private int fulltank;

    public int getrID() {
        return rID;
    }

    public void setrID(int rID) {
        this.rID = rID;
    }

    public Timestamp getReturnDateTime() {
        return returnDateTime;
    }

    public void setReturnDateTime(Timestamp returnDateTime) {
        this.returnDateTime = returnDateTime;
    }

    public float getOdometer() {
        return odometer;
    }

    public void setOdometer(float odometer) {
        this.odometer = odometer;
    }

    public int getFulltank() {
        return fulltank;
    }

    public void setFulltank(int fulltank) {
        this.fulltank = fulltank;
    }

}
