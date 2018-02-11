package com.anriku.simpleimageloader.strategy;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.anriku.simpleimageloader.utils.MD5Helper;

/**
 * 本类是内存缓存的具体实现
 * Created by anriku on 2018/2/10.
 */
public class MemoryCache implements ImageCache {

    //内存缓存核心类
    LruCache<String,Bitmap> memoryCache;

    public MemoryCache() {
        initImageCache();
    }

    private void initImageCache() {

        //获取最大可得内存
        final int maxMemory = (int) ((Runtime.getRuntime().maxMemory()) / 1024);

        //缓存为内存的四分之一
        final int cacheSize = maxMemory / 4;

        memoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    @Override
    public void putImage(String url, Bitmap bitmap) {
        String hashcode = MD5Helper.hashKeyForDisk(url);
        memoryCache.put(hashcode,bitmap);
    }

    @Override
    public Bitmap getImage(String url) {
        String hashCode = MD5Helper.hashKeyForDisk(url);
        return memoryCache.get(hashCode);
    }
}
