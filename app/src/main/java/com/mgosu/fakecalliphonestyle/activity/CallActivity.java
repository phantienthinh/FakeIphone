package com.mgosu.fakecalliphonestyle.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import at.markushi.ui.CircleButton;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.mgosu.fakecalliphonestyle.R;
import com.mgosu.fakecalliphonestyle.model.SharedPreferencesManager;
import jp.wasabeef.blurry.Blurry;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import static com.mgosu.fakecalliphonestyle.model.Constants.*;

public class CallActivity extends AppCompatActivity implements View.OnClickListener, OnStateChangeListener,AudioManager.OnAudioFocusChangeListener {
    private TextView tvNameCall, tvCallEnd, tvDealine, tvAccept;
    private ImageView ivMute, ivSpeaker;
    private ImageView ivImage;
    private CircleButton btn_CallEnd, btn_CallStart, btn_callEnd2;
    private boolean checkMute;
    private boolean checkSpeaker;
    private Chronometer chronometer;
    private SharedPreferencesManager sharedPreferencesManager;
    private LinearLayout llFunction, llTong;
    private RelativeLayout rl_groupRemind, rl_GroupAll, rl_groupCall;
    private String[] list;
    private InputStream ims;
    private Bitmap bitmapWallpaper, bitmapImage;
    private Vibrator vibrate;
    private MediaPlayer player, mediaPlayerVoice;
    private AudioManager audioManager;
    private SwipeButton swipeButton;
    private Handler handler;
    private Blurry blurry;
//    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_call);
        initView();
        clickItem();
        readSharePre();
        setImageLayout();

