package edu.uncc.midterm;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Sean on 3/12/2018.
 * AppsActivity.java
 * Midterm Exam
 */

public class AppsActivity extends AppCompatActivity implements AppList {

    private Button filterBtn;
    private TextView genreId;
    AlertDialog.Builder builder;
    private ProgressDialog dialog;
    private ArrayList<String> genres;
    private ListView listView;
    public static String APP = "application";
    private ArrayList<App> allAppsList, filteredAppList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        setTitle("Apps");

        genreId = (TextView)findViewById(R.id.genreId);
        filterBtn = (Button) findViewById(R.id.filterButton);
        builder = new AlertDialog.Builder(AppsActivity.this);
        genres = new ArrayList<>();
        dialog = new ProgressDialog(AppsActivity.this);
        dialog.setMessage("Loading Apps...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);

        dialog.setProgress(0);
        dialog.show();
        if(!isConnectedOnline()) {
            dialog.dismiss();
            Toast.makeText(AppsActivity.this, "No Internet Connection Found.  Please Restart App.", Toast.LENGTH_LONG).show();
        } else {

            new RetrieveApps(this).execute("https://rss.itunes.apple.com/api/v1/us/ios-apps/top-grossing/all/50/explicit.json");
            filterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("demo", "Detecting click.  Size of genres is " + genres.toArray(new String[genres.size()]).length);
                    builder.setTitle("Choose Genre")
                            .setCancelable(true)
                            .setItems(genres.toArray(new String[genres.size()]), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    genreId.setText(genres.get(which));
                                    filterApps(which);
                                }
                            });
                    final AlertDialog simpleAlert = builder.create();
                    simpleAlert.show();
                }
            });
        }

    }

    @Override
    public void updateApps(ArrayList<App> appsList) {
        dialog.dismiss();
        allAppsList = appsList;
        Collections.sort(allAppsList, new NameComparator());
        App currApp;
        final ArrayList<App> apps = appsList;
        for(int i = 0; i < appsList.size(); i++) {
            currApp = appsList.get(i);
            for(int j = 0; j < currApp.genres.size(); j++) {
                if(!genres.contains(currApp.genres.get(j))) {
                    genres.add(currApp.genres.get(j));
                }
            }
        }
        Collections.sort(genres);
        genres.add(0, "All");
        genreId.setText(genres.get(0));
        listView = (ListView)findViewById(R.id.listView);

        if(appsList.size() == 0) {
            Toast.makeText(AppsActivity.this, "No Articles Found", Toast.LENGTH_LONG).show();
            return;
        }
        AppAdapter adapter = new AppAdapter(AppsActivity.this, R.layout.app_layout, allAppsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AppsActivity.this, DetailActivity.class);
                intent.putExtra(AppsActivity.APP, apps.get(position));
                startActivity(intent);
            }
        });
    }

    public void filterApps(int genreIndex) {
        if(genreIndex == 0) {
            filteredAppList = allAppsList;
        } else {
            filteredAppList = new ArrayList<>();
            for (int i = 0; i < allAppsList.size(); i++) {
                if (allAppsList.get(i).genres.contains(genres.get(genreIndex))) {
                    filteredAppList.add(allAppsList.get(i));
                }
            }
        }

        if(filteredAppList.size() == 0) {
            Toast.makeText(AppsActivity.this, "No Articles Found", Toast.LENGTH_LONG).show();
            return;
        }

        Collections.sort(filteredAppList, new NameComparator());
        AppAdapter adapter = new AppAdapter(AppsActivity.this, R.layout.app_layout, filteredAppList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AppsActivity.this, DetailActivity.class);
                intent.putExtra(AppsActivity.APP, filteredAppList.get(position));
                startActivity(intent);
            }
        });
    }

    private boolean isConnectedOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
