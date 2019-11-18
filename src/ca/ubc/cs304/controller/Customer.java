package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Customer {
    //Customer database actions as described in pdf will live here

    DatabaseConnectionHandler db;
    ResultSet vehiclesOfInterest; //Used for viewNumberOfVehicles' returned tuples

    //TODO on sql exception rollbackConnection method for db

    public Customer(DatabaseConnectionHandler db) {
        this.db = db;
        assert (db.connection!=null); //check for database login otherwise methods won't run
    }

    /*
    View the number of available vehicles for a specific car type, location, and time interval.
The user should be able to provide any subset of {car type, location, time interval} to
view the available vehicles. If the user provides no information, your application should
automatically return a list of all vehicles (at that branch) sorted in some reasonable way
for the user to peruse.



The actual number of available vehicles should be displayed. After seeing the number of
vehicles, there should be a way for the user to see the details of the available vehicles if
the user desires to do so (e.g., if the user clicks on the number of available vehicles, a list
with the vehicles’ details should be displayed).
     */

    //should return a number and have list of returned tuples ready to go?
    public int viewNumberOfVehicles(String type, String location, Date pickupDate, Date returnDate) throws SQLException {
        boolean[] entered = new boolean[4]; //bitwise array of what info is present
        entered[0] = !(type == null);
        entered[1] = !(location == null);
        entered[2] = !(pickupDate == null);
        entered[3] = !(returnDate == null);

        //TODO querry db with required info and save to results
        return countTuples(vehiclesOfInterest,"Rent"); //table to querry wrong?
    }

    private int countTuples(ResultSet results, String tableName) throws SQLException {
        Statement state = db.connection.createStatement();
        ResultSet count = state.executeQuery("SELECT DISTINCT Count(*) as C FROM " + tableName);
        return count.getInt("C");
    }

    /*
    Make a reservation. If a customer is new, add the customer’s details to the database.
(You may choose to have a different interface for this).



Upon successful completion, a confirmation number for the reservation should be
shown along with the details entered during the reservation. Refer to the project
description for details on what kind of information the user needs to provide when
making a reservation.



If the customer’s desired vehicle is not available, an appropriate error message should
be shown.
     */

    public void makeReservation(int driversLicense,String location,String vehicleType, Date pickupDate, Date returnDate) throws SQLException {
        if (checkForExistingCustomer(driversLicense) & viewNumberOfVehicles(vehicleType,location,pickupDate,returnDate)>0) { //vehicle exists
            //TODO add method to determine if view number of vehicles has a date within an interval, find date interval class/method?
        } else {
        }
    }

    //if customer is new create new customer before preceding with caller's procedure
    public boolean checkForExistingCustomer(int driversLicense) throws SQLException {
        String query = "SELECT DISTINCT COUNT(*) AS C FROM Customer WHERE DLICENSE = " + driversLicense;
        Statement search = db.connection.createStatement();
        ResultSet output = search.executeQuery(query);
        output.next(); //iterates through rows but there's only 1 row in the count
        int number = output.getInt("C");
        if (number < 1) {
            createNewCustomer(driversLicense);
        } else if (number != 1) {
            throw new IllegalStateException("More than one instance of the same customer is in db");
        }
        return true;
    }


    private void createNewCustomer(int driversLicense) throws SQLException {
        String[] newCustomerSet; //todo hook into gui and display popup with missing info
//        newCustomerSet = Gui.newCustomerDialog();
//        int cellphone = Integer.parseInt(newCustomerSet[0]);
//        String name = newCustomerSet[1];
//        String address = newCustomerSet[2];
//        createNewCustomer(cellphone,name,address,driversLicense);
    }

    private void createNewCustomer(int cellphone,String name, String address, int driversLicense) throws SQLException {
        String orderedValues = cellphone + "," + name + "," + address + "," + driversLicense;
        String statement = "INSERT INTO Customer VALUES " + orderedValues;
        PreparedStatement ps = db.connection.prepareStatement(statement);
        ps.executeUpdate();
        db.connection.commit();
    }

    private void handleErrors(Exception e) {
        try {
            db.connection.rollback();
            e.printStackTrace();
        } catch (SQLException b) { //You're in real trouble if this is called
            b.printStackTrace();
            e.printStackTrace();
        }
    }

}

