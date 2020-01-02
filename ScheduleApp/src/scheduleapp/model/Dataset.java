/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import static scheduleapp.model.DBConnection.conn;

/**
 *
 * @author erick
 */
//i may add the option later to export to CSV, thus the serializable
public class Dataset implements Serializable {
    
    private ArrayList<Customer> allCustomers = new ArrayList<Customer>();
    private ArrayList<Address> allAddresses = new ArrayList<Address>();
    private ArrayList<City> allCities = new ArrayList<City>();
    private ArrayList<Country> allCountries = new ArrayList<Country>();
    private ArrayList<Appointment> allAppointments = new ArrayList<Appointment>();
    
    public Dataset() {
        //add code to parse all DB data and create the associated objects
        parseAllCustomers();
        parseAllAddresses();
        parseAllCities();
        parseAllCountries();
        parseAllAppointments();

            
        //iterates through each array list, and matches associated objects
        for(City p : allCities){
            for(Country q: allCountries){
                if(p.getCountryId()==q.getCountryId())
                    p.setCountry(q);
            }
        }
        
         for(Address p : allAddresses){
            for(City q: allCities){
                if(p.getCityId()==q.getCityId())
                    p.setCity(q);
            }
        }
         
          for(Customer p : allCustomers){
            for(Address q: allAddresses){
                if(p.getAddressId()==q.getAddressId())
                    p.setAddress(q);
            }
        }
          
          for(Appointment p: allAppointments){
              for(Customer q: allCustomers){
                  if(p.getCustomerId()==q.getId())
                      p.setCustomer(q);
                
              }
          }
           
    }
    
    public ArrayList<Customer> getAllCustomers() {return this.allCustomers;}
    public ArrayList<Address> getAllAddresses() {return this.allAddresses;}
    public ArrayList<City> getAllCities() {return this.allCities;}
    public ArrayList<Country> getAllCountries() {return this.allCountries;}
    public ArrayList<Appointment> getAllAppointments() {return this.allAppointments;}
    
    
    public void parseAllAddresses(){
        try{   

            Statement stmt = conn.createStatement();
           ResultSet rs =  stmt.executeQuery("select * from address;");
   
    while(rs.next()) {
    int addressId = rs.getInt("addressID");
    String address = rs.getString("address");
    String address2 = rs.getString("address2");
    int cityId = rs.getInt("cityId");
    String postalCode = rs.getString("postalCode");
    String phone = rs.getString("phone");
    Timestamp createDate = rs.getTimestamp("createDate");
    String createdBy = rs.getString("createdBy");
    Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
    String lastUpdateBy = rs.getString("lastUpdateBy");
    
    Address newAddress = new Address(addressId, address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy);
    allAddresses.add(newAddress);
    
    
    }
    
}
    catch(Exception ex){ex.printStackTrace();}
        
    }
    
    public void parseAllCustomers(){
           try{   

           Statement stmt = conn.createStatement();
           ResultSet rs =  stmt.executeQuery("select * from customer;");
   
    while(rs.next()) {
    
    int customerId = rs.getInt("customerId");  
    String customerName = rs.getString("customerName");
    int addressId = rs.getInt("addressID");
    int active = rs.getInt("active");
    Timestamp createDate = rs.getTimestamp("createDate");
    String createdBy = rs.getString("createdBy");
    Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
    String lastUpdateBy = rs.getString("lastUpdateBy");
    
    Customer newCustomer = new Customer(customerId, customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy);
   
    allCustomers.add(newCustomer);
   
    }
  
}
    catch(Exception ex){ex.printStackTrace();}
        
    }
    
    public void parseAllCities(){
             
        try{   

           Statement stmt = conn.createStatement();
           ResultSet rs =  stmt.executeQuery("select * from city;");
   
    while(rs.next()) {
    
    int cityId = rs.getInt("cityId");  
    String city = rs.getString("city");
    int countryId = rs.getInt("countryID");
    Timestamp createDate = rs.getTimestamp("createDate");
    String createdBy = rs.getString("createdBy");
    Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
    String lastUpdateBy = rs.getString("lastUpdateBy");
    
    City newCity = new City(cityId, city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy);
    
    allCities.add(newCity);
    
    }
 
        }
        
    catch(Exception ex){ex.printStackTrace();}
        
    }
    
    public void parseAllCountries(){
             try{   

           Statement stmt = conn.createStatement();
           ResultSet rs =  stmt.executeQuery("select * from country;");
   
    while(rs.next()) {
    
    int countryId = rs.getInt("countryId");  
    String country = rs.getString("country");
    Timestamp createDate = rs.getTimestamp("createDate");
    String createdBy = rs.getString("createdBy");
    Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
    String lastUpdateBy = rs.getString("lastUpdateBy");
    
    Country newCountry = new Country(countryId, country, createDate, createdBy, lastUpdate, lastUpdateBy);
   
    allCountries.add(newCountry);
    
    
    }
        }
        
    catch(Exception ex){ex.printStackTrace();}
        
    }
    
    public void parseAllAppointments(){
        try{   

           Statement stmt = conn.createStatement();
           ResultSet rs =  stmt.executeQuery("select * from appointment;");
   
    while(rs.next()) {
    
    int appointmentId = rs.getInt("appointmentId");  
    int customerId = rs.getInt("customerId");
    int userId  = rs.getInt("userId");
    String title = rs.getString("title");
    String description = rs.getString("description");
    String location = rs.getString("location");
    String contact = rs.getString("contact");
    String url = rs.getString("url");
    Timestamp start = rs.getTimestamp("start");
    Timestamp end = rs.getTimestamp("end");
    Timestamp createDate = rs.getTimestamp("createDate");
    String createdBy = rs.getString("createdBy");
    String lastUpdateBy = rs.getString("lastUpdateBy");
    String type = rs.getString("type");
    
             
    Appointment newAppointment = new Appointment(customerId, userId, title, description, location, 
            start, end, createdBy, lastUpdateBy, type);
   
    newAppointment.setAppointmentId(appointmentId);
    allAppointments.add(newAppointment);
   
        
    }
        }
    catch(Exception ex){ex.printStackTrace();}
        
    }
    
}
