package com.example.inclass04;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * In Class 04
 * MainActivity.java
 * @author Sean Fox
 * @author Andrew Lambropoulos
 */

public class MainActivity extends AppCompatActivity {

    SeekBar seekCount, seekLength;
    TextView count, length, password;
    ExecutorService threadPool;
    Button threadBtn, asyncBtn;
    Handler handler;
    ArrayList<String> passList = new ArrayList<>();
    ProgressDialog dialog;
    AlertDialog.Builder builder;
    static final int START = 0x00;
    static final int UPDATE = 0x01;
    static final int STOP = 0x02;
    static final String PROGRESS = "progress";
    static final String LIST = "list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekCount = (SeekBar)findViewById(R.id.seekBar);
        seekLength = (SeekBar)findViewById(R.id.seekBar2);
        count = (TextView)findViewById(R.id.textView3);
        length = (TextView)findViewById(R.id.textView4);
        threadBtn = (Button)findViewById(R.id.button);
        asyncBtn = (Button)findViewById(R.id.button2);
        password = (TextView)findViewById(R.id.textView7);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Getting Passwords");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch(message.what) {
                    case(START):
                        dialog.setMax(Integer.parseInt(count.getText().toString()));
                        dialog.setProgress(0);
                        dialog.show();
                        break;
                    case(UPDATE):
                        dialog.setProgress(message.getData().getInt(PROGRESS));
                        break;
                    case(STOP):
                        Log.d("demo", "Should be closing the dialog box.");
                        dialog.dismiss();
                        passList = message.getData().getStringArrayList(LIST);
                        builder = new AlertDialog.Builder(MainActivity.this);
                        String[] strArray = passList.toArray(new String[passList.size()]);
                        builder.setTitle(R.string.pass).setItems(strArray, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                password.setText(passList.get(which));
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                }
                return false;
            }
        });

        threadPool = Executors.newFixedThreadPool(2);

        count.setText(seekCount.getProgress() + 1 + "");
        length.setText(seekLength.getProgress() + 8 + "");

        seekCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("demo", "We found: " + seekCount.getProgress() + 1);
                count.setText(seekCount.getProgress() + 1 + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        seekLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                length.setText(seekLength.getProgress() + 8 + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        threadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threadPool.execute(new Task1(Integer.parseInt(count.getText().toString()), Integer.parseInt(length.getText().toString())));
            }
        });

        asyncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask().execute(Integer.parseInt(count.getText().toString()), Integer.parseInt(length.getText().toString()));
            }
        });
    }



    class Task1 implements Runnable {
        int count, length;
        Message message;
        Bundle bundle;
        ArrayList<String> tempList = new ArrayList<>();

        public Task1(int count, int length) {
            this.count = count;
            this.length = length;
        }
        @Override
        public void run() {
            message = new Message();
            message.what = MainActivity.START;
            handler.sendMessage(message);
            for(int i = 0; i < count; i++) {
                message = new Message();
                message.what = MainActivity.UPDATE;
                bundle = new Bundle();
                bundle.putInt(MainActivity.PROGRESS, i);
                message.setData(bundle);
                handler.sendMessage(message);
                tempList.add(Util.getPassword(length));
            }
            message = new Message();
            message.what = MainActivity.STOP;
            bundle = new Bundle();
            bundle.putStringArrayList(MainActivity.LIST, tempList);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }

    class AsyncTask extends android.os.AsyncTask<Integer, Integer, ArrayList<String>>{
        Message message;
        Bundle bundle;
        ArrayList<String> tempList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            message = new Message();
            message.what = MainActivity.START;
            handler.sendMessage(message);
        }

        @Override
        protected ArrayList<String> doInBackground(Integer... integers) {
            for(int i = 0; i < integers[0]; i++) {
                publishProgress(i);
                tempList.add(Util.getPassword(integers[1]));
            }
            return tempList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            message = new Message();
            message.what = MainActivity.STOP;
            bundle = new Bundle();
            bundle.putStringArrayList(MainActivity.LIST, strings);
            message.setData(bundle);
            handler.sendMessage(message);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            message = new Message();
            message.what = MainActivity.UPDATE;
            bundle = new Bundle();
            bundle.putInt(MainActivity.PROGRESS, values[0]);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }
}