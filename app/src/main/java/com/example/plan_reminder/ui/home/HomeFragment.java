package com.example.plan_reminder.ui.home;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.plan_reminder.FirebaseHandle;
import com.example.plan_reminder.MainActivity;
import com.example.plan_reminder.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    LinearLayout header;
    TextView txtDateDay;
    TextView txtDisplayDate;
    TextView txtDateYear;
    GridView gridView;
    FloatingActionButton fab;
    FirebaseAuth auth;
    FirebaseUser user;
    List<Calendar> events;


    FirebaseHandle firebaseHandle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        header = view.findViewById(R.id.calendar_header);
        txtDateDay = view.findViewById(R.id.date_display_day);
        txtDateYear = view.findViewById(R.id.date_display_year);
        txtDisplayDate = view.findViewById(R.id.date_display_date);
        gridView = view.findViewById(R.id.calendar_grid);
        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_plan_add);
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseHandle = new FirebaseHandle(user.getUid());


        getEventDates(view);
//        showCalendar(getDaysDisplay(), events);
        for (Calendar e : events){
            Toast.makeText(getContext(), e.MONTH, Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void getEventDates(View view) {
        final List<String> datelist = firebaseHandle.getAllDate();
        final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                         progressDialog.dismiss();
                         events = getEventCal(datelist);
                    }
                }, 3000);
    }

    private List<Calendar> getEventCal(List<String> datelist) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        List<Calendar> listCal = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (String str : datelist){
            try{
                cal.setTime(sdf.parse(str));
                listCal.add(cal);
            } catch (Exception e) {}
        }
        return listCal;
    }


    public void showCalendar(ArrayList<Date> days, List<Calendar> events){
        // update grid
        gridView.setAdapter(new CalendarAdapter(getContext(), days, events));

        Calendar currentDate = Calendar.getInstance();
        // update title
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,d MMM,yyyy");
        String[] dateToday = sdf.format(currentDate.getTime()).split(",");
        txtDateDay.setText(dateToday[0]);
        txtDisplayDate.setText(dateToday[1]);
        txtDateYear.setText(dateToday[2]);
    }

    public ArrayList<Date> getDaysDisplay(){
        ArrayList<Date> days = new ArrayList<>();
        Calendar currentDate = Calendar.getInstance();
        Calendar calendar = (Calendar)currentDate.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);
        while (days.size() < 35)
        {
            days.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days;
    }



}