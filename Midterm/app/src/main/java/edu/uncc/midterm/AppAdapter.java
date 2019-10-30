package edu.uncc.midterm;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Sean on 3/12/2018.
 * AppAdapter.java
 * Midterm Exam
 */

public class AppAdapter extends ArrayAdapter<App> {
    public AppAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        App app = getItem(position);
        ViewHolder viewHolder;
        StringBuilder temp = new StringBuilder();

        if(convertView == null){ //if no view to re-use then inflate a new one
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.app_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.artwork);
            viewHolder.name = (TextView) convertView.findViewById(R.id.appName);
            viewHolder.artist = (TextView) convertView.findViewById(R.id.artist);
            viewHolder.date = (TextView) convertView.findViewById(R.id.releaseDate);
            viewHolder.genres = (TextView) convertView.findViewById(R.id.genres);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //set the data from the app object
        //Picasso.with(convertView.getContext()).load(app.imageUrl).into(viewHolder.image);
        Picasso.get().load(app.imageUrl).into(viewHolder.image);
        viewHolder.name.setText(app.name);
        viewHolder.artist.setText(app.artist);
        viewHolder.date.setText(app.date);
        for(int i = 0; i < app.genres.size(); i++) {
            temp.append(app.genres.get(i));
            if(i + 1 != app.genres.size()) {
                temp.append(", ");
            }
        }
        viewHolder.genres.setText(temp.toString());

        return convertView;
    }

    //View Holder to cache the views
    private static class ViewHolder{
        ImageView image;
        TextView name, artist, genres, date;
    }

}
