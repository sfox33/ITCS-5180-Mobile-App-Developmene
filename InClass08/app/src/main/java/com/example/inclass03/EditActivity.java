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
import android.widget.TextView;

/**
 * Assignment #03
 * EditActivity.java
 * @author Sean Fox
 * @author Andrew Lambropoulos
 */

public class EditActivity extends AppCompatActivity {

    EditText nameText;
    EditText emailText;
    String dept;
    RadioGroup rg;
    Button button;
    SeekBar seek;
    TextView department, seekText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        setTitle("Edit Activity");

        rg = (RadioGroup) findViewById(R.id.Group2);
        button = (Button)findViewById(R.id.button2);
        seek = (SeekBar)findViewById(R.id.seekBar2);
        nameText = (EditText) findViewById(R.id.EditName);
        emailText = (EditText) findViewById(R.id.EditEmail);
        department = (TextView) findViewById(R.id.EditDept);
        seekText = (TextView) findViewById(R.id.EditMoodText);

        if(getIntent() != null && getIntent().getExtras() != null){

            Student student = (Student)getIntent().getExtras().get(DisplayActivity.STUDENT_KEY);

            if(getIntent().getStringExtra(DisplayActivity.TYPE).equals("name")) {
                emailText.setVisibility(View.GONE);
                seek.setVisibility(View.GONE);
                rg.setVisibility(View.GONE);
                department.setVisibility(View.GONE);
                seekText.setVisibility(View.GONE);

                nameText.setText(student.getName());
            } else if(getIntent().getStringExtra(DisplayActivity.TYPE).equals("email")) {
                nameText.setVisibility(View.GONE);
                seek.setVisibility(View.GONE);
                rg.setVisibility(View.GONE);
                department.setVisibility(View.GONE);
                seekText.setVisibility(View.GONE);

                emailText.setText(student.getEmail());
            } else if(getIntent().getStringExtra(DisplayActivity.TYPE).equals("dept")) {
                nameText.setVisibility(View.GONE);
                emailText.setVisibility(View.GONE);
                seek.setVisibility(View.GONE);
                seekText.setVisibility(View.GONE);

                String dept = student.getDept();
                if(dept.equals("SIS")) {
                    //RadioButton rb = (RadioButton)findViewById(R.id.radioButton5);
                    ((RadioButton)findViewById(R.id.radioButton5)).setChecked(true);
                } else if(dept.equals("CS")) {
                    ((RadioButton)findViewById(R.id.radioButton6)).setChecked(true);
                } else if(dept.equals("BIO")){
                    ((RadioButton)findViewById(R.id.radioButton7)).setChecked(true);
                } else if(dept.equals("Others")) {
                    ((RadioButton)findViewById(R.id.radioButton8)).setChecked(true);
                }
            } else if(getIntent().getStringExtra(DisplayActivity.TYPE).equals("mood")) {
                nameText.setVisibility(View.GONE);
                emailText.setVisibility(View.GONE);
                rg.setVisibility(View.GONE);
                department.setVisibility(View.GONE);

                int seekNum = student.getMood();
                seek.setProgress(seekNum);
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getIntent().getStringExtra(DisplayActivity.TYPE).equals("name")) {
                        String val = nameText.getText().toString();
                        if(val.equals("")) {
                            setResult(RESULT_CANCELED);
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra(DisplayActivity.VALUE_KEY, val);
                            setResult(RESULT_OK, intent);
                        }
                    } else if(getIntent().getStringExtra(DisplayActivity.TYPE).equals("email")) {
                        String val = emailText.getText().toString();
                        if(val.equals("")) {
                            setResult(RESULT_CANCELED);
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra(DisplayActivity.VALUE_KEY, val);
                            setResult(RESULT_OK, intent);
                        }
                    } else if(getIntent().getStringExtra(DisplayActivity.TYPE).equals("dept")) {
                        String val = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
                        if(val.equals("")) {
                            setResult(RESULT_CANCELED);
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra(DisplayActivity.VALUE_KEY, val);
                            setResult(RESULT_OK, intent);
                        }
                    } else if(getIntent().getStringExtra(DisplayActivity.TYPE).equals("mood")) {
                        int val = seek.getProgress();
                        if(val < 0 || val > seek.getMax()) {
                            setResult(RESULT_CANCELED);
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra(DisplayActivity.VALUE_KEY, val);
                            setResult(RESULT_OK, intent);
                        }
                    }
                    finish();
                }
            });
        }
    }
}
