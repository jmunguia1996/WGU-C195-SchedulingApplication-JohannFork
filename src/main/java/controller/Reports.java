package controller;

import applicationObject.*;
import applicationTools.CChoulesDevTools;
import dataAccessObject.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class Reports {

    private static Stage mainStage; //This is for page refresh. May not work with tabs


    //CUSTOMERS TAB\\
    public TableView<totalsReport> customersByCountryTable;
    public TableColumn<?, ?> customerByCountryName;
    public TableColumn<?, ?> customerByCountryNumber;
    public TableView<totalsReport> customersByDivisionTable;
    public TableColumn<?, ?> customersByDivisionName;
    public TableColumn<?, ?> customersByDivisionNumber;
    public TableView<totalsReport> customersGainedByMonthTable;
    public TableColumn<?, ?> customersGainedByMonthName;
    public TableColumn<?, ?> customersGainedByMonthNumber;
    //APPOINTMENT TOTALS TAB\\
    public Tab apTotalsTab;
    public TableView<totalsReport> AppointmentsByCountry;
    public TableColumn<?, ?> countryName;
    public TableColumn<?, ?> countryCounter;
    public TableView<totalsReport> appointmentsByTypeTable;
    public TableColumn<?, ?> apTotalsApTypeCol;
    public TableColumn<?, ?> apTotalsTypeTotalCol;
    public TableView<totalsReport> appointmentsByMonthTable;
    public TableColumn<?, ?> apTotalsByMonth;
    public TableColumn<?, ?> apTotalsMonthTotal;

    public Reports() throws SQLException {
    }


    //create a class to make object totals report

    public static class totalsReport {
        public String byWhat;
        public int howMany;

        totalsReport(String byWhat, int howMany) {
            this.byWhat = byWhat;
            this.howMany = howMany;
        }

        //Setter & Getter for byWhat
        public String getByWhat() { return byWhat; }
        public void setByWhat(String byWhat) { this.byWhat = byWhat; }

        //Getter & Setter for howMany
        public int getHowMany() { return howMany; }
        public void setHowMany(int howMany) { this.howMany = howMany; }
        public void addToHowMany(int howMany) {this.howMany = this.howMany + howMany;}

    }

    /*TODO [] Create
        Init
        combos
        update on combo select
     */

    public Button backToHomeFromRp;
    public TableView<applicationObject.Appointment> allApTable;
    public TableColumn<?, ?> apTitleRp;
    public TableColumn<?, ?> apTypeRp;
    public TableColumn<?, ?> apIdRp;
    public TableColumn<?, ?> apLocationRp;
    public TableColumn<?, ?> apDescriptionRp;
    public TableColumn<?, ?> apStartRp;
    public TableColumn<?, ?> apEndRp;
    public TableColumn<?, ?> apCustomerIdRp;
    public TableColumn<?, ?> apContactRp;
    public TableColumn<?, ?> apContactIdRp;
    public ComboBox<String> contactScheduleContactBox;
    public ComboBox<String> apHashTagSelection;
    public Tab rpCustomerByCountry;

    public void backToHomeAction(ActionEvent actionEvent) {
    }

    public void apDataByContact(ActionEvent actionEvent) {
    }

    public void addTagToSearch(ActionEvent actionEvent) {
    }

    public void customerByCountry(Event event) {
    }

    public void apTotalsTab(Event event) {
    }



    //BACK END METHODS\\

    //FXML LOADER METHOD//
    public static void loadThisFXML(Stage stage) throws IOException {

        Home.testMethod();
        //Home.loadHomeFXML(loginButton, getClass());

        CChoulesDevTools.println("Loading Reports.fxml");

        //Note: this is incorrect I keep doing this bellow:
        //FXMLLoader loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/controller/HomeMenu.fxml")));
        FXMLLoader loader = new FXMLLoader(controller.Reports.class.getResource("/views/Reports.fxml"));


        Parent root = loader.load();

        controller.Reports mp = loader.getController();

//        //passing the css settings
//        mp.passCss(cssPath, darkModeOn);


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        mainStage = stage;
    }



    public void initialize() throws SQLException {
        //Call populate Tables
        loadApByContactTable();
        loadTotalsReportTable(customersByCountryList(), customersByCountryTable, customerByCountryName, customerByCountryNumber);
        loadTotalsReportTable(customersByDivisionList(), customersByDivisionTable, customersByDivisionName, customersByDivisionNumber);
        loadTotalsReportTable(customersByMonthList(), customersGainedByMonthTable, customersGainedByMonthName, customersGainedByMonthNumber);
        loadTotalsReportTable(appointmentsByMonthList(), appointmentsByMonthTable, apTotalsByMonth, apTotalsMonthTotal);
        loadTotalsReportTable(appointmentsByTypeList(), appointmentsByTypeTable, apTotalsApTypeCol, apTotalsTypeTotalCol);
        //Call populate Combos


    }

    public void loadApByContactTable() throws SQLException {
        ObservableList<Appointment> appointmentList = AppointmentDAO.getAllAppointments();

        apIdRp.setCellValueFactory(new PropertyValueFactory<>("apId"));
        apTitleRp.setCellValueFactory(new PropertyValueFactory<>("apTitle"));
        apTypeRp.setCellValueFactory(new PropertyValueFactory<>("apType"));
        apLocationRp.setCellValueFactory(new PropertyValueFactory<>("apLocation"));
        apDescriptionRp.setCellValueFactory(new PropertyValueFactory<>("apDescription"));
        apStartRp.setCellValueFactory(new PropertyValueFactory<>("apStart")); //TODO [l] Create a getApStartClean method to clean start and end values to table format.
        apEndRp.setCellValueFactory(new PropertyValueFactory<>("apEnd"));
        apCustomerIdRp.setCellValueFactory(new PropertyValueFactory<>("apCustomerId"));
        apContactRp.setCellValueFactory(new PropertyValueFactory<>("apContactName"));
        apContactIdRp.setCellValueFactory(new PropertyValueFactory<>("apContactId"));

        allApTable.setItems(appointmentList); //Set the data to the table
    }

    public void loadTotalsReportTable(ObservableList<totalsReport> totalsList, TableView<totalsReport> theTable, TableColumn<?, ?> byWhat,TableColumn<?, ?> howMany) {

        //setting the values of the tables
        byWhat.setCellValueFactory(new PropertyValueFactory<>("byWhat"));
        howMany.setCellValueFactory(new PropertyValueFactory<>("howMany"));

        theTable.setItems(totalsList); //Set the data to the table
    }

    public ObservableList<totalsReport> customersByCountryList() throws SQLException {
        //use this to load the customersByCountryTable
        //List of totals to be returned after totals are added
        ObservableList<totalsReport> reportList = FXCollections.observableArrayList();
        //List of Customers to count from
        ObservableList<applicationObject.Customer> allCustomers = CustomerDAO.getAllCustomers();
        //List of Countries to count for
        ObservableList<Country> allCountries = CountryDAO.getAllCountries();
        //Creating a report for each country and counting the corresponding customers by comparing countryId
        for (Country country : allCountries) {
            int numberOfCustomers = 0;
            //Loop through all customers and count the ones with matching countryId
            for (applicationObject.Customer customer : allCustomers) {
                //TODO [Extra] this is using name when ID would be faster
                if (customer.getCustomerCountryName().equals(country.getCountryName())) {
                    numberOfCustomers++;
                }
            }
            // Create a totalsReport object and add it to the reportList
            totalsReport report = new totalsReport(country.getCountryName(), numberOfCustomers);
            reportList.add(report);
        }
        return reportList;
    }

    public ObservableList<totalsReport> customersByDivisionList() throws SQLException {
        //use this to load the customersByContactTable
        //List of totals to be returned after totals are added
        ObservableList<totalsReport> reportList = FXCollections.observableArrayList();
        //List of Customers to count from
        ObservableList<applicationObject.Customer> allCustomers = CustomerDAO.getAllCustomers();
        //List of Countries to count for
        ObservableList<FirstLvlDivision> allDivisions = FirstLvlDivisionDAO.getAllFirstLvlDivisions();
        //Creating a report for each contact and counting the corresponding customers by comparing contactId
        for (FirstLvlDivision division : allDivisions) {
            int numberOfCustomers = 0;
            //Loop through all customers and count the ones with matching countryId
            for (applicationObject.Customer customer : allCustomers) {
                //TODO [Extra] this is using name when ID would be faster
                if (customer.getCustomerDivisionId() == division.getId()) {
                    numberOfCustomers++;
                }
            }
            // Create a totalsReport object and add it to the reportList
            if (numberOfCustomers > 0) {
                totalsReport report = new totalsReport(division.getDivisionName(), numberOfCustomers);
                reportList.add(report);
            }
        }
        return reportList;
    }
    public ObservableList<totalsReport> appointmentsByTypeList() throws SQLException {
        //use this to load the appointmentsByContactTable
        //List of totals to be returned after totals are added
        ObservableList<totalsReport> reportList = FXCollections.observableArrayList();
        //List of Appointments to count from
        ObservableList<Appointment> allAppointments = AppointmentDAO.getAllAppointments();
        //List of Countries to count for

        //create list of appointment types
        ObservableList<String> availableTypes = FXCollections.observableArrayList();
        for (Appointment appointment : allAppointments){
            String type = appointment.getApType();
            if (availableTypes.contains(type)){
                continue;
            }
            availableTypes.add(type);
        }
        //Creating a report for each contact and counting the corresponding appointments by comparing contactId
        for (String type : availableTypes) {
            int numberOfAppointments = 0;
            //Loop through all appointments and count the ones with matching countryId
            for (Appointment appointment : allAppointments) {

                if (appointment.getApType().equals(type)) {
                    numberOfAppointments++;
                }
            }
            // Create a totalsReport object and add it to the reportList
            if (numberOfAppointments > 0) {
                totalsReport report = new totalsReport(type, numberOfAppointments);
                reportList.add(report);
            }
        }
        return reportList;
    }


    public ObservableList<totalsReport> customersByMonthList() throws SQLException {
        //use this to load the customersByContactTable
        //List of totals to be returned after totals are added
        ObservableList<totalsReport> reportList = FXCollections.observableArrayList();
        //List of Customers to count from
        ObservableList<applicationObject.Customer> allCustomers = CustomerDAO.getAllCustomers();
        //List of Countries to count for
        //ObservableList<Month> allMonths = FXCollections.observableArrayList();
        //allMonths.add(Month.JUNE, Month.JUNE);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM.yyyy");
        int customerCounter = 0;
        totalsReport currentReport = new totalsReport("noMonthFound", 0);


        ObservableList<String> allMonthYearUsed = FXCollections.observableArrayList();
        for (applicationObject.Customer customer : allCustomers) {
            //Check to see if we have moved on to a new month
            String monthName = customer.getCreateDate().format(dtf);

            if (!currentReport.byWhat.equals("noMonthFound")){ //For it not to run on start
                if (!currentReport.byWhat.equals(monthName)) { //If new month detected add last report.
                    if (currentReport.howMany > 0) {
                        reportList.add(currentReport);
                        CChoulesDevTools.println("Adding " + currentReport.byWhat);
                    }
                    currentReport = new totalsReport(monthName, 1);
                    allMonthYearUsed.add(monthName);
                    continue;
                }
            }

            //If month matches
            if (allMonthYearUsed.contains(monthName)) {
                currentReport.addToHowMany(1);
                CChoulesDevTools.println("Counting +1");
                continue;
            }

            //Only runs on start
            currentReport = new totalsReport(monthName, 1);
            allMonthYearUsed.add(monthName);

        }
        if (currentReport.howMany > 0) {
            reportList.add(currentReport);
        }

        return reportList;

    }
    public ObservableList<totalsReport> appointmentsByMonthList() throws SQLException {

        ObservableList<totalsReport> reportList = FXCollections.observableArrayList();

        ObservableList<Appointment> allAppointments = AppointmentDAO.getAllAppointments();


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM.yyyy");


        totalsReport currentReport = new totalsReport("noMonthFound", 0);


        ObservableList<String> allMonthYearUsed = FXCollections.observableArrayList();
        for (Appointment appointment : allAppointments) {
            //Check to see if we have moved on to a new month
            String monthName = appointment.getApStart().format(dtf);

            if (!currentReport.byWhat.equals("noMonthFound")){ //For it not to run on start
                if (!currentReport.byWhat.equals(monthName)) { //If new month detected add last report.
                    if (currentReport.howMany > 0) {
                        reportList.add(currentReport);
                        CChoulesDevTools.println("Adding " + currentReport.byWhat);
                    }
                    currentReport = new totalsReport(monthName, 1);
                    allMonthYearUsed.add(monthName);
                    continue;
                }
            }

            //If month matches
            if (allMonthYearUsed.contains(monthName)) {
                currentReport.addToHowMany(1);
                CChoulesDevTools.println("Counting +1");
                continue;
            }

            //Only runs on start
            currentReport = new totalsReport(monthName, 1);
            allMonthYearUsed.add(monthName);

        }
        if (currentReport.howMany > 0) {
            reportList.add(currentReport);
        }

        return reportList;

    }

    //public void loadAp










}
