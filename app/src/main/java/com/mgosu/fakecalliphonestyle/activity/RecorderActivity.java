package com.mgosu.fakecalliphonestyle.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import at.markushi.ui.CircleButton;
import com.github.piasy.rxandroidaudio.AudioRecorder;
import com.mgosu.fakecalliphonestyle.R;
import com.mgosu.fakecalliphonestyle.model.SharedPreferencesManager;

import java.io.File;

import static com.mgosu.fakecalliphonestyle.model.Constants.*;

public class RecorderActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvNameRecorder, tvRecorder;
    private Chronometer chronometer;
    private CircleButton btnRecorder;
    private ImageView ivClose, ivDone;
    private boolean checkStartRecorder;
    private File mAudioFile;
    private AudioRecorder mAudioRecorder;
    private String nameRecorder;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        initView();
        getDataIntent();
        clickItem();

    }

    private void createFristTime() {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "fakeIPhone");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
        } else {
        }
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        nameRecorder = intent.getStringExtra(KEY_NAME);
//        String replace = nameRecorder.replace(sharedPreferencesManager.getStringRandom(),"");
        tvNameRecorder.setText(nameRecorder);
    }

    private void clickItem() {
        btnRecorder.setOnClickListener(this);
        ivDone.setOnClickListener(this);
        ivClose.setOnClickListener(this);
    }

    private void initView() {
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
        tvRecorder = findViewById(R.id.tvRecorder);
        tvNameRecorder = findViewById(R.id.tvNameRecord);
        chronometer = findViewById(R.id.chronometer);
        chronometer.setText("00:00:00");
        btnRecorder = findViewById(R.id.recorder);
        ivClose = findViewById(R.id.ivClose);
        ivDone = findViewById(R.id.ivDone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recorder:
                if (checkStartRecorder) {
                    checkStartRecorder = false;
                    mAudioRecorder.stopRecord();
                    btnRecorder.setImageDrawable(getResources().getDrawable(R.drawable.ic_microphone));
//                    ivClose.setClickable(true);
//                    ivDone.setClickable(true);
                    chronometer.stop();
                } else {
                    checkStartRecorder = true;
                    createRecorder();
                    btnRecorder.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop));
//                    ivClose.setClickable(false);
//                    ivDone.setClickable(false);
                    runChronometer();
                }
                break;
            case R.id.ivClose:
                if (checkStartRecorder) {
                    Toast.makeText(this, R.string.close_recorder, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent1 = new Intent();
                    intent1.putExtra(KEY_RESULT, RESULT_CANCEL);
                    setResult(Activity.RESULT_CANCELED, intent1);
                    finish();
                }
                break;
            case R.id.ivDone:
                if (!checkStartRecorder) {
                    Intent intent = new Intent();
                    intent.putExtra(KEY_RESULT, REQUEST_CODE_OK);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    break;
                }else {
                    Toast.makeText(this, R.string.save_recorder, Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void runChronometer() {
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer c) {
                int cTextSize = c.getText().length();
                if (cTextSize == 5) {
                    chronometer.setText("00:" + c.getText().toString());
                } else if (cTextSize == 7) {
                    chronometer.setText("0" + c.getText().toString());
                }
            }
        });
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private void createRecorder() {
        mAudioRecorder = AudioRecorder.getInstance();
        mAudioFile = new File(
                Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "/fakeIPhone/" + nameRecorder + VOICE_TYPE);
        mAudioRecorder.prepareRecord(MediaRecorder.AudioSource.MIC,
                MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.AudioEncoder.AAC,
                mAudioFile);
        mAudioRecorder.startRecord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
