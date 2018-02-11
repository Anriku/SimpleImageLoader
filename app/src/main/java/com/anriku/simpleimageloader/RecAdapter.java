package com.anriku.simpleimageloader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anriku.simpleimageloader.core.ImageLoader;
import com.anriku.simpleimageloader.strategy.DoubleCache;


/**
 * Created by anriku on 2018/2/10.
 */

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {

    private String[] images;
    private ImageLoader imageLoader;

    public RecAdapter(Context context,String[] images) {
        this.images = images;
        imageLoader = new ImageLoader();
        DoubleCache doubleCache = new DoubleCache(context);
        imageLoader.setImageCache(doubleCache);
    }

    @Override
    public RecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecAdapter.ViewHolder holder, int position) {
        imageLoader.displayImage(images[position],holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv);
        }
    }
}
