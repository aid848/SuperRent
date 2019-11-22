package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.VehicleModel;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reports {

    DatabaseConnectionHandler db;

    public Reports(DatabaseConnectionHandler db) {
        this.db = db;
        assert (db.connection != null); //check for database login otherwise methods won't run
    }

    /*
   Daily Rentals:
   (1) This report contains information on all the vehicles rented out during the day.
   (2) The entries are grouped by branch, and within each branch, the entries are grouped by vehicle category.
   (3) The report also displays the number of vehicles rented per category (e.g., 5 sedan rentals, 2 SUV rentals, etc.), the number of rentals at each branch,
   (4) and the total number of new rentals across the whole company
    */

    /* (1)
    returns vehicle information on all the vehicles rented out during the day, ordered by Branch and Vehicle Type as HashMap<BRANCH, List<VEHICLE>>
    if no branch is given returns data for all branches, else for just the given branch.
    */
    public HashMap<String, List<VehicleModel>> reportRentedVehicleInfo(String date, String city, String location) {
        HashMap<String, List<VehicleModel>> result = new HashMap<String, List<VehicleModel>>();
        try {
            String sql = "";
            PreparedStatement stmt;
            java.sql.Date[] dates = getDates(date);
            if (city == null && location == null){
                sql = "select BRANCH.location, BRANCH.city, VEHICLE.vtname, VEHICLE.VID, VEHICLE.VLICENSE, VEHICLE.MAKE, VEHICLE.MODEL, VEHICLE.YEAR, VEHICLE.COLOR, VEHICLE.ODOMETER, VEHICLE.STATUS" +
                        "  from BRANCH,RENT,VEHICLE" +
                        "  where RENT.FROMDATETIME between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND VEHICLE.CITY = BRANCH.CITY" +
                        "  order by BRANCH.CITY, BRANCH.LOCATION";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
            }else{
                sql = "select BRANCH.location, BRANCH.city, VEHICLE.vtname, VEHICLE.VID, VEHICLE.VLICENSE, VEHICLE.MAKE, VEHICLE.MODEL, VEHICLE.YEAR, VEHICLE.COLOR, VEHICLE.ODOMETER, VEHICLE.STATUS" +
                        "  from BRANCH,RENT,VEHICLE" +
                        "  where RENT.FROMDATETIME between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND VEHICLE.CITY = BRANCH.CITY" +
                        "  AND BRANCH.CITY = ?" +
                        "  AND BRANCH.LOCATION = ?" +
                        "  order by BRANCH.LOCATION, VEHICLE.VTNAME";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
                stmt.setString(3, city);
                stmt.setString(4, location);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                VehicleModel model = new VehicleModel(
                        rs.getInt("vid"),
                        rs.getInt("vlicense"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("color"),
                        rs.getFloat("odometer"),
                        rs.getString("status"),
                        rs.getString("vtname"),
                        rs.getString("location"),
                        rs.getString("city"));

                String key = model.getCity() + ":" + model.getLocation() + ":" + model.getVtname();
                if (result.containsKey(key)) {
                    result.get(key).add(model);
                } else {
                    List<VehicleModel> newList = new ArrayList<VehicleModel>();
                    newList.add(model);
                    result.put(key, newList);
                }
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("SUPERRENT" + " " + e.getMessage());
        }
        return result;
    }
    public HashMap<String, List<VehicleModel>> reportRentedVehicleInfo(String date){
        return reportRentedVehicleInfo(date, null, null);
    }

    /* (2)
    returns the number of vehicles rented per vehicle category as HashMap<VTYPE, NUM RENTED> on given date
    if no branch is given returns data for all branches, else for just the given branch.
    */
    public HashMap<String, Integer> reportNumRentalsPerCategory(String date, String city, String location) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();

        try {
            String sql = "";
            PreparedStatement stmt;
            java.sql.Date[] dates = getDates(date);

            if (city == null && location == null) {
                sql = "select VEHICLE.vtname, Count(Vehicle.vtname)" +
                        "  from BRANCH,RENT,VEHICLE" +
                        "  where RENT.FROMDATETIME between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND VEHICLE.CITY = BRANCH.CITY" +
                        "  group by VEHICLE.VTNAME" +
                        "  order by VEHICLE.VTNAME";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
            }else{
                sql = "select VEHICLE.vtname, Count(Vehicle.vtname)" +
                        "  from BRANCH,RENT,VEHICLE" +
                        "  where RENT.FROMDATETIME between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND VEHICLE.CITY = BRANCH.CITY" +
                        "  AND BRANCH.CITY = ?" +
                        "  AND BRANCH.LOCATION = ?" +
                        "  group by VEHICLE.VTNAME" +
                        "  order by VEHICLE.VTNAME";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
                stmt.setString(3, city);
                stmt.setString(4, location);

            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String vtname = rs.getString("vtname");

                String key = vtname;
                result.put(key, rs.getInt(2));

            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("SUPERRENT" + " " + e.getMessage());
        }
        return result;

    }
    /* (3)
    returns the number of vehicles rented per branch as HashMap<BRANCH, NUM RENTED> on given date
    if no branch is given returns data for all branches, else for just the given branch.
    */
    public HashMap<String, Integer> reportNumRentalsPerBranch(String date, String city, String location) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();

        try {
            String sql = "";
            PreparedStatement stmt;
            java.sql.Date[] dates = getDates(date);

            if (city == null && location == null) {
                sql = "select VEHICLE.LOCATION, VEHICLE.CITY, Count(Vehicle.VLICENSE)" +
                        "  from BRANCH,RENT,VEHICLE" +
                        "  where RENT.FROMDATETIME between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND VEHICLE.CITY = BRANCH.CITY" +
                        "  group by VEHICLE.LOCATION, VEHICLE.CITY";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
            }else{
                sql = "select VEHICLE.LOCATION, VEHICLE.CITY, Count(Vehicle.VLICENSE)" +
                        "  from BRANCH,RENT,VEHICLE" +
                        "  where RENT.FROMDATETIME between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND VEHICLE.CITY = BRANCH.CITY" +
                        "  AND BRANCH.CITY = ?" +
                        "  AND BRANCH.LOCATION = ?" +
                        "  group by VEHICLE.LOCATION, VEHICLE.CITY";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
                stmt.setString(3, city);
                stmt.setString(4, location);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String citycol = rs.getString("city");
                String locationcol = rs.getString("location");

                String key = citycol + ":" + locationcol;
                result.put(key, rs.getInt(3));

            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("SUPERRENT" + " " + e.getMessage());
        }
        return result;
    }

    /* (4)
    returns the total number of new rentals across the whole company
    if given a Branch, gives num new rentals just for that Branch
    */
    public Integer reportNumNewRentals(String date, String city, String location) {
        Integer result = 0;

        try {
            String sql = "";
            PreparedStatement stmt;
            java.sql.Date[] dates = getDates(date);
            if (city == null && location == null) {

                sql = "select Count(VEHICLE.vlicense)" +
                        "  from BRANCH,RENT,VEHICLE" +
                        "  where RENT.FROMDATETIME between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND VEHICLE.CITY = BRANCH.CITY";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
            }else{
                sql = "select Count(VEHICLE.vlicense)" +
                        "  from BRANCH,RENT,VEHICLE" +
                        "  where RENT.FROMDATETIME between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND VEHICLE.CITY = BRANCH.CITY" +
                        "  AND BRANCH.CITY = ?" +
                        "  AND BRANCH.LOCATION = ?";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
                stmt.setString(3, city);
                stmt.setString(4, location);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result = rs.getInt(1);

            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("SUPERRENT" + " " + e.getMessage());
        }
        return result;
    }

    //input:    string date as 'YYYY-MM-DD'
    //          string branch.city
    //          string branch.location
    //returns a formatted daily rental report for a single branch as a string
    public String generateDailyRentalReport(String date, String city, String location){
        String result = "";
        result += "==========DAILY RENTAL REPORT========== \n";

        HashMap<String, List<VehicleModel>> map = reportRentedVehicleInfo(date, city, location);
        Object[] keys = map.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            result += keys[i] + "\n";
            List<VehicleModel> vehicles = map.get(keys[i]);
            for (VehicleModel vehicle : vehicles)
                result = result + "rented vehicle: " + " | " + vehicle.getVid() + " | " + vehicle.getVlicense() + " | " + vehicle.getMake() + " | " + vehicle.getModel() + " | " + vehicle.getYear() + " | " + vehicle.getColor() + " | " + vehicle.getOdometer() + "\n";
            result += "========================================\n";
        }

        HashMap<String, Integer> map2 = reportNumRentalsPerCategory(date, city, location);
        Object[] keys2 = map2.keySet().toArray();
        for (int i = 0; i < keys2.length; i++) {
            result += keys2[i] + "\n";
            result += "num rented today: " + map2.get(keys2[i]) + "\n";
            result += "========================================\n";
        }

        HashMap<String, Integer> map3 = reportNumRentalsPerBranch(date, city, location);
        Object[] keys3 = map3.keySet().toArray();
        for (int i = 0; i < keys3.length; i++) {
            result += keys3[i] + "\n";
            result += "num rented today: " + map3.get(keys3[i]) + "\n";
            result += "========================================\n";
        }

        Integer total = reportNumNewRentals(date, city, location);
        result += "total rented today: " + total + "\n";
        result += "========================================\n";
        return result;
    }

    //input:    string date as 'YYYY-MM-DD'
    //returns a formatted daily rental report for the whole company
    public String generateDailyRentalReport(String date){
        return generateDailyRentalReport(date, null, null);
    }



    /*
    Daily Returns:
    (1) The report contains information on all the vehicles returned during the day. The entries are grouped by branch, and within each branch, the entries are grouped by vehicle category.
    (2) The report also shows the number of vehicles returned per category, the revenue per category,
    (3) subtotals for the number of vehicles and revenue per branch,
    (4) and the grand totals for the day.
     */

    /* (1)
    returns vehicle information on all the vehicles returned during the day, ordered by Branch and Vehicle Type as HashMap<BRANCH, List<VEHICLE>>
    if no branch is given returns data for all branches, else for just the given branch.
    */
    public HashMap<String, List<VehicleModel>> reportReturnedVehicleInfo(String date, String city, String location) {
        HashMap<String, List<VehicleModel>> result = new HashMap<String, List<VehicleModel>>();
        try {
            String sql;
            PreparedStatement stmt;
            java.sql.Date[] dates = getDates(date);
            if (city == null && location == null){
                sql = "select BRANCH.location, BRANCH.city, VEHICLE.vtname, VEHICLE.VID, VEHICLE.VLICENSE, VEHICLE.MAKE, VEHICLE.MODEL, VEHICLE.YEAR, VEHICLE.COLOR, VEHICLE.ODOMETER, VEHICLE.STATUS" +
                        "  from BRANCH,RENT,VEHICLE, RETURN" +
                        "  where RETURN.RETURNDATETIME between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND RENT.RID = RETURN.RID" +
                        "  AND VEHICLE.CITY = BRANCH.CITY" +
                        "  order by BRANCH.CITY, BRANCH.LOCATION";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
            }else{
                sql = "select BRANCH.location, BRANCH.city, VEHICLE.vtname, VEHICLE.VID, VEHICLE.VLICENSE, VEHICLE.MAKE, VEHICLE.MODEL, VEHICLE.YEAR, VEHICLE.COLOR, VEHICLE.ODOMETER, VEHICLE.STATUS" +
                        "  from BRANCH,RENT,VEHICLE, RETURN" +
                        "  where RETURN.RETURNDATETIME between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND VEHICLE.CITY = BRANCH.CITY" +
                        "  AND RENT.RID = RETURN.RID" +
                        "  AND BRANCH.CITY = ?" +
                        "  AND BRANCH.LOCATION = ?" +
                        "  order by BRANCH.LOCATION, VEHICLE.VTNAME";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
                stmt.setString(3, city);
                stmt.setString(4, location);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                VehicleModel model = new VehicleModel(
                        rs.getInt("vid"),
                        rs.getInt("vlicense"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("color"),
                        rs.getFloat("odometer"),
                        rs.getString("status"),
                        rs.getString("vtname"),
                        rs.getString("location"),
                        rs.getString("city"));

                String key = model.getCity() + ":" + model.getLocation() + ":" + model.getVtname();
                if (result.containsKey(key)) {
                    result.get(key).add(model);
                } else {
                    List<VehicleModel> newList = new ArrayList<VehicleModel>();
                    newList.add(model);
                    result.put(key, newList);
                }
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("SUPERRENT" + " " + e.getMessage());
        }
        return result;
    }

    /* (2)
    returns the number of vehicles returned and revenue per vehicle category as HashMap<VTYPE, [NUM RENTED, REVENUE]> on given date
    if no branch is given returns data for all branches, else for just the given branch.
    */
    public HashMap<String, Integer[]> reportNumReturnedPerCategory(String date, String city, String location) {
        HashMap<String, Integer[]> result = new HashMap<String, Integer[]>();

        try {
            String sql;
            PreparedStatement stmt;
            java.sql.Date[] dates = getDates(date);

            if (city == null && location == null) {
                sql = "select VEHICLE.vtname, Count(Vehicle.vtname), SUM(RETURN.VALUE)" +
                        "  from BRANCH,RENT,VEHICLE, RETURN" +
                        "  where RETURN.RETURNDATETIME between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND RENT.RID = RETURN.RID" +
                        "  AND VEHICLE.CITY = BRANCH.CITY" +
                        "  group by VEHICLE.VTNAME" +
                        "  order by VEHICLE.VTNAME";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
            }else{
                sql = "select VEHICLE.vtname, Count(Vehicle.vtname), SUM(RETURN.VALUE)" +
                        "  from BRANCH,RENT,VEHICLE, RETURN" +
                        "  where RETURN.RETURNDATETIME between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND VEHICLE.CITY = BRANCH.CITY" +
                        "  AND RENT.RID = RETURN.RID" +
                        "  AND BRANCH.CITY = ?" +
                        "  AND BRANCH.LOCATION = ?" +
                        "  group by VEHICLE.VTNAME" +
                        "  order by VEHICLE.VTNAME";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
                stmt.setString(3, city);
                stmt.setString(4, location);

            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Integer [] resultList = new Integer[2];
                String vtname = rs.getString("vtname");

                String key = vtname;
                resultList[0] = rs.getInt(2);
                resultList[1] = rs.getInt(3);
                result.put(key, resultList);

            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("SUPERRENT" + " " + e.getMessage());
        }
        return result;

    }

    /* (3)
    returns the number of vehicles returned and revenue per branch as HashMap<BRANCH, [NUM RENTED, REVENUE}> on given date
    if no branch is given returns data for all branches, else for just the given branch.
    */
    public HashMap<String, Integer[]> reportNumReturnedPerBranch(String date, String city, String location) {
        HashMap<String, Integer[]> result = new HashMap<String, Integer[]>();

        try {
            String sql;
            PreparedStatement stmt;
            java.sql.Date[] dates = getDates(date);

            if (city == null && location == null) {
                sql = "select VEHICLE.LOCATION, VEHICLE.CITY, Count(Vehicle.VLICENSE), SUM(RETURN.VALUE)" +
                        "  from BRANCH,RENT,VEHICLE, RETURN" +
                        "  where RETURN.RETURNDATETIME between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND VEHICLE.CITY = BRANCH.CITY" +
                        "  AND RENT.RID = RETURN.RID" +
                        "  group by VEHICLE.LOCATION, VEHICLE.CITY";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
            }else{
                sql = "select VEHICLE.LOCATION, VEHICLE.CITY, Count(Vehicle.VLICENSE), SUM(RETURN.VALUE)" +
                        "  from BRANCH,RENT,VEHICLE, RETURN" +
                        "  where RETURN.RETURNDATETIME between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND RENT.RID = RETURN.RID" +
                        "  AND VEHICLE.CITY = BRANCH.CITY" +
                        "  AND BRANCH.CITY = ?" +
                        "  AND BRANCH.LOCATION = ?" +
                        "  group by VEHICLE.LOCATION, VEHICLE.CITY";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
                stmt.setString(3, city);
                stmt.setString(4, location);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer [] resultList = new Integer[2];
                String citycol = rs.getString("city");
                String locationcol = rs.getString("location");

                String key = citycol + ":" + locationcol;
                resultList[0] = rs.getInt(3);
                resultList[1] = rs.getInt(4);
                result.put(key, resultList);

            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("SUPERRENT" + " " + e.getMessage());
        }
        return result;
    }

    /* (4)
    returns the total number of returns and revenue across the whole company as [NUM RETURNED, REVENUE]
    if given a Branch, gives num returns and revenue just for that Branch
    */
    public Integer[] reportNumReturned(String date, String city, String location) {
        Integer [] resultList = new Integer[2];

        try {
            String sql = "";
            PreparedStatement stmt;
            java.sql.Date[] dates = getDates(date);
            if (city == null && location == null) {

                sql = "select Count(VEHICLE.vlicense), SUM(RETURN.VALUE)" +
                        "  from BRANCH,RENT,VEHICLE, RETURN" +
                        "  where RETURN.RETURNDATETIME between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND RENT.RID = RETURN.RID" +
                        "  AND VEHICLE.CITY = BRANCH.CITY";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
            }else{
                sql = "select Count(VEHICLE.vlicense), SUM(RETURN.VALUE)" +
                        "  from BRANCH,RENT,VEHICLE, RETURN" +
                        "  where RETURN.RETURNDATETIME  between " + '?' + " and " + '?' +
                        "  AND Rent.VLICENSE = VEHICLE.VLICENSE" +
                        "  AND VEHICLE.LOCATION = BRANCH.LOCATION" +
                        "  AND RENT.RID = RETURN.RID" +
                        "  AND VEHICLE.CITY = BRANCH.CITY" +
                        "  AND BRANCH.CITY = ?" +
                        "  AND BRANCH.LOCATION = ?";

                stmt = db.connection.prepareStatement(sql);
                stmt.setDate(1, dates[0]);
                stmt.setDate(2, dates[1]);
                stmt.setString(3, city);
                stmt.setString(4, location);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                resultList[0] = rs.getInt(1);
                resultList[1] = rs.getInt(2);

            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("SUPERRENT" + " " + e.getMessage());
        }
        return resultList;
    }



    //input:    string date as 'YYYY-MM-DD'
    //          string branch.city
    //          string branch.location
    //returns a formatted daily returns report for a single branch as a string
    public String generateDailyReturnReport(String date, String city, String location){
        String result = "";
        result += "==========DAILY RETURNS REPORT========== \n";
        HashMap<String, List<VehicleModel>> map = reportReturnedVehicleInfo(date, city, location);
        Object[] keys = map.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            result += keys[i] + "\n";
            List<VehicleModel> vehicles = map.get(keys[i]);
            for (VehicleModel vehicle : vehicles)
                result = result + "returned vehicle: " + " | " + vehicle.getVid() + " | " + vehicle.getVlicense() + " | " + vehicle.getMake() + " | " + vehicle.getModel() + " | " + vehicle.getYear() + " | " + vehicle.getColor() + " | " + vehicle.getOdometer() + "\n";
            result += "========================================\n";
        }

        HashMap<String, Integer[]> map2 = reportNumReturnedPerCategory(date, city, location);
        Object[] keys2 = map2.keySet().toArray();
        for (int i = 0; i < keys2.length; i++) {
            result += keys2[i] + "\n";
            result += "num returned today: " + map2.get(keys2[i])[0] + "\n";
            result += "revenue today: " + map2.get(keys2[i])[1] + "\n";
            result += "========================================\n";
        }

        HashMap<String, Integer[]> map3 = reportNumReturnedPerBranch(date, city, location);
        Object[] keys3 = map3.keySet().toArray();
        for (int i = 0; i < keys3.length; i++) {
            result += keys3[i] + "\n";
            result += "num returned today: " + map3.get(keys3[i])[0] + "\n";
            result += "revenue today: " + map3.get(keys3[i])[1] + "\n";
            result += "========================================\n";
        }

        Integer[] total = reportNumReturned(date, city, location);
        result += "grand total returned today: " + total[0] + "\n";
        result += "grand total revenue today: " + total[1] + "\n";
        result += "========================================\n";

        return result;
    }

    //input:    string date as 'YYYY-MM-DD'
    //returns a formatted daily returns report for the whole company
    public String generateDailyReturnReport(String date){
        return generateDailyReturnReport(date, null, null);
    }

    //return date and the next date as sqlDate array when given a string YYYY-MM-DD
    private java.sql.Date[] getDates(String stringDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date sdfDate = null;
        try {
            sdfDate = sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date date = new java.sql.Date(sdfDate.getTime());
        java.sql.Date nextDate = new java.sql.Date(sdfDate.getTime() + (1000 * 60 * 60 * 24));
        java.sql.Date[] dates = new java.sql.Date[2];
        dates[0] = date;
        dates[1] = nextDate;
        return dates;
    }
}
