package ca.ubc.cs304.ui;

import ca.ubc.cs304.controller.Customer;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class GuiMain implements LoginWindowDelegate {
    Customer customer;
    LoginWindow login;
    static DatabaseConnectionHandler db;
    private final Dimension standardWindowSize = new Dimension(600,400);
    private final Dimension standardButtonSize = new Dimension(50,20);
    Stack<JFrame> openWindows;
    MainMenuListener menuListener;


    public GuiMain(Customer customer, LoginWindow login) {
        this.customer = customer;
        db = new DatabaseConnectionHandler();
        this.login = login;
        login.showFrame(this);
        openWindows = new Stack<JFrame>();
        menuListener = new MainMenuListener(this);
    }

    public static void main(String[] args) {
        GuiMain app = new GuiMain(new Customer(), new LoginWindow());
    }


    @Override
    public void login(String username, String password) {
        boolean didConnect = db.login(username,password);
        if (didConnect) {
            // Once connected, remove login window and start text transaction flow
            login.dispose();
            mainMenuStart();

        } else {
            login.handleLoginFailed();

            if (login.hasReachedMaxLoginAttempts()) {
                login.dispose();
                System.out.println("You have exceeded your number of allowed attempts");
                System.exit(-1);
            }
        }
    }

    private void mainMenuStart() {
        createMainWindow();
    }

    private JFrame windowBuilder(Component[] components, boolean visible, LayoutManager layout) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setMinimumSize(standardWindowSize);
        window.setMaximumSize(standardWindowSize);
        window.setLayout(layout);
        for (Component c: components) {
            window.add(c);
        }
        window.setVisible(visible);
        return window;
    }

    private Component[] setupCustomerAndClerkActionButtons() {
        String[] actions = new String[]{"Search Available Vehicles", "Make Reservation","Rent Vehicle",
                "Return Vehicle", "All Daily Rentals Report","Daily Rentals by Branch", "Daily Returns", "Branch Daily Returns", "Create new Customer", "Terminal & Setup" };
        ArrayList<JButton> buttons = new ArrayList<>();
        int i = 1;
        for (String action : actions) {
            JButton b = new JButton(action);
            b.addActionListener(menuListener);
            b.setMinimumSize(standardButtonSize);
            b.setActionCommand("main" + i);
            buttons.add(b);
            i++;
        }
        System.out.print(buttons.size());
        return buttons.toArray(new JButton[]{});
    }


    public JFrame errorMessage(String message) { //use to show errors
        // TODO show message and provide back button
        return windowBuilder(null,false,null);
    }

    public JList TupleWindowgenerator(String[][] values,String[] attribute_header) {
        //TODO make this work
        JList output = new JList();
        for (String[] tuple: values) {

        }
        return null;
    }

    public void createMainWindow() {
        openWindows.removeAllElements();
        JFrame main = windowBuilder(setupCustomerAndClerkActionButtons(),true,new GridLayout(4,2));
        openWindows.push(main);
    }

    public JFrame createCustomerWindow() {
        //TODO 
        return null;
    }

    public JFrame createViewAvailableVehiclesWindow() {
        //TODO
        return null;
    }

    public JFrame createMakeReservationWindow() {
        //TODO
        return null;
    }

    public JFrame createRentVehicleWindow() {
        //TODO
        return null;
    }

    public JFrame createRentReceiptWindow() {
        //TODO
        return null;
    }

    public JFrame createReturnVehicleWindow() {
        //TODO
        return null;
    }

    public JFrame createReturnReceiptWindow() {
        //TODO
        return null;
    }
    public JFrame createGeneralReportWindow(String operation) {
        //Contains daily rentals AND Daily returns for all branches
        //TODO
        return null;
    }
    public JFrame createBranchReportWindow(String operation) {
        //Contains daily rentals AND Daily returns for a specific branch
        //TODO
        return null;
    }


    public JFrame createTerminalAndSetupWindow() {
        //TODO create a menu where you can initalize and populate tables and perform queries
        return null;
    }
}
