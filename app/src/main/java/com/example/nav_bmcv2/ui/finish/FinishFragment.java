package com.example.nav_bmcv2.ui.finish;

import android.annotation.SuppressLint;
import android.content.Intent;
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
        if(arrayList.size()==0) {
            for (int i = 0; i < 10; i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Id", String.format("派工單", i + 1));
                hashMap.put("Avg", String.format("DP2112210004 異常維修(4能源站、8個異常、總里程17.15公里、總移動時間4.76小時)", i + 1));
                hashMap.put("Read", "false");
                arrayList.add(hashMap);
            }
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
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //arrayList.removeAll(arrayList);
    }
}

