package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.DatabaseConnectionHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* test queries
select table_name from all_tables where owner=ORA_CWL
select * from vehicle
insert into vehicle values (999, 727272, 'New', 'Vehicle', 2000, 'Green', 1000,      'Available',    'SUV',      'Cooler Rentals',  'West Vancouver')
update vehicle set color='RAINBOW' where vid=999
delete from vehicle where vid=999
*/

public class SetupAndQuery extends JFrame {
    private JButton dropTablesButton;
    private JTextField queryField;
    private JButton createTablesButton;
    private JButton populateTablesButton;
    private JButton submitQueryButton;
    private JTextArea resultTextArea;
    private JPanel rootPanel;

    public SetupAndQuery(DatabaseConnectionHandler db) {
        super("Setup and Query");
        this.setContentPane(rootPanel);
        this.pack();
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);

        dropTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean status = false;
                try {
                    status = db.firstTimeSetupBool(0);
                } catch (Exception ex) {
                    resultTextArea.setText("Failed to drop tables.");
                    ex.printStackTrace();
                }
                if (status) {
                    resultTextArea.setText("Tables dropped!");
                } else {
                    resultTextArea.setText("Failed to drop tables.");
                }
            }
        });
        createTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean status = false;
                try {
                    status = db.firstTimeSetupBool(1);
                } catch (Exception ex) {
                    resultTextArea.setText("Failed to create tables.");
                    ex.printStackTrace();
                }
                if (status) {
                    resultTextArea.setText("Tables created!");
                } else {
                    resultTextArea.setText("Failed to create tables.");
                }
            }
        });
        populateTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean status = false;
                try {
                    status = db.firstTimeSetupBool(2);
                } catch (Exception ex) {
                    resultTextArea.setText("Failed to populate tables.");
                    ex.printStackTrace();
                }
                if (status) {
                    resultTextArea.setText("Tables populated!");
                } else {
                    resultTextArea.setText("Failed to populate tables.");
                }
            }
        });

        submitQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String result = db.executeStringQuery(queryField.getText());
                    resultTextArea.setText(result);
                } catch (Exception ex) {
                    resultTextArea.setText("Query failed.");
                    ex.printStackTrace();
                }
            }
        });
    }

}