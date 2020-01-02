/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp.view;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduleapp.ScheduleApp;
import scheduleapp.model.Address;
import scheduleapp.model.City;
import scheduleapp.model.Country;
import scheduleapp.model.Customer;
import scheduleapp.model.Dataset;


/**
 * FXML Controller class
 *
 * @author erick
 */
public class AddCustomerController implements Initializable {

    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtName;
    @FXML
    private RadioButton rdYes;
    @FXML
    private RadioButton rdNo;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtAddress2;
    @FXML
    private TextField txtPostalCode;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtCountry;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancel;
    
    Stage stage;
    @FXML
    private ComboBox<City> cmbCity;
    @FXML
    private ComboBox<Country> cmbCountry;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainScreenController controller = new MainScreenController();
        
        rdYes.setSelected(true);
        txtCity.setDisable(true);
        txtCountry.setDisable(true);
           
        
        cmbCity.setItems(controller.getCities());
        cmbCountry.setItems(controller.getCountries());
      
        
        //lambda expression for listener, user adds new city
        cmbCity.getSelectionModel().selectedItemProperty().addListener(event -> {
            if(cmbCity.getSelectionModel().getSelectedItem().getCity().equals("Add New City")){
                txtCity.setDisable(false);
                txtCity.setText("Type City Name Here");
                cmbCountry.setDisable(false);
            }
            //resets if they click off the add city
                else{
                txtCity.setDisable(true);
                txtCity.setText(null);
                
            }
            });
        
        //this beast of a statement automatically selects the country associated with the selected city
        cmbCity.getSelectionModel().selectedItemProperty().addListener(event ->{
            Country associatedCountry = cmbCity.getSelectionModel().getSelectedItem().getCountry();
            int indexOfCountry = controller.getCountries().indexOf(associatedCountry);
            cmbCountry.getSelectionModel().select(indexOfCountry);});
            
        //defauls selection
        cmbCity.getSelectionModel().select(0);
        
        //this will only work if new city is first selected, enabling the country combo box
        
