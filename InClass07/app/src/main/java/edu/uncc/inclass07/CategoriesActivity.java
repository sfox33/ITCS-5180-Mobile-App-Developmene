package edu.uncc.inclass07;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * In Class Assignment 07
 * CategoriesActivity.java
 * @author Sean Fox
 * @author Andrew Lambropoulos
 */

public class CategoriesActivity extends AppCompatActivity {

    public static String CATE = "category";

    private String[] category = new String[]{"business", "entertainment", "general", "health", "science", "sports", "technology"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Categories");

        ListView listView = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CategoriesActivity.this, android.R.layout.simple_list_item_1,
                                                                android.R.id.text1, category);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!isConnected()) {
                    Toast.makeText(CategoriesActivity.this, "No Internet Connection Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CategoriesActivity.this, NewsActivity.class);
                    intent.putExtra(CATE, category[i]);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                                && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
}
