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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.DateFormatter;
import static scheduleapp.model.DBConnection.conn;

/**
 *
 * @author erick
 */
public class Appointment {
    
    private int appointmentId;
    private int customerId;
    private Customer customer;
    private int userId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String url;
  
    private Timestamp start;
    private Timestamp end;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    private String type;
    
    private LocalDate date;
    private String startHour;
    private String startMin;
    private String endHour;
    private String endMin;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private ZonedDateTime zonedDate;
    
    
    
    public Appointment(int customerId, int userId, String title, String description, String location, LocalDate date,
            String startHour, String startMin, String endHour, String endMin, String createdBy, String lastUpdateBy, String type){
             
        this.customerId = customerId;
        this.userId = userId;
        this.title =  title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.startHour = startHour;
        this.endHour = endHour;
        this.startMin = startMin;
        this.endMin = endMin;       
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
        this.type = type;
        
       
        
        // then generates start and end timestammps in UTC. Thanks to the code repository for helping me out! 
        ZoneId userZone = ZoneId.systemDefault();
                   
        this.startTime = LocalDateTime.of(date.getYear(), date.getMonthValue(),
                date.getDayOfMonth(), Integer.parseInt(startHour), Integer.parseInt(startMin)).atZone(userZone);
         
        ZonedDateTime utcStart = startTime.withZoneSameInstant((ZoneId.of("UTC")));
        
        this.start = Timestamp.valueOf(utcStart.toLocalDateTime());
         
         
         this.endTime = LocalDateTime.of(date.getYear(), date.getMonthValue(),
               date.getDayOfMonth(), Integer.parseInt(endHour), Integer.parseInt(endMin)).atZone(userZone);
         
         ZonedDateTime utcEnd = endTime.withZoneSameInstant((ZoneId.of("UTC"))); 
         
        this.end = Timestamp.valueOf(utcEnd.toLocalDateTime());
        
        
                        
      
    }
    
    //constructor specifically for parsing data from the db
    public Appointment(int customerId, int userId, String title, String description, String location,
            Timestamp start, Timestamp end, String createdBy, String lastUpdateBy, String type){
              
       
                
        this.customerId = customerId;
        this.userId = userId;
        this.title =  title;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
        this.type = type;
        
        
       //creates zoned date and time for user display, then converts extracted times to zoned
       ZoneId userZone = ZoneId.systemDefault();
        ZonedDateTime parsedStart = start.toLocalDateTime().atZone(ZoneId.of("UTC"));
        this.startTime = parsedStart.withZoneSameInstant(userZone);
        
        ZonedDateTime parsedEnd = end.toLocalDateTime().atZone(ZoneId.of("UTC"));
           
        this.endTime = parsedEnd.withZoneSameInstant(userZone);
        
        this.date = startTime.toLocalDate();
        
        this.startHour = Integer.toString(startTime.getHour());
        this.startMin = Integer.toString(startTime.getMinute());
        this.endHour = Integer.toString(endTime.getHour());
        this.endMin = Integer.toString(endTime.getMinute());
        
        
        
    }
    
    public int getAppointmentId() {return this.appointmentId;}
    public int getCustomerId() {return this.customerId;}
    public int getUserId() {return this.userId;}
    public LocalDate getDate() {return this.date;}
    
        
    public Timestamp getStart() {return this.start;}
    public Timestamp getEnd() {return this.end;}
    public Timestamp getCreateDate() {return this.createDate;}
    public String getCreatedBy() {return this.createdBy;}
    public String getLastUpdateBy() {return this.lastUpdateBy;}
    public Customer getCustomer() {return this.customer;}
    public ZonedDateTime getStartTime() {return this.startTime;}
    public ZonedDateTime getEndTime() {return this.endTime;}
    
    //custom getters to format how data is displayed in the table view
    public LocalDate getDisplayDate() {
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy"); 
        String parseDate = this.date.format(displayFormat);
		LocalDate localDate = LocalDate.parse(parseDate, displayFormat);
        return localDate;}
    
    public LocalTime getDisplayStartTime() {
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("H mm"); 
        String parseTime = this.startTime.format(displayFormat);
		LocalTime localTime = LocalTime.parse(parseTime, displayFormat);
        return localTime;
    }   
    
    public LocalTime getDisplayEndTime() {
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("H mm"); 
        String parseTime = this.endTime.format(displayFormat);
		LocalTime localTime = LocalTime.parse(parseTime, displayFormat);
        return localTime;
        
    }
    
