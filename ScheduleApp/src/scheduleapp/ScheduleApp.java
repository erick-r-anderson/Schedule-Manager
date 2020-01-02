/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;

import java.sql.Statement;
import java.time.Instant;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import scheduleapp.model.Country;
import scheduleapp.model.DBConnection;
import static scheduleapp.model.DBConnection.conn;
import scheduleapp.model.Dataset;

/**
 *
 * @author erick
 */
public class ScheduleApp extends Application {
    
    //keeps track of which user is currently updating the database. avaialble to all classes 
    public static String currentUser;
    public static int currentUserId;
    
    //dataset to be shared by all observable lists
    public static Dataset allData; 
    
    @Override
    public void start(Stage stage) throws Exception {
        
      
        
        //localization
        Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("localization/rb");
        
        //loads main screen controller, which then immediately loads the login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/MainScreen.fxml"));
            loader.setResources(rb);
            Parent root = loader.load();
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("view/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }   /**
     * @param args the command line arguments
     */
    
    //alert for any instance of null entries by the user
    public static void alert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Missing " + message);
        alert.setContentText("Please enter a " + message);
        alert.showAndWait();
    }
    
    public static void characterAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Characters");
        alert.setContentText("Please enter only letters, numbers, and spaces");
        alert.showAndWait();
    }
    
    public static void alphaAlert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Characters");
        alert.setContentText("Please enter only numbers in the " + message);
        alert.showAndWait();
        
    }
    public static void main(String[] args) {
       
            
        launch(args);
        
       
    }
    
}
