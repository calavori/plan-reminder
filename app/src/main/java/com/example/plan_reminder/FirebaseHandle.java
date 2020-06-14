package com.example.plan_reminder;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FirebaseHandle {
    private static DatabaseReference database ;
    private static String user;
    List<String> allDates;
    List<Plan> allPlans;

    public FirebaseHandle(String user){
        this.database = FirebaseDatabase.getInstance().getReference();
        this.user = user;
    }

    public void addDB (final Plan plan, final ICallback cb) {

        final DatabaseReference dir = database.child(plan.getUser()).child(plan.getDate()).child(plan.getStart());
        dir.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    cb.onCallback(false);
                } else {
                    dir.setValue(plan);
                    cb.onCallback(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    public List<String> getAllDate(){
        allDates = new ArrayList<String>();
        DatabaseReference dir = database.child(user);

        dir.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()){
                    String key = child.getKey();
                    allDates.add(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return allDates;
    }

    public List<Plan> getAllPlans(String date){
        allPlans = new ArrayList<>();
        DatabaseReference dir = database.child(user).child(date);
        dir.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()){
                    allPlans.add(child.getValue(Plan.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return allPlans;
    }

    public void deletePlan(String date, String time){
        DatabaseReference dir = database.child(user).child(date).child(time);
        dir.removeValue();
    }


    public String calDateToString(Calendar cal){
        String myFormat = "MM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        return sdf.format(cal.getTime());
    }

    public String calTimeToString(Calendar cal){
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
        String time = sdf.format(cal.getTime());
        return time;
    }


}
