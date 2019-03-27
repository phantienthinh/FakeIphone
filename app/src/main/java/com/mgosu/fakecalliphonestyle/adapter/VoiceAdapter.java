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
import com.mgosu.fakecalliphonestyle.model.Sound;

import java.util.ArrayList;

public class VoiceAdapter extends RecyclerView.Adapter<VoiceAdapter.RecyclerViewHolder> {

    private ArrayList<Sound> data =  new ArrayList<Sound>();
    private Context mContext;

    public VoiceAdapter(ArrayList<Sound> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater =LayoutInflater.from(viewGroup.getContext());
        View view= inflater.inflate(R.layout.custom_item_recycler_voice,viewGroup,false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder viewHolder, int position) {

        viewHolder.tvNameVoice.setText(data.get(position).getTvNameSong());
        viewHolder.ivTickVoice.setImageResource(data.get(position).getIvCTickVoice());
        viewHolder.ivTickVoice.setVisibility(View.INVISIBLE);

        if (!SharedPreferencesManager.getInstance(mContext).getListVoiceOrRecorder()) {

            if (SharedPreferencesManager.getInstance(mContext).getpositionVoice() == position) {
                viewHolder.ivTickVoice.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivTickVoice.setVisibility(View.INVISIBLE);
            }
        }

        viewHolder.rlVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                               if (onItemClickedListener != null) {
                    onItemClickedListener.onItemClick(viewHolder.getAdapterPosition());
                }
            }
        });
//        viewHolder.rlVoice.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
//                alertDialog.setMessage("Do you remove file");
//                alertDialog.setCancelable(true);
//                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//                AlertDialog alert11 = alertDialog.create();
//                alert11.show();
//
//                return true;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameVoice;
        ImageView ivTickVoice;
        RelativeLayout rlVoice;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvNameVoice = itemView.findViewById(R.id.tvNameSong);
            ivTickVoice = itemView.findViewById(R.id.ivTickVoice);
            rlVoice = itemView.findViewById(R.id.rlVoice);

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
