package ca.ubc.cs304.ui;

import ca.ubc.cs304.controller.CustomerActions;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.model.Branch;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Stack;

public class GuiMain implements LoginWindowDelegate {
    CustomerActions customer;
    LoginWindow login;
    static DatabaseConnectionHandler db;
    private final Dimension standardWindowSize = new Dimension(600,400);
    private final Dimension standardButtonSize = new Dimension(50,20);
    Stack<JFrame> openWindows;
    MainMenuListener menuListener;
    CustomerActionsListener customerListener;
    JButton results; //used so listener can change text of resulting vehicles
    JScrollPane tuples;
    JTextField car;
    JTextField location;
    JFormattedTextField from;
    JFormattedTextField to;
    boolean vehiclesChecked = false;

    public GuiMain(LoginWindow login) {
        db = new DatabaseConnectionHandler();
        this.login = login;
        login.showFrame(this);
        this.customer = new CustomerActions(db);
        openWindows = new Stack<JFrame>();
        menuListener = new MainMenuListener(this);
        customerListener = new CustomerActionsListener(this);
    }

    public static void main(String[] args) {
       GuiMain app = new GuiMain(new LoginWindow());
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
        try { //closes any window before it
            openWindows.pop().dispose();
        } catch (Exception ignored){ }
        JFrame main = windowBuilder(setupCustomerAndClerkActionButtons(),true,new GridLayout(5,2));
        openWindows.push(main);
    }

    public void createCustomerWindow() throws SQLException {
        //TODO
        customer.createNewCustomer(this);
    }



    public void createViewAvailableVehiclesWindow() throws ParseException {
        //todo
        openWindows.pop().dispose();
        // have fields asking for {car type, location, time interval} set default branch as well
        JTextField car_hint = new JTextField("Car Type:");
        car_hint.setEditable(false);
        car = new JTextField(20);
        JTextField location_hint = new JTextField("Location:");
        location_hint.setEditable(false);
        location = new JTextField(20);
        JTextField from_hint = new JTextField("From (yyyy-mm-dd-24HH-MM):");
        from_hint.setEditable(false);
        from = new JFormattedTextField(new MaskFormatter("####-##-##-##-##"));
        from.setColumns(15);
        JTextField to_hint = new JTextField("To (yyyy-mm-dd-24HH-MM):");
        to_hint.setEditable(false);
        to = new JFormattedTextField(new MaskFormatter("####-##-##-##-##"));
        to.setColumns(15);
        //connecting hints and input field
        JPanel carStuff = new JPanel();
        carStuff.add(car_hint);
        carStuff.add(car);
        JPanel locationStuff = new JPanel();
        locationStuff.add(location_hint);
        locationStuff.add(location);
        JPanel dateStuff = new JPanel();
        dateStuff.add(from_hint);
        dateStuff.add(from);
        JPanel timeStuff = new JPanel();
        timeStuff.add(to_hint);
        timeStuff.add(to);
        // have clickable number shown
            results = new JButton("Results");
            results.setActionCommand("Show Details");
            results.addActionListener(customerListener);
            JButton findVehicles = new JButton("Find Vehicles");
            findVehicles.setActionCommand("Get Vehicles");
            findVehicles.addActionListener(customerListener);
            JPanel clickable = new JPanel();
            clickable.add(findVehicles);
            clickable.add(results);
        // have scrollable tuples shown
        tuples = new JScrollPane();
        tuples.setVisible(false);


        Component[] items = new Component[]{carStuff,locationStuff,dateStuff,timeStuff,clickable,tuples};

        JFrame window = windowBuilder(items,true,new GridLayout(4,2));
        window.setLayout(new GridLayout(4,2));
        window.setTitle("View Available Vehicles");
        openWindows.push(window);

    }

    public void getVehiclesShow() throws SQLException {
        if (vehiclesChecked) {
            customer.retrieveVehicles();
        }
    }

    public void getVehiclesParse() throws SQLException {
        String typeText;
        String locationText;
        LocalDateTime fromText;
        LocalDateTime toText;
        if (car.getText().isEmpty()) {
            typeText = null;
        }else {
            typeText = car.getText();
        }
        if (location.getText().isEmpty()) {
            locationText = null;
        } else {
            locationText = location.getText();
        }
        if (from.getText().isEmpty() | to.getText().isEmpty()) {
            fromText = null;
            toText = null;
        }else {
            try {
                //TODO format dates
                if (validateDate(from.getText()) & validateDate(to.getText())) {
                    fromText = createDate(from.getText());
                    toText = createDate(to.getText());
                } else {
                    fromText = null;
                    toText = null;
                }
            } catch (Exception e ) {
                fromText = null;
                toText = null;
            }
        }
        int resultingVehicles = customer.viewNumberOfVehicles(typeText,locationText,fromText,toText,new Branch("Cool Rentals", "East Vancouver"));
        vehiclesChecked = true;
        results.setText(Integer.toString(resultingVehicles));
    }

    private boolean validateDate(String date) {
        String[] output = date.split("-");
        Integer[] input = new Integer[output.length];
        int i = 0;
        try {
            for (String o : output) {
                input[i] = Integer.parseInt(o);
                i++;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        if(input[0] < 1970) {
            return false;
        }

        if (input[1] > 12 | input[1] < 1) {
            return false;
        }

        if (input[2] > 31 | input[2] < 1) {
            return false;
        }

        if( input[3] > 24 | input[3] < 0) {
            return false;
        }

        if( input[4] > 59 | input[4] < 0) {
            return false;
        }


        return true;
    }

    //REQUIRES: Validated date string
    public LocalDateTime createDate(String date) {
        String[] output = date.split("-");
        Integer[] input = new Integer[output.length];
        int i = 0;
        for(String o :output) {
            input[i] = Integer.parseInt(o);
            i++;
        }
        return LocalDateTime.of(input[0],input[1],input[2],input[3],input[4]);
    }

    public void createMakeReservationWindow() {
        //TODO
        openWindows.pop().dispose();
        //ask for driverslicense number and open create customer window if new
        //make reservation form and then show conformation number if no error
       // openWindows.push(windowBuilder());
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
