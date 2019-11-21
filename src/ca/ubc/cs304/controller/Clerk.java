package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.Card;
import ca.ubc.cs304.model.Reservation;
import ca.ubc.cs304.model.Vehicle;

import java.sql.*;
import java.util.Calendar;

public class Clerk {
    DatabaseConnectionHandler db;

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
