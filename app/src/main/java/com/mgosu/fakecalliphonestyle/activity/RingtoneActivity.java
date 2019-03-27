package com.mgosu.fakecalliphonestyle.activity;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.mgosu.fakecalliphonestyle.R;
import com.mgosu.fakecalliphonestyle.adapter.RingToneAdapter;
import com.mgosu.fakecalliphonestyle.model.RingTone;
import com.mgosu.fakecalliphonestyle.model.SharedPreferencesManager;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class RingtoneActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, RingToneAdapter.OnItemClickedListener,
        AudioManager.OnAudioFocusChangeListener {
    private final int TIME_CLICK_DELAY = 500;
    private RecyclerView rcl_RingTone;
    private ArrayList<RingTone> ringTones;
    private RingToneAdapter ringToneAdapter;
    private SwitchCompat swVibrate;
    private LinearLayout llBackRingtone;
    private MediaPlayer mediaPlayerRingTone;
    private SharedPreferencesManager preferencesManager;
    private AudioManager mAudioManager;
    private int curPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtone);
        createFocusAudio();
        initView();
        clickItem();
//        readFileJson();
        createListRingTone();
        readSharePre();

    }

    private void createFocusAudio() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mediaPlayerRingTone = MediaPlayer.create(this, R.raw.ringtones_01);
        AudioAttributes mAudioAttributes =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mAudioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
        }
        AudioFocusRequest mAudioFocusRequest =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mAudioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(mAudioAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(this)
                    .build();
        }
        int focusRequest = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            focusRequest = mAudioManager.requestAudioFocus(mAudioFocusRequest);
        }
        switch (focusRequest) {
            case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                // don’t start playback
//                mediaPause(mediaPlayerRingTone);
                Log.e("audio", "AUDIOFOCUS_REQUEST_FAILED: ");
            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
//                 actually start playback
//                mediaPlay(mediaPlayerRingTone);
                Log.e("audio", "AUDIOFOCUS_REQUEST_GRANTED: ");
        }
    }

    private void readSharePre() {
        swVibrate.setChecked(preferencesManager.getSwitchRingTone());
    }


    private void createListRingTone() {
        ringTones = new ArrayList<RingTone>();

        Field[] fields = R.raw.class.getFields();
        for (int i = 0; i < fields.length; i++) {

            String path = fields[i].getName();
            String nameConvert = path.replace("r", "R");
            String nameConvertVer2 = nameConvert.replace("_", " ");
            ringTones.add(new RingTone(path, nameConvertVer2));

        }
        ringToneAdapter = new RingToneAdapter(ringTones, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcl_RingTone.setLayoutManager(linearLayoutManager);
        rcl_RingTone.setAdapter(ringToneAdapter);
        ringToneAdapter.notifyDataSetChanged();

        ringToneAdapter.setOnItemClickedListener(this);


    }

    private void clickItem() {
        llBackRingtone.setOnClickListener(this);
        swVibrate.setOnCheckedChangeListener(this);
    }

    private void initView() {
        preferencesManager = SharedPreferencesManager.getInstance(this);
        rcl_RingTone = findViewById(R.id.rcl_RingTone);
        swVibrate = findViewById(R.id.swRingtone);
        llBackRingtone = findViewById(R.id.llBackRingtone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBackRingtone:
                onBackPressed();
                break;

        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            preferencesManager.setcheckSwitchRingTone(true);
        } else {
            preferencesManager.setcheckSwitchRingTone(false);

        }

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (mediaPlayerRingTone.isPlaying()) {
                mediaPlayerRingTone.release();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onItemClick(int i) {

        curPosition = i;
        delayHandler.removeCallbacks(delayRunnable);
        delayHandler.postDelayed(delayRunnable, TIME_CLICK_DELAY);
        ringToneAdapter.notifyDataSetChanged();
        preferencesManager.setRingtone(ringTones.get(i).getName());
        preferencesManager.setPositionRingTone(i);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }


    private Handler delayHandler = new Handler();
    private Runnable delayRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayerRingTone != null) {
                mediaPlayerRingTone.release();
                int id = getResources().getIdentifier(ringTones.get(curPosition).getPath(), "raw", getPackageName());
                mediaPlayerRingTone = MediaPlayer.create(RingtoneActivity.this, id);
                mediaPlayerRingTone.start();
                preferencesManager.setIdRingTone(id);
            } else {
                int id = getResources().getIdentifier(ringTones.get(curPosition).getPath(), "raw", getPackageName());
                mediaPlayerRingTone = MediaPlayer.create(RingtoneActivity.this, id);
                mediaPlayerRingTone.start();
                preferencesManager.setIdRingTone(id);
            }
            delayHandler.removeCallbacks(this);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAudioManager.abandonAudioFocus(this);
    }

    private void mediaPlay(MediaPlayer mediaPlayer) {
        int requestAudioFocusResult = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (requestAudioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mediaPlayer.start();
        }
    }

    private void mediaPause(MediaPlayer mediaPlayer) {
        mediaPlayer.pause();
        mAudioManager.abandonAudioFocus(this);
    }
}
