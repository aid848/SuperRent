package ca.ubc.cs304.test;


import ca.ubc.cs304.controller.Customer;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class CustomerTest {
    DatabaseConnectionHandler db;
    Customer customer;
    @Before
    public void setup() { //YOU MUST HAVE run THE CREATETABLES AND THE POPULATETABLES SCRIPT
        db = new DatabaseConnectionHandler();
        db.login("","");
        customer = new Customer(db);
    }


    @After
    public void end() {
        db.close();
    }

    @Test
    public void checkForExistingCustomerExistsTest() {
        //looking for bob from populatetables.sql
        try {
            assertTrue(customer.checkForExistingCustomer(1));

        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

    }



}
