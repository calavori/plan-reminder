package com.example.plan_reminder.ui.home;

import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.plan_reminder.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.zip.Inflater;

class CalendarAdapter extends BaseAdapter {
    // for view inflation
    private List<Date> days;
    private LayoutInflater inflater;
    private Context context;


    public CalendarAdapter(Context context, ArrayList<Date> days)
    {
        this.context = context;
        this.days = days;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        // day in question
        Calendar calendar = Calendar.getInstance();
        Date date = this.days.get(position);
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        TextView textView;

        // today
        Date today = new Date();
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTime(today);

        if (view == null){
            view = inflater.inflate(R.layout.date_grid_layout, null);
        }
        textView = (TextView) view.findViewById(R.id.date_in_grid);
        textView.setTypeface(null, Typeface.NORMAL);
        textView.setTextColor(Color.BLACK);

        if (month != calendarToday.get(Calendar.MONTH) || year != calendarToday.get(Calendar.YEAR)) {
            textView.setTextColor(Color.parseColor("#E0E0E0"));
        } else if (day == calendarToday.get(Calendar.DATE)) {
            view.setBackgroundResource(R.drawable.round_text);
        }

        // set text
        textView.setText(String.valueOf(calendar.get(Calendar.DATE)));

        return view;
    }
}
