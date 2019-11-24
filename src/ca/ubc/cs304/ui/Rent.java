package ca.ubc.cs304.ui;

import ca.ubc.cs304.controller.Clerk;
import ca.ubc.cs304.controller.CustomerActions;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;

// window used by the clerk to create vehicle rentals
public class Rent extends JFrame {
    private DatabaseConnectionHandler db;
    private JTextField confNumInput;
    private JTextField location;
    private JTextField vehicleType;
    private String[] monthStrings = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"};
    private String[] vTypeStrings = {"Economy", "Compact", "Mid-size", "Standard", "Full-size", "SUV", "Truck"};
    private JSpinner yearSpinner;
    private JSpinner monthSpinner;
    private JSpinner hourSpinner;
    private JPanel rootPanel;
    private JLabel yearLabel;
    private JLabel monthLabel;
    private JFormattedTextField reservationConfNumTextField;
    private JComboBox vTypeComboBox;
    private JFormattedTextField creditCardNoTextField;
    private JTextField creditCardExpiryTextField;
    private JTextField creditCardNameTextField;
    private JButton makeRentalWithConfirmationButton;
    private JTextField branchNameTextField;
    private JFormattedTextField dLicenseTextField;
    private JButton makeRentalWithoutConfirmationButton;
    private JSpinner daySpinner;
    private JTextField cityTextField;

    public Rent(DatabaseConnectionHandler db) {
        super("Create a Rental");
        this.db = db;
        this.setContentPane(rootPanel);
        this.pack();
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);

        // add year spinner
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        SpinnerModel yearModel = new SpinnerNumberModel(currentYear, // initial value
                                                        currentYear, // min
                                                        currentYear + 20, // max
                                                        1); // step
        yearSpinner.setModel(yearModel);
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));

        // add month spinner
        SpinnerListModel monthModel = new SpinnerListModel(monthStrings);
        monthModel.setValue(monthStrings[Calendar.getInstance().get(Calendar.MONTH)]);
        monthSpinner.setModel(monthModel);

        // add day spinner
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        SpinnerModel dayModel = new SpinnerNumberModel(currentDay + 1, 1, 31, 1);
        daySpinner.setModel(dayModel);

        // add hour spinner
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        SpinnerModel hourModel = new SpinnerNumberModel(currentHour, 0, 23, 1);
        hourSpinner.setModel(hourModel);

        // set vehicle type combobox
        ComboBoxModel comboBoxModel = new DefaultComboBoxModel(vTypeStrings);
        vTypeComboBox.setModel(comboBoxModel);

        // add button click listeners
        makeRentalWithConfirmationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (reservationConfNumTextField.getText().length() == 0) {
                        throw new IllegalArgumentException("Reservation confirmation number must be provided");
                    }
                    if (!isInteger(reservationConfNumTextField.getText())) {
                        throw new IllegalArgumentException("Reservation confirmation number must be an integer");
                    }
                    checkCreditCardConstraints();
                    checkBranchLocationConstraints();
                    long cardNo = Long.parseLong(creditCardNoTextField.getText());
                    DateFormat df = new SimpleDateFormat("MMYY");
                    Date expDate = new java.sql.Date(df.parse(creditCardExpiryTextField.getText()).getTime());
                    Card card = new Card();
                    card.setCardName(creditCardNameTextField.getText());
                    card.setCardNo(cardNo);
                    card.setExpDate(expDate);

                    String branchName = branchNameTextField.getText();
                    String city = cityTextField.getText();
                    Branch branch = new Branch(branchName, city);

                    Clerk clerk = new Clerk(db);
                    Reservation r = clerk.getReservation(Integer.parseInt(reservationConfNumTextField.getText()));
                    Vehicle v = clerk.getAvailableVehicle(r.getvTName(), branch);
                    Rental rental = clerk.getRentalReceipt(r, v, card);
                    JOptionPane.showMessageDialog(null,
                                                "Rental ID: " + rental.getrId() + "\n"
                                                        + "Reservation confirmation #: " + rental.getConfNo() + "\n"
                                                        + "From: " + rental.getFromDateTime() + "\n"
                                                        + "To: " + rental.getToDateTime() + "\n"
                                                        + "License plate: " + rental.getvLicense() + "\n"
                                                        + "Starting odometer: " + rental.getOdometer(),
                                                        "Receipt",
                                                            JOptionPane.INFORMATION_MESSAGE);
