package com.example.darrenlim.list;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

    @ParseClassName("ReminderObj")
public class Reminder extends ParseObject implements Serializable{

    public Reminder() {

    }

    public String getId() {return getObjectId();}

    public void setUser(String user) {put("user", user);}

    public String getUser() {return getString("user");}

    public void setTitle(String title) {put("title", title);}

    public String getTitle() {return getString("title");}

    public void setPriority(Integer priority) {put("priority", priority);}

    public Integer getPriority() {return getInt("priority");}

    public String getNotes() {return getString("notes");}

    public String getLabel() {return getString("label");}

    public Boolean getRemindOnDay() {return getBoolean("remindOnDay");}

    public Boolean getRemindOnLoc() {return getBoolean("remindOnLoc");}

    public Integer getDate() {return getInt("date");}

    public Integer getTime() {return getInt("time");}

    public void setNotes(String notes) {put("notes", notes);}

    public void setLabel(String label) {put("label", label);}

    public void setReminOnDay(Boolean value) {put("remindOnDay", value);}

    public void setRemindOnLoc(Boolean value) {put("remindOnLoc", value);}

    public void setDate(Integer date) {put("date", date);}

    public void setTime(Integer time) {put("time", time);}

    public void setLocation(String location) {put("location",location);}

    public String getLocation() {return getString("location");}

    public void setCategory(String category) {put("category",category);}

    public String getCategory() { return getString("category");}

}
