package ca.ubc.cs304.test;

import ca.ubc.cs304.controller.Reports;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ReportsTest {
    private static Reports reports;
    @BeforeAll
    static void login(){
        String username = "ora_"; //put your own credentials in here for testing
        String password = "a";
        DatabaseConnectionHandler db = new DatabaseConnectionHandler();
        db.login(username, password);
        reports = new Reports(db);
    }

    @Test
    void generateDailyRentalReport(){
        String report = reports.generateDailyRentalReport("2019-11-21");
        System.out.println(report);
    }

    @Test
    void generateDailyRentalReportOneBranch(){
        String report = reports.generateDailyRentalReport("2019-11-21", "East Vancouver", "Cool Rentals");
        System.out.println(report);
    }

    @Test
    void generateDailyReturnReport(){
        String report = reports.generateDailyReturnReport("2019-10-10");
        System.out.println(report);
    }

    @Test
    void generateDailyReturnReportOneBranch(){
        String report = reports.generateDailyReturnReport("2019-10-10", "Maple Ridge", "Bad Rentals");
        System.out.println(report);
    }

}
