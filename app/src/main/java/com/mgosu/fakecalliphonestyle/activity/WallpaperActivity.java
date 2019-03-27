package com.mgosu.fakecalliphonestyle.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import com.mgosu.fakecalliphonestyle.R;
import com.mgosu.fakecalliphonestyle.adapter.WallpaperAdapter;
import com.mgosu.fakecalliphonestyle.listener.OnItemWallpaperClickListenner;
import com.mgosu.fakecalliphonestyle.model.GridSpacingItemDecoration;
import com.mgosu.fakecalliphonestyle.model.SharedPreferencesManager;

import java.io.IOException;

public class WallpaperActivity extends AppCompatActivity implements View.OnClickListener, OnItemWallpaperClickListenner {
    private RecyclerView recyclerView;
    private WallpaperAdapter adapter;
    private LinearLayout llBackWallpaper;
    private String[] list;
    private SharedPreferencesManager preferencesManager;
    int spanCount = 3; // 3 columns
    int spacing = 20; // 50px
    boolean includeEdge = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        initView();
        clickItem();

//        listPath = new ArrayList<ImageWallpaper>();
        try {
            list = getAssets().list("imgs");
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter = (new WallpaperAdapter(this, list));

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListenner(this);

        readSharePre();
    }

    private void readSharePre() {
        adapter.notifyDataSetChanged();
    }

    private void clickItem() {
        llBackWallpaper.setOnClickListener(this);
    }

    private void initView() {
        preferencesManager = SharedPreferencesManager.getInstance(this);
        recyclerView = findViewById(R.id.rcl_Wallpaper);
        llBackWallpaper = findViewById(R.id.llBackWallpaper);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBackWallpaper:
                onBackPressed();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onItemClick(int i) {
        preferencesManager.setpositionWallpaper(i);
        adapter.notifyDataSetChanged();

    }

}
