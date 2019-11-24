package ca.ubc.cs304.ui;

import ca.ubc.cs304.controller.Reports;
import ca.ubc.cs304.database.DatabaseConnectionHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DailyReportsByBranch extends JFrame {
    private JTextField dateField;
    private JTextField cityField;
    private JTextField locationField;
    private JButton rentalsReportButton;
    private JButton returnsReportButton;
    private JTextArea textArea1;
    private JPanel rootPanel;

    public DailyReportsByBranch(DatabaseConnectionHandler db){
        super("Daily Reports By Branch");
        this.setContentPane(rootPanel);
        this.pack();
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        rentalsReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText();
                String city = cityField.getText();
                String location = locationField.getText();
                try {
                    validateFields(date, city, location);
                    Reports reports = new Reports(db);
                    String result = reports.generateDailyRentalReport(date, city, location);
                    textArea1.setText(result);
                }catch(Exception f){
                    textArea1.setText(f.getMessage());
                    f.printStackTrace();
                }
            }
        });
        returnsReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText();
                String city = cityField.getText();
                String location = locationField.getText();
                try {
                    validateFields(date, city, location);
                    Reports reports = new Reports(db);
                    String result = reports.generateDailyReturnReport(date, city, location);
                    textArea1.setText(result);
                }catch (Exception f){
                    textArea1.setText(f.getMessage());
                    f.printStackTrace();
                }
            }
        });
    }

    private void validateFields(String date, String city, String location) throws Exception{
        if (date.equals("") || city.equals("") || location.equals("")) {
            throw new Exception("Error: Empty Field(s)");
        }
        if (!date.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")){
            throw new Exception("Error: Invalid Date");
        }
    }
}
