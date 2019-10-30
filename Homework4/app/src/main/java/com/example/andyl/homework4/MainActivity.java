/*
MainActivity.java
Andrew Lambropoulos
Sean Fox
Homework4
 */

package com.example.andyl.homework4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button goButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private TextView text;
    private TextView title;
    private TextView publishedAt;
    private TextView description;
    private TextView number;
    private ImageView image;
    private AlertDialog.Builder builder;
    private ProgressDialog dialog;

    int index = 0;
    private String url = "http://rss.cnn.com/rss/";

    ArrayList<NewsObject> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goButton = (Button) findViewById(R.id.button);
        nextButton = (ImageButton) findViewById(R.id.imageButton);
        prevButton = (ImageButton) findViewById(R.id.imageButton2);
        text = (TextView) findViewById(R.id.textView);
        title = (TextView) findViewById(R.id.textView2);
        publishedAt = (TextView) findViewById(R.id.textView3);
        image = (ImageView) findViewById(R.id.imageView);
        description = (TextView) findViewById(R.id.textView4);
        number = (TextView) findViewById(R.id.textView5);

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);

        nextButton.setEnabled(false);
        prevButton.setEnabled(false);

        final String [] keywordsDisplay = {"Top Stories", "World", "U.S.", "Business", "Politics", "Technology", "Health", "Entertainment", "Travel", "Living", "Most Recent"};
        final String [] keywordsURL = {"cnn_topstories.rss", "cnn_world.rss", "cnn_us.rss", "money_latest.rss", "cnn_allpolitics.rss", "cnn_tech.rss", "cnn_health.rss", "cnn_showbiz.rss", "cnn_travel.rss", "cnn_living.rss", "cnn_latest.rss"};

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Choose a Keyword").setItems(keywordsDisplay, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            text.setText(keywordsDisplay[i]);
                            dialog.setMessage("Loading News Articles...");
                            dialog.setProgress(0);
                            dialog.show();
                            if (i == 3) {
                                new Async(MainActivity.this, true).execute(url + keywordsURL[i]);
                            }
                            else {
                                new Async(MainActivity.this, false).execute(url + keywordsURL[i]);
                            }
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = (index + 1) % news.size();
                doWork(news);
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = index - 1;
                if (index < 0) {
                    index += news.size();
                }
                doWork(news);
            }
        });

        image.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (news != null) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setDataAndType(Uri.parse(news.get(index).url), "html");
                    startActivity(intent);
                    /*if(intent.resolveActivity(getPackageManager()) != null) {

                    } else {
                        Toast.makeText(MainActivity.this, "Not application I guess", Toast.LENGTH_LONG).show();
                    }*/
                }
            }
        });

        title.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (news != null && !news.get(index).url.equals("")) {
                    Log.d("Demo","url equals" + news.get(index).url);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse(news.get(index).url);
                    //intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(data);
                    //Log.d("demo", "The URL is supposedly " + Uri.parse(news.get(index).url));
                    startActivity(intent);
                }
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    public void updateIndex() {
        index = 0;
    }

    public void doWork(ArrayList<NewsObject> newsObjects) {
        if (newsObjects.size() == 0) {
            Toast.makeText(MainActivity.this, "No news found", Toast.LENGTH_LONG).show();
            return;
        }
        if ((!prevButton.isEnabled() || !nextButton.isEnabled()) && newsObjects.size() > 1) {
            prevButton.setEnabled(true);
            nextButton.setEnabled(true);
        }
        this.news = newsObjects;
        dialog.dismiss();
        title.setText(news.get(index).title);
        publishedAt.setText(news.get(index).publishedAt);
        description.setText(news.get(index).description);
        Picasso.with(MainActivity.this).load(news.get(index).urlToImage).into(image);
        number.setText((index+1) + " out of " + news.size());
    }

}
