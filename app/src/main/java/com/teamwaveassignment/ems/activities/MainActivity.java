package com.teamwaveassignment.ems.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.marcoscg.dialogsheet.DialogSheet;
import com.teamwaveassignment.ems.EMS;
import com.teamwaveassignment.ems.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.name)
    TextView userName;
    @BindView(R.id.designation)
    TextView designation;
    @BindView(R.id.department)
    TextView department;
    @BindView(R.id.requestLeave)
    Button applyLeave;
    @BindView(R.id.employeeList)
    Button employeeList;
    @BindView(R.id.approveLeave)
    Button approveLeave;
    @BindView(R.id.profile)
    Button profile;
    @BindView(R.id.leaveHistory)
    Button leaveHistory;
    @BindView(R.id.logOut)
    Button logOut;
    EMS ems;
    DialogSheet dialogSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        ems=(EMS)getApplicationContext();
        userName.setText(ems.getName());
        designation.setText(ems.getDesignation());
        department.setText(ems.getDepartment());

        dialogSheet = new DialogSheet(MainActivity.this)
                .setTitle("Edit Profile")
                .setColoredNavigationBar(true)
                .setCancelable(true)
                .setRoundedCorners(true)
                .setBackgroundColor(getResources().getColor(R.color.colorBackground))
                .setColoredNavigationBar(true)
                .setPositiveButton(R.string.save, new DialogSheet.OnPositiveClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

        dialogSheet.setView(R.layout.bottomsheet_profile);
        View inflatedView = dialogSheet.getInflatedView();

        profile.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                          dialogSheet.show();
                                       }
                                   });
                applyLeave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, ApplyLeave.class));
                    }
                });

                employeeList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        startActivity(new Intent(MainActivity.this, EmployeeList.class));
               /* AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MainActivity.this,"GoodBye!!", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });*/

                    }
                });

                leaveHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                     startActivity(new Intent(MainActivity.this,LeaveHistory.class));
                     }
                });
                logOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AuthUI.getInstance()
                                .signOut(MainActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(MainActivity.this,"GoodBye!!", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }
                });

    }
}
