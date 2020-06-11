package com.example.plan_reminder;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHandle {
    private DatabaseReference database ;

    public FirebaseHandle(){
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void addDB (Plan plan){
        database.child("plans").child(plan.getUser()).setValue(plan);
    }
}
