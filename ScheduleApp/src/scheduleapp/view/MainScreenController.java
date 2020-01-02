/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp.view;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduleapp.ScheduleApp;
import scheduleapp.model.Address;
import scheduleapp.model.Appointment;
import scheduleapp.model.City;
import scheduleapp.model.Country;
import scheduleapp.model.Customer;
import scheduleapp.model.DBConnection;
import static scheduleapp.model.DBConnection.conn;
import scheduleapp.model.Dataset;

/**
 * FXML Controller class
 *
 * @author erick
 */
public class MainScreenController implements Initializable {
    
    Stage stage;
    Parent root;
    private static Dataset allData;
    private static ObservableList<Customer> allCustomers;
    private static ObservableList<Address> allAddresses;
    private static ObservableList<City> allCities;
    private static ObservableList<Country> allCountries;
    private static ObservableList<Appointment> allAppointments;
    
    private static ObservableList<Appointment> sortedAppointments;
    private static ObservableList<String> sort;
    private static LocalDate sortDay;
    private static LocalDate sortMonth;
    private static LocalDate sortWeek;
    
    private static ObservableList<String> reportTypes;
    
    private static Customer thisCustomer;
    private static Appointment thisAppointment;
    
    @FXML
    private ListView<Customer> lstCustomers;
    @FXML
    private TableView<Appointment> tblAppointments;
    @FXML
    private TableColumn<Appointment, LocalDate> colDate;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> colStartTime;
    @FXML
    private TableColumn<Appointment, String> colCustomer;
    @FXML
    private TableColumn<Appointment, String> colLocation;
    @FXML
    private TableColumn<Appointment, String> colType;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> colEndTime;
    @FXML
    private ComboBox<String> cmbSort;
    @FXML
    private Button btnCreateAppointment;
    @FXML
    private Button btnModifyAppointment;
    @FXML
    private Button btnDeleteAppointment;
    @FXML
    private Button btnPrevious;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnNewCustomer;
    @FXML
    private Button btnModifyCustomer;
    @FXML
    private Button btnDeleteCustomer;
    @FXML
    private Button btnExit;
    @FXML
    private Button btnReport;
    @FXML
    private ComboBox<String> cmbReport;
    

