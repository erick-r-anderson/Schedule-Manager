/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import static scheduleapp.model.DBConnection.conn;

/**
 *
 * @author erick
 */
public class Customer {
    private Address customerAddress;
    
    private int customerId;
    private String customerName;
    private int addressId;
    private int active;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    
    
    
    //constructor for user-created objects
    public Customer(String customerName, boolean active, String createdBy, String lastUpdateBy) {
     
        
        this.customerName = customerName;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
        
        //converts the boolean to the appropriate integer for the database
        if(active)
            this.active = 1;
        else
            this.active = 0;
            
    }
    
    //constructor for objects created from database
     public Customer(int customerId, String customerName, int addressId, int active, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy){
        
        this.customerId = customerId;
        this.addressId = addressId;
        this.customerName = customerName;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
    }
    
    
    //getters
    public int getId() {return this.customerId;}
    public String getCustomerName() {return this.customerName;}
    public int getAddressId() {return this.addressId;}
    public int getActive() {return this.active;}
    public Timestamp getLastUpdate() {return this.lastUpdate;}
    public String getLastUpdateBy() {return this.lastUpdateBy;}
    public Address getAddress() {return this.customerAddress;}
    public Timestamp getCreateDate() {return this.createDate;}
    
    //setters
    public void setID(int customerId) {this.customerId = customerId;}
    public void setCustomerName(String customerName) {this.customerName = customerName;}
    public void setAddressId(int addressId) {this.addressId = addressId;}
    public void setActive(int active) {this.active = active;}
    public void setLastUpdate(Timestamp lastUpdate) {this.lastUpdate = lastUpdate;}
    public void setLastUpdateBy(String lastUpdateBy) {this.lastUpdateBy = lastUpdateBy;}
    public void setAddress (Address customerAddress) {this.customerAddress = customerAddress;}
    
    
    //returns what shows up in the listview
    @Override
    public String toString(){return this.customerName;}
       
    
    //functions to write object to DB, delete object or modify object
    //include necessary SQL code to execute statements
    public void writeCustomer(){
    
    
   try {
            //get timestamp for write
            Instant now = Instant.now();
            java.sql.Timestamp inputDate = java.sql.Timestamp.from(now);
            this.createDate = inputDate;
            
            //create statement object
            Statement stmt = conn.createStatement();
            
            //write SQL insert statement 
            String sqlStatement = "INSERT into customer (customerName, addressId, active, createDate, createdBy, lastUpdateBy) " +
                    "VALUES ('" + this.customerName + "', '" + this.addressId + "', '" + this.active + "', '" + inputDate + "', '"  + this.createdBy + "', '" + this.lastUpdateBy + "');";
                    
            //execute insert statement
            stmt.executeUpdate(sqlStatement);
            
            //retrieve country Id
            sqlStatement = "SELECT LAST_INSERT_ID() FROM customer;";
            ResultSet rs = stmt.executeQuery(sqlStatement);
            rs.next();
            int customerId = rs.getInt(1);
            
            
            this.customerId = customerId;  
       
        } catch (SQLException ex) {
            Logger.getLogger(Country.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }
    
    public void updateCustomer() throws SQLException {
   
    Statement stmt = conn.createStatement();
    String condition = " WHERE customerId = '" + this.customerId + "';";
    
    String sqlStatement = "UPDATE customer SET customerName = '" + this.customerName + "' " + condition; 
        stmt.executeUpdate(sqlStatement);        
     
   
    sqlStatement = "UPDATE customer SET active = '" + this.active + "' " + condition;
        stmt.executeUpdate(sqlStatement);  
        
   sqlStatement = "UPDATE customer SET lastUpdateBy = '" + this.lastUpdateBy + "' " + condition;
        stmt.executeUpdate(sqlStatement);
        
    }
    
    
        
    
    
    public void deleteCustomer(){
       
    try{
    Statement stmt = conn.createStatement();
    String sqlStatement = "DELETE from customer WHERE customerId = " + this.customerId;
    stmt.execute(sqlStatement);
    
    }
    catch(Exception ex){ 
        //leave blank, likely to generate sql exceptions from not being able to delete child rows. will leave all of these uncaught
    }
    
     //then, deletea associated address
    try{
     customerAddress.deleteAddress();}
    catch(Exception ex){
        //eat the exception
    }
           
    }
       
}
