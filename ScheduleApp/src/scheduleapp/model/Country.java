/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import scheduleapp.ScheduleApp;
import static scheduleapp.model.DBConnection.conn;

/**
 *
 * @author erick
 */
public class Country {
    
    private int countryId;
    private String country;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    
    //constructor for user-generated objects
    public Country(String country, String createdBy, String lastUpdateBy){
        this.country = country;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
    }
               
    //constructor for parsing date from the DB
    public Country(int countryId, String country, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy){
        this.countryId = countryId;
        this.country = country;
        this.createDate  = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    
    }
    
    //setters
    public void setCountryId(int countryId) {this.countryId = countryId;}
    public void setCountry(String country) {this.country = country;}
    public void setCreateDate(Timestamp createDate) {this.createDate = createDate;}
    public void setCreatedBy(String createdBy) {this. createdBy = createdBy;}
    public void setLastUpdate(Timestamp lastUpdate) {this.lastUpdate = lastUpdate;}
    public void setLastUpdateBy(String lastUpdateBy) {this.lastUpdateBy = lastUpdateBy;}
    
    //getters
    public int getCountryId() {return this.countryId;}
    public String getCountry() {return this.country;}
    public Timestamp getCreateDate() {return this.createDate;}
    public String getCreatedBy() {return this.createdBy;}
    public Timestamp getLastUpdate() {return this.lastUpdate;}
    public String getLastUpdateBy() {return this.lastUpdateBy;}
    
    @Override
    public String toString() {return this.country;}
    
    public int writeCountry() {
        try {
            //get timestamp for write
            Instant now = Instant.now();
            java.sql.Timestamp inputDate = java.sql.Timestamp.from(now);
            this.createDate = inputDate;
            
            //create statement object
            Statement stmt = conn.createStatement();
            
            //write SQL insert statement 
            String sqlStatement = "INSERT into country (country, createDate, createdBy, lastUpdateBy) " +
                    "VALUES ('" + this.country + "', '"  + inputDate + "', '"  + this.createdBy + "', '" + this.lastUpdateBy + "');";
                    
            //execute insert statement
            stmt.executeUpdate(sqlStatement);
            
            //retrieve country Id
            sqlStatement = "SELECT LAST_INSERT_ID() FROM country;";
            ResultSet rs = stmt.executeQuery(sqlStatement);
            rs.next();
            int countryId = rs.getInt(1);
            
            
            return countryId;  
       
        } catch (SQLException ex) {
            Logger.getLogger(Country.class.getName()).log(Level.SEVERE, null, ex);
        }
        return countryId;  
        }
    
    public void updateCountry() {}
    
  public void deleteCountry() throws SQLException {//choosing not to catch SQL exception. If the delete is denied due to a foreign key constraint, 
       //the user doesn't care. it just means the address is used by another customer
   
       Statement stmt = conn.createStatement();
    String sqlStatement = "DELETE from country WHERE countryId = " + this.countryId;
    stmt.execute(sqlStatement);
    
  }
    
    
}
