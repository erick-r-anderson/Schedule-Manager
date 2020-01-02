/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp.view;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import scheduleapp.ScheduleApp;
import scheduleapp.model.Appointment;
import scheduleapp.model.Customer;

/**
 * FXML Controller class
 *
 * @author erick
 */
public class CreateAppointmentController implements Initializable {

  
    @FXML
    private ComboBox<Customer> cmbCustomer;
    @FXML
    private DatePicker dtpMeetingDate;
    @FXML
    private Button btnCreateAppointment;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtName;
    @FXML
    private TextArea txtDescription;
    private ComboBox<ZonedDateTime> cmbStart;
    
    LocalDate date;
    Customer customer;
    Appointment thisAppointment;
  
    String appointmentName;
    String description;
    String location;
    ObservableList<String> hourList;
    ObservableList<String> minList;
    LocalDateTime meeting;
    String startHour;
    String startMinute;
    String endHour;
    String endMinute;
    
    
    
    @FXML
    private ComboBox<String> cmbStartHour;
    @FXML
    private ComboBox<String> cmbStartMin;
    @FXML
    private ComboBox<String> cmbEndHour;
    @FXML
    private ComboBox<String> cmbEndMin;
    @FXML
    private TextField txtLocation;
    @FXML
    private TextField txtType;
   
        
    /**
     * Initializes the controller class.
     * @throws java.lang.Exception
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //populates customers combo box with all customers, then sets the customer selected by the user on the main screen
        MainScreenController controller = new MainScreenController();
        
        cmbCustomer.setItems(controller.getAllCustomers());
        cmbCustomer.getSelectionModel().select(controller.getCustomer());
                
        //configures date picker to diallow choosing saturday or sunday dates
        //credit for this solution goes to http://www.java2s.com/Tutorials/Java/JavaFX/0540__JavaFX_DatePicker.htm
        dtpMeetingDate.setDayCellFactory(event -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
            }
        });
        
        //defaults the date picker to today
        LocalDate today = LocalDate.now();
        dtpMeetingDate.setValue(today);
        
//populates time combo boxes 
        
        int i;
     for(i=9; i < 17; i++){
         cmbStartHour.getItems().add(Integer.toString(i));
     }
     for(i=9; i < 17; i++){
         cmbEndHour.getItems().add(Integer.toString(i));
     }
     
        cmbStartMin.getItems().addAll("00", "15", "30", "45" );
        cmbEndMin.getItems().addAll("00", "15", "30", "45" );
       
     cmbStartHour.getSelectionModel().select(0);
     cmbStartMin.getSelectionModel().select(0);
     cmbEndHour.getSelectionModel().select(1);
     cmbEndMin.getSelectionModel().select(0);
     
     //all of the following listeners should prevent every case of the user setting a meeting time that ends before the start time
     //automatically defaults end hour to one hour later than selected start hour
     cmbStartHour.getSelectionModel().selectedIndexProperty().addListener(event -> {
         if(cmbStartHour.getSelectionModel().getSelectedIndex() < 16)
             cmbEndHour.getSelectionModel().select(cmbStartHour.getSelectionModel().getSelectedIndex() + 1);
             });
     //sets half hour meeting by default
     cmbStartMin.getSelectionModel().selectedIndexProperty().addListener(event -> {
         switch(cmbStartMin.getSelectionModel().getSelectedIndex()){
             case 0: cmbEndMin.getSelectionModel().select(2);
             break;
             case 1: cmbEndMin.getSelectionModel().select(3);
             break;
             case 2: cmbEndMin.getSelectionModel().select(0);
                cmbEndHour.getSelectionModel().select((cmbStartHour.getSelectionModel().getSelectedIndex())+1);
                break;
             case 3: cmbEndMin.getSelectionModel().select(1);
                cmbEndHour.getSelectionModel().select((cmbStartHour.getSelectionModel().getSelectedIndex())+1);
         }                        
             });
     
     //prevents the end time from being before the start time
     cmbEndHour.getSelectionModel().selectedIndexProperty().addListener(event -> {
         if((cmbEndHour.getSelectionModel().getSelectedIndex()) < (cmbStartHour.getSelectionModel().getSelectedIndex()))
             cmbEndHour.getSelectionModel().select((cmbEndHour.getSelectionModel().getSelectedIndex())+1);
     });
     
     //the next two statements prevent setting the end minutes to be less than the start minutes within the same hour
     cmbEndHour.getSelectionModel().selectedIndexProperty().addListener(event -> {
         if((cmbEndHour.getSelectionModel().getSelectedIndex()) == (cmbStartHour.getSelectionModel().getSelectedIndex()) &&
               (cmbEndMin.getSelectionModel().getSelectedIndex()) < (cmbStartMin.getSelectionModel().getSelectedIndex())  )
             cmbEndHour.getSelectionModel().select((cmbEndHour.getSelectionModel().getSelectedIndex())+1);
     });
     
     cmbStartHour.getSelectionModel().selectedIndexProperty().addListener(event -> {
         if((cmbEndHour.getSelectionModel().getSelectedIndex()) == (cmbStartHour.getSelectionModel().getSelectedIndex()) &&
               (cmbEndMin.getSelectionModel().getSelectedIndex()) < (cmbStartMin.getSelectionModel().getSelectedIndex())  )
             cmbEndHour.getSelectionModel().select((cmbEndHour.getSelectionModel().getSelectedIndex())+1);
     });
     
    }
    



    @FXML
    private void onDateSelected(ActionEvent event) {
        date = dtpMeetingDate.getValue();
  
    }

    @FXML
    private void onCreateAppointment(ActionEvent event) {
           //checks all fields for null values
        if(txtName.getText().isEmpty()){
            ScheduleApp.alert("Appointment Name");
            return;}
      
        if(txtDescription.getText().isEmpty()){
            ScheduleApp.alert("Description");
            return;}
                    
        if(txtLocation.getText().isEmpty()) {
            ScheduleApp.alert("Location");
            return;}
        
        if(txtType.getText().isEmpty()) {
            ScheduleApp.alert("Type");
            return;}
       
        //characters other than letters and numbers can mess up both the SQL insert and the CSV export, so we check for them
        if(txtName.getText().contains(",") || txtName.getText().contains("'")){
            ScheduleApp.characterAlert();
            return;}
      
        if(txtLocation.getText().contains(",") || txtLocation.getText().contains("'")){
            ScheduleApp.characterAlert();
            return;}
                    
              
        if(txtType.getText().contains(",") || txtType.getText().contains("'")){
            ScheduleApp.characterAlert();
            return;}
        
        if(txtDescription.getText().contains(",") || txtDescription.getText().contains("'")){
            ScheduleApp.characterAlert();
            return;}
        
         if(txtType.getText().contains(",") || txtType.getText().contains("'")){
            ScheduleApp.characterAlert();
            return;}
        
      
        //asks for comation to add the customer
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("CONFIRM APPOINTMENT");
        alertConfirm.setContentText("Are you sure you would like to create this appointment?");
       
        Optional<ButtonType> result = alertConfirm.showAndWait();
        
        if (result.get() == ButtonType.CANCEL){
            //does not add , returns to add screen
            return;
        }
        else{
            //proceeds to add 
        }
     
        date = dtpMeetingDate.getValue();
        customer = cmbCustomer.getSelectionModel().getSelectedItem();
        appointmentName = txtName.getText();
        description = txtDescription.getText();
        startHour = cmbStartHour.getSelectionModel().getSelectedItem();
        startMinute = cmbStartMin.getSelectionModel().getSelectedItem();
        endHour = cmbEndHour.getSelectionModel().getSelectedItem();
        endMinute = cmbEndMin.getSelectionModel().getSelectedItem();
        location = txtLocation.getText();
        String type = txtType.getText();
       
        thisAppointment = new Appointment(customer.getId(), ScheduleApp.currentUserId, appointmentName, description, location, date,
            startHour, startMinute, endHour, endMinute, ScheduleApp.currentUser, ScheduleApp.currentUser, type);
        thisAppointment.setCustomer(customer);
        
        //fetches list of all appointments, checks for another appoitment at the same time
        MainScreenController controller = new MainScreenController();
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList(controller.getAllAppointments());
        
        for(Appointment p: allAppointments){
            ZonedDateTime start = p.getStartTime();
            ZonedDateTime end = p.getEndTime();
            ZonedDateTime newStart = thisAppointment.getStartTime();
            ZonedDateTime newEnd = thisAppointment.getEndTime();
                 
            if(newStart.isEqual(start)||(newStart.isAfter(start)&&(newStart.isBefore(end))||newEnd.isEqual(end)))
            {
                Alert alertDeny = new Alert(Alert.AlertType.WARNING);
        alertDeny.setTitle("Conflicting Appointment");
        alertDeny.setContentText("You have another appointment scheduled during this time");
        alertDeny.showAndWait();
        return;
            }
            
        }
        
        
        thisAppointment.writeAppointment();
        
        //closes window
        Stage stage = (Stage)btnCreateAppointment.getScene().getWindow();
            stage.close(); 
    }

    @FXML
    private void onCancel(ActionEvent event) {
        //closes window
        Stage stage = (Stage)txtName.getScene().getWindow();
            stage.close(); 
    }

    @FXML
    private void onSelect(ActionEvent event) {
    }

    
}