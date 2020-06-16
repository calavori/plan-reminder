package com.example.plan_reminder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
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

    public void editPlan(final Plan oldPlan, final Plan newPlan, final View view, Activity activity) {
        final DatabaseReference dir = database.child(oldPlan.getUser());
        final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Editing");
        progressDialog.show();
        dir.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    dir.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            dir.child(oldPlan.getDate()).child(oldPlan.getStart()).removeValue();
                            dir.child(newPlan.getDate()).child(newPlan.getStart()).setValue(newPlan);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean committed,
                                               DataSnapshot currentData) {
                            // Transaction completed

                        }
                    });

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(view.getContext(), "There is some problem while editing, please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
        navController.popBackStack();
        progressDialog.dismiss();

    }

    public void removeExpirePlan(){
        final DatabaseReference dir = database.child(user);
        final Calendar today = Calendar.getInstance();

        dir.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()){
                    Calendar cal = Calendar.getInstance();
                    String key = child.getKey();
                    SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
                    try{
                        cal.setTime(format.parse(key));
                    } catch (Exception e){}
                    if (today.get(Calendar.DATE) > cal.get(Calendar.DATE) && today.get(Calendar.MONTH) == cal.get(Calendar.MONTH) && today.get(Calendar.YEAR) == cal.get(Calendar.YEAR)){
                        dir.child(key).removeValue();
                    } else if (today.get(Calendar.MONTH) > cal.get(Calendar.MONTH) && today.get(Calendar.YEAR) == cal.get(Calendar.YEAR)){
                        dir.child(key).removeValue();
                    } else if (today.get(Calendar.YEAR) > cal.get(Calendar.YEAR)){
                        dir.child(key).removeValue();
                    } else if (today.get(Calendar.DATE) == cal.get(Calendar.DATE) && today.get(Calendar.MONTH) == cal.get(Calendar.MONTH) && today.get(Calendar.YEAR) == cal.get(Calendar.YEAR)){
                        final DatabaseReference dir2 = dir.child(key);
                        dir2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                for (DataSnapshot child: snapshot.getChildren()){
                                    Calendar cal = Calendar.getInstance();
                                    String key = child.getKey();
                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.US);
                                    try{
                                        cal.setTime(format.parse(key));
                                    } catch (Exception e){}
                                    if (today.get(Calendar.HOUR) > cal.get(Calendar.HOUR)){
                                        dir2.child(key).removeValue();
                                    } else if (today.get(Calendar.MINUTE) > cal.get(Calendar.MINUTE) && today.get(Calendar.HOUR) == cal.get(Calendar.HOUR)){
                                        dir2.child(key).removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





}
