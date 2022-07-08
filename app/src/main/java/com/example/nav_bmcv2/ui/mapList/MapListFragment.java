package com.example.nav_bmcv2.ui.mapList;

import androidx.constraintlayout.helper.widget.Layer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nav_bmcv2.R;
import com.example.nav_bmcv2.databinding.FragmentMapsBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class MapListFragment extends Fragment {

    private MapListViewModel mapListViewModel;
    private FragmentMapsBinding binding;

    private ArrayList<HashMap<String,String>> station = new ArrayList<>();
    private ArrayList<HashMap<String,String>> question = new ArrayList<>();

    private RecyclerView mainRecyclerView;
    private MainListAdapter mainListAdapter;


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
        //View view = inflater.inflate(R.layout.map_list_fragment, container, false);
        //---------------------------------------------
        //測試資料建立
        create_departments_list();
        create_classes_list();
        //---------------------------------------------
        mapListViewModel = new ViewModelProvider(this).get(MapListViewModel.class);
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.map_list_fragment, container, false);


        //設置RecycleView
        mainRecyclerView = view.findViewById(R.id.mainRecyclerView);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mainListAdapter = new MainListAdapter();
        mainRecyclerView.setAdapter(mainListAdapter);


        return view;
    }
    //----------------------------------------------------------------------
    private class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder>{
        private class ViewHolder extends RecyclerView.ViewHolder{
            private TextView textView_Station_Name;
            private LinearLayout LLRicycleView;
            private RecyclerView recycleviewsubitem;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView_Station_Name = itemView.findViewById(R.id.textView_Station_Name);
                LLRicycleView         = itemView.findViewById(R.id.LLRicycleView);
                recycleviewsubitem    = itemView.findViewById(R.id.recycleviewsubitem);
            }
        }
        @NonNull
        @Override
        public MainListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_list_recycle_item, parent, false);
            ViewHolder viewHolder = new MainListAdapter.ViewHolder(view);
            viewHolder.setIsRecyclable(false);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MainListAdapter.ViewHolder holder, int position) {
            holder.textView_Station_Name.setText(station.get(position).get("Name"));
            holder.LLRicycleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int flag = 1;//用於判斷當前是展開還是收縮狀態
                    System.out.println("FYBR");
                    SubViewAdapter adapter = new SubViewAdapter();
                    holder.recycleviewsubitem.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    holder.recycleviewsubitem.setAdapter(adapter);
                    //當flag不為空的時候,獲取flag的值。
                    if (holder.LLRicycleView.getTag() != null) {
                        flag = Integer.parseInt((String) holder.LLRicycleView.getTag());
                        System.out.println(flag);
                    }
                    //當flag為1時,新增子佈局。否則,移除子佈局。
                    if (flag == 1) {
                        //holder.LLRicycleView.addView(subView);
                        //subView.setTag(101);
                        //holder.LLRicycleView.addView(dRecyclerView);
                        holder.recycleviewsubitem.setVisibility(View.VISIBLE);
                        holder.recycleviewsubitem.setTag(101);
                        holder.LLRicycleView.setTag("2");
                    } else {
                        //holder.LLRicycleView.removeView(view.findViewWithTag(101));
                        holder.recycleviewsubitem.setVisibility(View.GONE);
                        holder.LLRicycleView.setTag("1");
                    }
                }
            });
        }

        //控制RecyclerView的長度
        @Override
        public int getItemCount() {
            return station.size();
        }
    }
    private class SubViewAdapter extends RecyclerView.Adapter<SubViewAdapter.SubViewHolder>{
        private class SubViewHolder extends RecyclerView.ViewHolder{
            private TextView textView_Error_Question;
            public SubViewHolder(@NonNull View itemView) {
                super(itemView);
                textView_Error_Question = itemView.findViewById(R.id.textView_Error_Question);
            }
        }
        @NonNull
        @Override
        public SubViewAdapter.SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SubViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.map_list_recycle_subitem,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull SubViewAdapter.SubViewHolder holder, int position) {
            holder.textView_Error_Question.setText(question.get(position).get("Question"));
        }

        @Override
        public int getItemCount() {
            return question.size();
        }
    }
    //----------------------------------------------------------------------
    private void create_departments_list(){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Name","約泰機車行(便利換電站)");
        station.add(hashMap);
        HashMap<String,String> hashMap1 = new HashMap<>();
        hashMap1.put("Name","佳生機車行(便利換電站)");
        station.add(hashMap);
        HashMap<String,String> hashMap2 = new HashMap<>();
        hashMap2.put("Name","志吉機車行(便利換電站)");
        station.add(hashMap);
//        departments.add("約泰機車行(便利換電站)");
//        departments.add("佳生機車行(便利換電站)");
//        departments.add("志吉機車行(便利換電站)");
    }
    private void create_classes_list(){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Question","(3200)電池曹卡異物");
        question.add(hashMap);
        HashMap<String,String> hashMap1 = new HashMap<>();
        hashMap1.put("Question","(3003)電池槽櫃後門被開啟");
        question.add(hashMap);
        HashMap<String,String> hashMap2 = new HashMap<>();
        hashMap2.put("Question","(3012)連線異常");
        question.add(hashMap);
//        test_0.add("(3200)電池曹卡異物");
//        test_0.add("(3003)電池槽櫃後門被開啟");
//        test_0.add("(3012)連線異常");
//        classes.add(test_0);
//        test_1.add("(3004)連線異常");
//        test_1.add("(3013)數位電錶異常");
//        test_1.add("(3202)某槽位上鎖失敗");
//        classes.add(test_1);
//        test_2.add("(3002)電池櫃前門被開啟");
//        test_2.add("(3000)AC電源異常");
//        classes.add(test_2);
    }
}