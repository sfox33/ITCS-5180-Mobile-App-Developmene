package edu.uncc.inclass07;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Andrew Lambropoulos on 2/19/2018.
 * Created by Sean Fox
 * RetrieveArticles.java
 * Assignment: InClass07
 */

class RetrieveArticles extends AsyncTask<String, Void, NewsObject> {

    NewsActivity act;

    public RetrieveArticles(NewsActivity context) {
        act = context;
    }

    @Override
    protected NewsObject doInBackground(String... params) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        NewsObject newsObject = new NewsObject();
        String result = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                JSONObject root = new JSONObject(json);
                String status = root.getString("status");
                int totalResults = root.getInt("totalResults");
                JSONArray articles = root.getJSONArray("articles");
                newsObject.status = status;
                newsObject.totalResults = totalResults;
                if (articles != null) {
                    for (int i = 0; i < articles.length(); i++) {
                        JSONObject articleJson = articles.getJSONObject(i);
                        Article article = new Article();
                        article.id = articleJson.getJSONObject("source").getString("id");
                        article.name = articleJson.getJSONObject("source").getString("name");
                        article.author = articleJson.getString("author");
                        article.title = articleJson.getString("title");
                        article.description = articleJson.getString("description");
                        article.url = articleJson.getString("url");
                        article.urlToImage = articleJson.getString("urlToImage");
                        article.publishedAt = articleJson.getString("publishedAt");
                        newsObject.articles.add(article);
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
        return newsObject;
    }

    @Override
    protected void onPostExecute(NewsObject newsObject) {
        act.doWork(newsObject);
    }
}