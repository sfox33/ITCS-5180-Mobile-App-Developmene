package com.example.andyl.to_dolistapp;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by andyl on 2/2/2018.
 */

public class Task implements Comparable<Task>, Serializable{

    private String name;
    private Calendar dateTime = Calendar.getInstance();
    private String priority;
    private String timeOfDay;

    public Task(String name, String date, String time, String priority) {
        int year, month, day;
        int hours, mins;

        String[] dateVal = date.split("/");
        month = Integer.parseInt(dateVal[0]);
        day = Integer.parseInt(dateVal[1]);
        year = Integer.parseInt(dateVal[2]);

        String[] timeVal = time.split(":");
        hours = Integer.parseInt(timeVal[0]);
        String temp = timeVal[1];
        if (temp.startsWith("0")) {
            timeOfDay = temp.substring(3);
            Log.d("demo", timeOfDay);
            temp = temp.substring(1,2);
        }
        else {
            timeOfDay = temp.substring(3);
            Log.d("demo", timeOfDay);
            temp = temp.substring(0,2);
        }
        mins = Integer.parseInt(temp);

        if (timeOfDay.equals("PM")) {
            if (hours != 12) {
                hours += 12;
            }

        }
        else {
            if (hours == 12) {
                hours = hours - 12;
            }
        }

        this.name = name;
        dateTime.set(year,month,day,hours,mins);
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        String str;
        str = this.dateTime.get(Calendar.MONTH) + "/" + this.dateTime.get(Calendar.DATE) + "/" + this.dateTime.get(Calendar.YEAR);
        return str;
    }

    public String getTaskTime() {
        String str;
        String min = String.valueOf(this.dateTime.get(Calendar.MINUTE));

        if (this.dateTime.get(Calendar.MINUTE) < 10) {
            min = "0" + this.dateTime.get(Calendar.MINUTE);
        }

        int hourOfDay = this.dateTime.get(Calendar.HOUR_OF_DAY);
        int hour = this.dateTime.get(Calendar.HOUR);
        Log.d("demo", "calendar hours " + hourOfDay);
        Log.d("demo", "hour" + this.dateTime.get(Calendar.HOUR));


        if (this.dateTime.get(Calendar.HOUR_OF_DAY) > 12){
            str = this.dateTime.get(Calendar.HOUR) + ":" + min + " PM";
        }
        else if (this.dateTime.get(Calendar.HOUR_OF_DAY) < 12 && this.dateTime.get(Calendar.HOUR_OF_DAY) != 0){
            str = this.dateTime.get(Calendar.HOUR) + ":" + min + " AM";
        }
        else {
            //str = this.dateTime.get(Calendar.HOUR) + ":" + min + " AM";
            hour += 12;
            if (timeOfDay.equals("AM")) {
                str = hour + ":" + min + " AM";
            }
            else {
                str = hour + ":" + min + " PM";
            }
        }

        return str;
    }

    public String getPriority() {
        return priority;
    }

    public Calendar getDateTime() {
        return dateTime;
    }

    @Override
    public int compareTo(Task t) {
        return this.dateTime.compareTo(t.dateTime);
    }
}
