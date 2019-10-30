package edu.uncc.inclass14;

/**
 * Created by Sean on 4/30/2018.
 */

public class Contact {
    private long id;
    private String  name,email, dept, phone;

    public Contact() {
    }

    public Contact(long id, String name, String email, String dept, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dept = dept;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
