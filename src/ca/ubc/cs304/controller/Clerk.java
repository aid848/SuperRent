package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.*;

import java.sql.*;
import java.util.Calendar;

public class Clerk {
    private DatabaseConnectionHandler db;

    public Clerk(DatabaseConnectionHandler db) {
        this.db = db;
        assert (this.db.connection != null);
    }

    // inserts Rent tuple into Rent table and returns true if the insertion is successful, also changes vehicle status to "Rented"
    public ResultSet getRentalReceipt(Reservation reservation, Vehicle vehicle, Card card) throws SQLException {
        PreparedStatement statement = db.connection.prepareStatement("INSERT INTO Rent VALUES (?,?,?,?,?,?,?,?,?,?)");
        int rID = reservation.getConfNum() + 1000000;
        statement.setInt(1, rID);
        statement.setInt(2, vehicle.getvLicense());
        statement.setInt(3, reservation.getdLicense());
        statement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
        statement.setTimestamp(5, reservation.getReturnDate());
        statement.setDouble(6, vehicle.getOdometer());
        statement.setString(7, card.getCardName());
        statement.setLong(8, card.getCardNo());
        statement.setDate(9, card.getExpDate());
        statement.setInt(10, reservation.getConfNum());

        int rows = statement.executeUpdate();
        // db.connection.commit();
        // ResultSet result = statement.getResultSet();
        if (rows != 1) {
            throw new IllegalStateException("Failed to insert Rent tuple");
        }
        statement.close();

        // change vehicle's status to "Rented"
        setVehicleStatusToRented(vehicle);
        return getRentTuple(rID);
    }

    public ReturnReceipt returnVehicle(Return ret) throws SQLException {
//        // check if vehicle is a rental
//        PreparedStatement statement = db.connection.prepareStatement("SELECT * FROM RENT WHERE RID = ?");
//        statement.setInt(1, ret.getrID());
//        ResultSet result = statement.executeQuery();
//        statement.close();
//        if (!result.next()) {
//            throw new IllegalArgumentException("Vehicle was not previously rented");
//        }
//
        // calculate total cost of rental
        // get vehicle type associated with rental to access rates
        PreparedStatement s = db.connection.prepareStatement("SELECT r1.RID, r1.FROMDATETIME, vt.WRATE, vt.DRATE, vt.HRATE, vt.DIRATE, vt.HIRATE, vt.KRATE\n" +
                "FROM Rent r1, VEHICLE v, VEHICLETYPE vt WHERE r1.RID = ? AND r1.VLICENSE = v.VLICENSE AND v.VTNAME = vt.VTNAME");
        s.setInt(1, ret.getrID());
        ResultSet r = s.executeQuery();

        // build return value
        ReturnReceipt returnVal = new ReturnReceipt();
        if (r.next()) {
            returnVal.setrID(r.getInt(1));
            returnVal.setRentalDate(r.getTimestamp(2));
            returnVal.setReturnDate(ret.getReturnDateTime());
            returnVal.setElapsedWeeks(elapsedWeeks(returnVal.getRentalDate(), returnVal.getReturnDate()));
            returnVal.setElapsedDays(elapsedDays(returnVal.getRentalDate(), returnVal.getReturnDate()));
            returnVal.setElapsedHours(elapsedHours(returnVal.getRentalDate(), returnVal.getReturnDate()));
            returnVal.setWeeklyRate(r.getDouble(3));
            returnVal.setDailyRate(r.getDouble(4));
            returnVal.setHourlyRate(r.getDouble(5));
            returnVal.setDailyInsuranceRate(r.getDouble(6));
            returnVal.setHourlyInsuranceRate(r.getDouble(7));
            returnVal.setkRate(r.getDouble(8));
            returnVal.setTotal(calculateTotalCost(returnVal));
        } else {
            throw new IllegalArgumentException("Vehicle was not previously rented");
        }

        // insert tuple into Return table
        PreparedStatement i = db.connection.prepareStatement("INSERT INTO RETURN VALUES (?,?,?,?,?)");
        i.setInt(1, ret.getrID());
        i.setTimestamp(2, ret.getReturnDateTime());
        i.setFloat(3, ret.getOdometer());
        i.setInt(4, ret.getFulltank());
        i.setDouble(5, returnVal.getTotal());
        i.executeUpdate();
        i.close();

        // TODO: set vehicle's status to available

        return returnVal;
    }

    private double calculateTotalCost(ReturnReceipt r) {
        double weeklyCost = elapsedWeeks(r.getRentalDate(), r.getReturnDate()) * r.getWeeklyRate();
        double dailyCost = elapsedDays(r.getRentalDate(), r.getReturnDate()) * (r.getDailyRate() + r.getDailyInsuranceRate());
        double hourlyCost = elapsedHours(r.getRentalDate(), r.getReturnDate()) * (r.getHourlyRate() + r.getHourlyInsuranceRate());
        return weeklyCost + dailyCost + hourlyCost;
    }

    // gets number of weeks elapsed between the given times
    private int elapsedWeeks(Timestamp from, Timestamp to) {
        long ms1 = from.getTime();
        long ms2 = to.getTime();
        long diff = ms2 - ms1;
        return (int) (diff / (7 * 24 * 60 * 60 * 1000));
    }

    // gets remainder of days elapsed between the given times
    private int elapsedDays(Timestamp from, Timestamp to) {
        long ms1 = from.getTime();
        long ms2 = to.getTime();
        long diff = ms2 - ms1;
        return ((int) (diff / (24 * 60 * 60 * 1000))) % 7;
    }

    // gets remainder of hours elapsed between the given times
    private int elapsedHours(Timestamp from, Timestamp to) {
        long ms1 = from.getTime();
        long ms2 = to.getTime();
        long diff = ms2 - ms1;
        return ((int) (diff / (60 * 60 * 1000))) % 24;
    }

    private ResultSet getRentTuple(int rID) throws SQLException {
        PreparedStatement ps = db.connection.prepareStatement("SELECT * FROM RENT WHERE RID = ?");
        ps.setInt(1, rID);
        return ps.executeQuery();
    }

    private boolean setVehicleStatusToRented(Vehicle vehicle) throws SQLException {
        String query = "UPDATE VEHICLE SET STATUS = 'Reserved'";
        Statement update = db.connection.createStatement();
        ResultSet result = update.executeQuery(query);
        if (result.next()) {
            return true;
        } else {
            throw new IllegalStateException("Failed to update Vehicle status");
        }
    }

    private String buildRentalReceiptQuery(int confNum) {
        return "SELECT * FROM Reservation WHERE confno = " + confNum;
    }
}
