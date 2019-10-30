package com.example.inclass03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Assignment #03
 * DisplayActivity.java
 * @author Sean Fox
 * @author Andrew Lambropoulos
 */

public class DisplayActivity extends AppCompatActivity {

    TextView nameText;
    TextView emailText;
    TextView deptText;
    TextView moodText;
    ImageButton button1, button2, button3, button4;
    static final int CODE1 = 100;
    static final int CODE2 = 101;
    static final int CODE3 = 102;
    static final int CODE4 = 103;
    public static String STUDENT_KEY = "STUDENT";
    public static String TYPE = "TYPE";
    public static String VALUE_KEY = "value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        setTitle("Display Activity");

        nameText = (TextView) findViewById(R.id.textView5);
        emailText = (TextView) findViewById(R.id.textView6);
        deptText = (TextView) findViewById(R.id.textView7);
        moodText = (TextView) findViewById(R.id.textView8);
        button1 = (ImageButton) findViewById(R.id.imageButton);
        button2 = (ImageButton) findViewById(R.id.imageButton2);
        button3 = (ImageButton) findViewById(R.id.imageButton3);
        button4 = (ImageButton) findViewById(R.id.imageButton4);

        if(getIntent() != null && getIntent().getExtras() != null){
            Student student = (Student) getIntent().getExtras().getSerializable(MainActivity.STUDENT_KEY);
            String name = student.getName();
            String email = student.getEmail();
            String dept = student.getDept();
            int mood = student.getMood();

            nameText.setText("Name:\t" + name);
            emailText.setText("Email:\t" + email);
            deptText.setText("Department:\t" + dept);
            moodText.setText("Mood:\t" + mood + "% Positive");

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Student student = (Student) getIntent().getExtras().getSerializable(MainActivity.STUDENT_KEY);
                    student.setName(nameText.getText().toString().substring(6));
                    Intent intent = new Intent("com.example.inclass03.intentdemo.intent.action.VIEW");
                    intent.putExtra(STUDENT_KEY, student);
                    intent.putExtra(TYPE, "name");
                    startActivityForResult(intent, CODE1);
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Student student = (Student) getIntent().getExtras().getSerializable(MainActivity.STUDENT_KEY);
                    student.setEmail(emailText.getText().toString().substring(7));
                    Intent intent = new Intent("com.example.inclass03.intentdemo.intent.action.VIEW");
                    intent.putExtra(STUDENT_KEY, student);
                    intent.putExtra(TYPE, "email");
                    startActivityForResult(intent, CODE2);
                }
            });

            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Student student = (Student) getIntent().getExtras().getSerializable(MainActivity.STUDENT_KEY);
                    student.setDept(deptText.getText().toString().substring(12));
                    Intent intent = new Intent("com.example.inclass03.intentdemo.intent.action.VIEW");
                    intent.putExtra(STUDENT_KEY, student);
                    intent.putExtra(TYPE, "dept");
                    startActivityForResult(intent, CODE3);
                }
            });

            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Student student = (Student) getIntent().getExtras().getSerializable(MainActivity.STUDENT_KEY);
                    String temp = moodText.getText().toString().substring(6);
                    int tempInt = temp.indexOf('%');
                    temp = temp.substring(0,tempInt);
                    student.setMood(Integer.parseInt(temp));
                    Intent intent = new Intent("com.example.inclass03.intentdemo.intent.action.VIEW");
                    intent.putExtra(STUDENT_KEY, student);
                    intent.putExtra(TYPE, "mood");
                    startActivityForResult(intent, CODE4);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        nameText = (TextView) findViewById(R.id.textView5);
        emailText = (TextView) findViewById(R.id.textView6);
        deptText = (TextView) findViewById(R.id.textView7);
        moodText = (TextView) findViewById(R.id.textView8);

        if(requestCode == CODE1) {
            if(resultCode == RESULT_OK) {
                nameText.setText("Name:\t" + data.getExtras().getString(VALUE_KEY));
            }
        } else if(requestCode == CODE2) {
            if(resultCode == RESULT_OK) {
                emailText.setText("Email:\t" + data.getExtras().getString(VALUE_KEY));
            }
        } else if(requestCode == CODE3) {
            if(resultCode == RESULT_OK) {
                deptText.setText("Department:\t" + data.getExtras().getString(VALUE_KEY));
            }
        } else if(requestCode == CODE4) {
            if(resultCode == RESULT_OK) {
                moodText.setText("Mood:\t" + data.getExtras().getInt(VALUE_KEY) + "% Positive");
            }
        }
    }
}
