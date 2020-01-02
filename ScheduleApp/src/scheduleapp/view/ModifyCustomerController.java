/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp.view;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scheduleapp.ScheduleApp;
import scheduleapp.model.Address;
import scheduleapp.model.City;
import scheduleapp.model.Country;
import scheduleapp.model.Customer;

/**
 * FXML Controller class
 *
 * @author erick
 */
public class ModifyCustomerController implements Initializable {

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
    private Button btnModify;
    @FXML
    private Button btnCancel;
    @FXML
    private ComboBox<City> cmbCity;
    @FXML
    private ComboBox<Country> cmbCountry;
    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtCountry;

    /**
     * Initializes the controller class.
     */
    Stage stage;
    
    Customer thisCustomer; 
    Address thisAddress;
    City thisCity;
    Country thisCountry;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
               
     
        MainScreenController controller = new MainScreenController();
        thisCustomer = controller.getCustomer();   
        thisAddress = thisCustomer.getAddress();
        thisCity = thisAddress.getCity();
        thisCountry = thisCity.getCountry();
           
        //populates fields
        txtName.setText(thisCustomer.getCustomerName());
        
        if(thisCustomer.getActive()==1){
            rdYes.setSelected(true);
            rdNo.setSelected(false);
        }
        else{
            rdYes.setSelected(false);
            rdNo.setSelected(true);
        }
        
        txtAddress.setText(thisAddress.getAddress());
        txtAddress2.setText(thisAddress.getAddress2());
        txtPhone.setText(thisAddress.getPhone());
        txtPostalCode.setText(thisAddress.getPostalCode());
                
        rdYes.setSelected(true);
        txtCity.setDisable(true);
        txtCountry.setDisable(true);
           
        
        cmbCity.setItems(controller.getCities());
        cmbCountry.setItems(controller.getCountries());
        cmbCity.getSelectionModel().select(thisCity);
        cmbCountry.getSelectionModel().select(thisCountry);
       
        
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
    private void onCancel(ActionEvent event) {
        //closes window
        stage = (Stage)btnModify.getScene().getWindow();
            stage.close(); 
    }

    @FXML
    private void onModify(ActionEvent event) throws SQLException{
        //leaving SQL exceptions not caught. if the database forbids a field to be modified, then the modify just won't happen but the other fields will be modified
     try{   
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
         //solution credit https://stackoverflow.com/questions/8248277/how-to-determine-if-a-string-has-non-alphanumeric-characters
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
        alertConfirm.setTitle("CONFIRM MODIFY");
        alertConfirm.setContentText("Are you sure you would like to modify this customer?");
       
        Optional<ButtonType> result = alertConfirm.showAndWait();
        
        if (result.get() == ButtonType.CANCEL){
            //does not add , returns to add screen
            return;
        }
        else{
            //proceeds to add 
        }
        
         //can use same city and country code from the add screen
        //determines if a new country and/or new city need to be created. will have to generate a new address record
        if(!(txtCountry.isDisabled())&&!(txtCity.isDisabled())){//case for both
            thisCountry = new Country(txtCountry.getText(),ScheduleApp.currentUser, ScheduleApp.currentUser);
            
            
             thisCity = new City(txtCity.getText(),ScheduleApp.currentUser, ScheduleApp.currentUser);
             thisCity.setCountryId(thisCountry.writeCountry());
             thisCity.setCountry(thisCountry);
             thisCity.setCityId(thisCity.writeCity());
             thisAddress.setCityId(thisCity.getCityId());
             
             
        }
        
        else if(!txtCountry.isDisabled()){//one or the other
            thisCountry = new Country(txtCountry.getText(),ScheduleApp.currentUser, ScheduleApp.currentUser);
            thisCountry.setCountryId(thisCountry.writeCountry());
            thisCity = cmbCity.getSelectionModel().getSelectedItem();
            thisCity.setCountryId(thisCountry.getCountryId());
            thisCity.setCountry(thisCountry);
            thisAddress.setCityId(thisCity.getCityId());
            
            }
    
        else if(!txtCity.isDisabled()){
            thisCountry = cmbCountry.getSelectionModel().getSelectedItem();            
            thisCity = new City(txtCity.getText(),ScheduleApp.currentUser, ScheduleApp.currentUser);
            thisCity.setCountry(thisCountry);
            thisCity.setCountryId(thisCountry.getCountryId());
            thisCity.setCityId(thisCity.writeCity());
            thisAddress.setCityId(thisCity.getCityId());
            
                        
        }
        
        else{//neither are new
            thisCountry = cmbCountry.getSelectionModel().getSelectedItem();
            thisCity = cmbCity.getSelectionModel().getSelectedItem();
            thisCity.setCountry(thisCountry);
            thisAddress.setCityId(thisCity.getCityId());
            
        }
        
        //getting info to update address table
        thisAddress.setAddress(txtAddress.getText());
        thisAddress.setAddress2(txtAddress2.getText());
        thisAddress.setPostalCode(txtPostalCode.getText());
        thisAddress.setPhone(txtPhone.getText());
        thisAddress.setCityId(thisCity.getCityId());
        thisAddress.setCity(thisCity);
        
        //update address
       thisAddress.updateAddress();
                
        //getting info to update customer table
        thisCustomer.setCustomerName(txtName.getText());
        if(rdYes.isSelected())
            thisCustomer.setActive(1);
        else
            thisCustomer.setActive(0);
        
        //update customer
        thisCustomer.updateCustomer();
     }
     catch(Exception ex){
         ex.printStackTrace();
     }
        
        
       
        
         //closes window
        stage = (Stage)btnModify.getScene().getWindow();
            stage.close(); 
        
    }
    
    
    
    
}
