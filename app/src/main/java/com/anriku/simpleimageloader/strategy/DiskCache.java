package com.anriku.simpleimageloader.strategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.anriku.simpleimageloader.utils.ImageResize;
import com.anriku.simpleimageloader.utils.MD5Helper;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by anriku on 2018/2/10.
 */

public class DiskCache implements ImageCache {

    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024;
    private Context context;

    public DiskCache(Context context) {
        this.context = context;
    }

    /**
     * 获取File对象
     *
     * @param context
     * @param dirName
     * @return
     */
    public File getFile(Context context, String dirName) {
        String filePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            filePath = context.getExternalCacheDir().getPath();
        } else {
            filePath = context.getCacheDir().getPath();
        }
        return new File(filePath + File.separator + dirName);
    }

    /**
     * 初始化DiskLruCache对象
     *
     * @param context
     */
    private DiskLruCache initDiskCache(Context context) {
        DiskLruCache diskLruCache = null;
        File file = getFile(context, "imageCache");
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            diskLruCache = DiskLruCache.open(file, 1, 1, DISK_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return diskLruCache;
    }

    @Override
    public void putImage(String url, Bitmap bitmap) {
        DiskLruCache diskLruCache = initDiskCache(context);
        DiskLruCache.Editor editor = null;
        String hashCode = MD5Helper.hashKeyForDisk(url);
        OutputStream os = null;
        InputStream in;
        BufferedInputStream bis;
        BufferedOutputStream bos;

        try {
            editor = diskLruCache.edit(hashCode);
            os = editor.newOutputStream(0);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            in = new ByteArrayInputStream(baos.toByteArray());
            bis = new BufferedInputStream(in, 8 * 1024);
            bos = new BufferedOutputStream(os, 8 * 1024);
            int length;
            while ((length = bis.read()) != -1) {
                bos.write(length);
            }
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Bitmap getImage(String url) {
        DiskLruCache diskLruCache = initDiskCache(context);
        String hashCode = MD5Helper.hashKeyForDisk(url);
        Bitmap bitmap = null;
        FileInputStream is = null;
        FileDescriptor fileDescriptor = null;
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(hashCode);
            if (snapshot != null) {
                is = (FileInputStream) snapshot.getInputStream(0);
                fileDescriptor = is.getFD();
            }
            if (fileDescriptor != null){
                bitmap = ImageResize.decodeSampledBitmapFromFileDescriptor(fileDescriptor,150,150);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return bitmap;
    }
}