        cmbCountry.getSelectionModel().selectedItemProperty().addListener(event -> {
            if(cmbCountry.getSelectionModel().getSelectedItem().getCountry().equals("Add New Country")){
                txtCountry.setDisable(false);
                txtCountry.setText("Type Country Name Here");}
            else{
                txtCountry.setDisable(true);
                txtCountry.setText(null);
            }
            });
       
    }    

    @FXML
    private void onSelectedYes(ActionEvent event) {
        rdNo.setSelected(false);
    }

    @FXML
    private void onSelectedNo(ActionEvent event) {
        rdYes.setSelected(false);
    }

    @FXML
    private void onAdd(ActionEvent event) {
        
        Country country;
        City city;
        int countryId;
        int cityId;
        
        //checks all fields for null values
        if(txtName.getText().isEmpty()){
            ScheduleApp.alert("Customer Name");
            return;}
      
        if(txtAddress.getText().isEmpty()){
            ScheduleApp.alert("Address");
            return;}
                    
        if(txtPostalCode.getText().isEmpty()) {
            ScheduleApp.alert("Postal Code");
            return;}
        
        if(txtPhone.getText().isEmpty()){
            ScheduleApp.alert("Phone Number");
            return;}
        
        if(!(txtCountry.isDisabled())&&(txtCountry.getText().isEmpty())){
            ScheduleApp.alert("Country");
            return;}
        
        if(!(txtCity.isDisabled())&&(txtCity.getText().isEmpty())){
            ScheduleApp.alert("City");
            return;}
        
        //characters other than letters and numbers can mess up both the SQL insert and the CSV export, so we check for them
        
        if(txtName.getText().contains(",") || txtName.getText().contains("'")){
            ScheduleApp.characterAlert();
            return;}
      
        if(txtAddress.getText().contains(",") || txtName.getText().contains("'")){
            ScheduleApp.characterAlert();
            return;}
                    
        if(txtPostalCode.getText().contains(",") || txtName.getText().contains("'")){
            ScheduleApp.characterAlert();
            return;}
        
        if(txtPhone.getText().contains(",") || txtName.getText().contains("'")){
            ScheduleApp.characterAlert();
            return;}
        
        if(!txtCity.isDisabled()||!txtCountry.isDisabled()){
            if(txtCountry.getText().contains(",") || txtName.getText().contains("'")){
            ScheduleApp.characterAlert();
            return;}
        
            if(txtCity.getText().contains(",") || txtName.getText().contains("'")){
            ScheduleApp.characterAlert();
            return;}     
        }
        
       //checks for alphanumeric characters in the phone number and zip code
       //solution credit: https://stackoverflow.com/questions/8248277/how-to-determine-if-a-string-has-non-alphanumeric-characters
        char[] check = txtPhone.getText().toCharArray();
            for(char c: check){
                if(Character.isAlphabetic(c)){
                    ScheduleApp.alphaAlert("phone number");
                return;
            }
            }
            
            check = txtPostalCode.getText().toCharArray();
            for(char c: check){
                if(Character.isAlphabetic(c)){
                    ScheduleApp.alphaAlert("postal code");
                return;
            }
            }
            
            
        
        //asks for comation to add the customer
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("CONFIRM ADD");
        alertConfirm.setContentText("Are you sure you would like to add this customer?");
       
        Optional<ButtonType> result = alertConfirm.showAndWait();
        
        if (result.get() == ButtonType.CANCEL){
            //does not add , returns to add screen
            return;
        }
        else{
            //proceeds to add 
        }
        

        //determines if a new country and/or new city need to be created
        if(!(txtCountry.isDisabled())&&!(txtCity.isDisabled())){//case for both
            country = new Country(txtCountry.getText(),ScheduleApp.currentUser, ScheduleApp.currentUser);
            
            
             city = new City(txtCity.getText(),ScheduleApp.currentUser, ScheduleApp.currentUser);
             city.setCountry(country);
             city.setCountryId(country.writeCountry());
             city.setCityId(city.writeCity());
        }
        
        else if(!txtCountry.isDisabled()){//one or the other
            country = new Country(txtCountry.getText(),ScheduleApp.currentUser, ScheduleApp.currentUser);
            country.setCountryId(country.writeCountry());
            city = cmbCity.getSelectionModel().getSelectedItem();
            city.setCountryId(country.getCountryId());
            city.setCountry(country);
           
            }
    
        else if(!txtCity.isDisabled()){
            country = cmbCountry.getSelectionModel().getSelectedItem();            
            city = new City(txtCity.getText(),ScheduleApp.currentUser, ScheduleApp.currentUser);
            city.setCountry(country);
            city.setCountryId(country.getCountryId());
            city.setCityId(city.writeCity());
                        
        }
        
        else{//neither are new
            country = cmbCountry.getSelectionModel().getSelectedItem();
            city = cmbCity.getSelectionModel().getSelectedItem();
            city.setCountry(country);
        }
            
        //address and customer pull info from the text fields, and attaches the objects created above     
        Address address = new Address(txtAddress.getText(), txtAddress2.getText(), txtPostalCode.getText(), txtPhone.getText(), ScheduleApp.currentUser, ScheduleApp.currentUser);  
                              
        address.setCity(city);
        address.setCityId(city.getCityId());
        address.setAddressId(address.writeAddress());
                                  
        boolean active;
        if(rdYes.isSelected())
            active = true;
        else
            active = false;
            
        Customer customer = new Customer(txtName.getText(), active, ScheduleApp.currentUser, ScheduleApp.currentUser);
            customer.setAddressId(address.getAddressId());
            customer.setAddress(address);
            
                  
        //writes customer object to the DB
        customer.writeCustomer();
             
     
        //closes window
        stage = (Stage)btnAdd.getScene().getWindow();
            stage.close(); 
                       
    }

    @FXML
    private void onCancel(ActionEvent event) {
        //closes window
        stage = (Stage)btnAdd.getScene().getWindow();
            stage.close(); 
                       
    }
    
     
     
    
   
    
}
