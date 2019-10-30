package com.example.andyl.to_dolistapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.example.andyl.to_dolistapp.MainActivity.TASK_KEY;

public class CreateTask extends AppCompatActivity {

    private EditText editTask;
    private EditText editDate;
    private EditText editTime;
    private RadioGroup rg;
    private Button button;
    private String priority = "";
    private Calendar calendar;
    private Calendar currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        setTitle("Create Task");

        editTask = (EditText) findViewById(R.id.editText);
        editDate = (EditText) findViewById(R.id.editText2);
        editTime = (EditText) findViewById(R.id.editText3);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        button = (Button)findViewById(R.id.button);

        editDate.setKeyListener(null);
        editTime.setKeyListener(null);

        calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                showDate();
            }
        };

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new DatePickerDialog(CreateTask.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String min = String.valueOf(minute);
                if (minute < 10) {
                    min = "0" + minute;
                }

                if (hourOfDay == 12){
                    editTime.setText(hourOfDay + ":" + min + " PM");
                }
                else if (hourOfDay > 12){
                    hourOfDay = hourOfDay - 12;
                    editTime.setText(hourOfDay + ":" + min + " PM");
                }
                else {
                    if (hourOfDay == 0) {
                        hourOfDay = 12;
                    }
                    editTime.setText(hourOfDay + ":" + min + " AM");
                }
            }
        };

        currentTime = Calendar.getInstance();

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                new TimePickerDialog(CreateTask.this, time, hour, minute, false).show();
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) findViewById(i);
                priority = rb.getText().toString();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskTitle = editTask.getText().toString();
                String taskDate = editDate.getText().toString();
                String taskTime = editTime.getText().toString();
                if(taskTitle.length() == 0 || taskDate.length() == 0 || taskTime.length() == 0 || priority.length() == 0) {
                    Toast.makeText(CreateTask.this, "Please Enter Information", Toast.LENGTH_SHORT).show();
                } else {
                    Task task = new Task(editTask.getText().toString(),
                            editDate.getText().toString(),
                            editTime.getText().toString(),
                            priority);
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.TASK_KEY, task);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }
    private void showDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        editDate.setText(dateFormat.format(calendar.getTime()));
    }
}
