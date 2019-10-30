/*************
 Homework 3
 Question.java
 Andrew Lambropoulos
 Sean Fox
 ******************/
package edu.uncc.homework03;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;


public class Question implements Serializable {
    private int questionNum, answerIndex;
    private String question, picture;
    private ArrayList<String> answers;

    public Question(ArrayList<String> inputArray) {
        Log.d("demo", inputArray.toString());
        this.answers = new ArrayList<String>();
        this.questionNum = Integer.parseInt(inputArray.get(0));
        this.question = inputArray.get(1);
        this.answerIndex = Integer.parseInt(inputArray.get(inputArray.size()-1));
        int count = 2;
        if(inputArray.get(count).length() > 4 && inputArray.get(count).substring(0,4).equals("http")) {
            this.picture = inputArray.get(count);
            count++;
        }
        while(count < inputArray.size()-1) {
            if(inputArray.get(count) != null) {
                answers.add(inputArray.get(count));
            } else {
                answers.add("");
            }
            count++;
        }
    }

    public String getQuestion() {
        return this.question;
    }

    public String[] getAnwers() {
        return answers.toArray(new String[answers.size()]);
    }

    public int getAnswerIndex() {
        return answerIndex;
    }

    public String getPicture() {
        if(this.picture != null){
            return picture;
        } else {
            return "";
        }
    }
}
