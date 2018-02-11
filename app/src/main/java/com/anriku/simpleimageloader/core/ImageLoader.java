package com.anriku.simpleimageloader.core;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.anriku.simpleimageloader.strategy.ImageCache;
import com.anriku.simpleimageloader.utils.ImageResize;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by anriku on 2018/2/10.
 */

public class ImageLoader {
    //这是一个缓存的抽象(在这里是一个接口，所有的缓存策略都应该实现这个接口)
    private ImageCache imageCache;

    //这里初始化的一个大小为Java虚拟机可用的处理器个数的线程池，
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 通过Handler的post方法实现到UI线程中去显示图片的功能
     *
     * @param imageView
     * @param bitmap
     */
    private void updateImageView(final ImageView imageView, final Bitmap bitmap) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    /**
     * 设置具体的Cache方案(这是一种策略模式);也体现了面向接口编程(面向抽象编程的优点)。
     * 这是面向对象几大原则核心体现地方，稍后再进行解释
     *
     * @param imageCache
     */
    public void setImageCache(ImageCache imageCache) {
        this.imageCache = imageCache;
    }

    /**
     * 对网上拉取还是本地拉取Bitmap进行一个判断
     *
     * @param url
     * @param imageView
     */
    public void displayImage(String url, ImageView imageView) {
        Bitmap bitmap = imageCache.getImage(url);
        imageView.setTag(url);
        if (bitmap != null) {
            if (imageView.getTag().equals(url)) {
                imageView.setImageBitmap(bitmap);
            }
            return;
        }
        //如果图片没有缓存就进行网上下载操作
        submitLoadRequest(url, imageView);
    }

    /**
     * 在线程池中进行网络请求，防止应用无响应(Application Not Response，ANR)
     *
     * @param url
     * @param imageView
     */
    private void submitLoadRequest(final String url, final ImageView imageView) {
        imageView.setTag(url);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url);
                if (bitmap == null) {
                    return;
                }
                if (imageView.getTag().equals(url)) {
                    updateImageView(imageView, bitmap);
                }
                imageCache.putImage(url, bitmap);
            }
        });
    }

    /**
     * 网络请求的具体实现方法
     *
     * @param url
     * @return
     */
    private Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            URL imageUrl = new URL(url);
            final HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            inputStream = conn.getInputStream();
            bufferedInputStream = new BufferedInputStream(inputStream);
            bitmap = ImageResize.decodeSampledBitmapFromInputStream(bufferedInputStream, 150, 150);
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
