package com.example.nav_bmcv2.ui.finish;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nav_bmcv2.R;
import com.example.nav_bmcv2.databinding.FragmentFinishBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FinishFragment extends Fragment {

    private FinishViewModel galleryViewModel;
    private FragmentFinishBinding binding;
    private String TAG = "mExample";
    private RecyclerView mRecyclerView;
    private MyListAdapter myListAdapter;
    private ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(FinishViewModel.class);

        binding = FragmentFinishBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        View view = inflater.inflate(R.layout.fragment_finish, container, false);

        //製造資料
        for (int i = 0;i<10;i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("Id",String.format("派工單",i+1));
            hashMap.put("Sub1",String.valueOf(new Random().nextInt(80) + 10));
            hashMap.put("Sub2",String.valueOf(new Random().nextInt(80) + 20));
            hashMap.put("Avg",String.format("DP2112210004 異常維修(4能源站、8個異常、總里程17.15公里、總移動時間4.76小時)",i+1));
//            hashMap.put("Avg",String.valueOf(
//                    (Integer.parseInt(hashMap.get("Sub1"))
//                            +Integer.parseInt(hashMap.get("Sub2")))/2));

            arrayList.add(hashMap);
        }
        //設置RecycleView
        mRecyclerView = view.findViewById(R.id.recycleviewF);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        myListAdapter = new MyListAdapter();
        mRecyclerView.setAdapter(myListAdapter);

        return view;
    }//onCreate

    private class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{


        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView tvId,tvAvg;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View view) {
                        NavController navController = Navigation.findNavController(requireView());
                        if (!navController.popBackStack(R.id.nav_map, false))
                            navController.navigate(R.id.nav_map);
                        view.setBackgroundColor(R.color.item);
                    }
                });
                tvId = itemView.findViewById(R.id.textView_Id);
                tvAvg  = itemView.findViewById(R.id.textView_con);
            }
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

            holder.tvAvg.setText(arrayList.get(position).get("Avg"));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }


}

