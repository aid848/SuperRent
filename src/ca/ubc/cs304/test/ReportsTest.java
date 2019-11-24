package ca.ubc.cs304.test;

import ca.ubc.cs304.controller.Reports;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReportsTest {
    private static Reports reports;
    @Before
    static void login(){
        String username = "ora_"; //put your own credentials in here for testing
        String password = "a";
        DatabaseConnectionHandler db = new DatabaseConnectionHandler();
        db.login(username, password);
        reports = new Reports(db);
    }

    @Test
    void generateDailyRentalReport(){
        String report = reports.generateDailyRentalReport("2019-10-10");
        System.out.println(report);
    }

    @Test
    void generateDailyRentalReportOneBranch(){
        String report = reports.generateDailyRentalReport("2019-10-10", "East Vancouver", "Cool Rentals");
        System.out.println(report);
        assertEquals("==========DAILY RENTAL REPORT========== \n" + "total rented today: 0\n" + "========================================\n",
                reports.generateDailyRentalReport("2019-10-10", "Not A Branch", "Cool Rentals"));
    }

    @Test
    void generateDailyReturnReport(){
        String report = reports.generateDailyReturnReport("2019-11-11");
        System.out.println(report);
    }

    @Test
    void generateDailyReturnReportOneBranch(){
        String report = reports.generateDailyReturnReport("2019-11-11", "Mapley Ridge", "Bad Rentals");
        System.out.println(report);
        assertEquals("==========DAILY RETURNS REPORT========== \ngrand total returned today: 0\ngrand total revenue today: 0\n========================================\n",
                reports.generateDailyReturnReport("2019-10-10", "Not A Branch", "Cool Rentals"));
    }

}
