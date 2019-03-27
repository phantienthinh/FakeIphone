package com.mgosu.fakecalliphonestyle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.mgosu.fakecalliphonestyle.R;
import com.mgosu.fakecalliphonestyle.listener.OnItemWallpaperClickListenner;
import com.mgosu.fakecalliphonestyle.model.SharedPreferencesManager;
import com.squareup.picasso.Picasso;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.RecyclerViewHolder> {
    private Context context;
    private String[] list;

    public WallpaperAdapter(Context context, String[] list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.custom_item_gridview, viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder viewHolder, final int position) {
        if (SharedPreferencesManager.getInstance(context).getpositionWallpaper() == position) {
            viewHolder.iv_tick.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_tick.setVisibility(View.INVISIBLE);
        }
        Picasso.get().load("file:///android_asset/imgs/" + list[position]).resize(200, 600).into(viewHolder.imageView);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemWallpaperClickListenner != null) {
                    onItemWallpaperClickListenner.onItemClick(viewHolder.getAdapterPosition());
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.length;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, iv_tick;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivGridView);
            iv_tick = itemView.findViewById(R.id.ivTickWallpaper);
        }
    }

    private OnItemWallpaperClickListenner onItemWallpaperClickListenner;

    public void setOnItemClickListenner(OnItemWallpaperClickListenner onItemClickListenner) {
        this.onItemWallpaperClickListenner = onItemClickListenner;
    }
}