    public String getLocation() {return this.location;}
    public String getType() {return this.type;}
    public String getTitle() {return this.title;}
    public String getStartHour() {return this.startHour;}
    public String getStartMin() {return this.startMin;}
    public String getEndHour() {return this.endHour;}
    public String getEndMin() {return this.endMin;}
    public String getDescription() {return this.description;}
    
    public void setAppointmentId(int appointmentId) {this.appointmentId = appointmentId;}
    public void setCustomerId(int customerId) {this.customerId = customerId;}
    public void setUserId(int userId) {this.userId = userId;}
    public void setTitle(String title) {this.title = title;}
    public void setDescription(String description) {this.description = description;}
    public void setLocation(String location) {this.location = location;}
    public void setContact(String contact) {this.contact = contact;}
    public void setUrl(String url) {this.url = url;}
    public void setStart(Timestamp start) {this.start = start;}
    public void setEnd(Timestamp end) {this.end  = end;}
    public void setCreateDate(Timestamp createDate) {this.createDate = createDate;}
    public void setCreatedBy(String createdBy) {this.createdBy = createdBy;}
    public void setLastUpdate(Timestamp lastUpdate) {this.lastUpdate = lastUpdate;}
    public void setLastUpdateBy(String lastUpdateBy) {this.lastUpdateBy = lastUpdateBy;}
    public void setType(String type) {this.type = type;}
    public void setCustomer(Customer customer) {this.customer = customer;}
    public void setDate(LocalDate date) {this.date = date;}
    
    //data manipuations functions
    public void writeAppointment(){
        try {
            //get timestamp for write
            Instant now = Instant.now();
            java.sql.Timestamp inputDate = java.sql.Timestamp.from(now);
            this.createDate = inputDate;
            
            //create statement object
            Statement stmt = conn.createStatement();
            
            //write SQL insert statement 
            String sqlStatement = "INSERT into appointment (customerId, userId, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdateBy, type) " +
                    "VALUES ('" + this.customer.getId() + "', '" + this.userId + "', '" + this.title + "', '" + this.description + "', '" +
                    this.location + "', '" + this.customer + "', ' ', '" + this.start + "', '" + this.end + "', '" + this.createDate + "', '"  + this.createdBy + "', '" + this.lastUpdateBy + 
                    "', '" + this.type + "');";
                    
            //execute insert statement
            stmt.executeUpdate(sqlStatement);
            
            //retrieve appointment Id
            sqlStatement = "SELECT LAST_INSERT_ID() FROM appointment;";
            ResultSet rs = stmt.executeQuery(sqlStatement);
            rs.next();
            int appointmentId = rs.getInt(1);
            
            
            this.appointmentId = appointmentId;  
       
        } catch (SQLException ex) {
            Logger.getLogger(Country.class.getName()).log(Level.SEVERE, null, ex);
        }
    
  
    }
    
    public void deleteAppointment(){
        try{
    Statement stmt = conn.createStatement();
    String sqlStatement = "DELETE from appointment WHERE appointmentId = " + this.appointmentId;
    stmt.execute(sqlStatement);
    
    }
    catch(Exception ex){ 
        //leave uncaught, may have issue with deleting child rows. this can be ignored
    }
    }
    
    public void updateAppointment(){
      
        try{
        Statement stmt = conn.createStatement();
    String condition = " WHERE appointmentId = '" + this.appointmentId + "';";
    
    String sqlStatement = "UPDATE appointment SET title = '" + this.title + "' " + condition; 
        stmt.executeUpdate(sqlStatement);        
     
   
    sqlStatement = "UPDATE appointment SET customerId = '" + this.customer.getId() + "' " + condition;
        stmt.executeUpdate(sqlStatement);  
        
    sqlStatement = "UPDATE appointment SET description = '" + this.description + "' " + condition;
        stmt.executeUpdate(sqlStatement);
        
    sqlStatement = "UPDATE appointment SET location = '" + this.location + "' " + condition;
        stmt.executeUpdate(sqlStatement);
        
    sqlStatement = "UPDATE appointment SET start = '" + this.start + "' " + condition;
        stmt.executeUpdate(sqlStatement);
        
    sqlStatement = "UPDATE appointment SET end = '" + this.end + "' " + condition;
        stmt.executeUpdate(sqlStatement);
        
    sqlStatement = "UPDATE appointment SET type = '" + this.type + "' " + condition;
        stmt.executeUpdate(sqlStatement);
        
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
    
