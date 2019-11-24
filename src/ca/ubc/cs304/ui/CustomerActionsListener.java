package ca.ubc.cs304.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;

public class CustomerActionsListener implements ActionListener {
    GuiMain guiMain;

    public CustomerActionsListener(GuiMain guiMain) {
        this.guiMain = guiMain;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Get Vehicles")) {
            try {
                guiMain.getVehiclesParse();
                guiMain.tuples.setVisible(false);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (command.equals("Show Details")) {
            try {
                guiMain.getVehiclesShow();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }  else if (command.equals("Reserve")) {
            try {
                guiMain.reserveVehicle();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (command.equals("Create")) {
            guiMain.createCustomer();
        } else if (command.equals("reset")) {
            try {
                guiMain.resetAvailableFields();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }


    }
}
