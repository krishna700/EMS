package com.teamwaveassignment.ems.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.teamwaveassignment.ems.R;

public class LeaveHistory extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_history);
        getSupportActionBar().hide();
    }
}
