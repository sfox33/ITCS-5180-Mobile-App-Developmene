package edu.uncc.inclass07;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * In Class Assignment 07
 * DetailActivity.java
 * @author Sean Fox
 * @author Andrew Lambropoulos
 */

public class DetailActivity extends AppCompatActivity {

    private Article article;
    private TextView title, pubDate, info;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        setTitle("Detail Activity");
        title = (TextView) findViewById(R.id.articleTitle);
        pubDate = (TextView) findViewById(R.id.articlePub);
        info = (TextView) findViewById(R.id.articleInfo);
        image = (ImageView) findViewById(R.id.articleImage);

        if(getIntent() != null && getIntent().getExtras() != null) {
            article = (Article) getIntent().getExtras().get(NewsActivity.ART);

            title.setText(article.title);
            pubDate.setText(article.publishedAt);
            info.setText(article.description);
            Picasso.with(DetailActivity.this).load(Uri.parse(article.urlToImage)).into(image);
        }
    }
}
