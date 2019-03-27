package com.mgosu.fakecalliphonestyle.model;

import android.widget.ImageView;
import android.widget.TextView;

public class Delay {
    private TextView timeDelay;
    private ImageView imageView;

    public Delay(TextView timeDelay, ImageView imageView) {
        this.timeDelay = timeDelay;
        this.imageView = imageView;
    }

    public TextView getTimeDelay() {
        return timeDelay;
    }

    public void setTimeDelay(TextView timeDelay) {
        this.timeDelay = timeDelay;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
