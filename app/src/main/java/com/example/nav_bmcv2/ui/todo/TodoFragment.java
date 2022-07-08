package com.example.nav_bmcv2.ui.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
            hashMap.put("Avg",String.format("系統已維護完畢，謝謝大家配合",i+1));

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
            private LinearLayout LLRicycleView;
            private ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvId = itemView.findViewById(R.id.textView_Id);
                tvAvg  = itemView.findViewById(R.id.textView_con);
                LLRicycleView = itemView.findViewById(R.id.LLRicycleView);
                imageView = itemView.findViewById(R.id.imageView);
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
            holder.tvId.setText(arrayList.get(position).get("Id"));
            holder.tvAvg.setText(arrayList.get(position).get("Avg"));
            //holder.LLRicycleView.setBackgroundColor(getResources().getColor(R.color.item_read, null));
            //holder.imageView.setImageResource(R.drawable.read);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}