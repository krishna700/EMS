package com.teamwaveassignment.ems.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.teamwaveassignment.ems.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class ApplyLeave extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        SlyCalendarDialog.Callback callback = new SlyCalendarDialog.Callback() {
            @Override
            public void onCancelled() {

            }

            @Override
            public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {


                int numberOfDays = 0;
                while (firstDate.before(secondDate)) {
                    if ((Calendar.SATURDAY != firstDate.get(Calendar.DAY_OF_WEEK))
                            &&(Calendar.SUNDAY != firstDate.get(Calendar.DAY_OF_WEEK))) {
                        numberOfDays++;
                    }
                    firstDate.add(Calendar.DATE,1);
                }
                Toast.makeText(ApplyLeave.this,""+(numberOfDays),Toast.LENGTH_LONG).show();
            }
        };

        new SlyCalendarDialog()
                .setSingle(false)
                .setCallback(callback)
                .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");


    }
}
