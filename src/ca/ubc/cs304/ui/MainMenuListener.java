package ca.ubc.cs304.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuListener implements ActionListener {

    private GuiMain gui;

    public MainMenuListener(GuiMain gui) {
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        switch (action) {
            case "main1":
                gui.createViewAvailableVehiclesWindow();
                System.out.print("available vehicles");
                break;
            case "main2":
                gui.createMakeReservationWindow();
                System.out.print("make reservation");
                break;
            case "main3":
                gui.createRentVehicleWindow();
                System.out.print("rent vehicle");
                break;
            case "main4":
                gui.createReturnVehicleWindow();
                System.out.print("return vehicle");
                break;
            case "main5":
                gui.createGeneralReportWindow("Rental");
                System.out.print("general rental report");
                break;
            case "main6":
                gui.createBranchReportWindow("Rental");
                System.out.print("branch rental report");
                break;
            case "main7":
                gui.createGeneralReportWindow("Returns");
                System.out.print("general return report");
                break;
            case "main8":
                gui.createBranchReportWindow("Returns");
                System.out.print("branch return report");
                break;
            case "main9":
                gui.createCustomerWindow();
                System.out.print("new customer");
                break;
            case "main10":
                gui.createTerminalAndSetupWindow();
                System.out.print("terminal and setup");
                break;
            default:
                gui.openWindows.pop(); //auto close most recent window
        }
    }
}
