package com.anriku.simpleimageloader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.anriku.simpleimageloader.utils.MD5Helper;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String[] images = new String[]{
            "http://ov80qs5d9.bkt.clouddn.com/2.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/20.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/1.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/111.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/12.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/11.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/10.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/1_201108022210121gfzQ.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/21.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/18.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/160%20%281%29.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/22.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/129_244346_28eb500fc4f28f8.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/220.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/13a534234528ed4444ebf2737971def3.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/160.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/15.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/222.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/220.png",
            "http://ov80qs5d9.bkt.clouddn.com/2.png",
            "http://ov80qs5d9.bkt.clouddn.com/2000.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/22.png",
            "http://ov80qs5d9.bkt.clouddn.com/t0276d2c2f0a2638662.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/t02d11a83bd95401b4d.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/t028560a01d57d64fe7.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720140119212443.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720140119212452.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/2000%20%281%29.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/20110305130518-1113682334.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720140119212436.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/t0232ec33baa2c5b868.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/t02481fe65e5ef2d17d.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720140203231014.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720140128210256.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/150814naoge333o6uklell.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/t0239107e0f06806305.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/20090313085301_5614_0.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720140215001659.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720140126214125.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/t02ab0804d6ba0f5b8d.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720140203231058.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/t02197ee545c57e424f.png",
            "http://ov80qs5d9.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720140203231125.jpg",
            "http://ov80qs5d9.bkt.clouddn.com/t0244bcb5c9a98ad5b8.png",
            "http://ov80qs5d9.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720140213201650.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            init();
        }
        Toast.makeText(this, MD5Helper.hashKeyForDisk(images[0]),Toast.LENGTH_LONG).show();
    }

    private void init() {
        recyclerView = findViewById(R.id.rv);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this,2);
        RecAdapter adapter = new RecAdapter(this,images);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    init();
                }else {
                    Toast.makeText(this,"请允许权限",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
