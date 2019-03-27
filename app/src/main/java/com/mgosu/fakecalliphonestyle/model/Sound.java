package com.mgosu.fakecalliphonestyle.model;

public class Sound {
    private int ivCTickVoice;
    private String tvNameSong;
    private String keyVoice;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Sound(int ivCTickVoice, String tvNameSong, String keyVoice, String path) {
        this.ivCTickVoice = ivCTickVoice;
        this.tvNameSong = tvNameSong;
        this.keyVoice = keyVoice;
        this.path = path;
    }

    //
    public String getKeyVoice() {
        return keyVoice;
    }

    public void setKeyVoice(String keyVoice) {
        this.keyVoice = keyVoice;

    }

    public int getIvCTickVoice() {
        return ivCTickVoice;
    }

    public void setIvCTickVoice(int ivCTickVoice) {
        this.ivCTickVoice = ivCTickVoice;
    }

    public String getTvNameSong() {
        return tvNameSong;
    }

    public void setTvNameSong(String tvNameSong) {
        this.tvNameSong = tvNameSong;
    }

}
