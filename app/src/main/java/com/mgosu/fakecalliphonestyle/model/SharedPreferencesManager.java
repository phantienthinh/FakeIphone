package com.mgosu.fakecalliphonestyle.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesManager {

    private SharedPreferences preferences;
    private static SharedPreferencesManager instance;

    private SharedPreferencesManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context);
        }
        return instance;
    }
    //trạng thái vibrate

    public void setStatusVibrate(boolean status) {
        preferences.edit().putBoolean("KEY_Status_Vibrate", status).apply();
    }

    public boolean getStatusVibrate() {
        return preferences.getBoolean("KEY_Status_Vibrate", true);
    }

    //songLocation
    public void setSongLocation(String location) {
        preferences.edit().putString("KEY_SongLocation", location).apply();
    }

    public String getSongLocation() {
        return preferences.getString("KEY_SongLocation", "");
    }

    // tên voice
    public void setNameVoice(String string) {
        preferences.edit().putString("KEY_Voice", string).apply();
    }

    public String getNameVoice() {
        return preferences.getString("KEY_Voice", "");
    }

    // positionVoice
    public void setpositionVoice(int i) {
        preferences.edit().putInt("KEY_positionVoice", i).apply();
    }

    public int getpositionVoice() {
        return preferences.getInt("KEY_positionVoice", 0);
    }

    // positionWallpaper
    public void setpositionWallpaper(int i) {
        preferences.edit().putInt("KEY_positionWallpaper", i).apply();
    }

    public int getpositionWallpaper() {
        return preferences.getInt("KEY_positionWallpaper", 0);
    }

    //time delay
    public void setRingtone(String string) {
        preferences.edit().putString("KEY_Ringtone", string).apply();
    }

    public String getRingtone() {
        return preferences.getString("KEY_Ringtone", "");
    }

    //delay
    public void setDelay(String string) {
        preferences.edit().putString("KEY_Delay", string).apply();
    }

    public String getDelay() {
        return preferences.getString("KEY_Delay", "");
    }

    //check radioButton
    public void setCheckRadioButton(boolean button) {
        preferences.edit().putBoolean("KEY_CheckRadioButton", button).apply();
    }

    public boolean getCheckRadioButton() {
        return preferences.getBoolean("KEY_CheckRadioButton", false);
    }

    //check NameContact
    public void setNameContact(String name) {
        preferences.edit().putString("KEY_NameContact", name).apply();
    }

    public String getNameContact() {
        return preferences.getString("KEY_NameContact", "");
    }

    //check PhoneContact
    public void setPhoneContact(String phone) {
        preferences.edit().putString("KEY_PhoneContact", phone).apply();
    }

    public String getPhoneContact() {
        return preferences.getString("KEY_PhoneContact", "");
    }

    //check Switch Ringtone
    public void setcheckSwitchRingTone(boolean sw) {
        preferences.edit().putBoolean("KEY_SwitchRingTone", sw).apply();
    }

    public boolean getSwitchRingTone() {
        return preferences.getBoolean("KEY_SwitchRingTone", false);
    }

    //check Position Delay
    public void setPositionDelay(int PositionDelay) {
        preferences.edit().putInt("KEY_PositionDelay", PositionDelay).apply();
    }

    public int getPositionDelay() {
        return preferences.getInt("KEY_PositionDelay", 0);
    }


    //String Time Delay
    public void setTimeDelay(String Delay) {
        preferences.edit().putString("KEY_TimeDelay", Delay).apply();
    }

    public String getTimeDelay() {
        return preferences.getString("KEY_TimeDelay", "None");
    }


    //position RingTone
    public void setPositionRingTone(int i) {
        preferences.edit().putInt("KEY_PositionRingTone", i).apply();
    }

    public int getPositionRingTone() {
        return preferences.getInt("KEY_PositionRingTone", 0);
    }

    //idRingTone
    public void setIdRingTone(int i) {
        preferences.edit().putInt("KEY_IdRingTone", i).apply();
    }

    public int getIdRingTone() {
        return preferences.getInt("KEY_IdRingTone", 100);
    }

    //save ImgeChose
    public void setImgeChose(String s) {
        preferences.edit().putString("KEY_ImgeChose", s).apply();
    }

    public String getImgeChose() {
        return preferences.getString("KEY_ImgeChose", "");
    }

    //save ScreenOnOff
    public void setScreenOnOff(boolean b) {
        preferences.edit().putBoolean("KEY_ScreenOnOff", b).apply();
    }

    public boolean getScreenOnOff() {
        return preferences.getBoolean("KEY_ScreenOnOff", false);
    }

    //setTimeInApp
    public void setTimeInApp(long b) {
        preferences.edit().putLong("KEY_TimeInApp", b).apply();
    }

    public long getTimeInApp() {
        return preferences.getLong("KEY_TimeInApp", 0);
    }


    //checkTimeOutApp
    public void setcheckTimeOutApp(boolean b) {
        preferences.edit().putBoolean("KEY_checkTimeOutApp", b).apply();
    }

    public boolean getcheckTimeOutApp() {
        return preferences.getBoolean("KEY_checkTimeOutApp", false);
    }


    //TimeAlarmService
    public void setTimeAlarmService(long b) {
        preferences.edit().putLong("KEY_TimeAlarmService", b).apply();
    }

    public long getTimeAlarmService() {
        return preferences.getLong("KEY_TimeAlarmService", 0);
    }



    //check 2 list
    public void setListVoiceOrRecorder(boolean b) {
        preferences.edit().putBoolean("KEY_ListVoiceOrRecorder", b).apply();
    }

    public boolean getListVoiceOrRecorder() {
        return preferences.getBoolean("KEY_ListVoiceOrRecorder", false);
    }

    //posVoiceRecorder
    public void setPosVoiceRecorder(int b) {
        preferences.edit().putInt("KEY_VoiceRecorder", b).apply();
    }

    public int getPosVoiceRecorder() {
        return preferences.getInt("KEY_VoiceRecorder", 0);
    }

    //FirstTime
    public void setFirstTime(boolean b) {
        preferences.edit().putBoolean("KEY_FirstTime", b).apply();
    }

    public boolean getFirstTime() {
        return preferences.getBoolean("KEY_FirstTime", false);
    }


    //StringRandom
    public void setStringRandom(int b) {
        preferences.edit().putInt("KEY_StringRandom", b).apply();
    }

    public int getStringRandom() {
        return preferences.getInt("KEY_StringRandom", 1);
    }

}
