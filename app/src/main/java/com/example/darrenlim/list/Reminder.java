package com.example.darrenlim.list;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by darrenlim on 12/2/15.
 */

//@ParseClassName("ReminderObj")
//public class Reminder extends ParseObject{
//
//    public Reminder(String id, String title, String notes, String label, Integer priority, Boolean remindOnDay, Boolean remindAtLoc, Integer date, Integer time) {
//        super();
//        put("title", title);
//        put("notes", notes);
//        put("label", label);
//        put("priority", priority);
//        put("remindOnDay", remindOnDay);
//        put("remindAtLoc", remindAtLoc);
//        put("date", date);
//        put("time", time);
//    }

public class Reminder{

    private String _id;
    private String _title;
    private String _notes;
    private String _label;
    private Integer _priority;
    private Boolean _remindOnDay;
    private Boolean _remindOnLoc;
    private Integer _date;
    private Integer _time;

    public Reminder(String id, String title, String notes, String label, Integer priority, Boolean remindOnDay, Boolean remindOnLoc, Integer date, Integer time) {
        _id = id;
        _title = title;
        _notes = notes;
        _label = label;
        _priority = priority;
        _remindOnDay = remindOnDay;
        _remindOnLoc = remindOnLoc;
        _date = date;
        _time = time;
    }

    public String getId() {
        return _id;
    }

    public String getTitle() {
        return _title;
    }

    public String getNotes() {
        return _notes;
    }

    public String getLabel() {
        return _label;
    }

    public Integer getPriority() {
        return _priority;
    }

    public Boolean isRemindOnDay() {
        return _remindOnDay;
    }

    public Boolean _isRemindOnLoc() {
        return _remindOnLoc;
    }

    public Integer getDate() {
        return _date;
    }

    public Integer getTime() {
        return _time;
    }


}
