package com.mgosu.fakecalliphonestyle.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import com.mgosu.fakecalliphonestyle.R;
import com.mgosu.fakecalliphonestyle.adapter.DelayAdapter;
import com.mgosu.fakecalliphonestyle.model.SharedPreferencesManager;

public class DelayActivity extends AppCompatActivity implements View.OnClickListener, DelayAdapter.OnItemClickedListener {
    private LinearLayout llBackDelay;
    private RecyclerView rcl_Delay;
    private SharedPreferencesManager preferencesManager;
    private long[] list= {0,3000,10000,30000,60000,120000,300000,480000,720000,1200000,3600000};
    private DelayAdapter delayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay);
        initView();
        createListDelay();
        clickItem();
        readSharePre();

    }

    private void createListDelay() {
        delayAdapter = new DelayAdapter(list,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcl_Delay.setLayoutManager(layoutManager);
        rcl_Delay.setAdapter(delayAdapter);
        delayAdapter.notifyDataSetChanged();
        delayAdapter.setOnItemClickedListener(this);

    }

    private void readSharePre() {
        int pos = preferencesManager.getPositionDelay();
    }

    private void clickItem() {
        llBackDelay.setOnClickListener(this);
    }

    private void initView() {
        preferencesManager = SharedPreferencesManager.getInstance(this);
        llBackDelay = findViewById(R.id.llBackDelay);
        rcl_Delay = findViewById(R.id.rcl_Delay);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.llBackDelay:
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
        preferencesManager.setPositionDelay(i);
        delayAdapter.notifyDataSetChanged();

        switch (preferencesManager.getPositionDelay()){
            case 1:
                preferencesManager.setTimeDelay(getResources().getString(R.string._3_seconds_late));
                break;
            case 2:
                preferencesManager.setTimeDelay(getResources().getString(R.string._10_seconds_late));
                break;
            case 3:
                preferencesManager.setTimeDelay(getResources().getString(R.string._30_seconds_late));
                break;
            case 4:
                preferencesManager.setTimeDelay(getResources().getString(R.string._1_minute_late));
                break;
            case 5:
                preferencesManager.setTimeDelay(getResources().getString(R.string._2_minute_late));
                break;
            case 6:
                preferencesManager.setTimeDelay(getResources().getString(R.string._5_minute_late));
                break;
            case 7:
                preferencesManager.setTimeDelay(getResources().getString(R.string._8_minute_late));
                break;
            case 8:
                preferencesManager.setTimeDelay(getResources().getString(R.string._12_minute_late));
                break;
            case 9:
                preferencesManager.setTimeDelay(getResources().getString(R.string._20_minute_late));
                break;
            case 10:
                preferencesManager.setTimeDelay(getResources().getString(R.string._1_hour_late));
                break;
            case 0:
                preferencesManager.setTimeDelay(getResources().getString(R.string.none));
                break;
        }
    }
}
