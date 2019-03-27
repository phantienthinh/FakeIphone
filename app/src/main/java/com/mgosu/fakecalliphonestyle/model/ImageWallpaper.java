package com.mgosu.fakecalliphonestyle.model;

import android.widget.ImageView;

public class ImageWallpaper {
    private ImageView imageView;
    private int pathImageResouce;
    private boolean checkTickWallPaper;

    public ImageWallpaper(ImageView imageView, int pathImageResouce, boolean checkTickWallPaper) {
        this.imageView = imageView;
        this.pathImageResouce = pathImageResouce;
        this.checkTickWallPaper = checkTickWallPaper;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getPathImageResouce() {
        return pathImageResouce;
    }

    public void setPathImageResouce(int pathImageResouce) {
        this.pathImageResouce = pathImageResouce;
    }

    public boolean getCheckTickWallPaper() {
        return checkTickWallPaper;
    }

    public void setCheckTickWallPaper(boolean checkTickWallPaper) {
        this.checkTickWallPaper = checkTickWallPaper;
    }
}
