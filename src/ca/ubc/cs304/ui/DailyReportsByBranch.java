package ca.ubc.cs304.ui;

import ca.ubc.cs304.controller.Reports;
import ca.ubc.cs304.database.DatabaseConnectionHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        this.setSize(500,500);
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
                    System.out.println("error");
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
                    System.out.println("error");
                }
            }
        });
    }

    private void validateFields(String date, String city, String location) throws Exception{
        if (date.equals("") || city.equals("") || location.equals("")) {
            textArea1.setText("Error: Empty Field(s)");
            throw new Exception();
        }
        if (!date.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")){
            textArea1.setText("Error: Invalid Date");
            throw new Exception();
        }
    }
}