    /**
     * Initializes the controller class.
     * @throws java.sql.SQLException
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
             
                 
      // connect to database
           try {
               //create database connection
               DBConnection.makeConnection();
                                         
               //check for valid login
               callLogin();
               
                           
        //populates all data
        setAllData();   
        
           
           }
           catch (Exception ex) {
               Alert alert = new Alert(Alert.AlertType.WARNING);
               alert.setTitle("Database Not Found");
               alert.setContentText("Database not found. Terminating application.");
               alert.showAndWait();
               ex.printStackTrace();
               //System.exit(0);
               
           }
           
           //populates sort list
           sortDay = LocalDate.now();
           sortMonth = LocalDate.now();
           sortWeek = LocalDate.now();
           sort = FXCollections.observableArrayList("all", "today", "this week", "this month");
           cmbSort.setItems(sort);
           cmbSort.getSelectionModel().select("all");
           
           //lambda uses to implement listener for the sort feature
           cmbSort.getSelectionModel().selectedItemProperty().addListener(event -> {
               sortDay = LocalDate.now();
               sortMonth = LocalDate.now();
               switch(cmbSort.getSelectionModel().getSelectedIndex()){
                   case 0: sortAll();
                   break;
                   case 1: sortDay();
                   break;
                   case 2: sortWeek();
                   break;
                   case 3: sortMonth();
                   break;
               }
           });
           
           //populates list of avaialble reports
           reportTypes = FXCollections.observableArrayList("Appointments Types by Month", "Appointment Schedule by User", "Active Customers");
           cmbReport.setItems(reportTypes);
           cmbReport.getSelectionModel().select(0);
           
           //checks for appointments within the next 15 minutes, and alerts the user. special thanks to the code in the repository!
           for(Appointment p: allAppointments){
               
               LocalDateTime now = LocalDateTime.now();
               LocalDateTime inFifteenMinutes = now.plusMinutes(15);
                              
               if(p.getStartTime().toLocalDateTime().isAfter(now) && p.getStartTime().toLocalDateTime().isBefore(inFifteenMinutes))
                   meetingAlert();
           }
       
       }
               
               
    @FXML
       private void onExit(ActionEvent event) throws ClassNotFoundException, SQLException {
            //close DBConnection upon any termination of application
                   DBConnection.closeConnection();
            System.exit(0);
    }

    @FXML
    private void onAddCustomer(ActionEvent event) throws IOException {
        stage = new Stage();
           root = FXMLLoader.load(getClass().getResource("AddCustomer.fxml"));
           stage.setScene(new Scene(root));
           stage.setTitle("Add Customer");
           stage.initModality(Modality.APPLICATION_MODAL);
           stage.showAndWait();
           
           //refreshes all data
         this.initialize(null, null);
    }

    
    @FXML
    private void onDelete (ActionEvent event) {
        thisCustomer = lstCustomers.getSelectionModel().getSelectedItem();

//will catch if an object has not been selected
        try{
            thisCustomer.getId();
        }
        catch(NullPointerException ex){
            selectCustomerAlert();
            return;
        }
        //asks for confirmation to delete
           //asks for comation to add the customer
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("CONFIRM DELETE");
        alertConfirm.setContentText("Are you sure you would like to delete this customer?");
       
        Optional<ButtonType> result = alertConfirm.showAndWait();
        
        
            thisCustomer.deleteCustomer();
        
        
        //refreshes all data
        this.initialize(null, null);
    }
    
    private void setAllData() {
               //creates new dataset object to parse data, then populates observable lists
           allData = new Dataset(); 
           
           allCustomers = FXCollections.observableArrayList(allData.getAllCustomers());
           allAddresses = FXCollections.observableArrayList(allData.getAllAddresses());
           allCities = FXCollections.observableArrayList(allData.getAllCities());
           allAppointments = FXCollections.observableArrayList(allData.getAllAppointments());
           
           //adds slections for new country, new city, and select country for the add and modify controllers     
           allCountries = FXCollections.observableArrayList(allData.getAllCountries());
                  Country newCountry = new Country("Add New Country", ScheduleApp.currentUser, ScheduleApp.currentUser);
                  Country selectCountry = new Country("Select Country", ScheduleApp.currentUser, ScheduleApp.currentUser);  
                    
                  City newCity = new City("Add New City", ScheduleApp.currentUser, ScheduleApp.currentUser);
                    newCity.setCountry(selectCountry);
                allCities.add(newCity);
                allCountries.add(newCountry);
                allCountries.add(selectCountry);
                  
          lstCustomers.setItems(allCustomers);
          
          
          //links table cells to variables
            colDate.setCellValueFactory(new PropertyValueFactory<>("displayDate"));
            colStartTime.setCellValueFactory(new PropertyValueFactory<>("displayStartTime"));
            colEndTime.setCellValueFactory(new PropertyValueFactory<>("displayEndTime"));
            colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
            colType.setCellValueFactory(new PropertyValueFactory<>("type"));
            colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
            
            //sets appointment list
          tblAppointments.setItems(allAppointments);
  
    }
    
    public ObservableList<City> getCities() {return allCities;}
    public ObservableList<Country> getCountries() {return allCountries;}
    public ObservableList<Customer> getAllCustomers() {return allCustomers;}
    public ObservableList<Appointment> getAllAppointments() {return allAppointments;}
    public Customer getCustomer() { return thisCustomer;}
    public Appointment getAppointment() {return thisAppointment;}
    
    private void callLogin() {
     //checks that no user is logged in yet
          if(!LoginScreenController.getValidLogin()){
       try {
           //calls login screen first
           stage = new Stage();
           root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
           stage.setScene(new Scene(root));
           stage.setTitle("Login");
           stage.initModality(Modality.APPLICATION_MODAL);
           stage.showAndWait();
       }
       catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
       
       //immediately terminates application if valid login not registered
       if(!LoginScreenController.getValidLogin())
           System.exit(0);
          }
          
    
}
        
    

    @FXML
    private void onModify(ActionEvent event) throws IOException {
           
           
        thisCustomer = lstCustomers.getSelectionModel().getSelectedItem();
        
        //will catch if an object has not been selected
        try{
            thisCustomer.getId();
        }
        catch(NullPointerException ex){
            selectCustomerAlert();
            return;
        }
       
          stage = new Stage();
           root = FXMLLoader.load(getClass().getResource("ModifyCustomer.fxml"));
           stage.setScene(new Scene(root));
           stage.setTitle("Modify");
           stage.initModality(Modality.APPLICATION_MODAL);
           stage.showAndWait();
        
           
           this.initialize(null, null);
        
    }

    @FXML
    private void onCreateAppointment(ActionEvent event) throws IOException {
        //selects the customer to work with
        thisCustomer = lstCustomers.getSelectionModel().getSelectedItem();
        
        //will catch if an object has not been selected
        try{
            thisCustomer.getId();
        }
        catch(NullPointerException ex){
            selectCustomerAlert();
            return;
        }

        //calls create appointment screen screen
        stage = new Stage();
           root = FXMLLoader.load(getClass().getResource("CreateAppointment.fxml"));
           stage.setScene(new Scene(root));
           stage.setTitle("Create Appointment");
           stage.initModality(Modality.APPLICATION_MODAL);
           stage.showAndWait();
           
           this.initialize(null, null);
    }
    
    private void selectCustomerAlert(){
        
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Customer Selected");
        alert.setContentText("Please select a customer");
        alert.showAndWait();
        
    }
    
    private void selectAppointmentAlert(){
        
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Appointment Selected");
        alert.setContentText("Please select an apppointment");
        alert.showAndWait();
        
    }
    
    private void meetingAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Upcoming meeting");
        alert.setContentText("You have a meeting within 15 minutes!");
        alert.showAndWait();
    }

    @FXML
    private void onModifyAppointment(ActionEvent event) throws IOException {
        thisAppointment = tblAppointments.getSelectionModel().getSelectedItem();
        
        //will catch if an object has not been selected
        try{
            thisAppointment.getAppointmentId();
        }
        catch(NullPointerException ex){
            selectAppointmentAlert();
            return;
        }
       
          stage = new Stage();
           root = FXMLLoader.load(getClass().getResource("ModifyAppointment.fxml"));
           stage.setScene(new Scene(root));
           stage.setTitle("Modify");
           stage.initModality(Modality.APPLICATION_MODAL);
           stage.showAndWait();
        
           
           this.initialize(null, null);
    }

    @FXML
    private void onDeleteAppointment(ActionEvent event) {
        thisAppointment = tblAppointments.getSelectionModel().getSelectedItem();

//will catch if an object has not been selected
        try{
            thisAppointment.getAppointmentId();
        }
        catch(NullPointerException ex){
            selectAppointmentAlert();
            return;
        }
        //asks for confirmation to delete
           //asks for comation to add the customer
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("CONFIRM DELETE");
        alertConfirm.setContentText("Are you sure you would like to delete this appointment?");
       
        Optional<ButtonType> result = alertConfirm.showAndWait();
        
      
        thisAppointment.deleteAppointment();
       
        
        //refreshes all data
        this.initialize(null, null);
    }
    
    private void sortAll(){
    tblAppointments.setItems(allAppointments);
    }
    
    private void sortDay() {
    
    //finds each appointment which day matches today's date
    
   
    sortedAppointments = FXCollections.observableArrayList();
    
    for(Appointment p: allAppointments){
        if ((p.getDate().getDayOfYear())==((sortDay.getDayOfYear()))) 
              sortedAppointments.add(p);
    }
      tblAppointments.setItems(sortedAppointments);
      tblAppointments.refresh();
    }
    
    private void sortWeek(){
        sortedAppointments = FXCollections.observableArrayList();
    
    for(Appointment p: allAppointments){
        LocalDate appointmentDate = p.getDate();
        LocalDate startWeek = sortWeek.minusWeeks(1);
        LocalDate endWeek = sortWeek.plusWeeks(1);
        if (appointmentDate.isAfter(startWeek)&&appointmentDate.isBefore(endWeek))
                         sortedAppointments.add(p);
    }
      tblAppointments.setItems(sortedAppointments);
      tblAppointments.refresh();
    }
    
    private void sortMonth() {
    
    sortedAppointments = FXCollections.observableArrayList();
    
    for(Appointment p: allAppointments){
        if ((p.getDate().getMonth())==((sortMonth.getMonth()))) 
              sortedAppointments.add(p);
    }
        tblAppointments.setItems(sortedAppointments);
        tblAppointments.refresh();
    }

    @FXML
    private void onPrevious(ActionEvent event) {
        if(cmbSort.getSelectionModel().getSelectedIndex()==1){
            sortDay = sortDay.minusDays(1);
            sortDay();
        }
        else if(cmbSort.getSelectionModel().getSelectedIndex()==2){
            sortWeek = sortWeek.minusWeeks(1);
            sortWeek();
        }
        else if(cmbSort.getSelectionModel().getSelectedIndex()==3){  
            
            sortMonth = sortMonth.minusMonths(1);
            sortMonth();
        }
            
        
    }

    @FXML
    private void onNext(ActionEvent event) {
         if(cmbSort.getSelectionModel().getSelectedIndex()==1){
            sortDay = sortDay.plusDays(1);
            sortDay();
        }
         else if(cmbSort.getSelectionModel().getSelectedIndex()==2){
            sortWeek = sortWeek.plusWeeks(1);
            sortWeek();
         }
         else if(cmbSort.getSelectionModel().getSelectedIndex()==3) {  
            
            sortMonth = sortMonth.plusMonths(1);
            sortMonth();
        }
    }

    private void onOpenLogin(ActionEvent event) {
        //opens the log file in the local text editor
        try{
            File log = new File("log.txt");
            Desktop.getDesktop().open(log);
                       
        }
        catch(IOException ex){
             Alert alertConfirm = new Alert(Alert.AlertType.WARNING);
        alertConfirm.setTitle("FILE NOT FOUND");
        alertConfirm.setContentText("Unable to open file");
        alertConfirm.showAndWait();
           
            
        }
    }

    @FXML
    private void onGenerateReport(ActionEvent event) {
      Alert alertConfirm = new Alert(Alert.AlertType.INFORMATION);
        alertConfirm.setTitle("Generating Report");
        alertConfirm.setContentText("Save file and open in Excel for best view");
        alertConfirm.showAndWait();
        
        switch(cmbReport.getSelectionModel().getSelectedIndex()){
           case 0:
               appointmentsByType();
               break;
           case 1:
               appointmentsByUser();
               break;           
           case 2:        
             customerReport();
             break;
       }
    }
    
    private void customerReport() {
        //writes, and then opens, a CSV file with the selected data
       
        File customerCSV = new  File("customer_report.csv");
        
        try(FileWriter report = new FileWriter(customerCSV, false);)
        {       
        
        PrintWriter output = new PrintWriter(report);
       
        output.println("Customer ID, Name, Address, Address2, City, Country");

//here's a lambda for you! ForEach is an easier way to iterate through a list
        allCustomers.forEach(t -> {
            
            //if customer is active, print to CSV
            if(t.getActive()==1){        
            output.print(t.getId());
            output.print(",");
            output.print(t.getCustomerName());
            output.print(",");
            output.print(t.getAddress().getAddress());
            output.print(",");
            output.print(t.getAddress().getAddress2());
            output.print(",");
            output.print(t.getAddress().getCity().getCity());
            output.print(",");
            output.print(t.getAddress().getCity().getCountry());
            output.println();
        }
        });
        //opens the file
           Desktop.getDesktop().open(customerCSV);
       }
       catch(Exception ex) {ex.printStackTrace();}
        
    }
    
    private void appointmentsByType(){
         //creates file and print witer objects
        ZoneId zone = ZoneId.systemDefault();
        
        File appointmentsCSV = new File("appointments_by_type.csv");
        
        try(FileWriter report = new FileWriter(appointmentsCSV, false);)
        {       
        
        PrintWriter output = new PrintWriter(report); 
        
       // queries all appointments and then sorts by type
                 
           Statement stmt = conn.createStatement();
           ResultSet rs =  stmt.executeQuery("SELECT type, start, COUNT(*) FROM appointment GROUP BY type ORDER BY start;");
           
        output.println("Month, Type, Number");
        
    while(rs.next()) {
    //extracts date and start time from the start timestamp
    ZonedDateTime start = rs.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
        output.print(start.toLocalDate().getMonth());
        output.print(",");
        
      //retrieves customer name from the customer table based on customerId
        output.print(rs.getString("type"));
        output.print(",");
        
      //retrieves the count
      output.print(rs.getInt("COUNT(*)"));
      output.println();
        }
    
     Desktop.getDesktop().open(appointmentsCSV);
                 
        }
    
    catch(Exception ex){ex.printStackTrace();}
        
    }
      
     private void appointmentsByUser(){
         //creates file and print witer objects
        ZoneId zone = ZoneId.systemDefault();
        
        File appointmentsCSV = new File("appointments_by_user.csv");
        
        try(FileWriter report = new FileWriter(appointmentsCSV, false);)
        {       
        
        PrintWriter output = new PrintWriter(report); 
        
       // queries all appointments and then sorts by type
                 
           Statement stmt = conn.createStatement();
           ResultSet rs =  stmt.executeQuery("SELECT userId, start, end, customerId, description, location, type FROM appointment ORDER BY userId;");
           
        output.println("User, Date, Start Time, End Time, Customer, Description, Location, Type");
        
    while(rs.next()) {
    //extracts userId and assocaites to name in user table
     int id = rs.getInt("userId");
    Statement stmt2 = conn.createStatement();
    ResultSet rs2 = stmt2.executeQuery("SELECT userName from user WHERE userId = " + id + ";");
        while(rs2.next()){
        output.print(rs2.getString("userName"));
        output.print(",");}
        

    //extracts date and start time from the start timestamp
    ZonedDateTime start = rs.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
        output.print(start.toLocalDate());
        output.print(",");
        output.print(start.toLocalTime());
        output.print(",");
    //end time
    ZonedDateTime end = rs.getTimestamp("end").toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
        output.print(end.toLocalTime());
        output.print(",");
    //retrieves customer name from the customer table based on customerId
    id = rs.getInt("customerId");
    Statement stmt3 = conn.createStatement();
    ResultSet rs3 = stmt3.executeQuery("SELECT customerName from customer WHERE customerId = " + id + ";");
        while(rs3.next()){
        output.print(rs3.getString("customerName"));
        output.print(",");}
    //prints rest of values
        output.print(rs.getString("description"));
        output.print(",");
        output.print(rs.getString("location"));
        output.print(",");
        output.print(rs.getString("type"));
        output.println();
        }
    
     Desktop.getDesktop().open(appointmentsCSV);
                 
        }
    
    catch(Exception ex){ex.printStackTrace();}
        
    }
      
}
        
    

