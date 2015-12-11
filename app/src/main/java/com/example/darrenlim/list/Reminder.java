package com.example.darrenlim.list;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by darrenlim on 12/2/15.
 */

    @ParseClassName("ReminderObj")
public class Reminder extends ParseObject{

    public Reminder() {

    }

    public String getId() {return getObjectId();}

    public String getUser() {return getString("user");}

    public String getTitle() {return getString("title");}

    public String getNotes() {return getString("notes");}

    public String getLabel() {return getString("label");}

    public Integer getPriority() {return getInt("priority");}

    public Boolean getRemindOnDay() {return getBoolean("remindOnDay");}

    public Boolean getRemindOnLoc() {return getBoolean("remindOnLoc");}

    public Integer getDate() {return getInt("date");}

    public Integer getTime() {return getInt("time");}

    public void setUser(String user) {put("user", user);}

    public void setTitle(String title) {put("title", title);}

    public void setNotes(String notes) {put("notes", notes);}

    public void setLabel(String label) {put("label", label);}

    public void setPriority(Integer priority) {put("priority", priority);}

    public void setReminOnDay(Boolean value) {put("remindOnDay", value);}

    public void setRemindOnLoc(Boolean value) {put("remindOnLoc", value);}

    public void setDate(Integer date) {put("date", date);}

    public void setTime(Integer time) {put("time", time);}



//    public Reminder(String id, String title, String notes, String label, Integer priority, Boolean remindOnDay, Boolean remindOnLoc, Integer date, Integer time) {
//        _id = id;
//        _title = title;
//        _notes = notes;
//        _label = label;
//        _priority = priority;
//        _remindOnDay = remindOnDay;
//        _remindOnLoc = remindOnLoc;
//        _date = date;
//        _time = time;
//    }
//
//    public String getId() {
//        return _id;
//    }
//
//    public String getTitle() {
//        return _title;
//    }
//
//    public String getNotes() {
//        return _notes;
//    }
//
//    public String getLabel() {
//        return _label;
//    }
//
//    public Integer getPriority() {
//        return _priority;
//    }
//
//    public Boolean isRemindOnDay() {
//        return _remindOnDay;
//    }
//
//    public Boolean _isRemindOnLoc() {
//        return _remindOnLoc;
//    }
//
//    public Integer getDate() {
//        return _date;
//    }
//
//    public Integer getTime() {
//        return _time;
//    }


}
