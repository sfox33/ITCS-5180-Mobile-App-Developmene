/*************
 Homework 3
 StatsActivity.java
 Andrew Lambropoulos
 Sean Fox
 ******************/
package edu.uncc.homework03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    private TextView percent, remark;
    private int correct, total;
    private ProgressBar percentBar;
    private Button exit, again;
    private ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        if(getIntent() != null && getIntent().getExtras() != null) {
            correct = (Integer) getIntent().getExtras().get(TriviaActivity.RESULT);
            total = (Integer) getIntent().getExtras().get(TriviaActivity.SIZE);
            questions = (ArrayList<Question>) getIntent().getExtras().getSerializable(MainActivity.LIST);
        }

        percent = (TextView) findViewById(R.id.percent);
        remark = (TextView) findViewById(R.id.resultInfo);
        percentBar = (ProgressBar) findViewById(R.id.percentBar);
        exit = (Button) findViewById(R.id.button);
        again = (Button) findViewById(R.id.button2);

        int percentage = Math.round((correct*1.0f) / (total*1.0f) * 100.0f);
        percent.setText(percentage + "%");
        percentBar.setProgress(percentage);
        if(percentage != 100) {
            remark.setText("Try again and see if you can get\nall of the correct answers!");
        } else {
            remark.setText("Congratulations!");
        }

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatsActivity.this, TriviaActivity.class);
                intent.putExtra(MainActivity.LIST, questions);

                startActivity(intent);
                finish();
            }
        });
    }
}
