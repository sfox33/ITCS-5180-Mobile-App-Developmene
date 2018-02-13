package com.example.tipcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Homework 1
 * MainActivity.java
 * @author Sean Fox
 * @author Andrew Lambropoulos
 */


public class MainActivity extends AppCompatActivity {

    SeekBar seekPer;
    TextView percent;
    EditText billTotal;
    TextView outBill, outTip;
    RadioGroup radioGaGa;
    Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Tip Calculator");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        seekPer = (SeekBar) findViewById(R.id.seekBar);
        percent = (TextView) findViewById(R.id.textView8);
        billTotal = (EditText) findViewById(R.id.editText);
        radioGaGa = (RadioGroup) findViewById(R.id.radioGroup);
        outTip = (TextView) findViewById(R.id.textView6);
        outBill = (TextView) findViewById(R.id.textView7);
        exit = (Button) findViewById(R.id.button);

        seekPer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                percent.setText(seekPer.getProgress() + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                radioGaGa.check(R.id.radioButton3);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(billTotal.getText().length() != 0) {
                    updateTip();
                } else {
                    billTotal.setError("Enter bill total");
                }
            }
        });

        billTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if(billTotal.getText().toString().length() == 0) {
                    billTotal.setError("Enter bill total");
                    outBill.setText("0.00");
                    outTip.setText("0.00");
                } else {
                    updateTip();
                }
            }
        });

        radioGaGa.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                updateTip();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void updateTip() {
        seekPer = (SeekBar) findViewById(R.id.seekBar);
        billTotal = (EditText) findViewById(R.id.editText);
        radioGaGa = (RadioGroup) findViewById(R.id.radioGroup);
        outTip = (TextView) findViewById(R.id.textView6);
        outBill = (TextView) findViewById(R.id.textView7);

        float tip;
        float bill;

        if(billTotal.getText().toString().length() == 0 || billTotal.getText().toString().equals(".")) {
            billTotal.setError("Enter bill total");
        } else {

            bill = Float.parseFloat(billTotal.getText().toString());

            if (((RadioButton)findViewById(radioGaGa.getCheckedRadioButtonId())).getText().toString().equals("Custom")) {
                tip = seekPer.getProgress() / 100.0f;
            } else {
                String temp = ((RadioButton)findViewById(radioGaGa.getCheckedRadioButtonId())).getText().toString();
                Log.d("demo", "Temp now reads: " + temp);
                tip = Float.parseFloat(temp.substring(0, temp.length() - 1))/100.0f;
                Log.d("demo", "Tip now reads: " + tip);
            }

            outTip.setText(Float.toString(Math.round(bill * tip * 100.0f) / 100.0f));
            outBill.setText(Float.toString(Math.round((bill + Math.round(bill * tip * 100.0f) / 100.0f) * 100.0f) / 100.0f));
        }
    }

}