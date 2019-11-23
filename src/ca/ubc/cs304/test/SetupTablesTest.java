package ca.ubc.cs304.test;

import ca.ubc.cs304.database.DatabaseConnectionHandler;

import java.io.FileNotFoundException;

/* I'm just checking to see if the tables show up in the database*/


public class SetupTablesTest {

    public static void main(String[] args) {
        String username = "ora_"; //put your own credentials in here for testing
        String password = "a";
        DatabaseConnectionHandler db = new DatabaseConnectionHandler();
        db.login(username,password);
        try {
            db.oldFirstTimeSetup();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
