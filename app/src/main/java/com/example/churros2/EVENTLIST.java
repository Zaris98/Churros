package com.example.churros2;

public class EVENTLIST {
    private String Event, Date, Time, Location, Description;

    public EVENTLIST(){

    }
    //This is the constructers that are created
    public EVENTLIST(String event, String date, String time, String location, String description) {
        this.Event = event;
        Date = date;
        Time = time;
        Location = location;
        Description = description;
    }

    //These are the setters and getters that are created
    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}

