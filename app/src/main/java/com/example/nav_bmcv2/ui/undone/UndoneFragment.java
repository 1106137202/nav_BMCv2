package com.example.nav_bmcv2.ui.undone;

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
import com.example.nav_bmcv2.databinding.FragmentUndoneBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class UndoneFragment extends Fragment {

    private UndoneViewModel slideshowViewModel;
    private FragmentUndoneBinding binding;
    private String TAG = "mExample";
    private RecyclerView mRecyclerView;
    private MyListAdapter myListAdapter;
    private ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(UndoneViewModel.class);

        binding = FragmentUndoneBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        View view = inflater.inflate(R.layout.fragment_undone, container, false);
        for (int i = 0;i<10;i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("Id",String.format("派工單",i+1));
            hashMap.put("Avg",String.format("DP2112210004 異常維修(4能源站、8個異常、總里程17.15公里、總移動時間4.76小時)",i+1));
            arrayList.add(hashMap);
        }
        //設置RecycleView
        mRecyclerView = view.findViewById(R.id.recycleviewU);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        myListAdapter = new MyListAdapter();
        mRecyclerView.setAdapter(myListAdapter);

        return view;
    }//onCreate

    private class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{


        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView tvId,tvAvg;
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
        public MyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_item,parent,false);
            return new MyListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyListAdapter.ViewHolder holder, int position) {
            holder.tvId.setText(arrayList.get(position).get("Id"));
            holder.tvAvg.setText(arrayList.get(position).get("Avg"));
            holder.LLRicycleView.setBackgroundColor(getResources().getColor(R.color.item_read, null));
            holder.imageView.setImageResource(R.drawable.read);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}