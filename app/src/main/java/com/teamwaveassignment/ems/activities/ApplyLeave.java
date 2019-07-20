package com.teamwaveassignment.ems.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.teamwaveassignment.ems.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import github.ishaan.buttonprogressbar.ButtonProgressBar;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class ApplyLeave extends AppCompatActivity {


    @BindView(R.id.selectDate)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);
        ButterKnife.bind(this);
        getSupportActionBar().hide();


        final ButtonProgressBar save = (ButtonProgressBar) findViewById(R.id.bpb_main);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.startLoader();
                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


            }
        });

        SlyCalendarDialog.Callback callback = new SlyCalendarDialog.Callback() {
            @Override
            public void onCancelled() {

            }

            @Override
            public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {


              Date first=firstDate.getTime();
              Date currentDate=Calendar.getInstance().getTime();
                if(first.before(currentDate)){
                    Toasty.error(ApplyLeave.this, "Select an upcoming date", Toast.LENGTH_SHORT, true).show();
                }


                if (true) {
                    int numberOfDays = 0;
                    while (firstDate.before(secondDate)) {
                        if ((Calendar.SATURDAY != firstDate.get(Calendar.DAY_OF_WEEK))
                                &&(Calendar.SUNDAY != firstDate.get(Calendar.DAY_OF_WEEK))) {
                            numberOfDays++;
                        }
                        firstDate.add(Calendar.DATE,1);
                    }
                   // Toast.makeText(ApplyLeave.this,""+(numberOfDays),Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ApplyLeave.this,"Can't select old date",Toast.LENGTH_LONG).show();
                    finish();
                }


            }
        };

        new SlyCalendarDialog()
                .setSingle(false)
                .setCallback(callback)
                .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SlyCalendarDialog()
                        .setSingle(false)
                        .setCallback(callback)
                        .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");
            }
        });


    }
}
