package com.example.nav_bmcv2.ui.announcement;

import androidx.lifecycle.ViewModelProvider;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nav_bmcv2.R;
import com.example.nav_bmcv2.ui.ItemSlideHelper;
import com.example.nav_bmcv2.ui.todo.TodoFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class AnnouncementFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private AnnListAdapter AnnListAdapter;

    private AnnouncementViewModel mViewModel;

    public static AnnouncementFragment newInstance() {
        return new AnnouncementFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.announcement_fragment, container, false);
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Id", String.format("技術通報", i + 1));
            hashMap.put("Avg", String.format("馬達問題解決方法", i + 1));

            arrayList.add(hashMap);
        }
        //設置RecycleView
        mRecyclerView = view.findViewById(R.id.recycleviewA);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        AnnListAdapter = new AnnListAdapter();
        mRecyclerView.setAdapter(AnnListAdapter);

        return view;
    }

    private class AnnListAdapter extends RecyclerView.Adapter<AnnListAdapter.ViewHolder> implements ItemSlideHelper.Callback{
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
        public AnnListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_item,parent,false);
            return new AnnListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AnnListAdapter.ViewHolder holder, int position) {
            holder.tvId.setText(arrayList.get(position).get("Id"));
            holder.tvAvg.setText(arrayList.get(position).get("Avg"));
            //holder.LLRicycleView.setBackgroundColor(getResources().getColor(R.color.item_read, null));
            //holder.imageView.setImageResource(R.drawable.read);
            holder.tv_msg_remind_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.LLRicycleView.setBackgroundColor(getContext().getResources().getColor(R.color.item_read));
                    holder.imageView.setImageResource(R.drawable.read);
                    //notifyItemChanged(holder.getAdapterPosition());
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