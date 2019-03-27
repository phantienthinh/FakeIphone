package com.mgosu.fakecalliphonestyle.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.mgosu.fakecalliphonestyle.R;
import com.mgosu.fakecalliphonestyle.broadcast.CallingReceiver;
import com.mgosu.fakecalliphonestyle.model.SharedPreferencesManager;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.File;

import static com.mgosu.fakecalliphonestyle.model.Constants.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private CircleImageView ivContacts;
    private Dialog dialogContacts;
    private RelativeLayout  rlVoice, rlDelay, rlWallpaper, rlRingtone, rl_click;
    private SwitchCompat swVibrate;
    private TextView tvContacts, tvPolice, tvUnknown, tvGirlfriend, tvBoyfriend, tvDelay, tvNameVoice, tvRingTone;
    private String nameContacts, phoneContacts;
    private EditText edtNameContact;
    private EditText edtPhoneContact;
    private RadioButton rabNameContact, rabPhoneContact;
    private Button btnStartCall;
    private boolean aBooleanCheckStartCall;
    private SharedPreferencesManager sharedPreferencesManager;
    private long timeCountDownt;
    private CountDownTimer countDownTimer;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private BroadcastReceiver recever;
    private IntentFilter filter;
    private boolean isCheckImageVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        readSharePre();
        onClickItem();
        createBroadcast();

        sharedPreferencesManager.setTimeInApp(System.currentTimeMillis());
        long timeLoginApp = System.currentTimeMillis();
        if (sharedPreferencesManager.getcheckTimeOutApp()) {
            if (timeLoginApp < sharedPreferencesManager.getTimeAlarmService()) {
                btnStartCall.setText("Cancel Call");
                btnStartCall.setTextColor(getResources().getColor(R.color.colorRed));
                aBooleanCheckStartCall = true;
                rl_click.setClickable(true);
                long time = (sharedPreferencesManager.getTimeAlarmService() - timeLoginApp);
                long timeUpdate = System.currentTimeMillis() + time;
                createAlarmService(timeUpdate);


                countDownTimer = new CountDownTimer(time, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int seconds = (int) (millisUntilFinished / 1000) % 60;
                        int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                        int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                        if (minutes < 10) {
                            if (seconds < 10) {
                                tvDelay.setText("0" + hours + ":0" + minutes + ":0" + seconds);
                            } else {
                                tvDelay.setText("0" + hours + ":0" + minutes + ":" + seconds);
                            }
                        } else {
                            if (seconds < 10) {
                                tvDelay.setText("0" + hours + ":" + minutes + ":0" + seconds);
                            } else {
                                tvDelay.setText("0" + hours + ":" + minutes + ":" + seconds);
                            }
                        }

                    }

                    @Override
                    public void onFinish() {
                        tvDelay.setText("00:00:00");
                    }
                }.start();


            }else {
                rl_click.setClickable(false);
            }
        }

    }


    private void createPermisson() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) this, PERMISSIONS, REQUEST);
            } else {

            }
        } else {

        }

    }

    private void createBroadcast() {
        recever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case CHECK_START_CALL:
                        aBooleanCheckStartCall = false;
                        btnStartCall.setText("Start Call");
                        btnStartCall.setTextColor(getResources().getColor(R.color.colorEditText));
                        rl_click.setClickable(false);
                        break;
                }
            }
        };
        filter = new IntentFilter();
        filter.addAction(CHECK_START_CALL);
        getBaseContext().registerReceiver(recever, filter);

    }

    private void createAlarmService(long timeStart) {
        Intent intent = new Intent(this, CallingReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), REQUEST_CODE_PENDING_INTENT, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeStart, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeStart, pendingIntent);
        }
        sharedPreferencesManager.setTimeAlarmService(timeStart);
    }

    private void readSharePre() {
        swVibrate.setChecked(sharedPreferencesManager.getStatusVibrate());
        edtNameContact.setText(sharedPreferencesManager.getNameContact());
        edtPhoneContact.setText(sharedPreferencesManager.getPhoneContact());

        if (sharedPreferencesManager.getImgeChose().equals("")) {
            ivContacts.setImageDrawable(getResources().getDrawable(R.drawable.image_icon));
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(sharedPreferencesManager.getImgeChose());
            ivContacts.setImageBitmap(bitmap);
        }

        File file = new File(sharedPreferencesManager.getImgeChose());
        if (!file.exists()) {
            ivContacts.setImageDrawable(getResources().getDrawable(R.drawable.image_icon));
        }

    }

    private void onClickItem() {
        rlRingtone.setOnClickListener(this);
        btnStartCall.setOnClickListener(this);
//        rlVibrate.setOnClickListener(this);
        swVibrate.setOnCheckedChangeListener(this);
        rlVoice.setOnClickListener(this);
        ivContacts.setOnClickListener(this);
        rlDelay.setOnClickListener(this);

        rabNameContact.setOnClickListener(this);
        rabPhoneContact.setOnClickListener(this);
        rlWallpaper.setOnClickListener(this);

    }

    private void initView() {
        rl_click = findViewById(R.id.rl_click);

        tvDelay = findViewById(R.id.tvTime);
        tvNameVoice = findViewById(R.id.tvNameVoice);
        tvRingTone = findViewById(R.id.tvRingTone);

        rlRingtone = findViewById(R.id.rlGroupRingtone);
        btnStartCall = findViewById(R.id.btn_Start_Call);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

        edtNameContact = findViewById(R.id.edtNameContact);
        edtPhoneContact = findViewById(R.id.edtPhoneContact);
        rabNameContact = findViewById(R.id.radName);
        rabPhoneContact = findViewById(R.id.radPhone);

//        rlVibrate = findViewById(R.id.rlGroupVibrate);
        swVibrate = findViewById(R.id.swVibrate);
        rlVoice = findViewById(R.id.rlGroupVoice);
        ivContacts = findViewById(R.id.imv_Contacts);
        rlDelay = findViewById(R.id.rlDelay);
        rlWallpaper = findViewById(R.id.rlWallpaper);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Start_Call:
                boolean check = sharedPreferencesManager.getCheckRadioButton();

                if (!aBooleanCheckStartCall) {
                    aBooleanCheckStartCall = true;
                    sharedPreferencesManager.setcheckTimeOutApp(true);
                    btnStartCall.setText("Cancel Call");
                    rl_click.setClickable(true);
                    btnStartCall.setTextColor(getResources().getColor(R.color.colorRed));
                    if (check) {
                        phoneContacts = edtPhoneContact.getText().toString().trim();
                        sharedPreferencesManager.setPhoneContact(phoneContacts);
                    } else {
                        nameContacts = edtNameContact.getText().toString().trim();
                        sharedPreferencesManager.setNameContact(nameContacts);
                    }
                    checkTimeDelay();

                    if (timeCountDownt == 0) {
                        sharedPreferencesManager.setScreenOnOff(true);
                        startActivity(new Intent(this, CallActivity.class));
                    } else {
                        cancleAlarm(alarmManager, pendingIntent);
                        createAlarmService(System.currentTimeMillis() + timeCountDownt);
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        countDownTimer = new CountDownTimer(timeCountDownt, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                int seconds = (int) (millisUntilFinished / 1000) % 60;
                                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                                if (minutes < 10) {
                                    if (seconds < 10) {
                                        tvDelay.setText("0" + hours + ":0" + minutes + ":0" + seconds);
                                    } else {
                                        tvDelay.setText("0" + hours + ":0" + minutes + ":" + seconds);
                                    }
                                } else {
                                    if (seconds < 10) {
                                        tvDelay.setText("0" + hours + ":" + minutes + ":0" + seconds);
                                    } else {
                                        tvDelay.setText("0" + hours + ":" + minutes + ":" + seconds);
                                    }
                                }

                            }

                            @Override
                            public void onFinish() {
                                tvDelay.setText("00:00:00");
                            }
                        }.start();
                    }


                } else {
                    rl_click.setClickable(false);
                    aBooleanCheckStartCall = false;
                    sharedPreferencesManager.setcheckTimeOutApp(false);
                    btnStartCall.setText("Start Call");
                    btnStartCall.setTextColor(getResources().getColor(R.color.colorEditText));
                    tvDelay.setText(sharedPreferencesManager.getTimeDelay());
                    cancleAlarm(alarmManager, pendingIntent);
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                }


                break;
            case R.id.rlWallpaper:
                startActivity(new Intent(this, WallpaperActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.rlGroupVoice:

                isCheckImageVoice = true;
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    if (!hasPermissions(this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) this, PERMISSIONS, REQUEST);
                    } else {
                        nextScreenVoice();
                    }
                } else {
                    nextScreenVoice();
                }

                break;
            case R.id.imv_Contacts:
                isCheckImageVoice = false;

                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    if (!hasPermissions(this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) this, PERMISSIONS, REQUEST);
                    } else {
                        nextScreenImage();
                    }
                } else {
                    nextScreenImage();
                }


                break;
            case R.id.radName:
                rabNameContact.setChecked(true);
                rabPhoneContact.setChecked(false);
                sharedPreferencesManager.setCheckRadioButton(false);
                edtNameContact.setVisibility(View.VISIBLE);
                edtPhoneContact.setVisibility(View.INVISIBLE);
                break;
            case R.id.radPhone:
                rabNameContact.setChecked(false);
                rabPhoneContact.setChecked(true);
                sharedPreferencesManager.setCheckRadioButton(true);
                edtNameContact.setVisibility(View.INVISIBLE);
                edtPhoneContact.setVisibility(View.VISIBLE);
