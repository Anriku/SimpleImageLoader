package com.anriku.simpleimageloader.strategy;

import android.graphics.Bitmap;

/**
 * Created by anriku on 2018/2/10.
 */

public interface ImageCache {

    void putImage(String url, Bitmap bitmap);

    Bitmap getImage(String url);
}
