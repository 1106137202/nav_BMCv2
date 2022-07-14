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
import com.example.nav_bmcv2.ui.ItemSlideHelper;
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

    private class TOdoListAdapter extends RecyclerView.Adapter<TOdoListAdapter.ViewHolder> implements ItemSlideHelper.Callback{
        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView tvId,tvAvg,tvDate;
            private LinearLayout LLRicycleView;
            private ImageView imageView;
            private TextView tv_msg_remind_delete;
            private  TextView tv_msg_remind_check;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvId = itemView.findViewById(R.id.textView_Id);
                tvAvg  = itemView.findViewById(R.id.textView_con);
                LLRicycleView = itemView.findViewById(R.id.LLRicycleView);
                imageView = itemView.findViewById(R.id.imageView);
                tv_msg_remind_delete = itemView.findViewById(R.id.tv_msg_remind_delete);
                tv_msg_remind_check  = itemView.findViewById(R.id.tv_msg_remind_check);
            }
        }
        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            mRecyclerView = recyclerView;
            mRecyclerView.addOnItemTouchListener(new ItemSlideHelper(mRecyclerView.getContext(), this));
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
            holder.tv_msg_remind_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.LLRicycleView.setBackgroundColor(getContext().getResources().getColor(R.color.item_read));
                    holder.imageView.setImageResource(R.drawable.read);
                    notifyItemChanged(holder.getAdapterPosition());
                }
            });
            holder.tv_msg_remind_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeData(holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        @Override
        public int getHorizontalRange(RecyclerView.ViewHolder holder) {
            if (holder.itemView instanceof LinearLayout) {
                ViewGroup viewGroup = (ViewGroup) holder.itemView;
                //viewGroup包含3個控制元件,即訊息主item、標記已讀、刪除,返回為標記已讀寬度+刪除寬度
                return viewGroup.getChildAt(1).getLayoutParams().width  + viewGroup.getChildAt(2).getLayoutParams().width;
            }
            return 0;
        }

        @Override
        public RecyclerView.ViewHolder getChildViewHolder(View childView) {
            return mRecyclerView.getChildViewHolder(childView);
        }

        @Override
        public View findTargetView(float x, float y) {
            return mRecyclerView.findChildViewUnder(x, y);
        }
        public void removeData(int position) {
            arrayList.remove(position);
            notifyItemRemoved(position);
        }
    }
}