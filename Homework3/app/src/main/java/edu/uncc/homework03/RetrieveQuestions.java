package edu.uncc.homework03;

import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

/*************
 Homework 3
 RetrieveQuestions.java
 Andrew Lambropoulos
 Sean Fox
 ******************/

public class RetrieveQuestions extends AsyncTask<String, Void, ArrayList> {

    MainActivity main;
    public RetrieveQuestions(MainActivity m) {
        this.main = m;
    }

    @Override
    protected ArrayList doInBackground(String... strings) {
        URL url = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringTokenizer tokenizer = null;
        ArrayList<String> inputArray = new ArrayList<String>();
        ArrayList<Question> questions = new ArrayList<Question>();
        try{
            url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String temp = reader.readLine();
            while(temp != null) {
                inputArray.clear();
                tokenizer = new StringTokenizer(temp, ";");
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    if(token != null) {
                        inputArray.add(token);
                    } else {
                        break;
                    }
                }
                questions.add(new Question(inputArray));
                temp = reader.readLine();
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return questions;
    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        main.addQuestions(arrayList);
        main.updatePic();
    }
}
