package org.example;

import java.sql.Time;

public class Schedule {
    private int scheduleId;
    private int eventId;
    private String sessionTitle;
    private Time sessionTime;
   
    public Schedule(int scheduleId, int eventId, String sessionTitle, Time sessionTime) {
        this.scheduleId = scheduleId;
        this.eventId = eventId;
        this.sessionTitle = sessionTitle;
        this.sessionTime = sessionTime;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getSessionTitle() {
        return sessionTitle;
    }

    public void setSessionTitle(String sessionTitle) {
        this.sessionTitle = sessionTitle;
    }

    public Time getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(Time sessionTime) {
        this.sessionTime = sessionTime;
    }
}
