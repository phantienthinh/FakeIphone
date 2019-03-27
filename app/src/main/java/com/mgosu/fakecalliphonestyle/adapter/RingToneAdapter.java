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
import com.mgosu.fakecalliphonestyle.model.RingTone;
import com.mgosu.fakecalliphonestyle.model.SharedPreferencesManager;

import java.util.List;

public class RingToneAdapter extends RecyclerView.Adapter<RingToneAdapter.RecyclerViewHolder> {
    private List<RingTone> ringTones;
    private Context context;


    public RingToneAdapter(List<RingTone> ringTones, Context context) {
        this.ringTones = ringTones;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.custom_item_recycler_voice, viewGroup, false);


        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder viewHolder, int position) {

        viewHolder.textView.setText(ringTones.get(position).getName());
        viewHolder.imageView.setImageResource(R.drawable.tick);

        if (SharedPreferencesManager.getInstance(context).getPositionRingTone() == position ) {
            viewHolder.imageView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imageView.setVisibility(View.INVISIBLE);
        }




        viewHolder.rlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener != null) {
                    onItemClickedListener.onItemClick(viewHolder.getAdapterPosition());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return ringTones.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;
        public RelativeLayout rlLayout;

        public RecyclerViewHolder(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.tvNameSong);
            imageView = view.findViewById(R.id.ivTickVoice);
            rlLayout = view.findViewById(R.id.rlVoice);
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
