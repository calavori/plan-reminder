package com.example.plan_reminder;

import java.util.Calendar;

public class Plan {
    private String user;
    private String title;
    private Calendar start;
    private Calendar end;
    private String description;

    public Plan(String user, String title, Calendar start, Calendar end, String description){
        this.user = user;
        this.title = title;
        this.start = start;
        this.end = end;
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public Calendar getStart() {
        return start;
    }

    public Calendar getEnd() {
        return end;
    }

    public String getDescription() {
        return description;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