//                    Vehicle v = clerk.getAvailableVehicle()

                } catch (Exception err){
                    err.printStackTrace();
                    JOptionPane.showMessageDialog(null, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        makeRentalWithoutConfirmationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkCreditCardConstraints();
                    checkBranchLocationConstraints();
                    if (dLicenseTextField.getText().length() == 0) {
                        throw new IllegalArgumentException("Driver's license must be provided");
                    }
                    if (!isInteger(dLicenseTextField.getText())) {
                        throw new IllegalArgumentException("Driver's license must be an integer");
                    }
                    yearSpinner.commitEdit();
                    int year = (int) yearSpinner.getValue();
                    monthSpinner.commitEdit();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new SimpleDateFormat("MMM").parse((String) monthSpinner.getValue()));
                    int month = cal.get(Calendar.MONTH) + 1;
                    daySpinner.commitEdit();
                    int day = (int) daySpinner.getValue();
                    hourSpinner.commitEdit();
                    int hour = (int) hourSpinner.getValue();

                    LocalDateTime from = LocalDateTime.ofInstant(Instant.ofEpochMilli(Calendar.getInstance().getTimeInMillis()), ZoneOffset.UTC);

                    DateFormat df = new SimpleDateFormat("yyyy/MM/dd/HH");
                    java.util.Date date = df.parse(year + "/" + month + "/" + day + "/" + hour);
                    Instant instant = Instant.ofEpochMilli(date.getTime());
                    LocalDateTime to = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);


                    String vtName = (String) vTypeComboBox.getSelectedItem();
                    int dLicense = Integer.parseInt(dLicenseTextField.getText());

                    CustomerActions customerActions = new CustomerActions(db);
                    int confirmationNo = customerActions.makeReservation(vtName, dLicense, from, to);
                    if (confirmationNo == -1) {
                        throw new IllegalArgumentException("Vehicle type is not available at the given location");
                    }

                    long cardNo = Long.parseLong(creditCardNoTextField.getText());
                    DateFormat df2 = new SimpleDateFormat("MMYY");
                    Date expDate = new java.sql.Date(df2.parse(creditCardExpiryTextField.getText()).getTime());
                    Card card = new Card();
                    card.setCardName(creditCardNameTextField.getText());
                    card.setCardNo(cardNo);
                    card.setExpDate(expDate);

                    String branchName = branchNameTextField.getText();
                    String city = cityTextField.getText();
                    Branch branch = new Branch(branchName, city);

                    Clerk clerk = new Clerk(db);
                    Reservation r = clerk.getReservation(confirmationNo);
                    Vehicle v = clerk.getAvailableVehicle(r.getvTName(), branch);
                    Rental rental = clerk.getRentalReceipt(r, v, card);
                    JOptionPane.showMessageDialog(null,
                            "Rental ID: " + rental.getrId() + "\n"
                                    + "Reservation confirmation #: " + rental.getConfNo() + "\n"
                                    + "Location: " + branch.getLocation() + ", " + branch.getCity() + "\n"
                                    + "From: " + rental.getFromDateTime() + "\n"
                                    + "To: " + rental.getToDateTime() + "\n"
                                    + "License plate: " + rental.getvLicense() + "\n"
                                    + "Starting odometer: " + rental.getOdometer(),
                            "Receipt",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception err) {
                    err.printStackTrace();
                    JOptionPane.showMessageDialog(null, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void checkCreditCardConstraints() {
        if (creditCardNameTextField.getText().length() == 0) {
            throw new IllegalArgumentException("Credit card name must be provided");
        }
        if (creditCardNoTextField.getText().length() != 16) {
            throw new IllegalArgumentException("Credit card number must be 16 digits");
        }
        if (creditCardExpiryTextField.getText().length() != 4) {
            throw new IllegalArgumentException("Credit card expiry date must be 4 digits");
        }
    }

    private void checkBranchLocationConstraints() {
        if (branchNameTextField.getText().length() == 0) {
            throw new IllegalArgumentException("Branch name must be provided");
        }
        if (cityTextField.getText().length() == 0) {
            throw new IllegalArgumentException("City must be provided");
        }
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
