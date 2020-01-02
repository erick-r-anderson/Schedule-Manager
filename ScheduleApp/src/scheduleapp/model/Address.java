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
public class Address {
    
    private City city;
    
    private int addressId;
    private String address;
    private String address2;
    private int cityId;
    private String postalCode;
    private String phone;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
   
    
    //FIXME add city to constructor 
    public Address(String address, String address2, String postalCode, 
            String phone, String createdBy, String lastUpdateBy ) {
        
        this.address = address;
        this.address2 = address2;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
                
    }
    
    public Address(int addressId, String address, String address2, int cityId, String postalCode, String phone, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy){
        
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.cityId = cityId;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
    }
    
    //setters
    public void setAddressId(int addressId) {this.addressId = addressId;}
    public void setAddress(String address) {this.address = address;}
    public void setAddress2(String address2) { this.address2 = address2;}
    public void setCityId(int cityId) {this.cityId = cityId;}
    public void setPostalCode(String postalCode) { this.postalCode = postalCode;}
    public void setPhone(String phone) {this.phone = phone;}
    public void setCreateDate(Timestamp createDate) {this.createDate = createDate;}
    public void setCreatedBy(String createdBy) {this.createdBy = createdBy;}
    public void setLastUpdate(Timestamp lastUpdate) {this.lastUpdate = lastUpdate;}
    public void setLastUpdateBy(String lastUpdateBy) {this.lastUpdateBy = lastUpdateBy;}
    public void setCity(City city) {this.city = city;}
    
    //getters
   public int getAddressId() {return this.addressId;}
   public String getAddress() {return this.address;}
   public String getAddress2() {return this.address2;}
   public int getCityId() {return this.cityId;}
   public String getPostalCode() {return this.postalCode;}
   public String getPhone() {return this.phone;}
   public Timestamp getCreateDate() {return this.createDate;}
   public String getCreatedBy() {return this.createdBy;}
   public Timestamp getLastUpdate() {return this.lastUpdate;}
   public String getLastUpdateBy() {return this.lastUpdateBy;}
   public City getCity() {return this.city;}
   
   public String toString() {return this.address + this.address2;} 
   
   //database access functions
   public int writeAddress() {
   
       //this.cityId = city.writeCity();
    
    
    //next, execute all sql to add city to database
    try {
            //get timestamp for write
            Instant now = Instant.now();
            java.sql.Timestamp inputDate = java.sql.Timestamp.from(now);
            this.createDate = inputDate;
            
            //create statement object
            Statement stmt = conn.createStatement();
            
            //write SQL insert statement 
            String sqlStatement = "INSERT into address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) " +
                    "VALUES ('" + this.address + "', '" + this.address2 + "', '" + this.cityId + "', '"  + this.postalCode + "', '" + this.phone + "', '" + inputDate + "', '"  + this.createdBy + "', '" + this.lastUpdateBy + "');";
             
            //execute insert statement
            stmt.executeUpdate(sqlStatement);
            
            //retrieve country Id
            sqlStatement = "SELECT LAST_INSERT_ID() FROM address;";
            ResultSet rs = stmt.executeQuery(sqlStatement);
            rs.next();
            int addressId = rs.getInt(1);
            
            return addressId;
       
        } catch (SQLException ex) {
            Logger.getLogger(Country.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cityId;
   
   }
   public void updateAddress()throws SQLException {
   
    Statement stmt = conn.createStatement();
    String condition = " WHERE addressId = '" + this.addressId + "';";
    
    String sqlStatement = "UPDATE address SET cityId = '" + this.cityId + "' " + condition; 
        stmt.executeUpdate(sqlStatement);  
    
    sqlStatement = "UPDATE address SET address = '" + this.address + "' " + condition; 
        stmt.executeUpdate(sqlStatement);        
     
   
    sqlStatement = "UPDATE address SET address2 = '" + this.address2 + "' " + condition;
        stmt.executeUpdate(sqlStatement);  
        
    sqlStatement = "UPDATE address SET phone = '" + this.phone + "' " + condition;
        stmt.executeUpdate(sqlStatement);  
        
    sqlStatement = "UPDATE address SET postalCode = '" + this.postalCode + "' " + condition;
        stmt.executeUpdate(sqlStatement);  
        
   sqlStatement = "UPDATE address SET lastUpdateBy = '" + this.lastUpdateBy + "' " + condition;
        stmt.executeUpdate(sqlStatement);}
   
   public void deleteAddress() throws SQLException {//choosing not to catch SQL exception. If the delete is denied due to a foreign key constraint, 
       //the user doesn't care. it just means the address is used by another customer
   
       Statement stmt = conn.createStatement();
    String sqlStatement = "DELETE from address WHERE addressId = " + this.addressId;
    stmt.execute(sqlStatement);
    
    //delete associated city
    city.deleteCity();
   }
   

}
