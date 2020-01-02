/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp.model;

import java.sql.Timestamp;

/**
 *
 * @author erick
 */
public class User {
    
    private int userId;
    private String userName;
    private String password;
    private int active;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    public User(String userName, String password, int active, Timestamp createDate, String createdBy, String lastUpdateBy){
        this.userName = userName;
        this.password = password;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        }
        
    public void setId(int userId) {this.userId = userId;}
    public void setName(String userName) {this.userName = userName;}
    public void setPassword(String password) {this.password = password;}
    public void setActive(int active) {this.active = active;}
    public void setCreateDate(Timestamp createDate) {this.createDate = createDate;}
    public void setCreatedBy(String createdBy) {this.createdBy = createdBy;}
    public void setLastUpdate(Timestamp lastUpdate) {this.lastUpdate = lastUpdate;}
    public void setLastUpdateBy(String lastUpdateBy) {this.lastUpdateBy = lastUpdateBy;}
    
    public int getId() {return this.userId;}
    public String getName() {return this.userName;}
    public String getPassword() {return this.password;}
    public int getActive() {return this.active;}
    public Timestamp getCreateDate() {return this.createDate;}
    public String getCreatedBy() {return this.createdBy;}
    public Timestamp getLastUpdate() {return this.lastUpdate;}
    public String getLastUpdateBy() {return this.lastUpdateBy;}
    
    
    
}
