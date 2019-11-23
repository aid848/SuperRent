package ca.ubc.cs304.ui;

import ca.ubc.cs304.controller.Reports;
import ca.ubc.cs304.database.DatabaseConnectionHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        this.setSize(500,500);
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
                    System.out.println("error");
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
                    System.out.println("error");
                }
            }
        });
    }

    private void validateFields(String date) throws Exception{
        if (date.equals("")) {
            textArea1.setText("Error: No Date");
            throw new Exception();
        }
        if (!date.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")){
            textArea1.setText("Error: Invalid Date");
            throw new Exception();
        }
    }
}
