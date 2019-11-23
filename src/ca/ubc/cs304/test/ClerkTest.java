package ca.ubc.cs304.test;

import ca.ubc.cs304.controller.Clerk;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

// Assumes setuptables.sql and populatetables.sql have been run beforehand
// NOTE: if tests hang on commits to the db, check to make sure there are no other connections to the database, as multiple connections may cause that
public class ClerkTest {
    private DatabaseConnectionHandler db;
    private Clerk clerk;
    private Reservation reservation;
    private Card card;
    private Vehicle vehicle;
    private Return ret;

    @Before
    public void setup() {
        db = new DatabaseConnectionHandler();
        db.login("ora_eatnow", "a31745136");
        clerk = new Clerk(db);
        try {
            firstTimeSetup(1);
            firstTimeSetup(2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        createReservation();
        createCard();
        createVehicle();
        createReturn();
    }

    public void firstTimeSetup(int stage) throws FileNotFoundException {
        String path;
        if(stage == 1) {
            path = "setuptables.sql";
        } else if (stage == 2) {
            path = "populatetables.sql";
        }else {
            path = "droptables.sql";
        }
        ArrayList<String> statements = new ArrayList<String>();
        try {
            Scanner in = new Scanner(new File("SQL\\" + path));
            in.useDelimiter(";");
            while (in.hasNext())
                statements.add(in.next());
            for (String statement: statements) {
                PreparedStatement p = db.connection.prepareStatement(statement);
                p.executeUpdate();
                db.connection.commit();
                p.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createVehicle() {
        vehicle = new Vehicle();
        vehicle.setvId(4);
        vehicle.setvLicense(444555);
        vehicle.setMake("Ford");
        vehicle.setModel("Ranger");
        vehicle.setYear(2002);
        vehicle.setColor("Blue");
        vehicle.setOdometer(50000);
        vehicle.setStatus("Maintenance");
        vehicle.setVTName("Truck");
        vehicle.setLocation("Cool Rentals");
        vehicle.setCity("East Vancouver");
    }

    private void createCard() {
        card = new Card();
        card.setCardName("Tom");
        card.setCardNo(1234123412341234L);
        long expDate = LocalDateTime.parse("2022-06-20 00:00:00", DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"))
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        card.setExpDate(new Date(expDate));
    }

    private void createReservation() {
        long pickupDate = LocalDateTime.parse("2019-11-20 10:00:00", DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"))
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        long returnDate = LocalDateTime.parse("2019-12-25 08:15:00", DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"))
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        reservation = new Reservation();
        reservation.setPickupDate(new Timestamp(pickupDate));
        reservation.setReturnDate(new Timestamp(returnDate));
        reservation.setConfNum(1234);
        reservation.setdLicense(2);
        reservation.setvTName("Truck");
    }

    private void createReturn() {
        long returnDate = LocalDateTime.parse("2019-12-30 12:15:00", DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"))
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        ret = new Return();
        ret.setrID(1);
        ret.setReturnDateTime(new Timestamp(returnDate));
        ret.setOdometer(250000);
        ret.setFulltank(1);
    }

    @After
    public void end() {
        try {
            firstTimeSetup(3);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        db.close();
    }

    @Test
    public void testGetReservationReceipt() {
        try {
            ResultSet result = clerk.getRentalReceipt(reservation, vehicle, card);
            if (result.next()) {
                System.out.println(result.getInt(1));
                System.out.println(result.getInt(2));
                System.out.println(result.getInt(3));
                System.out.println(result.getTimestamp(4));
                System.out.println(result.getTimestamp(5));
                System.out.println(result.getFloat(6));
                System.out.println(result.getString(7));
                System.out.println(result.getLong(8));
            } else {
                fail("Should have one tuple");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetReturnReceipt() {
        try {
            ReturnReceipt result = clerk.returnVehicle(ret);
            System.out.println(result.getrID());
            System.out.println(result.getRentalDate());
            System.out.println(result.getReturnDate());
            System.out.println(result.getElapsedWeeks());
            System.out.println(result.getElapsedDays());
            System.out.println(result.getElapsedHours());

        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }
}
