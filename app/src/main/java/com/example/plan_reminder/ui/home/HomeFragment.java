package com.example.plan_reminder.ui.home;

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

public class HomeFragment extends Fragment {

    LinearLayout header;
    TextView txtDateDay;
    TextView txtDisplayDate;
    TextView txtDateYear;
    GridView gridView;
    FloatingActionButton fab;
    FirebaseAuth auth;
    FirebaseUser user;

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

        showCalendar(getDaysDisplay());

        return view;
    }

    public void showCalendar(ArrayList<Date> days){
        // update grid
        gridView.setAdapter(new CalendarAdapter(getContext(), days));

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