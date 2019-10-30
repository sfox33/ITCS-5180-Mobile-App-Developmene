package com.example.inclass03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * Assignment #03
 * MainActivity.java
 * @author Sean Fox
 * @author Andrew Lambropoulos
 */

public class MainActivity extends AppCompatActivity {

    EditText nameText;
    EditText emailText;
    String dept;
    RadioGroup rg;
    Button button;
    SeekBar seek;

    public static String STUDENT_KEY = "STUDENT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Main Activity");

        rg = (RadioGroup) findViewById(R.id.radioGroup);
        button = (Button)findViewById(R.id.button);
        seek = (SeekBar)findViewById(R.id.seekBar);
        nameText = (EditText) findViewById(R.id.editText);
        emailText = (EditText) findViewById(R.id.editText2);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) findViewById(i);
                dept = rb.getText().toString();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameText.getText().toString();
                String email = emailText.getText().toString();
                if(name.length() == 0 || email.length() == 0) {
                    Toast.makeText(MainActivity.this, "Please Enter Information", Toast.LENGTH_SHORT).show();
                } else {
                    Student student = new Student(nameText.getText().toString(),
                            emailText.getText().toString(),
                            dept,
                            seek.getProgress());
                    Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                    intent.putExtra(STUDENT_KEY, student);
                    startActivity(intent);
                }

            }
        });
    }
}
