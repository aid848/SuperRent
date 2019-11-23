package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.Branch;
import ca.ubc.cs304.model.Customer;
import ca.ubc.cs304.ui.GuiMain;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;


public class CustomerActions {
    //Customer database actions as described in pdf lives here
    DatabaseConnectionHandler db;
    ResultSet vehiclesOfInterest; //Used for viewNumberOfVehicles' returned tuples


    //Caller of all methods must handle SQLException
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

    // returns number of vehicles available for rent at the given branch for the given vehicle type
    private int getNumberOfVehicles(String vType, Branch branch) throws SQLException {
        PreparedStatement statement = db.connection.prepareStatement("SELECT COUNT(*) FROM VEHICLE WHERE LOCATION = ? AND CITY = ? AND VTNAME = ? AND STATUS = 'Available'");
        statement.setString(1, branch.getLocation());
        statement.setString(2, branch.getCity());
        statement.setString(3, vType);
        ResultSet r = statement.executeQuery();
        if (r.next()) {
            return r.getInt(1);
        }
        throw new IllegalArgumentException("No vehicles of this type are available to rent at this location");
    }

    //should return a number and have list of returned tuples ready to go
    public int viewNumberOfVehicles(String type, String location, LocalDateTime pickupDate, LocalDateTime returnDate, Branch branch) throws SQLException {
        boolean[] entered = new boolean[5]; //bitwise array of what info is present
        entered[0] = !(type == null);
        entered[1] = !(location == null);
        entered[2] = !(pickupDate == null);
        entered[3] = !(returnDate == null);
        entered[4] = !(branch == null);
        String query = viewNumberOfVehiclesQueryGen(entered, type, location, pickupDate, returnDate, branch);
        Statement state = db.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        vehiclesOfInterest = state.executeQuery(query); //saves results to class var for later
        return countTuples(vehiclesOfInterest); //table to query wrong?
    }

    //REQUIRES: viewNumberOfVehicles to have been run
    //Just shows names of vehicles returned from viewNumberOfVehicles
    public String retrieveVehicles() throws SQLException { //rework to gui elements with vid embedded for identifying what the user selects
        if (vehiclesOfInterest==null) {
            return "";
        }
        vehiclesOfInterest.beforeFirst();
        StringBuilder output = new StringBuilder();
        while (vehiclesOfInterest.next()) {
            //SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v.vtname,v.location,v.city
            output.append(vehiclesOfInterest.getString(1)).append(" ").append(vehiclesOfInterest.getString(2) + " ").append(vehiclesOfInterest.getString(3)).append(" ").append(vehiclesOfInterest.getString(4) + " ").append(vehiclesOfInterest.getString(5)).append(" ").append(vehiclesOfInterest.getString(6) + " ").append(vehiclesOfInterest.getString(7)).append(" ").append(vehiclesOfInterest.getString(8) + " ").append(vehiclesOfInterest.getString(9)).append(" ").append(vehiclesOfInterest.getString(10)).append("\n");
        }


        return output.toString();
    }