//
                break;
            case R.id.rlDelay:
                startActivity(new Intent(this, DelayActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.rlGroupRingtone:
                startActivity(new Intent(this, RingtoneActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    private void nextScreenImage() {
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.complete_action_using)), ACTIVITY_SELECT_IMAGE);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void cancleAlarm(AlarmManager alarmManager, PendingIntent pendingIntent) {
        try {
            alarmManager.cancel(pendingIntent);
        } catch (Exception e) {
        }
    }

    private void checkTimeDelay() {
        switch (sharedPreferencesManager.getPositionDelay()) {
            case 0:
                timeCountDownt = 0;
                break;
            case 1:
                timeCountDownt = 3000;
                break;
            case 2:
                timeCountDownt = 10000;
                break;
            case 3:
                timeCountDownt = 30000;
                break;
            case 4:
                timeCountDownt = 60000;
                break;
            case 5:
                timeCountDownt = 120000;
                break;
            case 6:
                timeCountDownt = 300000;
                break;
            case 7:
                timeCountDownt = 480000;
                break;
            case 8:
                timeCountDownt = 720000;
                break;
            case 9:
                timeCountDownt = 1200000;
                break;
            case 10:
                timeCountDownt = 3600000;
                break;
        }
    }

    private void setNameContacts(String nameContacts) {
        this.nameContacts = nameContacts;
        dialogContacts.dismiss();
//        edtNameCall.setText(nameContacts);

    }

    private void createDialogContacts() {
        dialogContacts = new Dialog(this);
        dialogContacts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogContacts.setCancelable(true);
        dialogContacts.setContentView(R.layout.custom_dialog_contacts);
        dialogContacts.show();

        tvContacts = dialogContacts.findViewById(R.id.tvContacts);
        tvPolice = dialogContacts.findViewById(R.id.tvPolice);
        tvUnknown = dialogContacts.findViewById(R.id.tvUnknown);
        tvGirlfriend = dialogContacts.findViewById(R.id.tvGirlfriend);
        tvBoyfriend = dialogContacts.findViewById(R.id.tvBoyfriend);

        tvContacts.setOnClickListener(this);
        tvPolice.setOnClickListener(this);
        tvUnknown.setOnClickListener(this);
        tvGirlfriend.setOnClickListener(this);
        tvBoyfriend.setOnClickListener(this);

    }

    private void nextScreenVoice() {
        Intent intent = new Intent(this, VoiceActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!isCheckImageVoice)
                        nextScreenImage();
                    else nextScreenVoice();
                } else {
                    Toast.makeText(this, "Please grant the application permission to continue using the app", Toast.LENGTH_LONG).show();
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
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case ACTIVITY_SELECT_IMAGE:

                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String filePath;
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                    if (cursor == null){
                        filePath =selectedImage.getPath();
                    }else {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        filePath = cursor.getString(columnIndex);
                        cursor.close();
                    }

                    sharedPreferencesManager.setImgeChose(filePath);
                    Bitmap SelectedImage = BitmapFactory.decodeFile(filePath);
                    ivContacts.setImageBitmap(SelectedImage);

                }


                break;
        }
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvDelay.setText(sharedPreferencesManager.getTimeDelay());
        tvNameVoice.setText(sharedPreferencesManager.getNameVoice());
        tvRingTone.setText(sharedPreferencesManager.getRingtone());

        if (sharedPreferencesManager.getCheckRadioButton()) {
            rabPhoneContact.setChecked(true);
            rabNameContact.setChecked(false);
            edtNameContact.setVisibility(View.INVISIBLE);
            edtPhoneContact.setVisibility(View.VISIBLE);
        } else {
            rabNameContact.setChecked(true);
            rabPhoneContact.setChecked(false);
            edtPhoneContact.setVisibility(View.INVISIBLE);
            edtNameContact.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recever != null) {
            getBaseContext().unregisterReceiver(recever);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharedPreferencesManager.setStatusVibrate(true);
                } else {
                    sharedPreferencesManager.setStatusVibrate(false);
                }
    }
}
