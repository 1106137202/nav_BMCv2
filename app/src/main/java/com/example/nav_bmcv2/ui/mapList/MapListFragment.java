package com.example.nav_bmcv2.ui.mapList;

import androidx.constraintlayout.helper.widget.Layer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nav_bmcv2.R;
import com.example.nav_bmcv2.databinding.FragmentMapsBinding;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;

public class MapListFragment extends Fragment {

    private MapListViewModel mapListViewModel;
    private FragmentMapsBinding binding;

    private ArrayList<HashMap<String,String>> station = new ArrayList<>();
    private ArrayList<HashMap<String,String>> tmp = new ArrayList<>();
    private ArrayList<HashMap<String,String>> tmp1 = new ArrayList<>();
    private ArrayList<HashMap<String,String>> tmp2 = new ArrayList<>();
    private ArrayList<ArrayList<HashMap<String,String>>> question = new ArrayList<>();

    private final String[] items = {"待解析", "待料/待工", "其他"};

    private int itemNo = 1;

    private RecyclerView mainRecyclerView;
    private MainListAdapter mainListAdapter;

    private TextView subMemo;

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
        System.out.println(question);
        //---------------------------------------------
        mapListViewModel = new ViewModelProvider(this).get(MapListViewModel.class);
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.map_list_fragment, container, false);


        //設置Main RecycleView
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
            private ImageView expand;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView_Station_Name = itemView.findViewById(R.id.textView_Station_Name);
                LLRicycleView         = itemView.findViewById(R.id.LLRicycleView);
                recycleviewsubitem    = itemView.findViewById(R.id.recycleviewsubitem);
                expand                = itemView.findViewById(R.id.expand);


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
        public void onBindViewHolder(@NonNull MainListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.textView_Station_Name.setText(station.get(position).get("Name"));
            holder.LLRicycleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int flag = 1;//用於判斷當前是展開還是收縮狀態
                    //建立子項目的Adapter，傳入要展開的選項
                    SubViewAdapter adapter = new SubViewAdapter(position);
                    LinearLayoutManager LLM = new LinearLayoutManager(view.getContext());
                    holder.recycleviewsubitem.setLayoutManager(LLM);

                    holder.recycleviewsubitem.setAdapter(adapter);
                    //當flag不為空的時候,獲取flag的值。
                    if (holder.LLRicycleView.getTag() != null) {
                        flag = Integer.parseInt((String) holder.LLRicycleView.getTag());
                        System.out.println(flag);
                    }
                    //當flag為1時,顯示子RecyclerView。否則,隱藏子RecyclerView。
                    if (flag == 1) {
                        holder.recycleviewsubitem.setVisibility(View.VISIBLE);
                        holder.expand.setImageResource(R.drawable.expand_on);
                        holder.recycleviewsubitem.setTag(101);
                        holder.LLRicycleView.setTag("2");
                    } else {
                        holder.recycleviewsubitem.setVisibility(View.GONE);
                        holder.expand.setImageResource(R.drawable.expand_off);
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
        private int item_position = 0;
        private String memoText = "";
        public SubViewAdapter(int position){
            item_position = position;
        }
        private class SubViewHolder extends RecyclerView.ViewHolder{
            public TextView subMemo;
            private TextView textView_Error_Question;
            private LinearLayout LLRicycleViewSubItem;
            private ImageButton edit;

//            private TextView subMemo;

            public SubViewHolder(@NonNull View itemView) {
                super(itemView);
                //itemView.setBackgroundColor(getResources().getColor(R.color.Map_List_subitem_background));
                //設定View
                textView_Error_Question = itemView.findViewById(R.id.textView_Error_Question);
                LLRicycleViewSubItem    = itemView.findViewById(R.id.LLRicycleViewSubItem);
                edit = itemView.findViewById(R.id.edit);
                subMemo = itemView.findViewById(R.id.subMemo);

            }
        }
        @NonNull
        @Override
        public SubViewAdapter.SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SubViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.map_list_recycle_subitem,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull SubViewAdapter.SubViewHolder holder, @SuppressLint("RecyclerView") int position) {

            //取得顯示的文字
            holder.textView_Error_Question.setText(question.get(item_position).get(position).get("Question"));
            holder.LLRicycleViewSubItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //子項目點擊事件
                    System.out.println("FYBR");
                }
            });
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("備註：");
                    builder.setSingleChoiceItems(items, itemNo, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            memoText = items[which];

//                            }
                        }
                    });
                    builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //holder.subMemo.setText(itemSelect.get(position));
                            holder.subMemo.setText(memoText);

                            Toast.makeText(getContext(), "提交成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            //計算RecycleView要顯示的項目

            return question.get(item_position).size();

        }
    }
    //----------------------------------------------------------------------
    private void create_departments_list(){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Name","約泰機車行(便利換電站)");
        station.add(hashMap);
        HashMap<String,String> hashMap1 = new HashMap<>();
        hashMap1.put("Name","佳生機車行(便利換電站)");
        station.add(hashMap1);
        HashMap<String,String> hashMap2 = new HashMap<>();
        hashMap2.put("Name","志吉機車行(便利換電站)");
        station.add(hashMap2);
//        departments.add("約泰機車行(便利換電站)");
//        departments.add("佳生機車行(便利換電站)");
//        departments.add("志吉機車行(便利換電站)");
    }
    private void create_classes_list(){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Question","(3200)電池曹卡異物");
        tmp.add(hashMap);
        HashMap<String,String> hashMap1 = new HashMap<>();
        hashMap1.put("Question","(3003)電池槽櫃後門被開啟");
        tmp.add(hashMap1);
        HashMap<String,String> hashMap2 = new HashMap<>();
        hashMap2.put("Question","(3012)連線異常");
        tmp.add(hashMap2);
        question.add(tmp);

        HashMap<String,String> hashMap3 = new HashMap<>();
        hashMap3.put("Question","(3004)連線異常");
        tmp1.add(hashMap3);
        HashMap<String,String> hashMap4 = new HashMap<>();
        hashMap4.put("Question","(3013)數位電錶異常");
        tmp1.add(hashMap4);
        HashMap<String,String> hashMap5 = new HashMap<>();
        hashMap5.put("Question","(3202)某槽位上鎖失敗");
        tmp1.add(hashMap5);
        question.add(tmp1);

        HashMap<String,String> hashMap6 = new HashMap<>();
        hashMap6.put("Question","(3002)電池櫃前門被開啟");
        tmp2.add(hashMap6);
        HashMap<String,String> hashMap7 = new HashMap<>();
        hashMap7.put("Question","(3000)AC電源異常");
        tmp2.add(hashMap7);
        question.add(tmp2);
    }
}