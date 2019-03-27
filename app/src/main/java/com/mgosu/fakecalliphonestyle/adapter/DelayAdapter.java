package com.mgosu.fakecalliphonestyle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mgosu.fakecalliphonestyle.R;
import com.mgosu.fakecalliphonestyle.model.SharedPreferencesManager;

public class DelayAdapter extends RecyclerView.Adapter<DelayAdapter.RecyclerViewHolder> {
    private long[] list;
    private Context context;
    private SharedPreferencesManager sharedPreferencesManager;

    public DelayAdapter(long[] list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.custom_item_delay,viewGroup,false);
        return new RecyclerViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder viewHolder, final int position) {
        int seconds = (int) (list[position] / 1000) % 60;
        int minutes = (int) ((list[position] / (1000 * 60)) % 60);
//        int hours = (int) ((list[position] / (1000 * 60 * 60)) % 24);


        if (list[position]<60000){
            viewHolder.tvTimeDelay.setText(seconds+" Seconds Late");
            if (list[position]==0){
                viewHolder.tvTimeDelay.setText("None");
            }
        }
        else {
            viewHolder.tvTimeDelay.setText(minutes+" Minute Late");
            if (list[position]== 3600000){
                viewHolder.tvTimeDelay.setText("1 Hour Late");
            }
        }

       if (SharedPreferencesManager.getInstance(context).getPositionDelay() == position){
           viewHolder.ivTick.setVisibility(View.VISIBLE);
       }else {
           viewHolder.ivTick.setVisibility(View.INVISIBLE);
       }

       viewHolder.rl_All.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (onItemClickedListener != null){
                   onItemClickedListener.onItemClick(viewHolder.getAdapterPosition());
               }
           }
       });
    }
    @Override
    public int getItemCount() {
        return list.length;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimeDelay;
        ImageView ivTick;
        RelativeLayout rl_All;
        public RecyclerViewHolder(@NonNull View view) {
            super(view);
            tvTimeDelay = view.findViewById(R.id.tvTimeDelay);
            ivTick = view.findViewById(R.id.ivTick);
            rl_All = view.findViewById(R.id.rl_All);
        }
    }
    public interface OnItemClickedListener {
        void onItemClick(int i);
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}
