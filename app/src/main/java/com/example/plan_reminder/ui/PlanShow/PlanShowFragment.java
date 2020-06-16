package com.example.plan_reminder.ui.PlanShow;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.plan_reminder.FirebaseHandle;
import com.example.plan_reminder.ICallback;
import com.example.plan_reminder.Plan;
import com.example.plan_reminder.PlanListAdapter;
import com.example.plan_reminder.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class PlanShowFragment extends Fragment {
    private static final int MENU_ITEM_DELETE = 111;
    private static final int MENU_ITEM_EDIT = 222;

    ListView listView;
    Spinner spinner;
    List<String> datelist;
    List<Plan> planList;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseHandle firebaseHandle;

    ArrayAdapter<String> spinnerAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_plan_show, container, false);

        listView = (ListView)view.findViewById(R.id.planList);
        registerForContextMenu(listView);
        spinner = (Spinner)view.findViewById(R.id.dateSpinner);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseHandle = new FirebaseHandle(user.getUid());

        String arg_date = null;
        if(getArguments() != null) {
            arg_date = (String) getArguments().getString("arg_choose_date");
        }

        initialSpinner(spinner, view, arg_date);

        return view;
    }

    private void initialListview(final ListView listView, final View view, String date) {
        planList = firebaseHandle.getAllPlans(date);

        final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        PlanListAdapter adapter = new PlanListAdapter(view.getContext(), planList);
                        listView.setAdapter(adapter);
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void initialSpinner(final Spinner spinner, final View view, final String choose) {
        datelist = firebaseHandle.getAllDate();

        final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        spinnerAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, datelist);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(spinnerAdapter);
                        if(choose != null){
                            int spinnerPosition = spinnerAdapter.getPosition(choose);
                            spinner.setSelection(spinnerPosition);
                        }
//                        spinnerAdapter.notifyDataSetChanged();
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View v,
                                                       int postion, long id) {
                                // TODO Auto-generated method stub
                                String date = parent.getItemAtPosition(postion).toString();
                                initialListview(listView, view, date);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub

                            }
                        });
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo)    {

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Select The Action");

        // groupId, itemId, order, title
        menu.add(0, MENU_ITEM_EDIT , 0, "Edit Item");
        menu.add(0, MENU_ITEM_DELETE , 0, "Delete Item");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        final AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Plan plan = (Plan) this.listView.getItemAtPosition(info.position);

        if(item.getItemId() == MENU_ITEM_DELETE) {
            // Ask before deleting.
            new AlertDialog.Builder(getContext())
                    .setMessage("Are you sure you want to delete this plan ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
//                            Toast.makeText(getActivity(), plan.getTitle(), Toast.LENGTH_SHORT).show();
                            firebaseHandle.deletePlan(plan.getDate(), plan.getStart());
                            initialSpinner(spinner, info.targetView, null);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else if (item.getItemId() == MENU_ITEM_EDIT){
            Bundle bundle = new Bundle();
            bundle.putParcelable("arg_plan", plan);
            Navigation.findNavController(getView()).navigate(R.id.nav_plan_edit, bundle);
        }
        else {
            return false;
        }
        return true;
    }




}