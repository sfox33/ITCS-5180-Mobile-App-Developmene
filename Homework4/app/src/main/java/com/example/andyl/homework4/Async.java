/*
MainActivity.java
Andrew Lambropoulos
Sean Fox
Homework4
 */

package com.example.andyl.homework4;

import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



public class Async extends AsyncTask<String, Void, ArrayList<NewsObject>> {

    MainActivity main;
    boolean isBusiness;
    public Async(MainActivity context, boolean isBusiness) {
        main = context;
        this.isBusiness = isBusiness;
    }

    @Override
    protected ArrayList<NewsObject> doInBackground(String... params) {
        HttpURLConnection connection = null;
        ArrayList<NewsObject> result = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = NewsParser.NewsSAXParser.parseNewsObject(connection.getInputStream(), isBusiness);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<NewsObject> result) {
        main.updateIndex();
        main.doWork(result);
        if (result != null) {
            Log.d("demo", result.toString());
            for (int i =0; i < result.size(); i++) {
                Log.d("demo","title " + result.get(i).title);
                Log.d("demo","description " + result.get(i).description);
                Log.d("demo", "Link " + result.get(i).url);
                Log.d("demo", "Image link " + result.get(i).urlToImage);
                Log.d("demo", "pub date " + result.get(i).publishedAt);
            }

        } else {
            Log.d("demo", "null result");
        }
    }


}
