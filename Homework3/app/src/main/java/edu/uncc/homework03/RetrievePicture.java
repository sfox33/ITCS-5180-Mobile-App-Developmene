package edu.uncc.homework03;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*************
 Homework
 RetrievePicture.java
 Andrew Lambropoulos
 Sean Fox
 ******************/

public class RetrievePicture extends AsyncTask<String, Void, Bitmap> {

    TriviaActivity trivia;
    Bitmap bitmap = null;

    public RetrievePicture(TriviaActivity tr) {
        this.trivia = tr;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        HttpURLConnection connection = null;
        bitmap = null;
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.d("demo", bitmap.toString());
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap map) {
        //Log.d("demo", map.toString());
        trivia.updatePic(map);
    }
}
