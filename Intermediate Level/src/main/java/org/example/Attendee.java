package org.example;

public class Attendee {
    private int attendeeId;
    private String name;
    private String email;
    private int eventId;
   
   
    public Attendee(int attendeeId, String name, String email, int eventId) {
        this.attendeeId = attendeeId;
        this.name = name;
        this.email = email;
        this.eventId = eventId;
    }


    public int getAttendeeId() {
        return attendeeId;
    }


    public void setAttendeeId(int attendeeId) {
        this.attendeeId = attendeeId;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public int getEventId() {
        return eventId;
    }


    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
