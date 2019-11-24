package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.*;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.*;
import java.util.Calendar;

public class Clerk {
    private DatabaseConnectionHandler db;

    public Clerk(DatabaseConnectionHandler db) {
        this.db = db;
        assert (this.db.connection != null);
    }

    // gets first available vehicle of given type at the given branch
    public Vehicle getAvailableVehicle(String vehicleType, Branch branch) throws SQLException {
        PreparedStatement statement = db.connection.prepareStatement("SELECT * FROM VEHICLE WHERE VTNAME = ? AND LOCATION = ? AND CITY = ? AND STATUS = 'Available'");
        statement.setString(1, vehicleType);
        statement.setString(2, branch.getLocation());
        statement.setString(3, branch.getCity());
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            if (result.getString("STATUS").equals("Available")) {
                Vehicle v = new Vehicle();
                v.setvId(result.getInt("VID"));
                v.setvLicense(result.getInt("VLICENSE"));
                v.setMake(result.getString("MAKE"));
                v.setModel(result.getString("MODEL"));
                v.setYear(result.getInt("YEAR"));
                v.setColor(result.getString("COLOR"));
                v.setOdometer(result.getFloat("ODOMETER"));
                v.setStatus(result.getString("STATUS"));
                v.setVTName(result.getString("VTNAME"));
                v.setLocation(result.getString("LOCATION"));
                v.setCity(result.getString("CITY"));
                return v;
            }
        }
        throw new IllegalArgumentException("No vehicles are currently available to rent at this location");
    }

    // gets Reservation associated with the given reservation confirmation #
    public Reservation getReservation(int confirmationNumber) throws SQLException {
        PreparedStatement statement = db.connection.prepareStatement("SELECT * FROM RESERVATION WHERE RID = ?");
        statement.setInt(1, confirmationNumber);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            Reservation r = new Reservation();
            r.setConfNum(result.getInt("RID"));
            r.setvTName(result.getString("VTNAME"));
            r.setdLicense(result.getInt("DLICENSE"));
            r.setPickupDate(result.getTimestamp("FROMDATETIME"));
            r.setReturnDate(result.getTimestamp("TODATETIME"));
            return r;
        } else {
            throw new IllegalArgumentException("Reservation does not exist");
        }
    }

    // inserts Rent tuple into Rent table and returns true if the insertion is successful, also changes vehicle status to "Rented"
    public Rental getRentalReceipt(Reservation reservation, Vehicle vehicle, Card card) throws SQLException {
        if (vehicleAlreadyRented(vehicle)) {
            throw new IllegalArgumentException("Vehicle is already rented");
        }
        if (rentalMadeForReservation(reservation)) {
            throw new IllegalArgumentException("This reservation was already used to rent a vehicle");
        }
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
        db.connection.commit();
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
        // check if vehicle was returned already
        PreparedStatement ps = db.connection.prepareStatement("SELECT * FROM RETURN WHERE RID = ?");
        ps.setInt(1, ret.getrID());
        ResultSet result = ps.executeQuery();
        db.connection.commit();
        if (result.next()) {
            throw new IllegalArgumentException("Vehicle was already returned");
        }
//
        // calculate total cost of rental
        // get vehicle type associated with rental to access rates
        PreparedStatement s = db.connection.prepareStatement("SELECT r1.RID, r1.FROMDATETIME, vt.WRATE, vt.DRATE, vt.HRATE, vt.DIRATE, vt.HIRATE, vt.KRATE, v.ODOMETER\n" +
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
            returnVal.setElapsedDays(elapsedDaysRemainder(returnVal.getRentalDate(), returnVal.getReturnDate()));
            returnVal.setElapsedHours(elapsedHoursRemainder(returnVal.getRentalDate(), returnVal.getReturnDate()));
            returnVal.setWeeklyRate(r.getDouble(3));
            returnVal.setDailyRate(r.getDouble(4));
            returnVal.setHourlyRate(r.getDouble(5));
            returnVal.setDailyInsuranceRate(r.getDouble(6));
            returnVal.setHourlyInsuranceRate(r.getDouble(7));
            returnVal.setkRate(r.getDouble(8));
            returnVal.setStartOdometer(r.getDouble(9));
            returnVal.setEndOdometer(ret.getOdometer());
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
        db.connection.commit();
        i.close();

        // get vehicle license associated with rental
        PreparedStatement state = db.connection.prepareStatement("SELECT v.VLICENSE FROM VEHICLE v, RENT rent " +
                                                                        "WHERE v.VLICENSE = rent.VLICENSE AND rent.RID = ?");
        state.setInt(1, ret.getrID());
        ResultSet rs = state.executeQuery();
        int vLicense;
        if (rs.next()) {
            vLicense = rs.getInt("VLICENSE");
        } else {
            throw new IllegalStateException("Error finding vehicle associated with this rental");
        }
        PreparedStatement p = db.connection.prepareStatement("UPDATE VEHICLE SET STATUS = 'Available' WHERE VLICENSE = ?");
        p.setInt(1, vLicense);
        int res = p.executeUpdate();
        db.connection.commit();
        if (res != 1) {
            throw new IllegalStateException("Failed to update Vehicle status to Available");
        }
        return returnVal;
    }

    // returns true if a reservation was already made into a rental
    private boolean rentalMadeForReservation(Reservation res) throws SQLException {
        PreparedStatement ps = db.connection.prepareStatement("SELECT * FROM RENT WHERE CONFNO = ?");
        ps.setInt(1, res.getConfNum());
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    // returns true if a vehicle is 'Rented'
    private boolean vehicleAlreadyRented(Vehicle vehicle) throws SQLException {
        PreparedStatement ps = db.connection.prepareStatement("SELECT * FROM VEHICLE WHERE VLICENSE = ? AND STATUS = 'Rented'");
        ps.setInt(1, vehicle.getvLicense());
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    private double calculateTotalCost(ReturnReceipt r) {
        double weeklyCost = elapsedWeeks(r.getRentalDate(), r.getReturnDate()) * r.getWeeklyRate();
        double dailyCost = (elapsedDaysRemainder(r.getRentalDate(), r.getReturnDate()) * r.getDailyRate())
                        + (elapsedDays(r.getRentalDate(), r.getReturnDate()) * r.getDailyInsuranceRate());
        double hourlyCost = elapsedHoursRemainder(r.getRentalDate(), r.getReturnDate()) * (r.getHourlyRate() + r.getHourlyInsuranceRate());
        System.out.println("weekly cost: " + weeklyCost + " daily cost: " + dailyCost + " hourly cost: " + hourlyCost + "\n");
        System.out.println("elapsed weeks: " + elapsedWeeks(r.getRentalDate(), r.getReturnDate()) + " elapsed days remainder: " + elapsedDaysRemainder(r.getRentalDate(), r.getReturnDate())
                            + " total elapsed days: " + elapsedDays(r.getRentalDate(), r.getReturnDate()) + " elapsed hours remainder: " + elapsedHoursRemainder(r.getRentalDate(), r.getReturnDate()));
        double kmCost = (r.getEndOdometer() - r.getStartOdometer()) * r.getkRate();
        return weeklyCost + dailyCost + hourlyCost + kmCost;
    }

    // gets number of weeks elapsed between the given times
    private int elapsedWeeks(Timestamp from, Timestamp to) {
        long ms1 = from.getTime();
        long ms2 = to.getTime();
        long diff = ms2 - ms1;
        return (int) (diff / (7 * 24 * 60 * 60 * 1000));
    }

    // gets remainder of days elapsed between the given times
    private int elapsedDaysRemainder(Timestamp from, Timestamp to) {
        long ms1 = from.getTime();
        long ms2 = to.getTime();
        long diff = ms2 - ms1;
        return ((int) (diff / (24 * 60 * 60 * 1000))) % 7;
    }

    // gets total number of days elapsed between the given times
    private int elapsedDays(Timestamp from, Timestamp to) {
        long ms1 = from.getTime();
        long ms2 = to.getTime();
        long diff = ms2 - ms1;
        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    // gets remainder of hours elapsed between the given times
    private int elapsedHoursRemainder(Timestamp from, Timestamp to) {
        long ms1 = from.getTime();
        long ms2 = to.getTime();
        long diff = ms2 - ms1;
        return ((int) (diff / (60 * 60 * 1000))) % 24;
    }

    private Rental getRentTuple(int rID) throws SQLException {
        PreparedStatement ps = db.connection.prepareStatement("SELECT * FROM RENT WHERE RID = ?");
        ps.setInt(1, rID);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            Rental r = new Rental();
            r.setrId(result.getInt("RID"));
            r.setvLicense(result.getInt("VLICENSE"));
            r.setdLicense(result.getInt("DLICENSE"));
            r.setFromDateTime(result.getTimestamp("FROMDATETIME"));
            r.setToDateTime(result.getTimestamp("TODATETIME"));
            r.setOdometer(result.getFloat("ODOMETER"));
            r.setCardName(result.getString("CARDNAME"));
            r.setCardNo(result.getLong("CARDNO"));
            r.setExpDate(result.getDate("EXPDATE"));
            r.setConfNo(result.getInt("CONFNO"));
            return r;
        } else {
            throw new IllegalArgumentException("Rental with given ID does not exist");
        }
    }

    private boolean setVehicleStatusToRented(Vehicle vehicle) throws SQLException {
        PreparedStatement statement = db.connection.prepareStatement("UPDATE VEHICLE SET STATUS = 'Rented' WHERE VLICENSE = ?");
        statement.setInt(1, vehicle.getvLicense());
        int result = statement.executeUpdate();
        db.connection.commit();
        if (result == 1) {
            return true;
        } else {
            throw new IllegalStateException("Failed to update Vehicle status");
        }
    }

    private String buildRentalReceiptQuery(int confNum) {
        return "SELECT * FROM Reservation WHERE confno = " + confNum;
    }
}
