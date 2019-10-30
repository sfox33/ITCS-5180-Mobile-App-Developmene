package edu.uncc.midterm;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sean on 3/12/2018.
 * App.java
 * Midterm Exam
 */

public class App implements Serializable {
    public String name, artist, date, imageUrl, copyright;
    public ArrayList<String> genres = new ArrayList<>();
}
