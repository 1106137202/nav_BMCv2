package com.example.nav_bmcv2.ui.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nav_bmcv2.R;

import com.example.nav_bmcv2.databinding.FragmentTodoBinding;
import com.example.nav_bmcv2.ui.finish.FinishFragment;
import com.example.nav_bmcv2.ui.undone.UndoneFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TodoFragment extends Fragment {

    private TodoViewModel homeViewModel;
    private FragmentTodoBinding binding;
    private RecyclerView mRecyclerView;
    private TOdoListAdapter TOdoListAdapter;
    private ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(TodoViewModel.class);

        binding = FragmentTodoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        for (int i = 0;i<10;i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("Id",String.format("技術通報",i+1));
            hashMap.put("tvDate",String.format("2022/06/17 15:26",i+1));
            hashMap.put("Sub2",String.valueOf(new Random().nextInt(80) + 20));
            hashMap.put("Avg",String.format("系統已維護完畢，謝謝大家配合",i+1));
//            hashMap.put("Avg",String.valueOf(
//                    (Integer.parseInt(hashMap.get("Sub1"))
//                            +Integer.parseInt(hashMap.get("Sub2")))/2));

            arrayList.add(hashMap);
        }
        //設置RecycleView
        mRecyclerView = view.findViewById(R.id.recycleviewT);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        TOdoListAdapter = new TOdoListAdapter();
        mRecyclerView.setAdapter(TOdoListAdapter);

        return view;
    }//onCreate

    private class TOdoListAdapter extends RecyclerView.Adapter<TOdoListAdapter.ViewHolder>{


        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView tvId,tvAvg,tvDate;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvId = itemView.findViewById(R.id.textView_Id);
                tvDate = itemView.findViewById(R.id.textDate);
                tvAvg  = itemView.findViewById(R.id.textView_con);
            }
        }
        @NonNull
        @Override
        public TOdoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_item,parent,false);
            return new TOdoListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TOdoListAdapter.ViewHolder holder, int position) {
//            int avgS = Integer.parseInt(arrayList.get(position).get("Avg"));
//            if (avgS>=80){
//                holder.tvId.setBackgroundResource(R.color.green_TOKIWA);
//            }else if (avgS<80 &&avgS>=60){
//                holder.tvId.setBackgroundResource(R.color.blue_RURI);
//            }else if(avgS<60 &&avgS>=40){
//                holder.tvId.setBackgroundResource(R.color.yellow_YAMABUKI);
//            }else {
//                holder.tvId.setBackgroundResource(R.color.red_GINSYU);
//            }
            holder.tvId.setText(arrayList.get(position).get("Id"));
            holder.tvDate.setText(arrayList.get(position).get("tvDate"));
            holder.tvAvg.setText(arrayList.get(position).get("Avg"));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}