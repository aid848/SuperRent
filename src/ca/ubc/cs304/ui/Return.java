package ca.ubc.cs304.ui;

import ca.ubc.cs304.controller.Clerk;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.ReturnReceipt;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Return extends JFrame {
    private DatabaseConnectionHandler db;
    private String[] monthStrings = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"};
    private JTextField rentalIDTextField;
    private JTextField odometerTextField;
    private JRadioButton yesRadioButton;
    private JSpinner yearSpinner;
    private JSpinner monthSpinner;
    private JSpinner daySpinner;
    private JSpinner hourSpinner;
    private JButton returnVehicleButton;
    private JRadioButton noRadioButton;
    private JPanel rootPanel;

    public Return(DatabaseConnectionHandler db) {
        super("Return a Rental");
        this.db = db;
        this.setContentPane(rootPanel);
        this.pack();
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);

        // add year spinner
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        SpinnerModel yearModel = new SpinnerNumberModel(currentYear, // initial value
                currentYear - 20, // min
                currentYear + 20, // max
                1); // step
        yearSpinner.setModel(yearModel);
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));

        // add month spinner
        SpinnerListModel monthModel = new SpinnerListModel(monthStrings);
        monthSpinner.setModel(monthModel);

        // add day spinner
        SpinnerModel dayModel = new SpinnerNumberModel(1, 1, 31, 1);
        daySpinner.setModel(dayModel);

        // add hour spinner
        SpinnerModel hourModel = new SpinnerNumberModel(0, 0, 23, 1);
        hourSpinner.setModel(hourModel);

        // add radio buttons to group
        ButtonGroup bg = new ButtonGroup();
        bg.add(yesRadioButton);
        bg.add(noRadioButton);
        yesRadioButton.setSelected(true);

        returnVehicleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (rentalIDTextField.getText().length() == 0) {
                        throw new IllegalArgumentException("Rental ID must be provided");
                    }
                    if (!isInteger(rentalIDTextField.getText())) {
                        throw new IllegalArgumentException("Rental ID must be an integer");
                    }
                    if (odometerTextField.getText().length() == 0) {
                        throw new IllegalArgumentException("Odometer reading must be provided");
                    }
                    if (!isFloat(odometerTextField.getText())) {
                        throw new IllegalArgumentException("Odometer reading must be a number");
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

                    DateFormat df = new SimpleDateFormat("yyyy/MM/dd/HH");
                    Timestamp ts = new Timestamp(df.parse(year + "/" + month + "/" + day + "/" + hour).getTime());

                    int rentalID = Integer.parseInt(rentalIDTextField.getText());
                    float odometer = Float.parseFloat(odometerTextField.getText());
                    int tankFull = 0;
                    if (yesRadioButton.isSelected()) {
                        tankFull = 1;
                    }

                    ca.ubc.cs304.model.Return ret = new ca.ubc.cs304.model.Return();
                    ret.setrID(rentalID);
                    ret.setOdometer(odometer);
                    ret.setReturnDateTime(ts);
                    ret.setFulltank(tankFull);

                    Clerk clerk = new Clerk(db);
                    ReturnReceipt receipt = clerk.returnVehicle(ret);

                    JOptionPane.showMessageDialog(null,
                            "Rental ID: " + receipt.getrID() + "\n"
                                    + "From: " + receipt.getRentalDate() + "\n"
                                    + "To: " + receipt.getReturnDate() + "\n"
                                    + "Elapsed weeks: " + receipt.getElapsedWeeks() + "\n"
                                    + "Elapsed days: " + receipt.getElapsedDays() + "\n"
                                    + "Elapsed hours: " + receipt.getElapsedHours() + "\n"
                                    + "Weekly Rate: " + receipt.getWeeklyRate() + "\n"
                                    + "Daily Rate: " + receipt.getDailyRate() + "\n"
                                    + "Hourly Rate: " + receipt.getHourlyRate() + "\n"
                                    + "Daily Insurance Rate: " + receipt.getDailyInsuranceRate() + "\n"
                                    + "Hourly Insurance Rate: " + receipt.getHourlyInsuranceRate() + "\n"
                                    + "Start Odometer: " + receipt.getStartOdometer() + "\n"
                                    + "End Odometer: " + receipt.getEndOdometer() + "\n"
                                    + "Total KM Traveled: " + (receipt.getEndOdometer() - receipt.getStartOdometer()) + "\n"
                                    + "Per KM Rate: " + receipt.getkRate() + "\n"
                                    + "Grand total: " + receipt.getTotal() + "\n",
                            "Rental Return Receipt",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception err) {
                    err.printStackTrace();
                    JOptionPane.showMessageDialog(null, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
