package edu.uncc.inclass11;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Andrew Lambropoulos on 4/9/2018.
 * Created by Sean Fox
 * QueryResult.java
 * Assignment: InClass11
 */
public class QueryResult {
    ArrayList<Post> data = new ArrayList<>();
    String success;
    String status;

    public ArrayList<Post> getData() {
        return data;
    }

    public void setData(ArrayList<Post> data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
