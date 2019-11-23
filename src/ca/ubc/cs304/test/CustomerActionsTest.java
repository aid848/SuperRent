package ca.ubc.cs304.test;


import ca.ubc.cs304.controller.CustomerActions;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.Branch;
import ca.ubc.cs304.model.Customer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class CustomerActionsTest {
    static DatabaseConnectionHandler db;
    static CustomerActions customerActions;
    private static LocalDateTime example1;
    private static LocalDateTime example2;

    //TODO add more unit tests for vehicle info

    @BeforeClass
    public static void setup() { //YOU MUST HAVE RUN THE CREATETABLES AND THE POPULATETABLES SCRIPT
        db = new DatabaseConnectionHandler();
        db.login("","");
        example1 = LocalDateTime.of(2018,10,5,12,30);
        example2 = LocalDateTime.of(2018,11,7,10,30);
        customerActions = new CustomerActions(db);
        try {
            try {
                db.firstTimeSetup(3);
            } catch(Exception e) {
                System.out.println("no tables to clear");
            }
            try {
                db.firstTimeSetup();
            } catch (Exception ignored) {

            }
        } catch (Exception e) {
            fail("Initialization failure");
        }

    }




    @Test
    public void checkForExistingCustomerExistsTest() {
        try {
            assertTrue(customerActions.checkForExistingCustomer(1));

        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }


    @Test
    public void viewNumberOfVehiclesTypeAndLocationAndTimeTest() {
        String results = "1 111222 Honda Civic 1990 Red 200000 Economy Cool Rentals East Vancouver\n" +
                "2 222333 Toyota Corolla 2001 Pink 50000 Economy Cool Rentals East Vancouver\n"; //returned tuples
        try {
            int num = customerActions.viewNumberOfVehicles("Economy","Cool Rentals",example1,example2, new Branch("Cool Rentals", "East Vancouver") );
            assertEquals(2, num);
            assertEquals(results,customerActions.retrieveVehicles());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void viewNumberOfVehiclesTypeAndIntervalTest() {
        String results = "1 111222 Honda Civic 1990 Red 200000 Economy Cool Rentals East Vancouver\n" +
                "5 555666 Tesla Model 3 2019 Black 10000 Economy Bad Rentals Maple Ridge\n" +
                "2 222333 Toyota Corolla 2001 Pink 50000 Economy Cool Rentals East Vancouver\n"; //returned tuples
        try {
            int num = customerActions.viewNumberOfVehicles("Economy", null,example1,example2,null);
            assertEquals(3, num);
            assertEquals(results,customerActions.retrieveVehicles());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void viewNumberOfVehiclesTypeAndLocation(){
        String results = "2 222333 Toyota Corolla 2001 Pink 50000 Economy Cool Rentals East Vancouver\n"; //returned tuples
        try {
            int num = customerActions.viewNumberOfVehicles("Economy","Cool Rentals", null,null,null);
            assertEquals(1, num);
            assertEquals(results,customerActions.retrieveVehicles());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void viewNumberOfVehiclesType(){
        String results = "2 222333 Toyota Corolla 2001 Pink 50000 Economy Cool Rentals East Vancouver\n"; //returned tuples
        try {
            int num = customerActions.viewNumberOfVehicles("Economy",null,null,null,null);
            assertEquals(1, num);
            assertEquals(results,customerActions.retrieveVehicles());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void viewNumberOfVehiclesLocationOnly(){
        String results = "4 444555 Ford Ranger 2002 Blue 50000 Truck Cool Rentals East Vancouver\n" +
                "2 222333 Toyota Corolla 2001 Pink 50000 Economy Cool Rentals East Vancouver\n" +
                "3 333444 Ford F-150 2007 Green 50000 Truck Cool Rentals East Vancouver\n"; //returned tuples
        try {
            int num = customerActions.viewNumberOfVehicles(null,"Cool Rentals", null,null,null);
            assertEquals(3, num);
            assertEquals(results,customerActions.retrieveVehicles());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void viewNumberOfVehiclesLocationAndInterval(){
        String results = "1 111222 Honda Civic 1990 Red 200000 Economy Cool Rentals East Vancouver\n" +
                "4 444555 Ford Ranger 2002 Blue 50000 Truck Cool Rentals East Vancouver\n" +
                "2 222333 Toyota Corolla 2001 Pink 50000 Economy Cool Rentals East Vancouver\n" +
                "3 333444 Ford F-150 2007 Green 50000 Truck Cool Rentals East Vancouver\n"; //returned tuples
        try {
            int num = customerActions.viewNumberOfVehicles(null, "Cool Rentals",example1,example2,null);
            assertEquals(4, num);
            assertEquals(results,customerActions.retrieveVehicles());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void viewNumberOfVehiclesTimeIntervalOnly(){
        String results = "1 111222 Honda Civic 1990 Red 200000 Economy Cool Rentals East Vancouver\n" +
                "5 555666 Tesla Model 3 2019 Black 10000 Economy Bad Rentals Maple Ridge\n" +
                "7 777888 Chevrolet Equinox 2019 Black 1000 SUV Bad Rentals Maple Ridge\n" +
                "4 444555 Ford Ranger 2002 Blue 50000 Truck Cool Rentals East Vancouver\n" +
                "6 666777 Dodge Charger 2017 Black 10000 Standard Bad Rentals Maple Ridge\n" +
                "2 222333 Toyota Corolla 2001 Pink 50000 Economy Cool Rentals East Vancouver\n" +
                "3 333444 Ford F-150 2007 Green 50000 Truck Cool Rentals East Vancouver\n"; //returned tuples
        try {
            int num = customerActions.viewNumberOfVehicles(null,null,example1,example2,null);
            assertEquals(7, num);
            assertEquals(results,customerActions.retrieveVehicles());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void viewNumberOfVehiclesBranchOnly(){
        String results = "4 444555 Ford Ranger 2002 Blue 50000 Truck Cool Rentals East Vancouver\n" +
                "3 333444 Ford F-150 2007 Green 50000 Truck Cool Rentals East Vancouver\n" +
                "2 222333 Toyota Corolla 2001 Pink 50000 Economy Cool Rentals East Vancouver\n"; //returned tuples
        try {
            int num = customerActions.viewNumberOfVehicles(null,null,null,null,new Branch("Cool Rentals", "East Vancouver"));
            assertEquals(3, num);
            assertEquals(results,customerActions.retrieveVehicles());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void noResultsTest(){
        String results = ""; //returned tuples
        try {
            int num = customerActions.viewNumberOfVehicles("Yellow Submarine",null,null,null,null);
            assertEquals(0, num);
            assertEquals(results,customerActions.retrieveVehicles());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }




    @Test
    public void countTuplesTest() {
        int num = 0;
        try {
            num = customerActions.countTuples("Branch");
        } catch (SQLException e) {
            fail();
        }
        assertEquals(4,num);

        try {
            Statement s = db.connection.createStatement();
            ResultSet r = s.executeQuery("SELECT * FROM BRANCH");
            int value = customerActions.countTuples(r);
            assertEquals(4,value);
        } catch (Exception e) {
            fail();
        }

    }


    @Test
    public void makeReservationCheckTest() {
        try {
            assertTrue(customerActions.makeReservationCheck(1,  "Economy", example1, example2));// good
            assertFalse(customerActions.makeReservationCheck(1, "Economy", example1, example2));// no vehicle matching request
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void makeReservationTest() { //only tests to see if confirmation number occurs TODO check table automatically for entry
        int value = -1;
        try {
            value = customerActions.makeReservation("Truck",2,example1, example2);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
        assertNotEquals(-1,value);
    }

    @Test
    public void checkForExistingCustomerTest() {
        try {
            assertTrue(customerActions.checkForExistingCustomer(1));
            assertFalse(customerActions.checkForExistingCustomer(92));
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void createNewCustomerTest() {
        try {
            assertFalse(customerActions.checkForExistingCustomer(42));
            customerActions.createNewCustomer(new Customer(39, "Alarm Force", "212 st", 42));
            assertTrue(customerActions.checkForExistingCustomer(42));
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

    }

}
