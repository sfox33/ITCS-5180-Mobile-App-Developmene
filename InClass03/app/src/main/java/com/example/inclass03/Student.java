package com.example.inclass03;

import java.io.Serializable;

/**
 * Assignment #03
 * Student.java
 * @author Sean Fox
 * @author Andrew Lambropoulos
 */

public class Student implements Serializable {

    private String name;
    private String email;
    private String dept;
    private int mood;

    public Student(String name, String email, String dept, int mood) {
        this.name = name;
        this.email = email;
        this.dept = dept;
        this.mood = mood;
    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public String getDept(){
        return this.dept;
    }

    public int getMood(){
        return this.mood;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setDept(String dept){
        this.dept = dept;
    }

    public void setMood(int mood){
        this.mood = mood;
    }
}
