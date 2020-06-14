package com.example.plan_reminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PlanListAdapter extends BaseAdapter {
    private List<Plan> listPlan;
    private LayoutInflater layoutInflater;
    private Context context;

    public PlanListAdapter(Context context, List<Plan> listPlan){
        this.context = context;
        this.listPlan = listPlan;
        this.layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return listPlan.size();
    }

    @Override
    public Object getItem(int position) {
        return listPlan.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.plan_list_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.txtTitle = convertView.findViewById(R.id.txtTitle);
            viewHolder.txtDate = convertView.findViewById(R.id.txtDate);
            viewHolder.txtTime = convertView.findViewById(R.id.txtTime);
            viewHolder.txtDescription = convertView.findViewById(R.id.txtDescription);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Plan plan = this.listPlan.get(position);

        viewHolder.txtTitle.setText(plan.getTitle());
        viewHolder.txtDate.setText(plan.getDate());
        viewHolder.txtTime.setText(plan.getStart() + " - " + plan.getEnd());
        viewHolder.txtDescription.setText(plan.getDescription());
        return convertView;
    }



    static class ViewHolder{
        TextView txtTitle;
        TextView txtDate;
        TextView txtTime;
        TextView txtDescription;
    }
}
