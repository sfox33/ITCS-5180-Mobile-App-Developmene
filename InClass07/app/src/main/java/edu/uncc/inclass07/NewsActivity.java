package edu.uncc.inclass07;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * In Class Assignment 07
 * NewsActivity.java
 * @author Sean Fox
 * @author Andrew Lambropoulos
 */

public class NewsActivity extends AppCompatActivity {

    private String category;
    private ProgressDialog dialog;
    private NewsObject news;
    private ListView listView;
    private String url = "https://newsapi.org/v2/top-headlines?category=";
    private String url2 = "&apiKey=a38a569078e24e65b542a8ed24c7389f";
    public static String ART ="article";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        dialog = new ProgressDialog(NewsActivity.this);
        Log.d("demo", "Huey lewis");

        if(getIntent() != null && getIntent().getExtras() != null) {
            category = getIntent().getExtras().getString(CategoriesActivity.CATE);
            Log.d("demo", "Intent was found with " + category);
            setTitle(category);
            String ultURL = url + category + url2;
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading Headlines");
            dialog.setProgress(0);

            dialog.show();
            new RetrieveArticles(NewsActivity.this).execute(ultURL);
        }
    }

    public void doWork(NewsObject newNews) {
        news=newNews;
        dialog.dismiss();

        listView = (ListView)findViewById(R.id.listView2);

        if(news.articles.size() == 0) {
            Toast.makeText(NewsActivity.this, "No Articles Found", Toast.LENGTH_LONG).show();
            return;
        }
        ArticleAdapter adapter = new ArticleAdapter(NewsActivity.this, R.layout.article_layout, news.articles);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NewsActivity.this, DetailActivity.class);
                intent.putExtra(NewsActivity.ART, news.articles.get(position));
                startActivity(intent);
            }
        });


    }
}
