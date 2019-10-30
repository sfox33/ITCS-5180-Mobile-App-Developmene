/*************
Homework 3
MainActivity.java
Andrew Lambropoulos
Sean Fox
******************/
package edu.uncc.homework03;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button start, exit;
    private String questionLocation = "http://dev.theappsdr.com/apis/trivia_json/trivia_text.php";
    private ArrayList<Question> questionList;
    private ImageView image;
    private ProgressDialog progress;
    private ProgressBar spinner;
    private Handler handler;
    public static String LIST = "list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.startButton);
        exit = (Button) findViewById(R.id.exitButton);
        start.setEnabled(false);
        questionList = new ArrayList<>();
        image = findViewById(R.id.imageView);
        progress = new ProgressDialog(MainActivity.this);
        progress.setCancelable(false);
        progress.setMessage("Loading Trivia");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.dismiss();
                finish();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start.isEnabled() && isConnectedOnline()) {
                    Intent intent = new Intent(MainActivity.this, TriviaActivity.class);
                    intent.putExtra(MainActivity.LIST, questionList);

                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(MainActivity.this, "Not connected online", Toast.LENGTH_LONG).show();
                }
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if(message.what == 0) {
                    progress.setProgress(0);
                    progress.show();
                } else if(message.what == 1) {
                    progress.dismiss();
                }
                return false;
            }
        });

        /*Message message = new Message();
        message.what = 0;
        handler.sendMessage(message);*/

        new RetrieveQuestions(MainActivity.this).execute(questionLocation);
    }

    public void addQuestions(ArrayList<Question> questions) {
        this.questionList = questions;
    }

    public void updatePic(){
        /*Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);*/
        image.setImageDrawable(getResources().getDrawable(R.mipmap.logo_foreground));
        start.setEnabled(true);
        spinner.setVisibility(View.GONE);
    }

    private boolean isConnectedOnline() {
        ConnectivityManager cm  =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
