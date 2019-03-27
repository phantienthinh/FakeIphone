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

import java.io.File;

import static com.mgosu.fakecalliphonestyle.model.Constants.VOICE_TYPE;

public class RecorderAdapter extends RecyclerView.Adapter<RecorderAdapter.RecyclerViewHolder> {
    private File[] list;
    private Context context;

    public RecorderAdapter(File[] list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.custom_item_voice_recorder, viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder viewHolder, int pos) {
        String convert = list[pos].getName().replace(VOICE_TYPE,"");
//        String covert1 = convert.replace(SharedPreferencesManager.getInstance(context).getStringRandom(),"");
        viewHolder.tv_name.setText(convert);
        viewHolder.ivTick.setImageResource(R.drawable.tick);
        viewHolder.ivRemove.setImageResource(R.drawable.ic_remove);


        viewHolder.ivRemove.setVisibility(View.VISIBLE);
        viewHolder.ivTick.setVisibility(View.INVISIBLE);

        if (SharedPreferencesManager.getInstance(context).getListVoiceOrRecorder()) {

            if (SharedPreferencesManager.getInstance(context).getPosVoiceRecorder() == pos) {
                viewHolder.ivTick.setVisibility(View.VISIBLE);
//                viewHolder.ivRemove.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivRemove.setVisibility(View.INVISIBLE);
//                viewHolder.ivTick.setVisibility(View.INVISIBLE);
            }
        }

        viewHolder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemRemoveClickedListener != null){
                    onItemRemoveClickedListener.onItemClickRemove(viewHolder.getAdapterPosition());
                }
            }
        });
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener!=null){
                    onItemClickedListener.onItemClickLayout(viewHolder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        TextView tv_name;
        ImageView ivTick, ivRemove;

        public RecyclerViewHolder(@NonNull View view) {
            super(view);
            layout = view.findViewById(R.id.rl_grouptick);
            tv_name = view.findViewById(R.id.tvNameRecordVoice);
            ivTick = view.findViewById(R.id.ivTickVoice);
            ivRemove = view.findViewById(R.id.ivRemoveVoice);
        }
    }
    public interface OnItemClickedListener {
        void onItemClickLayout(int i);
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }


    public interface OnItemRemoveClickedListener {
        void onItemClickRemove(int i);
    }
    private OnItemRemoveClickedListener onItemRemoveClickedListener;

    public void setOnItemRemoveClickedListener(OnItemRemoveClickedListener onItemRemoveClickedListener) {
        this.onItemRemoveClickedListener = onItemRemoveClickedListener;
    }
}
