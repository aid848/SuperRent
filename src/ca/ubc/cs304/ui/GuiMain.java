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
    DefaultListModel tupleModel;
    JList tuplesList;
    JTextField car;
    JTextField location;
    JFormattedTextField from;
    JFormattedTextField to;
    boolean vehiclesChecked = false;
    JPanel tupleArea;
    LocalDateTime fromText;
    LocalDateTime toText;
    JTextArea phone;
    JTextArea address;
    JTextArea dL;
    JTextArea name;
    JTextField vtName;
    JTextField DL;
    JFormattedTextField fromReserve;
    JFormattedTextField toReserve;
    JFrame reservationWindow;


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
        //buttons.remove(1);
        System.out.print(buttons.size());
        return buttons.toArray(new JButton[]{});
    }


    public void errorMessage(String message) { //use to show errors
        JTextArea messageTxt = new JTextArea(message);
        messageTxt.setEditable(false);
        JFrame error = new JFrame();
        error.add(messageTxt);
        error.setMinimumSize(new Dimension(150,150));
        error.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        error.setAlwaysOnTop(true);
        error.setVisible(true);

    }



    public void createMainWindow() {
        try { //closes any window before it
            openWindows.pop().dispose();
        } catch (Exception ignored){ }
        JFrame main = windowBuilder(setupCustomerAndClerkActionButtons(),true,new GridLayout(5,2));
        openWindows.push(main);
    }


    public void createCustomerWindow() throws SQLException {
        JButton createCustomer = new JButton("Create");
        createCustomer.addActionListener(customerListener);
        JTextArea phone_hint = new JTextArea("Phone #:");
        phone_hint.setEditable(false);
        phone = new JTextArea(1,20);
        JTextArea name_hint = new JTextArea("Name:");
        name_hint.setEditable(false);
        name = new JTextArea(1,20);
        JTextArea address_hint = new JTextArea("Address:");
        address_hint.setEditable(false);
        address = new JTextArea(1,20);
        JTextArea dL_hint = new JTextArea("DL #");
        dL_hint.setEditable(false);
        dL = new JTextArea(1,20);
        JPanel phoneP = new JPanel();
        phoneP.add(phone_hint);
        phoneP.add(phone);
        JPanel nameP = new JPanel();
        nameP.add(name_hint);
        nameP.add(name);
        JPanel addressP = new JPanel();
        addressP.add(address_hint);
        addressP.add(address);
        JPanel dLp = new JPanel();
        dLp.add(dL_hint);
        dLp.add(dL);

        Component[] components = new Component[]{phoneP,nameP,addressP,dLp,createCustomer};
        JFrame customerWindow = windowBuilder(components,true,new GridLayout(3,2));
        customerWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        openWindows.push(customerWindow);
    }



    public void createViewAvailableVehiclesWindow() throws ParseException {
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
//            JButton reserveVehicles = new JButton("Reserve vehicle");
//            reserveVehicles.setActionCommand("Reserve");
//            reserveVehicles.addActionListener(customerListener);
            JPanel clickable = new JPanel();

//            clickable.add(reserveVehicles);
            clickable.add(findVehicles);
            clickable.add(results);
        // have scrollable tuples shown
        tupleModel = new DefaultListModel();
        tuplesList = new JList(tupleModel);
        tuples = new JScrollPane(tuplesList);
        tuples.setVisible(false);


        JTextArea header = new JTextArea("Tuple legend: VID|License|Make|Model|Year|Color|Odometer|Type|Location|City");


        clickable.add(header);
        Component[] items = new Component[]{carStuff,locationStuff,dateStuff,timeStuff,clickable,tuples};

        JFrame window = windowBuilder(items,false,new GridLayout(4,2));
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.setLayout(new GridLayout(4,2));
        window.setTitle("View Available Vehicles");
        openWindows.push(window);
        window.setMinimumSize(new Dimension(900,400));
        window.repaint();
        window.setVisible(true);
    }

    public void getVehiclesShow() throws SQLException {
        if (vehiclesChecked) {
            tupleModel.removeAllElements();
            for (String tuple: customer.retrieveVehicles()) {
                tupleModel.addElement(tuple);
            }

            tuples.setVisible(true);
        }
    }

    public void getVehiclesParse() throws SQLException {
        String typeText;
        String locationText;
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

    public void createMakeReservationWindow(String vehicleType, String locationName, LocalDateTime fromDate, LocalDateTime toDate, Branch branch) {
        //ask for driverslicense number and open create customer window if new
        //make reservation form and then show conformation number if no error
       // openWindows.push(windowBuilder());
        System.out.println(vehicleType);
        System.out.println(locationName);
        System.out.println(fromDate);
        System.out.println(toDate);
        System.out.println(branch);

    }

    public void createMakeReservationWindow() throws ParseException { //TO make reservation confno vtname, dlicense, fromdataTime, todateTime

        JTextField DL_hint = new JTextField("Driver's License #:");
        DL_hint.setEditable(false);
        DL = new JTextField(20);
        JTextField vtName_hint = new JTextField("Vehicle Type:");
        vtName_hint.setEditable(false);
        vtName = new JTextField(20);
        JTextField from_hint = new JTextField("From (yyyy-mm-dd-24HH-MM):");
        from_hint.setEditable(false);
        fromReserve = new JFormattedTextField(new MaskFormatter("####-##-##-##-##"));
        fromReserve.setColumns(15);
        JTextField to_hint = new JTextField("To (yyyy-mm-dd-24HH-MM):");
        to_hint.setEditable(false);
        toReserve = new JFormattedTextField(new MaskFormatter("####-##-##-##-##"));
        toReserve.setColumns(15);
        //connecting hints and input field
        JPanel dLthings = new JPanel();
        dLthings.add(DL_hint);
        dLthings.add(DL);
        JPanel vtNamethings = new JPanel();
        vtNamethings.add(vtName_hint);
        vtNamethings.add(vtName);
        JPanel dateStuff = new JPanel();
        dateStuff.add(from_hint);
        dateStuff.add(fromReserve);
        JPanel timeStuff = new JPanel();
        timeStuff.add(to_hint);
        timeStuff.add(toReserve);
        //buttons
        JButton reserve = new JButton("Reserve");
        reserve.addActionListener(customerListener);
        Component[] c = new Component[]{dLthings,vtNamethings,dateStuff,timeStuff,reserve};



        reservationWindow = windowBuilder(c,true,new GridLayout(3,2));
        reservationWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        

    }

    public void createRentVehicleWindow() {
        //TODO
        Rent rent = new Rent(db);
        rent.setVisible(true);
    }
    public JFrame createRentReceiptWindow() {
        //TODO
        return null;
    }

    public void createReturnVehicleWindow() {
        //TODO
        Return ret = new Return(db);
        ret.setVisible(true);
    }
    public JFrame createReturnReceiptWindow() {
        //TODO
        return null;
    }
    public void createGeneralReportWindow(String operation) {
        //Contains daily rentals AND Daily returns for all branches
        DailyReports dailyReports = new DailyReports(db);
        dailyReports.setVisible(true);
    }

    public void createBranchReportWindow(String operation) {
        //Contains daily rentals AND Daily returns for a specific branch
        DailyReportsByBranch dailyReportsByBranch = new DailyReportsByBranch(db);
        dailyReportsByBranch.setVisible(true);
    }

    public void createTerminalAndSetupWindow() {
        //initalize and populate tables and perform queries
        SetupAndQuery setupAndQuery = new SetupAndQuery(db);
        setupAndQuery.setVisible(true);
    }

    public void reserveVehicle() throws SQLException {
        boolean pass = true; //checks info is suitable
        int dNum = 0;
        if ((DL.getText().isEmpty()) || (vtName.getText().isEmpty()) || (fromReserve.getText().isEmpty()) || (toReserve.getText().isEmpty())) {
            pass = false;
        } else if (!validateDate(fromReserve.getText()) || !validateDate(toReserve.getText())) {
            pass = false;
        }
        try {
            dNum = Integer.parseInt(DL.getText());
        } catch (NumberFormatException e) {
            pass = false;
        }

        if (!pass) {
            errorMessage("Info is not valid");
            return;
        }

        if(!customer.checkForExistingCustomer(dNum)) {
            errorMessage("Customer does not exist");
            createCustomerWindow();
            return;
        }

        int confNo = customer.makeReservation(vtName.getText(),dNum,createDate(fromReserve.getText()), createDate(toReserve.getText()));

        if (confNo == -1){
            errorMessage("Vehicle Type unavailable");
            return;
        }


        reservationWindow.dispose();

        JTextArea dLconf = new JTextArea("Drivers License:" + dNum);
        dLconf.setEditable(false);
        JTextArea fromconf = new JTextArea("From Date:" + fromReserve.getText());
        fromconf.setEditable(false);
        JTextArea toconf = new JTextArea("To Date:" + toReserve.getText());
        toconf.setEditable(false);
        JTextArea confnum = new JTextArea("Confirmation #" + confNo);
        confnum.setEditable(false);

        Component[] c = new Component[]{dLconf,fromconf,toconf,confnum};
        JFrame receipt = windowBuilder(c,true,new GridLayout(4,1));
        receipt.setTitle("Reservation Success!");
        receipt.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void createCustomer() {

        try {
            customer.createNewCustomer(Long.parseLong(phone.getText()),name.getText(),address.getText(),Integer.parseInt(dL.getText()));
            openWindows.pop().dispose();
            errorMessage("Success");
            createMainWindow();
        } catch (SQLException e) {
            errorMessage("Customer already exists");
            openWindows.pop().dispose();
            try {
                createCustomerWindow();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            errorMessage("invalid data entered!");
            openWindows.pop().dispose();
            try {
                createCustomerWindow();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}
