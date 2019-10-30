package edu.uncc.midterm;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Sean on 3/12/2018.
 * RetrieveApps.java
 * Midterm Exam
 */

public class RetrieveApps extends AsyncTask<String, Void, ArrayList<App>> {

    AppList ref;

    public RetrieveApps(AppList inRef) {
        ref = inRef;
    }

    @Override
    protected ArrayList<App> doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        App currApp = null;
        ArrayList<App> appList = new ArrayList<>();
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                JSONObject root = new JSONObject(json);
                JSONObject feed = root.getJSONObject("feed");
                JSONArray apps = feed.getJSONArray("results");
                if (apps != null) {
                    for (int i = 0; i < apps.length(); i++) {
                        JSONObject appJson = apps.getJSONObject(i);
                        currApp = new App();
                        currApp.artist = appJson.getString("artistName");
                        currApp.date = appJson.getString("releaseDate");
                        currApp.imageUrl = appJson.getString("artworkUrl100");
                        currApp.name = appJson.getString("name");
                        currApp.copyright = appJson.getString("copyright");
                        JSONArray genres = appJson.getJSONArray("genres");
                        for(int j = 0; j < genres.length(); j++) {
                            currApp.genres.add(genres.getJSONObject(j).getString("name"));
                        }
                        Collections.sort(currApp.genres);
                        appList.add(currApp);
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return appList;
    }

    @Override
    protected void onPostExecute(ArrayList<App> apps) {
        ref.updateApps(apps);
    }
}
