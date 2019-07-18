package com.teamwaveassignment.ems.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.teamwaveassignment.ems.R;

public class EmployeeList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeelist);
        getSupportActionBar().hide();

    }
}