    private String viewNumberOfVehiclesQueryGen(boolean[] entered, String type, String location, LocalDateTime pickupDate, LocalDateTime returnDate, Branch branch) {
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

    private String viewNumberOfVehiclesTypeAndLocationAndTime(String type, String location, LocalDateTime pickupDate, LocalDateTime returnDate) {
        return String.format("SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v.vtname,v.location,v.city " +
                "FROM Vehicle v " +
                "left outer join RENT R on v.VLICENSE = R.VLICENSE " +
                "where (((v.VLICENSE is not null AND r.VLICENSE is null) OR R.VLICENSE in ( " +
                "    Select r.VLICENSE " +
                "    From RENT r " +
                "    WHERE ((%s < r.FROMDATETIME) OR (%s >r.TODATETIME))))) AND v.VTNAME = '%s' AND v.LOCATION = '%s'",dateTimeToOracle(returnDate),dateTimeToOracle(pickupDate),type,location);
    }

    private String viewNumberOfVehiclesTypeAndInterval(String type, LocalDateTime pickupDate, LocalDateTime returnDate) {
        return String.format("SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v.vtname,v.location,v.city " +
                "FROM Vehicle v " +
                "left outer join RENT R on v.VLICENSE = R.VLICENSE " +
                "where (((v.VLICENSE is not null AND r.VLICENSE is null) OR R.VLICENSE in ( " +
                "    Select r.VLICENSE " +
                "    From RENT r " +
                "    WHERE ((%s < r.FROMDATETIME) OR (%s >r.TODATETIME))))) AND v.VTNAME = '%s'",dateTimeToOracle(returnDate),dateTimeToOracle(pickupDate),type);
    }

    private String viewNumberOfVehiclesTypeAndLocation(String type, String location) {
        return String.format("SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v.vtname,v.location,v.city\n" +
                "FROM Vehicle v " +
                "left outer join RENT R on v.VLICENSE = R.VLICENSE " +
                "WHERE v.VTNAME = '%s' AND v.location = '%s' And v.VLICENSE is not null AND r.VLICENSE is null",type,location);
    }

    private String viewNumberOfVehiclesType(String type) {
        return String.format("SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v.vtname,v.location,v.city " +
                "FROM Vehicle v " +
                "left outer join RENT R on v.VLICENSE = R.VLICENSE " +
                "WHERE v.VTNAME = '%s' And v.VLICENSE is not null AND r.VLICENSE is null",type);
    }

    private String viewNumberOfVehiclesLocationOnly(String location) {
        return String.format("SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v.vtname,v.location,v.city " +
                "FROM Vehicle v " +
                "left outer join RENT R on v.VLICENSE = R.VLICENSE " +
                "WHERE v.LOCATION = '%s' And v.VLICENSE is not null AND r.VLICENSE is null",location);
    }

    private String viewNumberOfVehiclesLocationAndInterval(String location, LocalDateTime pickupDate, LocalDateTime returnDate) {
        return String.format("SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v.vtname,v.location,v.city \n" +
                "FROM Vehicle v left outer join RENT R on v.VLICENSE = R.VLICENSE  " +
                "where ((v.VLICENSE is not null AND r.VLICENSE is null) OR R.VLICENSE in ( " +
                "     Select r.VLICENSE " +
                "From RENT r " +
                " WHERE (( %s < r.FROMDATETIME) OR ( %s > r.TODATETIME)))) " +
                "  AND v.LOCATION = '%s'",dateTimeToOracle(returnDate),dateTimeToOracle(pickupDate),location);
    }

    private String viewNumberOfVehiclesTimeIntervalOnly(LocalDateTime pickupDate, LocalDateTime returnDate) {
        return String.format("SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v.vtname,v.location,v.city " +
                "FROM Vehicle v " +
                "left outer join RENT R on v.VLICENSE = R.VLICENSE " +
                "where ((v.VLICENSE is not null AND r.VLICENSE is null) OR R.VLICENSE in ( " +
                "    Select r.VLICENSE " +
                "    From RENT r " +
                "    WHERE (( %s < r.FROMDATETIME) OR ( %s > r.TODATETIME))))",dateTimeToOracle(returnDate),dateTimeToOracle(pickupDate));

    }

    private String viewNumberOfVehiclesBranchOnly(String location, String city) {
        return String.format("SELECT v.vid,v.vlicense, v.make,v.model,v.year,v.color,v.odometer,v.vtname,v.location,v.city " +
                "FROM Vehicle v " +
                "left outer join RENT R on v.VLICENSE = R.VLICENSE " +
                "WHERE v.LOCATION = '%s' AND v.CITY = '%s' AND v.VLICENSE is not null AND r.VLICENSE is null " +
                "Order by v.MAKE",location,city);
    }

    public int countTuples(String tableName) throws SQLException {
        Statement state = db.connection.createStatement();
        ResultSet count = state.executeQuery("SELECT DISTINCT Count(*) as C FROM " + tableName);
        count.next();
        return count.getInt("C");
    }

    public int countTuples(ResultSet results) throws SQLException {
        int count = 0;
        while(results.next()) {
            count++;
        }
        return count;
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

    public boolean makeReservationCheck(int driversLicense,String location,String vehicleType, LocalDateTime pickupDate, LocalDateTime returnDate,Branch branch) throws SQLException {
        // int hits = viewNumberOfVehicles(vehicleType,location,pickupDate,returnDate,branch);
        int hits = getNumberOfVehicles(vehicleType, branch);
        if (hits>0) { //vehicle exists
            if(!checkForExistingCustomer(driversLicense)) {
                createNewCustomer(driversLicense);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean makeReservationCheck(GuiMain g,int driversLicense,String location,String vehicleType, LocalDateTime pickupDate, LocalDateTime returnDate,Branch branch) throws SQLException {
        int hits = viewNumberOfVehicles(vehicleType,location,pickupDate,returnDate,branch);
        if (hits>0) { //vehicle exists
            if(!checkForExistingCustomer(driversLicense)) {
                createNewCustomer(g);
            }
            return true;
        } else {
            return false;
        }
    }

    public int makeReservation(String vehicleTypeName ,int driversLicense,String location, LocalDateTime fromDateTime,LocalDateTime toDateTime, Branch branch) throws SQLException {
        int confirmationNumber = Math.abs((int)System.currentTimeMillis());
        if (makeReservationCheck(driversLicense,location,vehicleTypeName,fromDateTime,toDateTime,branch)) {
            String statement = "INSERT INTO Reservation VALUES (" + confirmationNumber + ",'" + vehicleTypeName + "'," + driversLicense + "," + dateTimeToOracle(fromDateTime) + "," + dateTimeToOracle(toDateTime) + ")";
            PreparedStatement ps = db.connection.prepareStatement(statement);
            ps.executeUpdate();
            db.connection.commit();
            return confirmationNumber;
        } else {
            return -1; //used to display error if vehicle is not available
        }
    }

    //if customer checks to see if customer is in database
    public boolean checkForExistingCustomer(int driversLicense) throws SQLException {
        String query = "SELECT DISTINCT COUNT(*) AS C FROM Customer WHERE DLICENSE = " + driversLicense;
        Statement search = db.connection.createStatement();
        ResultSet output = search.executeQuery(query);
        output.next(); //iterates through rows but there's only 1 row in the count
        int number = output.getInt("C");
        if (number < 1) {
            return false;
        } else if (number > 1) {
            throw new IllegalStateException("More than one instance of the same customer is in db");
        }
        return true;
    }

    private String dateTimeToOracle(LocalDateTime d) {
        //TO_TIMESTAMP('2019-12-25 08:15:00.000000 ', 'YYYY-MM-DD HH24:MI:SS.FF')
        StringBuilder output = new StringBuilder();
        output.append("TO_TIMESTAMP(");
        output.append("'" + d.getYear() + "-" + d.getMonthValue() + "-" + d.getDayOfMonth() + " " +  d.getHour() + ":" +  d.getMinute() + ":" + d.getSecond() +".000000'");
        output.append(", 'YYYY-MM-DD HH24:MI:SS.FF')");
        return output.toString();
    }

    public void createNewCustomer() throws SQLException {

    }

    private void createNewCustomer(int dLicense) throws SQLException{
        PreparedStatement statement = db.connection.prepareStatement("INSERT INTO CUSTOMER VALUES (?,?,?,?)");
        statement.setInt(1, -1);
        statement.setString(2, null);
        statement.setString(3, null);
        statement.setInt(4, dLicense);
        statement.executeUpdate();
        db.connection.commit();
    }

    public void createNewCustomer(GuiMain g) throws SQLException {
        g.createCustomerWindow();
    }

    public void createNewCustomer(Customer c) throws SQLException {
        String orderedValues = "'" + c.getCellphone() + "','" + c.getName() + "','" + c.getAddress() + "','" + c.getDlicense() + "'";
        String statement = "INSERT INTO Customer VALUES (" + orderedValues + ")";
        PreparedStatement ps = db.connection.prepareStatement(statement);
        ps.executeUpdate();
        db.connection.commit();
    }

}
