package com.example.nav_bmcv2.ui.total;

import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nav_bmcv2.R;
import com.example.nav_bmcv2.databinding.ResultListFragmentBinding;
import com.example.nav_bmcv2.ui.todo.TodoFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultListFragment extends Fragment {

    private ResultListViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    private ResultListAdapter ResultListAdapter;
    private ResultListFragmentBinding binding;


    public static ResultListFragment newInstance() {
        return new ResultListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ResultListFragmentBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.result_list_fragment, container, false);
        for (int i = 0;i<10;i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("Id",String.format("技術通報",i+1));
            hashMap.put("Avg",String.format("系統已維護完畢，謝謝大家配合",i+1));
            hashMap.put("Read", "false");

            arrayList.add(hashMap);
        }
        //設置RecycleView
        mRecyclerView = view.findViewById(R.id.recycleviewR);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        ResultListAdapter = new ResultListAdapter();
        mRecyclerView.setAdapter(ResultListAdapter);

        return view;
    }

    private class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.ViewHolder>{
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
        public ResultListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_item,parent,false);
            return new ResultListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ResultListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.tvId.setText(arrayList.get(position).get("Id"));
            holder.tvAvg.setText(arrayList.get(position).get("Avg"));
            if(arrayList.get(position).get("Read").equals("true")){
                holder.LLRicycleView.setBackgroundColor(getResources().getColor(R.color.item_read, null));
                holder.imageView.setImageResource(R.drawable.read);
            }
            holder.LLRicycleView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    NavController navController = Navigation.findNavController(requireView());
                    if (!navController.popBackStack(R.id.nav_map, false))
                        navController.navigate(R.id.nav_map);
                    arrayList.get(position).put("Read", "true");
                }
            //holder.LLRicycleView.setBackgroundColor(getResources().getColor(R.color.item_read, null));
            //holder.imageView.setImageResource(R.drawable.read);
        });


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }


    }}