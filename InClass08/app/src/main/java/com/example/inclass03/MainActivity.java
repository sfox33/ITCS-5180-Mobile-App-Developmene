package com.example.inclass03;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * Assignment #08
 * MainActivity.java
 * @author Sean Fox
 * @author Andrew Lambropoulos
 */

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, DisplayFragment.OnFragmentInteractionListener, EditFragment.OnFragmentInteractionListener{

    EditText nameText;
    EditText emailText;
    String dept;
    RadioGroup rg;
    Button button;
    SeekBar seek;

    Student student;


    //public static String STUDENT_KEY = "STUDENT";
    public static String STUDENT_KEY = "STUDENT2";
    public static String CHOICE = "choice";

    int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        index = 0;
        getSupportFragmentManager().beginTransaction().add(R.id.ContentView, new MainFragment(), "firstFragment").commit();

    }

    @Override
    public void onFragmentInteraction(int index) {
        Bundle bundle = new Bundle();
        //bundle.putSerializable("STUDENT2", student);
        //bundle.putInt("CHOICE", index);

        Log.d("Demo", "index: "+ index);

        if (index == 0) {
            DisplayFragment df = new DisplayFragment();
            bundle.putSerializable(DisplayFragment.STUDENT_KEY, student);
            df.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.ContentView, df, "secondFrgament").commit();
            //DisplayFragment df = (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.ContentView);
        }
        else if (index == 1) {
            EditFragment ef = new EditFragment();
            bundle.putSerializable(EditFragment.STUDENT_KEY, student);
            bundle.putInt(EditFragment.CHOICE, index);
            ef.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.ContentView, ef, "thirdFrgament").commit();
            //EditFragment ef = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.ContentView);
        }
        else if (index == 2) {
            EditFragment ef = new EditFragment();
            bundle.putSerializable(EditFragment.STUDENT_KEY, student);
            bundle.putInt(EditFragment.CHOICE, index);
            ef.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.ContentView, ef, "fourthFrgament").commit();
            //EditFragment ef = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.ContentView);
        }
        else if (index == 3) {
            EditFragment ef = new EditFragment();
            bundle.putSerializable(EditFragment.STUDENT_KEY, student);
            bundle.putInt(EditFragment.CHOICE, index);
            ef.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.ContentView, ef, "fifthFrgament").commit();
            //EditFragment ef = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.ContentView);
        }
        else {
            EditFragment ef = new EditFragment();
            bundle.putSerializable(EditFragment.STUDENT_KEY, student);
            bundle.putInt(EditFragment.CHOICE, index);
            ef.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.ContentView, ef, "sixFrgament").commit();
            //EditFragment ef = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.ContentView);
        }

    }

    @Override
    public void passToMain(Student student) {
        this.student = student;
    }


}
