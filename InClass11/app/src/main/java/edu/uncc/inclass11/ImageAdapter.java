package edu.uncc.inclass11;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Andrew Lambropoulos on 4/9/2018.
 * Created by Sean Fox
 * ImageAdapter.java
 * Assignment: InClass11
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    ArrayList<Post> posts;
    int index;

    public ImageAdapter(ArrayList<Post> posts){
        index = 0;
        this.posts = posts;
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo, parent, false);
        ImageAdapter.ViewHolder vh = new ImageAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        index = position;
        if(posts.get(index).getImages().size() == 0) {
            Picasso.get().load(posts.get(index).getLink()).into(holder.image);
        } else {
            Picasso.get().load(posts.get(index).getImages().get(0).getLink()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public ViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.photo);
        }
    }
}
