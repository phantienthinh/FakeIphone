package com.mgosu.fakecalliphonestyle.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.mgosu.fakecalliphonestyle.R;
import com.mgosu.fakecalliphonestyle.adapter.RecorderAdapter;
import com.mgosu.fakecalliphonestyle.adapter.VoiceAdapter;
import com.mgosu.fakecalliphonestyle.model.Constants;
import com.mgosu.fakecalliphonestyle.model.SharedPreferencesManager;
import com.mgosu.fakecalliphonestyle.model.Sound;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static com.mgosu.fakecalliphonestyle.model.Constants.*;


public class VoiceActivity extends AppCompatActivity implements View.OnClickListener, VoiceAdapter.OnItemClickedListener,
        RecorderAdapter.OnItemClickedListener, RecorderAdapter.OnItemRemoveClickedListener,
        AudioManager.OnAudioFocusChangeListener {
    private LinearLayout llBackVoice;
    private FloatingActionButton fabRecorder;
    private Dialog dialogNameRecorder, dialogRecorderVoice;
    private Button btnStartVoiceRecorder;
    private EditText edtNameRecorder;
    private String nameRecorder;
    private TextView  tvRecorderVoice;
    private Context mContext = VoiceActivity.this;
    ArrayList<Sound> arrayListSound;
    private RecyclerView rcl_Song, rcl_RecorderVoice;
    private SharedPreferencesManager sharedPreferencesManager;
    private VoiceAdapter voiceAdapter;
    private MediaPlayer player;
    private RecorderAdapter recorderAdapter;
    private File[] files;
    private AudioManager mAudioManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        initView();
        onClickItem();
        if (!SharedPreferencesManager.getInstance(this).getFirstTime()) {
            createFristTime();
            SharedPreferencesManager.getInstance(this).setFirstTime(true);
        }
        createFocusAudio();
        setDataListViewSong();
        setdataRecyclerViewVoiceRecorder();
        readSharePre();


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

    private void setdataRecyclerViewVoiceRecorder() {
        final String location = Environment.getExternalStorageDirectory().toString() + "/fakeIPhone";
        File directory = new File(location);
        files = directory.listFiles();
        recorderAdapter = new RecorderAdapter(files, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcl_RecorderVoice.setAdapter(recorderAdapter);
        rcl_RecorderVoice.setLayoutManager(linearLayoutManager);
        recorderAdapter.notifyDataSetChanged();
        recorderAdapter.setOnItemClickedListener(this);
        recorderAdapter.setOnItemRemoveClickedListener(this);

    }

    private void readSharePre() {
//        for (int b = 0; b < arrayListSound.size(); b++) {
//            if (b == sharedPreferencesManager.getpositionVoice()) {
//                arrayListSound.get(b).setShowHideIvTick(true);
//            }
//        }
//        voiceAdapter.notifyDataSetChanged();
    }

    private void setDataListViewSong() {

        getSong();
        voiceAdapter = new VoiceAdapter(arrayListSound, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcl_Song.setLayoutManager(linearLayoutManager);
        rcl_Song.setAdapter(voiceAdapter);
        voiceAdapter.notifyDataSetChanged();
        voiceAdapter.setOnItemClickedListener(this);
    }

    public void getSong() {
        ContentResolver contentResolver = VoiceActivity.this.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        arrayListSound = new ArrayList<Sound>();
        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songTime = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int keySong = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE_KEY);
            int keyLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                boolean statusCheck = false;
                String currentTitle = songCursor.getString(songTitle);
                String currentTime = songCursor.getString(songTime);
                String songLocation = songCursor.getString(keyLocation);
                String currentKeySong = songCursor.getString(keySong);
//                Toast.makeText(mContext, currentKeySong, Toast.LENGTH_SHORT).show();
                if (!currentTitle.equals("")) {
                    arrayListSound.add(new Sound(R.drawable.tick, currentTitle, currentKeySong, songLocation));
                }
            } while (songCursor.moveToNext());
        }
    }

    private void onClickItem() {
        llBackVoice.setOnClickListener(this);
//        fabRecorder.setOnClickListener(this);
        tvRecorderVoice.setOnClickListener(this);
    }

    private void initView() {
        tvRecorderVoice = findViewById(R.id.tvRecorderVoice);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
        llBackVoice = findViewById(R.id.llBackVoice);
//        fabRecorder = findViewById(R.id.fab_Recorder);
        rcl_Song = findViewById(R.id.rcl_Song);
        rcl_RecorderVoice = findViewById(R.id.rcl_VoiceRecerder);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRecorderVoice:

                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    if (!hasPermissions(mContext, PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, Constants.REQUEST);
                    } else {
                        createDialogNameRecorder();
                    }
                } else {
                    createDialogNameRecorder();
                }

                break;
            case R.id.llBackVoice:
                onBackPressed();
                break;
            case R.id.btn_Start_Record:
                nameRecorder = edtNameRecorder.getText().toString().trim();
                dialogNameRecorder.cancel();
                if (nameRecorder.equals("")) {
                    long date = System.currentTimeMillis();
                    String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                    String day = (String) DateFormat.format("dd", date); // 20
                    String monthString = (String) DateFormat.format("MMM", date); // Jun
                    String monthNumber = (String) DateFormat.format("MM", date); // 06
                    String year = (String) DateFormat.format("yyyy", date); // 2013

                    random();
                    nameRecorder = day + "_" + monthNumber + "_Record_" + (sharedPreferencesManager.getStringRandom() + 1);
                    sharedPreferencesManager.setStringRandom(sharedPreferencesManager.getStringRandom() + 1);
                }
                Intent intent = new Intent(VoiceActivity.this, RecorderActivity.class);
                intent.putExtra(KEY_NAME, nameRecorder);
                startActivityForResult(intent, REQUEST_CODE_VOICE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.tvSaveRecorder:
                dialogRecorderVoice.cancel();
                break;
            case R.id.tvCancelRecorder:
                dialogRecorderVoice.cancel();
                break;
        }
    }


    private void createDialogNameRecorder() {
        dialogNameRecorder = new Dialog(this);
        dialogNameRecorder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogNameRecorder.setCancelable(true);
        dialogNameRecorder.setContentView(R.layout.custom_dialog_voice_name);
        dialogNameRecorder.show();

        showKeyboard();
        btnStartVoiceRecorder = dialogNameRecorder.findViewById(R.id.btn_Start_Record);
        edtNameRecorder = dialogNameRecorder.findViewById(R.id.edt_Name_Record);
        edtNameRecorder.requestFocus();
        btnStartVoiceRecorder.setOnClickListener(this);
        dialogNameRecorder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                closeKeyboard();
            }
        });
    }


    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(Constants.MAX_LENGTH);
        int tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (int) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createDialogNameRecorder();
                } else {
                    Toast.makeText(mContext, "The app was not allowed to permissions.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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
            if (player != null) {
                player.release();
            }

        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_VOICE) {
            if (resultCode == Activity.RESULT_OK) {
//                encode();
                setdataRecyclerViewVoiceRecorder();
//                fileOutput = Environment.getExternalStorageDirectory().getAbsolutePath() +
//                                File.separator + nameRecorder + VOICE_TYPE;
//
//                Log.e("request", "REQUEST_CODE_OK: ");
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(MediaStore.Audio.AudioColumns.DATA, fileOutput);
//                contentValues.put(MediaStore.Audio.AudioColumns.TITLE, nameRecorder);
//                contentValues.put(MediaStore.Audio.AudioColumns.DISPLAY_NAME, nameRecorder);
//                Uri uri = getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);
//                setDataListViewSong();

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("request", "RESULT_CANCEL: ");
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "/fakeIPhone/" + nameRecorder + VOICE_TYPE);
                boolean deleted = file.delete();
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(int i) {
        sharedPreferencesManager.setNameVoice(arrayListSound.get(i).getTvNameSong());
        sharedPreferencesManager.setpositionVoice(i);
        sharedPreferencesManager.setSongLocation(arrayListSound.get(i).getPath());
        sharedPreferencesManager.setListVoiceOrRecorder(false);
        if (player != null) {
            player.release();
        }
        {
            player = MediaPlayer.create(this, Uri.parse(arrayListSound.get(i).getPath()));
            player.start();
        }
        voiceAdapter.notifyDataSetChanged();
        recorderAdapter.notifyDataSetChanged();


    }

    @Override
    public void onItemClickLayout(int i) {
        sharedPreferencesManager.setListVoiceOrRecorder(true);
        sharedPreferencesManager.setPosVoiceRecorder(i);
        sharedPreferencesManager.setNameVoice(files[i].getName());
        sharedPreferencesManager.setSongLocation(files[i].getPath());
        if (player != null) {
            player.release();

        }
        player = MediaPlayer.create(getApplicationContext(), Uri.parse(sharedPreferencesManager.getSongLocation()));
        player.start();

        recorderAdapter.notifyDataSetChanged();
        voiceAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClickRemove(final int i) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setMessage("Do you remove file?");
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferencesManager.setNameVoice("");
                sharedPreferencesManager.setListVoiceOrRecorder(false);
                File file = new File(files[i].getPath());
                boolean deleted = file.delete();
                setdataRecyclerViewVoiceRecorder();
                if (player != null) {
                    player.release();
                }

                dialog.cancel();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert11 = alertDialog.create();
        alert11.show();


    }

    public void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void createFocusAudio() {
        player = MediaPlayer.create(this,R.raw.ringtones_01);
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
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
//                mediaPause(player);
            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
//                mediaPlay(player);
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAudioManager.abandonAudioFocus(this);
    }
}