        createFocusAudio();
        checkCallSwip();
        if (sharedPreferencesManager.getSwitchRingTone()) {
//            createRingTone();
        }
        if (sharedPreferencesManager.getStatusVibrate()) {
            createVibrate();
        }


        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, TIME_KILLER_SCREEN_CALL);


    }

    private void createFocusAudio() {
//        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

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
            focusRequest = audioManager.requestAudioFocus(mAudioFocusRequest);
        }
        switch (focusRequest) {
            case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                mediaPause(mediaPlayerVoice);
            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                mediaPlay(mediaPlayerVoice);
        }
    }

    private void checkCallSwip() {
        if (sharedPreferencesManager.getScreenOnOff()) {
            swipeButton.setVisibility(View.INVISIBLE);
            rl_groupCall.setVisibility(View.VISIBLE);
            tvDealine.setVisibility(View.VISIBLE);
            tvAccept.setVisibility(View.VISIBLE);
        } else {
            swipeButton.setVisibility(View.VISIBLE);
            rl_groupCall.setVisibility(View.INVISIBLE);
            tvDealine.setVisibility(View.INVISIBLE);
            tvAccept.setVisibility(View.INVISIBLE);
        }

    }

    private void createRingTone() {


        if (sharedPreferencesManager.getIdRingTone() != DEFAULT) {
            player = MediaPlayer.create(this, sharedPreferencesManager.getIdRingTone());
            player.start();
        } else {
            if (player != null){
                player.release();
            }
            Field[] fields = R.raw.class.getFields();
            int id = getResources().getIdentifier(fields[0].getName(), "raw", getPackageName());
            player = MediaPlayer.create(this, id);
            player.start();
        }
    }

    private void createVibrate() {
        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
        vibrate.vibrate(pattern, 0);

    }

    private void setImageLayout() {
        try {
            list = this.getAssets().list("imgs");
            ims = this.getAssets().open("imgs/" + list[sharedPreferencesManager.getpositionWallpaper()]);
            bitmapWallpaper = BitmapFactory.decodeStream(ims);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Drawable dr = new BitmapDrawable(bitmapWallpaper);
        ivImage.setImageDrawable(dr);
    }

    private void startTime() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private void readSharePre() {
        String name = sharedPreferencesManager.getNameContact();
        String phone = sharedPreferencesManager.getPhoneContact();

        if (sharedPreferencesManager.getCheckRadioButton()) {
            if (phone.equals("")) {
                phone = "Unknow";
            } else {
                tvNameCall.setText(phone);
            }
        } else {
            if (name.equals("")) {
                name = "Unknow";
            } else {
                tvNameCall.setText(name);
            }
        }

    }

    private void clickItem() {
        btn_callEnd2.setOnClickListener(this);
        ivMute.setOnClickListener(this);
        ivSpeaker.setOnClickListener(this);
        btn_CallEnd.setOnClickListener(this);
        btn_CallStart.setOnClickListener(this);

        swipeButton.setOnStateChangeListener(this);
    }

    private void initView() {
        swipeButton = findViewById(R.id.swipe_btn);
        ivImage = findViewById(R.id.ivImage);
        rl_groupCall = findViewById(R.id.rl_groupCall);
        llTong = findViewById(R.id.llTong);
        rl_GroupAll = findViewById(R.id.rl_GroupAll);
        tvDealine = findViewById(R.id.tvDealine);
        tvAccept = findViewById(R.id.tvAccept);
        btn_callEnd2 = findViewById(R.id.btn_callEnd2);
        rl_groupRemind = findViewById(R.id.rl_groupRemind);
        llFunction = findViewById(R.id.llFuntion);
        chronometer = findViewById(R.id.chronometer);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
        tvNameCall = findViewById(R.id.tvNameCall);
        tvCallEnd = findViewById(R.id.tvPhoneCall);
        ivMute = findViewById(R.id.imgMute);
        ivSpeaker = findViewById(R.id.imgSpeaker);
        btn_CallEnd = findViewById(R.id.btn_CallEnd);
        btn_CallStart = findViewById(R.id.btn_CallStart);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_callEnd2:
                callEnd();
                if (mediaPlayerVoice != null) {
                    mediaPlayerVoice.release();
                }
                tvCallEnd.setText("Call Ended");
                break;
            case R.id.btn_CallEnd:
                callEnd();
                handler.removeCallbacksAndMessages(null);
                break;
            case R.id.btn_CallStart:
                choseListen();
                handler.removeCallbacksAndMessages(null);
                break;
            case R.id.imgMute:
                if (checkMute) {
                    ivMute.setImageResource(R.drawable.mute);
                    checkMute = false;
                } else {
                    checkMute = true;
                    ivMute.setImageResource(R.drawable.mute_click);
                }
                break;
            case R.id.imgSpeaker:
                if (checkSpeaker) {
                    checkSpeaker = false;
                    ivSpeaker.setImageResource(R.drawable.speaker);
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                    audioManager.setSpeakerphoneOn(false);

                    if (mediaPlayerVoice != null) {
                        mediaPlayerVoice.release();
                    }

                } else {
                    checkSpeaker = true;
                    ivSpeaker.setImageResource(R.drawable.speaker_click);
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                    audioManager.setSpeakerphoneOn(true);

                    if (!sharedPreferencesManager.getSongLocation().equals("")) {
                        File file = new File(sharedPreferencesManager.getSongLocation());
                        if(!file.exists()){

                        }else {
                            mediaPlayerVoice = MediaPlayer.create(this, Uri.parse(sharedPreferencesManager.getSongLocation()));
                            mediaPlayerVoice.start();
                        }
                    }
                }
                break;

        }

    }

    private void choseListen() {
        if (!sharedPreferencesManager.getImgeChose().equals("")) {
            File file = new File(sharedPreferencesManager.getImgeChose());
            if (!file.exists()){
                ivImage.setImageDrawable(null);
            }else {
                bitmapImage = BitmapFactory.decodeFile(sharedPreferencesManager.getImgeChose());
                blurry.with(this)
                        .radius(25)
                        .from(bitmapImage)
                        .into(ivImage);
            }
        }

        hideItem();
        startTime();
        cancleVibrate();
        cancleRingTone();
    }

    private void callEnd() {
        tvCallEnd.setText(R.string.call_declined);
        tvCallEnd.setVisibility(View.VISIBLE);
        rl_GroupAll.setVisibility(View.GONE);
        llFunction.setVisibility(View.GONE);
        chronometer.setVisibility(View.GONE);
        btn_callEnd2.setVisibility(View.GONE);
        createCountDownTimer();
        cancleVibrate();
        cancleRingTone();
    }

    private void cancleVibrate() {
        if (vibrate != null) {
            vibrate.cancel();
        }
    }

    private void createCountDownTimer() {
        CountDownTimer count = new CountDownTimer(1000, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                finish();
            }
        }.start();
    }

    private void hideItem() {
        chronometer.setVisibility(View.VISIBLE);
        rl_GroupAll.setVisibility(View.GONE);
        tvAccept.setVisibility(View.INVISIBLE);
        tvDealine.setVisibility(View.INVISIBLE);
        llFunction.setVisibility(View.VISIBLE);
        rl_groupRemind.setVisibility(View.GONE);
        btn_CallEnd.setVisibility(View.INVISIBLE);
        btn_CallStart.setVisibility(View.INVISIBLE);
        btn_callEnd2.setVisibility(View.VISIBLE);
    }

    @Override
    public void finish() {
        super.finish();

    }

    private void cancleRingTone() {
        if (player != null) {
            player.release();
        }
    }

    private void unlockScreen() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();
        unlockScreen();
    }

    @Override
    public void onStateChange(boolean active) {
        if (active) {
            swipeButton.setVisibility(View.INVISIBLE);
            cancleRingTone();
            choseListen();


        }
    }

    private void cancleVoice() {
        if (mediaPlayerVoice != null) {
            mediaPlayerVoice.release();
        }
    }

    @Override
    protected void onDestroy() {
        sendBroadcast(new Intent(CHECK_START_CALL));
        cancleVibrate();
        cancleRingTone();
        cancleVoice();
        audioManager.abandonAudioFocus(this);
        super.onDestroy();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    private void mediaPlay(MediaPlayer mediaPlayer) {
        int requestAudioFocusResult = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (requestAudioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            if (sharedPreferencesManager.getSwitchRingTone()) {
                createRingTone();
            }
            }
        }


    private void mediaPause(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null){
            mediaPlayer.pause();
        }
        audioManager.abandonAudioFocus(this);
    }
}
