package edu.uncc.inclass07;

import android.content.Context;
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
 * In Class Assignment 07
 * ArticleAdapter.java
 * @author Sean Fox
 * @author Andrew Lambropoulos
 */

public class ArticleAdapter extends ArrayAdapter<Article> {
    public ArticleAdapter(@NonNull Context context, int resource, @NonNull List<Article> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article art = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.article_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.articleImage);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.pubInfo = (TextView) convertView.findViewById(R.id.pubInfo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //set the data from the email object
        Picasso.with(convertView.getContext()).load(art.urlToImage).into(viewHolder.image);
        viewHolder.title.setText(art.title);
        viewHolder.pubInfo.setText(art.publishedAt);
        return convertView;
    }

    private static class ViewHolder{
        ImageView image;
        TextView title;
        TextView pubInfo;
    }

}
