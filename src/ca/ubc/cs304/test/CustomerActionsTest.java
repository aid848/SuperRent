package ca.ubc.cs304.test;


import ca.ubc.cs304.controller.CustomerActions;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class CustomerActionsTest {
    DatabaseConnectionHandler db;
    CustomerActions customerActions;
    @Before
    public void setup() { //YOU MUST HAVE run THE CREATETABLES AND THE POPULATETABLES SCRIPT
        db = new DatabaseConnectionHandler();
        db.login("ora_afrost99","a31139991");
        customerActions = new CustomerActions(db);
    }


    @After
    public void end() {
        db.close();
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
    public void checkForExistingCustomerNotExistsTest() {
        try {
            assertFalse(customerActions.checkForExistingCustomer(99));

        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void viewNumberOfVehiclesTest() {

        //tuple counter part
        //assertEquals(4, customerActions.countTuples());
    }



}
