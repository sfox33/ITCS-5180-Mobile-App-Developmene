package edu.uncc.midterm;

import java.util.Comparator;

/**
 * Created by Sean on 3/12/2018.
 * NameComparator.java
 * Midterm Exam
 */

public class NameComparator implements Comparator<App> {
    @Override
    public int compare(App app, App t1) {
        return app.name.compareTo(t1.name);
    }
}
