package ca.ubc.cs304.model;

import java.sql.Timestamp;

public class ReturnReceipt {
    private int rID;
    private Timestamp rentalDate;
    private Timestamp returnDate;
    private int elapsedWeeks;
    private int elapsedDays;
    private int elapsedHours;
    private double weeklyRate;
    private double dailyRate;
    private double hourlyRate;
    private double dailyInsuranceRate;
    private double hourlyInsuranceRate;
    private double kRate;
    private double total;

    public double getkRate() {
        return kRate;
    }

    public void setkRate(double kRate) {
        this.kRate = kRate;
    }

    public int getrID() {
        return rID;
    }

    public void setrID(int rID) {
        this.rID = rID;
    }

    public Timestamp getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Timestamp rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Timestamp getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }

    public int getElapsedWeeks() {
        return elapsedWeeks;
    }

    public void setElapsedWeeks(int elapsedWeeks) {
        this.elapsedWeeks = elapsedWeeks;
    }

    public int getElapsedDays() {
        return elapsedDays;
    }

    public void setElapsedDays(int elapsedDays) {
        this.elapsedDays = elapsedDays;
    }

    public int getElapsedHours() {
        return elapsedHours;
    }

    public void setElapsedHours(int elapsedHours) {
        this.elapsedHours = elapsedHours;
    }

    public double getWeeklyRate() {
        return weeklyRate;
    }

    public void setWeeklyRate(double weeklyRate) {
        this.weeklyRate = weeklyRate;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getDailyInsuranceRate() {
        return dailyInsuranceRate;
    }

    public void setDailyInsuranceRate(double dailyInsuranceRate) {
        this.dailyInsuranceRate = dailyInsuranceRate;
    }

    public double getHourlyInsuranceRate() {
        return hourlyInsuranceRate;
    }

    public void setHourlyInsuranceRate(double hourlyInsuranceRate) {
        this.hourlyInsuranceRate = hourlyInsuranceRate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
