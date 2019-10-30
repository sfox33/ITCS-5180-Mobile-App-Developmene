package edu.uncc.homework03;

import android.os.AsyncTask;
import android.util.Log;

/*************
 Homework 3
 UpdateScore.java
 Andrew Lambropoulos
 Sean Fox
 ******************/

public class UpdateScore extends AsyncTask<Void, Void, Boolean> {

    TriviaActivity trivia;
    int chosen, correct;

    public UpdateScore(TriviaActivity trivia, int chosen, int correct) {
        this.trivia = trivia;
        this.chosen = chosen;
        this.correct = correct;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Log.d("demo", "Survey says we chose " + chosen + " where we needed " + correct);
        return chosen == correct;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        trivia.updateScore(aBoolean);
    }
}
