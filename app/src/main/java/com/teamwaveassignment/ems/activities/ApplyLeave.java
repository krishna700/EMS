package com.teamwaveassignment.ems.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.teamwaveassignment.ems.EMS;
import com.teamwaveassignment.ems.R;
import com.teamwaveassignment.ems.models.Leave;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import github.ishaan.buttonprogressbar.ButtonProgressBar;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class ApplyLeave extends AppCompatActivity {


    @BindView(R.id.selectDate)
    Button button;
    @BindView(R.id.startDate)
    TextView startDate;
    @BindView(R.id.endDate)
    TextView endDate;
    @BindView(R.id.dayOffLeft)
    TextView leaveBalance;
    @BindView(R.id.status)
    EditText reason;
    @BindView(R.id.applyLayout)
    LinearLayout applyLayout;
    @BindView(R.id.dayOffCount)
    TextView leaveCount;
    @BindView(R.id.dayCountString)
    TextView noOfDays;
    EMS ems;

    String firstDateString,endDateString,timeStamp,status,
            approvedBy="This request is yet to be approved"
            ,reasonString;
    int balance,noOfDaysInt;


    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);
        db=FirebaseFirestore.getInstance();
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        ems=(EMS)getApplicationContext();
        balance=ems.getLeaves();
        leaveBalance.setText(""+ems.getLeaves());
        reason.setVisibility(View.INVISIBLE);
        applyLayout.setVisibility(View.INVISIBLE);
        noOfDays.setVisibility(View.INVISIBLE);


        final ButtonProgressBar save = (ButtonProgressBar) findViewById(R.id.bpb_main);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reasonString=reason.getText().toString();
                if(!reasonString.equals(""))
                {
                    save.startLoader();
                    String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss Z", Locale.getDefault()).format(new Date());
                    final int random = new Random().nextInt(100) + 10;
                    final int randomOne = new Random().nextInt(10000) + 100;
                    String id=""+random+randomOne;
                    int status=0;
                    Leave leave=new Leave(id,approvedBy,reasonString,endDateString,firstDateString
                    ,date,ems.getName(),ems.getDesignation(),ems.getDepartment(),ems.getEmail(),ems.getPhone(),noOfDaysInt,status);
                    db.collection("leaves").document(id).set(leave);

                    db.collection("employees").document(ems.getEmail()).collection("myLeaves")
                            .document(id).set(leave)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                      save.stopLoader();

                                    final int splash_length = 500;
                                    new Handler().postDelayed(
                                            new Runnable() {

                                                @Override
                                                public void run() {

                                                    Toasty.success(ApplyLeave.this, "Day-Off Applied! Please wait for approval", Toast.LENGTH_LONG, true).show();
                                                    finish();
                                                }


                                            }, splash_length);

                                }
                            });
                }
                else
                {
                   reason.setError("Oops! Empty");
                    Toasty.info(ApplyLeave.this, "Reason for Day-off is mandatory", Toast.LENGTH_SHORT, true).show();
                }



            }
        });

        SlyCalendarDialog.Callback callback = new SlyCalendarDialog.Callback() {
            @Override
            public void onCancelled() {
                Toasty.info(ApplyLeave.this, "Please select date range", Toast.LENGTH_SHORT, true).show();
            }

            @Override
            public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {


                if(secondDate==null) secondDate=firstDate;
                Date first=firstDate.getTime();
                Date second=secondDate.getTime();
                Date currentDate=Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                firstDateString = dateFormat.format(firstDate.getTime());
                endDateString=dateFormat.format(secondDate.getTime());

                startDate.setText(firstDateString);
                endDate.setText(endDateString);

                if(first.before(currentDate)){
                    Toasty.error(ApplyLeave.this, "Select an upcoming date", Toast.LENGTH_SHORT, true).show();
                    reason.setVisibility(View.INVISIBLE);
                    applyLayout.setVisibility(View.INVISIBLE);
                    noOfDays.setVisibility(View.INVISIBLE);
                    leaveCount.setText("");
                }
               else
                {
                    int numberOfDays = 0;
                    while (firstDate.before(secondDate)) {
                        if ((Calendar.SATURDAY != firstDate.get(Calendar.DAY_OF_WEEK))
                                &&(Calendar.SUNDAY != firstDate.get(Calendar.DAY_OF_WEEK))) {
                            numberOfDays++;

                        }
                        firstDate.add(Calendar.DATE,1);
                    }
                    if(firstDate.equals(secondDate) &&( (Calendar.SATURDAY != firstDate.get(Calendar.DAY_OF_WEEK))
                            &&(Calendar.SUNDAY != firstDate.get(Calendar.DAY_OF_WEEK))))
                    {
                        numberOfDays+=1;
                    }
                    if(balance>=numberOfDays)
                    {
                        leaveCount.setText(""+numberOfDays);
                       reason.setVisibility(View.VISIBLE);
                       noOfDaysInt=numberOfDays;
                       applyLayout.setVisibility(View.VISIBLE);
                       noOfDays.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        reason.setVisibility(View.INVISIBLE);
                        leaveCount.setText("");
                        applyLayout.setVisibility(View.INVISIBLE);
                        noOfDays.setVisibility(View.INVISIBLE);
                        Toasty.warning(ApplyLeave.this, "Exceeds day-off balance.", Toast.LENGTH_SHORT, true).show();
                    }
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

    private void sendNotification() {
        String topic = "highScores";

// See documentation on defining a message payload.


// Send a message to the devices subscribed to the provided topic.
       // String response = FirebaseMessaging.getInstance().send("");



    }
}
