package ca.ubc.cs304.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (command.equals("Show Details")) {
            try {
                guiMain.getVehiclesShow();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }


    }
}
