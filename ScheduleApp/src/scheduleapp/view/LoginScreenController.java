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
import java.time.Instant;
import java.util.Locale;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduleapp.ScheduleApp;
import scheduleapp.model.DBConnection;
import static scheduleapp.model.DBConnection.conn;
import sun.security.util.Password;

/**
 * FXML Controller class
 *
 * @author erick
 */
public class LoginScreenController implements Initializable {

    @FXML
    private Label lblUserName;
    @FXML
    private TextField txtUserName;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblPassword;
    @FXML
    private Button btnOk;
    @FXML
    private Button btnExit;
    @FXML
    private Label lblWelcome;
    
    Stage stage;
    Parent root;
    static Boolean validLogin = false;
    /**
     * Initializes the controller class.
     */
    ResourceBundle rb; 
    
    
    @Override        
    
    public void initialize(URL url, ResourceBundle rb) {
        //Locale myLocale = new Locale("es", "MX");
        Locale.getDefault();
        
        rb = ResourceBundle.getBundle("localization/rb");
        
       lblWelcome.setText(rb.getString("welcome"));
       lblUserName.setText(rb.getString("name"));
       lblPassword.setText(rb.getString("pass"));
       btnOk.setText(rb.getString("ok"));
       btnExit.setText(rb.getString("exit"));
        
    }    

    @FXML
    private void onOk(ActionEvent event) throws IOException{
        try {
            //sets alert language
            rb = ResourceBundle.getBundle("localization/rb");
            
            //tests for proper credentials, searches database for the user info
            
            Statement stmt = conn.createStatement();
            ResultSet rs =  stmt.executeQuery("select * from user;");
            
                while(rs.next()){
                    String userName = rs.getString("userName");
                    String password = rs.getString("password");
                    
                    if(txtUserName.getText().equals(userName) && txtPassword.getText().equals(password)){
                        this.validLogin = true;
                        
                        //registers current user in the instance of the program. will be used to track updates in the DB
                        ScheduleApp.currentUser = txtUserName.getText();
                        ScheduleApp.currentUserId = rs.getInt("userId");
                        
                        //records username and instant in log
                        try(FileWriter logFile = new FileWriter("log.txt", true))
                        { PrintWriter userLog = new PrintWriter(logFile);
                        
                        userLog.println(txtUserName.getText() + " " + Instant.now());
                        
                        }
                        
                        //closes popup, proceeds to main app
                        stage = (Stage)btnOk.getScene().getWindow();
                        stage.close();
                        return;
                    }
                }
                     //should only be reached if no valid login found
                        this.validLogin = false;
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        
                        alert.setTitle(rb.getString("alerttitle"));
                        alert.setContentText(rb.getString("alerttext"));
                        alert.showAndWait();
                        
                    
                }   catch (SQLException ex) {
                Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
       
    }
    

    @FXML
    private void onExit(ActionEvent event) {
        
             System.exit(0);
        
    }
    
    public static boolean getValidLogin(){return LoginScreenController.validLogin;}

    private String File(String logtxt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
