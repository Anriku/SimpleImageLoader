package com.anriku.simpleimageloader.strategy;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by anriku on 2018/2/10.
 */

public class DoubleCache implements ImageCache {

    private DiskCache diskCache;
    private MemoryCache memoryCache;

    public DoubleCache(Context context) {
        diskCache = new DiskCache(context);
        memoryCache = new MemoryCache();
    }

    @Override
    public void putImage(String url, Bitmap bitmap) {
        memoryCache.putImage(url,bitmap);
        diskCache.putImage(url,bitmap);
    }

    @Override
    public Bitmap getImage(String url) {
        return memoryCache.getImage(url) != null ? memoryCache.getImage(url) : diskCache.getImage(url);
    }
}
