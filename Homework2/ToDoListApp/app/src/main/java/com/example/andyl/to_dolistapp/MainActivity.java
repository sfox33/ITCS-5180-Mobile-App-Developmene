package com.example.andyl.to_dolistapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    TextView taskTitle;
    TextView taskDate;
    TextView taskTime;
    TextView taskPriority;
    TextView taskNumber;

    ImageButton btn1, btn2, btn3, btn4, btn5, btn6, btn7;

    public final static int reqCode = 100;
    public final static int reqCode2 = 200;
    public final static String TASK_KEY = "TASK";
    public final static String EDIT_TASK = "EDIT";
    public final static String CURRENT_TASK = "CURRENT";
    public final static String SIZE = "SIZE";

    private int current = 0;

    LinkedList<Task> linkedList = new LinkedList<Task>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("View Tasks");

        taskTitle = (TextView) findViewById(R.id.textView);
        taskDate = (TextView) findViewById(R.id.textView2);
        taskTime = (TextView) findViewById(R.id.textView3);
        taskPriority = (TextView) findViewById(R.id.textView4);
        taskNumber = (TextView) findViewById(R.id.textView5);

        btn1 = (ImageButton) findViewById(R.id.imageButton);
        btn2 = (ImageButton) findViewById(R.id.imageButton2);
        btn3 = (ImageButton) findViewById(R.id.imageButton3);
        btn4 = (ImageButton) findViewById(R.id.imageButton4);
        btn5 = (ImageButton) findViewById(R.id.imageButton5);
        btn6 = (ImageButton) findViewById(R.id.imageButton6);
        btn7 = (ImageButton) findViewById(R.id.imageButton7);

        taskNumber.setText("Task " + current + " of " + linkedList.size());

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == 1) {
                    Toast.makeText(MainActivity.this, "Already at first task", Toast.LENGTH_LONG).show();
                }
                else {
                    current = 1;
                    taskTitle.setText(linkedList.getFirst().getName());
                    taskDate.setText(linkedList.getFirst().getDate());
                    taskTime.setText(linkedList.getFirst().getTaskTime());
                    taskPriority.setText(linkedList.getFirst().getPriority());
                    taskNumber.setText("Task " + current + " of " + linkedList.size());
                }

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == 1) {
                    Toast.makeText(MainActivity.this, "Already at first task", Toast.LENGTH_LONG).show();
                }
                else {
                    current = current - 1;
                    taskTitle.setText(linkedList.get(current - 1).getName());
                    taskDate.setText(linkedList.get(current - 1).getDate());
                    taskTime.setText(linkedList.get(current - 1).getTaskTime());
                    taskPriority.setText(linkedList.get(current - 1).getPriority());
                    taskNumber.setText("Task " + current + " of " + linkedList.size());
                }

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (current > 0) {
                    Intent intent = new Intent(MainActivity.this, EditTask.class);
                    intent.putExtra(EDIT_TASK,linkedList.get(current -1));
                    intent.putExtra(CURRENT_TASK, current);
                    intent.putExtra(SIZE, linkedList.size());
                    linkedList.remove(current - 1);
                    startActivityForResult(intent, reqCode2);
                }
                else {
                    Toast.makeText(MainActivity.this, "You need to create an activity first!", Toast.LENGTH_LONG).show();
                }

            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkedList.remove(current - 1);
                current = 1;
                taskTitle.setText(linkedList.getFirst().getName());
                taskDate.setText(linkedList.getFirst().getDate());
                taskTime.setText(linkedList.getFirst().getTaskTime());
                taskPriority.setText(linkedList.getFirst().getPriority());
                taskNumber.setText("Task " + current + " of " + linkedList.size());


            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == linkedList.size()) {
                    Toast.makeText(MainActivity.this, "Already at last task", Toast.LENGTH_LONG).show();
                }
                else {
                    //taskNumber.setText("Task " + current + " of " + size);
                    current = current + 1;
                    taskTitle.setText(linkedList.get(current - 1).getName());
                    taskDate.setText(linkedList.get(current - 1).getDate());
                    taskTime.setText(linkedList.get(current - 1).getTaskTime());
                    taskPriority.setText(linkedList.get(current - 1).getPriority());
                    taskNumber.setText("Task " + current + " of " + linkedList.size());
                }

            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == linkedList.size()) {
                    Toast.makeText(MainActivity.this, "Already at last task", Toast.LENGTH_LONG).show();
                }
                else {
                    current = linkedList.size();
                    taskTitle.setText(linkedList.getLast().getName());
                    taskDate.setText(linkedList.getLast().getDate());
                    taskTime.setText(linkedList.getLast().getTaskTime());
                    taskPriority.setText(linkedList.getLast().getPriority());
                    taskNumber.setText("Task " + current  + " of " + linkedList.size());
                }
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateTask.class);
                startActivityForResult(intent, reqCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        if (requestCode == reqCode) {
            if (resultCode == RESULT_OK) {
                Task task  =  (Task) data.getExtras().getSerializable(TASK_KEY);
                linkedList.add(task);
                if (linkedList.size() > 1) {
                    Collections.sort(linkedList, new Comparator<Task>() {
                        @Override
                        public int compare(Task t1, Task t2) {
                            return t1.getDateTime().compareTo(t2.getDateTime());
                        }
                    });
                }
                taskTitle.setText(task.getName());
                taskDate.setText(task.getDate());
                taskTime.setText(task.getTaskTime());
                taskPriority.setText(task.getPriority());
                current = linkedList.indexOf(task) + 1;
                //size = linkedList.size();
                taskNumber.setText("Task " + current + " of " + linkedList.size());
                //String name = task.getName();
                //Toast.makeText(MainActivity.this, "name: " + name , Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == reqCode2) {
            if (resultCode == RESULT_OK) {
                Task task  =  (Task) data.getExtras().getSerializable(EDIT_TASK);
                linkedList.add(task);
                if (linkedList.size() > 1) {
                    Collections.sort(linkedList, new Comparator<Task>() {
                        @Override
                        public int compare(Task t1, Task t2) {
                            return t1.getDateTime().compareTo(t2.getDateTime());
                        }
                    });
                }
                taskTitle.setText(task.getName());
                taskDate.setText(task.getDate());
                taskTime.setText(task.getTaskTime());
                taskPriority.setText(task.getPriority());
                current = linkedList.indexOf(task) + 1;
                //size = linkedList.size();
                taskNumber.setText("Task " + current + " of " + linkedList.size());
            }
        }
    }
}
