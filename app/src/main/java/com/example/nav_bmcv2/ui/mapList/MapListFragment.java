package com.example.nav_bmcv2.ui.mapList;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nav_bmcv2.R;

import java.util.ArrayList;

public class MapListFragment extends Fragment {

    private MapListViewModel mViewModel;

    ArrayList<String> departments = new ArrayList<String >();
    ArrayList<String> test_0 = new ArrayList<String>();
    ArrayList<String> test_1 = new ArrayList<String>();
    ArrayList<String> test_2 = new ArrayList<String>();
    ArrayList<ArrayList<String>> classes = new ArrayList<ArrayList<String >>();

    public static MapListFragment newInstance() {
        return new MapListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_list_fragment, container, false);

        ExpandableListView listView = view.findViewById(R.id.listView);
        create_departments_list();
        create_classes_list();
        System.out.println(departments);
        System.out.println(classes);
        ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(getActivity(), departments, classes);
        listView.setAdapter(adapter);

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                String departmentName = adapter.getGroup(groupPosition).toString();
                Toast.makeText(getActivity(), departmentName, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                String departmentName = adapter.getGroup(groupPosition).toString();
                String className = adapter.getChild(groupPosition, childPosition).toString();
                String classTitle = departmentName + " " + className;
                Toast.makeText(getContext(), classTitle, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return view;
    }

    private void create_departments_list(){
        departments.add("約泰機車行(便利換電站)");
        departments.add("佳生機車行(便利換電站)");
        departments.add("志吉機車行(便利換電站)");
    }
    private void create_classes_list(){
        test_0.add("(3206)Chg_Circuit_Failed");
        test_0.add("(3003)Charging_BackDoor_Opened");
        test_0.add("(3012)Internet_Abnormal");
        classes.add(test_0);
        test_1.add("(3004)Internet_Abnormal");
        test_1.add("(6104)Battery_6104");
        test_1.add("(3201)Charger_Failed");
        classes.add(test_1);
        test_2.add("(3002)Charging_FrontDoor_Opened");
        test_2.add("(3201)Charger_Failed");
        classes.add(test_2);
    }

    public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

        private Context context;
        private ArrayList<String> departments;
        private ArrayList<ArrayList<String>> classes;


        public ExpandableListViewAdapter( Context cont,
                                          ArrayList<String> depart,
                                          ArrayList<ArrayList<String>> cla){
            context = cont;
            departments = depart;
            classes = cla;
        }

        @Override
        public int getGroupCount() {
            return departments.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return classes.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return departments.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return classes.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition * 100 + childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            LayoutInflater LI = ((Activity)context).getLayoutInflater();
            View view = LI.inflate(R.layout.item_department, null);
            TextView textView = view.findViewById(R.id.txtDepartmentName);
            textView.setText(departments.get(groupPosition));
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            LayoutInflater LI = ((Activity)context).getLayoutInflater();
            View view = LI.inflate(R.layout.item_class, null);
            TextView textView = view.findViewById(R.id.txtClassName);
            textView.setText(classes.get(groupPosition).get(childPosition));
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }

}