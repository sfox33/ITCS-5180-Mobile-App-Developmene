/*************
 Homework 3
 TriviaActivity.java
 Andrew Lambropoulos
 Sean Fox
 ******************/
package edu.uncc.homework03;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TriviaActivity extends AppCompatActivity {

    private TextView questionNum, question, timer, picStatus;
    private RadioGroup rgLeft, rgRight;
    private ImageView image;
    private Button quit, next;
    private ArrayList<Question> questions;
    private int questionIndex, numCorrect = 0, answer;
    private ProgressBar spinner;
    private RadioButton buttons[];
    public static String RESULT = "result";
    public static String SIZE = "size";


    private RadioGroup.OnCheckedChangeListener listener1;
    private RadioGroup.OnCheckedChangeListener listener2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        if(getIntent() != null && getIntent().getExtras() != null){
            questions = (ArrayList<Question>) getIntent().getExtras().getSerializable(MainActivity.LIST);
        }

        rgLeft = (RadioGroup) findViewById(R.id.radioGroup);
        rgLeft.setOrientation(LinearLayout.VERTICAL);
        rgRight = (RadioGroup) findViewById(R.id.radioGroup2);
        rgRight.setOrientation(LinearLayout.VERTICAL);
        image = (ImageView) findViewById(R.id.imageView2);
        quit = (Button) findViewById(R.id.quitButton);
        next = (Button) findViewById(R.id.nextButton);
        questionNum = (TextView) findViewById(R.id.textQuestionNum);
        question = (TextView) findViewById(R.id.textQuestion);
        timer = (TextView) findViewById(R.id.timer);
        questionIndex = 0;
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        picStatus = (TextView) findViewById(R.id.textView5);
        numCorrect = 0;

        new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                String time = String.format(Locale.getDefault(), "Time Remaining %2d min: %02d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                timer.setText(time);
//              timer.setText("Time Left: " + millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                timer.setText("No time left");
                Intent intent = new Intent(TriviaActivity.this, StatsActivity.class);
                intent.putExtra(TriviaActivity.RESULT, numCorrect);
                intent.putExtra(TriviaActivity.SIZE, questions.size());
                intent.putExtra(MainActivity.LIST, questions);
                startActivity(intent);
                finish();
                return;
            }
        }.start();

        listener1 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    rgRight.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                    rgRight.clearCheck(); // clear the second RadioGroup!
                    rgRight.setOnCheckedChangeListener(listener2); //reset the listener
                }
            }
        };

        listener2 = new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    rgLeft.setOnCheckedChangeListener(null);
                    rgLeft.clearCheck();
                    rgLeft.setOnCheckedChangeListener(listener1);
                }
            }
        };

        if(questions.size() == 0) {
            Toast.makeText(TriviaActivity.this, "Error: No questions found.", Toast.LENGTH_LONG).show();
        } else {
            updateQuestion();
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("demo", "Next was clicked");
                int choice;
                if(rgLeft.getCheckedRadioButtonId() == -1 && rgRight.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(TriviaActivity.this, "Please select an answer.", Toast.LENGTH_SHORT).show();
                } else if(rgLeft.getCheckedRadioButtonId() != -1) {
                    Log.d("demo", "Left group");
                    int radioButtonID = rgLeft.getCheckedRadioButtonId();
                    View radioButton = rgLeft.findViewById(radioButtonID);
                    choice = rgLeft.indexOfChild(radioButton);
                    new UpdateScore(TriviaActivity.this, choice, answer).execute();
                    updateQuestion();
                } else {
                    Log.d("demo", "Right group");
                    int radioButtonID = rgRight.getCheckedRadioButtonId();
                    View radioButton = rgRight.findViewById(radioButtonID);
                    choice = rgRight.indexOfChild(radioButton);
                    new UpdateScore(TriviaActivity.this, (choice + 5), answer).execute();
                    updateQuestion();
                }
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TriviaActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        rgLeft.setOnCheckedChangeListener(listener1);

        rgRight.setOnCheckedChangeListener(listener2);


    }

    private void updateQuestion() {
        if(questionIndex >= questions.size()) {
            Intent intent = new Intent(TriviaActivity.this, StatsActivity.class);
            intent.putExtra(TriviaActivity.RESULT, numCorrect);
            intent.putExtra(TriviaActivity.SIZE, questions.size());
            intent.putExtra(MainActivity.LIST, questions);
            startActivity(intent);
            finish();
            return;
        }
        Question curr = questions.get(questionIndex);
        String pictureLoc = curr.getPicture();
        answer = curr.getAnswerIndex();
        rgLeft.removeAllViews();
        rgRight.removeAllViews();
        rgLeft.clearCheck();
        rgRight.clearCheck();
        image.setImageDrawable(null);
        spinner.setVisibility(View.VISIBLE);
        picStatus.setVisibility(View.VISIBLE);
        picStatus.setText(R.string.loading);
        questionNum.setText("Q" + (questionIndex+1));
        //timer.setText("Time Left: X Seconds");
        if(!pictureLoc.equals("")) {
            new RetrievePicture(TriviaActivity.this).execute(pictureLoc);
        } else {
            spinner.setVisibility(View.GONE);
            picStatus.setText(R.string.notFound);
        }
        question.setText(curr.getQuestion());

        buttons = new RadioButton[curr.getAnwers().length];
        for(int i = 0; i < buttons.length; i++) {
            buttons[i] = new RadioButton(TriviaActivity.this);
            buttons[i].setText(curr.getAnwers()[i]);
            if(i <= 4) {
                rgLeft.addView(buttons[i]);
            } else {
                rgRight.addView(buttons[i]);
            }
        }
        questionIndex++;
    }

    public void updatePic(Bitmap bitmap){
        image.setImageBitmap(bitmap);
        spinner.setVisibility(View.GONE);
        picStatus.setVisibility(View.GONE);
    }

    public void updateScore(Boolean bool) {
        if(bool) {
            numCorrect++;
        }
        Log.d("demo", "Answer selected. Result was " + bool + " with a score of " + numCorrect);
    }
}
