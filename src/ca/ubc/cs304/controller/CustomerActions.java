package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.Branch;
import ca.ubc.cs304.model.Customer;

import java.sql.*;
import java.time.Period;


public class CustomerActions {
    //Customer database actions as described in pdf will live here

    DatabaseConnectionHandler db;
    ResultSet vehiclesOfInterest; //Used for viewNumberOfVehicles' returned tuples

    //TODO on sql exception rollbackConnection method for db

    public CustomerActions(DatabaseConnectionHandler db) {
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
    public int viewNumberOfVehicles(String type, String location, Timestamp pickupDate, Timestamp returnDate, Branch branch) throws SQLException {
        boolean[] entered = new boolean[5]; //bitwise array of what info is present
        entered[0] = !(type == null);
        entered[1] = !(location == null);
        entered[2] = !(pickupDate == null);
        entered[3] = !(returnDate == null);
        entered[4] = !(branch == null);
        String query = viewNumberOfVehiclesQueryGen(entered, type, location, pickupDate, returnDate, branch);
        Statement state = db.connection.createStatement();
        vehiclesOfInterest = state.executeQuery(query); //saves results to class var for later
        //TODO querry db with required info and save to results
        return countTuples(vehiclesOfInterest,"Rent"); //table to querry wrong?
    }

    //REQUIRES: viewNumberOfVehicles to have been run
    public String retrieveVehicles() throws SQLException {
        if (vehiclesOfInterest==null) {
            return "";
        }
        StringBuilder output = new StringBuilder();
        while(vehiclesOfInterest.next()) {
            output.append(vehiclesOfInterest.getStatement()); //test to see if this returns rows
        }
        return output.toString();
    }

    public String viewNumberOfVehiclesQueryGen(boolean[] entered,String type, String location, Timestamp pickupDate, Timestamp returnDate, Branch branch) {
            //TODO change v.license = r.license to left outer join!
            if (!entered[0]) { //no type
                if (!entered[1]) { //no location
                    if ((entered[2] & entered[3])) { //has time interval
                        return viewNumberOfVehiclesTimeIntervalOnly(pickupDate, returnDate);
                    } else { // no time interval
                        return viewNumberOfVehiclesBranchOnly(branch.getLocation(),branch.getCity());
                    }
                }else { // has location
                    if ((entered[2] & entered[3])) { //has time interval
                        return viewNumberOfVehiclesLocationAndInterval(location,pickupDate,returnDate);
                    } else { // no time interval
                        return viewNumberOfVehiclesLocationOnly(location);
                    }
                }
            }else { //has type
                if (!entered[1]) { //no location
                    if ((entered[2] & entered[3])) { //has time interval
                        return viewNumberOfVehiclesTypeAndInterval(type,pickupDate,returnDate);
                    } else { // no time interval
                        return viewNumberOfVehiclesType(type);
                    }
                } else { //has location
                    if ((entered[2] & entered[3])) { //has time interval
                        return viewNumberOfVehiclesTypeAndLocationAndTime(type,location,pickupDate,returnDate);
                    } else { // no time interval
                        return viewNumberOfVehiclesTypeAndLocation(type,location);
                    }
                }
            }
    }

    private String viewNumberOfVehiclesTypeAndLocationAndTime(String type, String location, Timestamp pickupDate, Timestamp returnDate) {
        return "SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v,vtname,v.location,v.city\n" +
                "FROM Vehicle v, Rent r\n" +
                "WHERE v.vlicense = r.vlicense AND "+ pickupDate.toString() + " > r.toDateTime AND " + returnDate.toString() +" < r.fromDateTIme AND NOT IN r AND v.type = "+ type + " AND v.location = " + location;
    }

    private String viewNumberOfVehiclesTypeAndInterval(String type, Timestamp pickupDate, Timestamp returnDate) {
        return "SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v,vtname,v.location,v.city\n" +
                "FROM Vehicle v, Rent r\n" +
                "WHERE v.vlicense = r.vlicense AND " + pickupDate.toString() + " > r.toDateTime AND "+ returnDate.toString() +" < r.fromDateTIme AND NOT IN r AND v.type = " + type;
    }

    private String viewNumberOfVehiclesTypeAndLocation(String type, String location) {
        return "SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v,vtname,v.location,v.city\n" +
                "FROM Vehicle v, Rent r\n" +
                "WHERE v.vlicense = r.vlicense AND NOT IN r AND v.type = " + type + " AND v.location = " + location;
    }

    private String viewNumberOfVehiclesType(String type) {
        return "SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v,vtname,v.location,v.city\n" +
                "FROM Vehicle v, Rent r\n" +
                "WHERE v.vlicense = r.vlicense AND NOT IN r AND v.type = " + type;
    }

    private String viewNumberOfVehiclesLocationOnly(String location) {
        return "SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v,vtname,v.location,v.city\n" +
                "FROM Vehicle v, Rent r\n" +
                "WHERE v.vlicense = r.vlicense AND NOT IN r AND v.location = " + location;
    }

    private String viewNumberOfVehiclesLocationAndInterval(String location, Timestamp pickupDate, Timestamp returnDate) {
        return "SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v,vtname,v.location,v.city\n" +
                "FROM Vehicle v, Rent r\n" +
                "WHERE v.vlicense = r.vlicense AND " + pickupDate.toString() + " > r.toDateTime AND " + returnDate.toString() + " < r.fromDateTIme AND v.location = " + location;
    }

    private String viewNumberOfVehiclesTimeIntervalOnly(Timestamp pickupDate, Timestamp returnDate) {
        // TODO verify date bounds used for all interval queries

        return "SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v,vtname,v.location,v.city\n" +
                "FROM Vehicle v, Rent r\n" +
                "WHERE v.vlicense = r.vlicense AND " + pickupDate.toString() + " > r.toDateTime AND " + returnDate.toString() + " < r.fromDateTIme\n";
    }

    private String viewNumberOfVehiclesBranchOnly(String location, String city) {
        return "SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v,vtname,v.location,v.city\n" +
                "FROM Vehicle v, Branch b, Rent r\n" +
                "WHERE v.location = b.location AND v.city = b.city AND b.city = " + city + " AND b.location = " + location + " AND r.vlicense = v.vlicense AND NOT IN(r)\n" +
                "Group by v.make";
    }

    public int countTuples(ResultSet results, String tableName) throws SQLException {
        Statement state = db.connection.createStatement();
        ResultSet count = state.executeQuery("SELECT DISTINCT Count(*) as C FROM " + tableName);
        count.next();
        return count.getInt("C");
    }

    public String[] vehiclesOfInterestIterator() {
        //return string array of all vehicles obtained by view number of vehicles
        //stub
        return null;
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

    public void makeReservation(int driversLicense,String location,String vehicleType, Timestamp pickupDate, Timestamp returnDate,Branch branch) throws SQLException {
        if (checkForExistingCustomer(driversLicense) & viewNumberOfVehicles(vehicleType,location,pickupDate,returnDate,branch)>0) { //vehicle exists
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
            createNewCustomer();
            return false;
        } else if (number > 1) {
            throw new IllegalStateException("More than one instance of the same customer is in db");
        }
        return true;
    }


    private void createNewCustomer() throws SQLException {
        Customer c = new Customer();
        //TODO hook into gui and prompt for customer details
        //Customer c = Gui.newCustomerDialogue();
        createNewCustomer(c);
    }

    private void createNewCustomer(Customer c) throws SQLException {
        String orderedValues = c.getCellphone() + "," + c.getName() + "," + c.getAddress() + "," + c.getDlicense();
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

