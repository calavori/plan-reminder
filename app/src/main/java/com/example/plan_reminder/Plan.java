package com.example.plan_reminder;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Plan implements Parcelable {
    private String user;
    private String title;
    private String date;
    private String start;
    private String end;
    private String description;

    public Plan(){

    }

    public Plan(String user, String title, Calendar start, Calendar end, String description){
        this.user = user;
        this.title = title;
        String myFormat = "MM-dd-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        this.date =  sdf.format(start.getTime());
        sdf = new SimpleDateFormat("kk:mm");
        this.start = sdf.format(start.getTime());
        this.end = sdf.format(end.getTime());
        this.description = description;
    }


    public String getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getDate(){
        return date;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
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

//    public void setStart(Calendar start) {
//        this.start = start;
//    }
//
//    public void setEnd(Calendar end) {
//        this.end = end;
//    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Plan(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.user = data[0];
        this.title = data[1];
        this.date = data[2];
        this.start = data[3];
        this.end = data[4];
        this.description = data[5];
    }


    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.user,
                this.title,
                this.date,
                this.start,
                this.end,
                this.description});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Plan createFromParcel(Parcel in) {
            return new Plan(in);
        }

        public Plan[] newArray(int size) {
            return new Plan[size];
        }
    };
}


