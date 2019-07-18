package com.teamwaveassignment.ems;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class EMS extends Application {
    String name,department,designation,phone,email;
    int leaves;
    @Override
    public void onCreate()
    {
        super.onCreate();

        FirebaseApp.initializeApp(this);

    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLeaves(int leaves) {
        this.leaves = leaves;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getDepartment() {
        return department;
    }

    public int getLeaves() {
        return leaves;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
