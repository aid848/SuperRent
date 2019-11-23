package ca.ubc.cs304.ui;

import ca.ubc.cs304.controller.Reports;
import ca.ubc.cs304.database.DatabaseConnectionHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DailyReports extends JFrame{
    private JTextField dateField;
    private JButton rentalsReportButton;
    private JButton returnsReportButton;
    private JTextArea textArea1;
    private JPanel rootPanel;

    public DailyReports(DatabaseConnectionHandler db){
        super("Daily Reports");
        this.setContentPane(rootPanel);
        this.pack();
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        rentalsReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText();
                try {
                    validateFields(date);
                    Reports reports = new Reports(db);
                    String result = reports.generateDailyRentalReport(date);
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
                try {
                    validateFields(date);
                    Reports reports = new Reports(db);
                    String result = reports.generateDailyReturnReport(date);
                    textArea1.setText(result);
                }catch(Exception f){
                    textArea1.setText(f.getMessage());
                    f.printStackTrace();
                }
            }
        });
    }

    private void validateFields(String date) throws Exception{
        if (date.equals("")) {
            throw new Exception("Error: No Date");
        }
        if (!date.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")){
            throw new Exception("Error: Invalid Date");
        }
    }
}
