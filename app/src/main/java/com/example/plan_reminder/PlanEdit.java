package com.example.plan_reminder;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class PlanEdit extends Fragment {

    EditText input_title;
    EditText input_date;
    EditText input_time_start;
    EditText input_time_end;
    EditText input_description;
    Button btn_edit;


    Calendar timeCalendar1;
    Calendar timeCalendar2;
    Plan oldPlan;
    Plan plan;
    FirebaseHandle firebaseHandle;
    Boolean edit_result;


    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_plan_edit, container, false);
        btn_edit = (Button) view.findViewById(R.id.btn_Edit);
        input_title = (EditText) view.findViewById(R.id.input_title);
        input_description = (EditText) view.findViewById(R.id.input_description);

        if(getArguments() != null) {
             oldPlan = (Plan) getArguments().getParcelable("arg_plan");
             plan = (Plan) getArguments().getParcelable("arg_plan");
        }
        displayArgToView(view);


        //create datepicker and timepicker
        create_date_picker(view);
        create_start_time_picker(view);
        create_end_time_picker(view);




        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseHandle = new FirebaseHandle(user.getUid());

        btn_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String title = input_title.getText().toString();
                String description = input_description.getText().toString();

                plan = new Plan(user.getUid(), title, timeCalendar1, timeCalendar2, description);

                firebaseHandle.editPlan(oldPlan, plan, view, getActivity());

            }
        });


        return view;
    }




    private void updateDateLabel(Calendar calendar) {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        input_date.setText(sdf.format(calendar.getTime()));
    }

    private void create_date_picker(View view) {
        final DatePickerDialog.OnDateSetListener date;
        input_date = (EditText) view.findViewById(R.id.input_date);
        updateDateLabel(timeCalendar1);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                timeCalendar1.set(Calendar.YEAR, year);
                timeCalendar1.set(Calendar.MONTH, monthOfYear);
                timeCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(timeCalendar1);

                timeCalendar2.set(Calendar.YEAR, year);
                timeCalendar2.set(Calendar.MONTH, monthOfYear);
                timeCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }

        };

        input_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, timeCalendar1
                        .get(Calendar.YEAR), timeCalendar1.get(Calendar.MONTH),
                        timeCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void create_start_time_picker(View view) {
        final TimePickerDialog.OnTimeSetListener time;
        input_time_start = (EditText) view.findViewById(R.id.input_time_start);
        updateTimeStartLabel(timeCalendar1);
        time = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                // TODO Auto-generated method stub
                timeCalendar1.set(Calendar.HOUR, sHour);
                timeCalendar1.set(Calendar.MINUTE, sMinute);
                updateTimeStartLabel(timeCalendar1);
            }

        };
        input_time_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(getContext(), time, timeCalendar1
                        .get(Calendar.HOUR), timeCalendar1.get(Calendar.MINUTE), true).show();
            }
        });
    }

    private void updateTimeStartLabel(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(calendar.getTime());
        input_time_start.setText(time);
    }

    private void create_end_time_picker(View view) {
        final TimePickerDialog.OnTimeSetListener time;
        input_time_end = (EditText) view.findViewById(R.id.input_time_end);
        updateTimeEndLabel(timeCalendar2);
        time = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                // TODO Auto-generated method stub
                timeCalendar2.set(Calendar.HOUR, sHour);
                timeCalendar2.set(Calendar.MINUTE, sMinute);
                updateTimeEndLabel(timeCalendar2);
            }

        };
        input_time_end.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(getContext(), time, timeCalendar2
                        .get(Calendar.HOUR), timeCalendar2.get(Calendar.MINUTE), true).show();
            }
        });
    }

    private void updateTimeEndLabel(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(calendar.getTime());
        input_time_end.setText(time);
    }

    private void displayArgToView(View view){
        input_title.setText(plan.getTitle());
        input_description.setText(plan.getDescription());
        timeCalendar1 = Calendar.getInstance();
        timeCalendar2 = (Calendar) timeCalendar1.clone();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm", Locale.US);
        try{
            timeCalendar1.setTime(format.parse(plan.getDate() + " " + plan.getStart()));
            timeCalendar2.setTime(format.parse(plan.getDate() + " " + plan.getEnd()));
        } catch (Exception e) {
            Toast.makeText(view.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

}

