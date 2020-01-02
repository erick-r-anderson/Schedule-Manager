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
public class City {
    
    private Country country;
    
    private int cityId;
    private String city;
    private int countryId;
    private Timestamp createDate;
    private String createdBy;
    private String lastUpdateBy;
    private Timestamp lastUpdate;
    
    //constructor for user-created objects
    public City(String city, String createdBy, String lastUpdateBy){
        
       this.city = city;
       this.createdBy = createdBy;
       this.lastUpdateBy = lastUpdateBy;
    }
    
    //constructor for objects parsed from database
     //constructor for objects created from database
     public City(int cityId, String city, int countryId, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy){
        
        this.cityId = cityId;
        this.countryId = countryId;
        this.city = city;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
        this.lastUpdate = lastUpdate;
    }
    
    //setters
    public void setCityId(int cityId) {this.cityId = cityId;}
    public void setCity(String city)  {this.city = city;}
    public void setCountryId(int countryId) {this.countryId = countryId;}
    public void setCreateDate(Timestamp createDate) {this.createDate = createDate;}
    public void setCreatedBy(String createdBy) {this.createdBy = createdBy;}
    public void setLastUpdateBy(String lastUpdateBy) {this.lastUpdateBy = lastUpdateBy;}
    public void setCountry(Country country) {this.country = country;}
    public void setLastUpdate(Timestamp lastUpdate) {this.lastUpdate = lastUpdate;}
    
    //getters
    public int getCityId() {return this.cityId;}
    public String getCity() {return this.city;}
    public int getCountryId() {return this.countryId;}
    public Timestamp getCreateDate() {return this.createDate;}
    public String getCreatedBy() {return this.createdBy;}
    public String getLastUpdateBy() {return this.lastUpdateBy;}
    public Country getCountry() {return this.country;} 
    public Timestamp getLastUpdate() {return this.lastUpdate;}
    
    @Override
    public String toString() {return this.city;}
    
    public int writeCity() {
    
    
   
    
    //next, execute all sql to add city to database
    try {
            //get timestamp for write
            Instant now = Instant.now();
            java.sql.Timestamp inputDate = java.sql.Timestamp.from(now);
            this.createDate = inputDate;
            
            //create statement object
            Statement stmt = conn.createStatement();
            
            //write SQL insert statement 
            String sqlStatement = "INSERT into city (city, countryId, createDate, createdBy, lastUpdateBy) " +
                    "VALUES ('" + this.city + "', '" + this.countryId + "', '"  + inputDate + "', '"  + this.createdBy + "', '" + this.lastUpdateBy + "');";
             
                    
            //execute insert statement
            stmt.executeUpdate(sqlStatement);
            
            //retrieve country Id
            sqlStatement = "SELECT LAST_INSERT_ID() FROM city;";
            ResultSet rs = stmt.executeQuery(sqlStatement);
            rs.next();
            int cityId = rs.getInt(1);
            
            return cityId;
            
                  
       
        } catch (SQLException ex) {
            Logger.getLogger(Country.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cityId;
    
    }
    public void updateCity() {}
    
    public void deleteCity() throws SQLException {//choosing not to catch SQL exception. If the delete is denied due to a foreign key constraint, 
       //the user doesn't care. it just means the address is used by another customer
   
       Statement stmt = conn.createStatement();
    String sqlStatement = "DELETE from city WHERE cityId = " + this.cityId;
    stmt.execute(sqlStatement);
    
    //delete associated country
    country.deleteCountry();
    
    }
    
    
    
}
