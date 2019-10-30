package com.example.andyl.to_dolistapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTask extends AppCompatActivity {

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
        setContentView(R.layout.activity_edit_task);

        setTitle("Edit Task");

        editTask = (EditText) findViewById(R.id.editText);
        editDate = (EditText) findViewById(R.id.editText2);
        editTime = (EditText) findViewById(R.id.editText3);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        button = (Button)findViewById(R.id.button);

        editDate.setKeyListener(null);
        editTime.setKeyListener(null);

        if(getIntent() != null && getIntent().getExtras() != null){
            Task task = (Task) getIntent().getExtras().getSerializable(MainActivity.EDIT_TASK);
            String title = task.getName();
            priority = task.getPriority();
            String taskDate = task.getDate();
            String taskTime = task.getTaskTime();
            int current = getIntent().getExtras().getInt(MainActivity.CURRENT_TASK);
            int size = getIntent().getExtras().getInt(MainActivity.SIZE);

            editTask.setText(title);
            editDate.setText(taskDate);
            editTime.setText(taskTime);

            if(priority.equals("High Priority")) {
                ((RadioButton)findViewById(R.id.radioButton)).setChecked(true);
            } else if(priority.equals("Medium Priority")) {
                ((RadioButton)findViewById(R.id.radioButton2)).setChecked(true);
            } else if(priority.equals("Low Priority")){
                ((RadioButton)findViewById(R.id.radioButton3)).setChecked(true);
            }

            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    RadioButton rb = (RadioButton) findViewById(i);
                    priority = rb.getText().toString();
                }
            });

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
                    new DatePickerDialog(EditTask.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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
                    new TimePickerDialog(EditTask.this, time, hour, minute, false).show();
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String taskTitle = editTask.getText().toString();
                    String taskDate = editDate.getText().toString();
                    String taskTime = editTime.getText().toString();
                    if(taskTitle.length() == 0 || taskDate.length() == 0 || taskTime.length() == 0 || priority.length() == 0) {
                        Toast.makeText(EditTask.this, "Please Enter Information", Toast.LENGTH_SHORT).show();
                    } else {
                        Task task = new Task(editTask.getText().toString(),
                                editDate.getText().toString(),
                                editTime.getText().toString(),
                                priority);
                        Intent intent = new Intent();
                        intent.putExtra(MainActivity.EDIT_TASK, task);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                }
            });

        }
    }

    private void showDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        editDate.setText(dateFormat.format(calendar.getTime()));
    }


}
