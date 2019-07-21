package com.teamwaveassignment.ems.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.llollox.androidprojects.compoundbuttongroup.CompoundButtonGroup;
import com.teamwaveassignment.ems.EMS;
import com.teamwaveassignment.ems.R;
import com.teamwaveassignment.ems.ViewDialog;
import com.teamwaveassignment.ems.models.Employee;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.codeshuffle.typewriterview.TypeWriterView;


public class UserData extends AppCompatActivity {


    @BindView(R.id.typeWriterView)
    TypeWriterView typeWriterView;
    @BindView(R.id.designation)
    EditText designation;
    @BindView(R.id.number)
    EditText number;
    @BindView(R.id.textInputLayout)
    TextInputLayout textInputLayout;
    @BindView(R.id.textInputLayoutTwo)
    TextInputLayout textInputLayoutTwo;
    @BindView(R.id.departments)
    CardView linearLayout;
    @BindView(R.id.department)
    CompoundButtonGroup compoundButtonGroup;
    @BindView(R.id.save)
    LinearLayout save;

    StringBuilder nameString;
    String name,email,phone,post,department;
    boolean isHr;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private FirebaseFirestore db;
    List<AuthUI.IdpConfig> providers;
    final int RC_SIGN_IN=12;
    EMS ems;
    ViewDialog viewDialog;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdata);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        activity=this;
        viewDialog=new ViewDialog(this);
        viewDialog.showDialog();
        designation.setVisibility(View.INVISIBLE);
        textInputLayout.setVisibility(View.INVISIBLE);
        textInputLayoutTwo.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        db = FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        ems=(EMS) getApplicationContext();

        email=currentUser.getEmail();

        checkUser();


        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Can't be empty !");
                    textInputLayoutTwo.setVisibility(View.INVISIBLE);
                }

                if (s.length() > 0) {
                    textInputLayout.setError(null);
                    textInputLayout.setErrorEnabled(false);
                    textInputLayoutTwo.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        designation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    textInputLayoutTwo.setErrorEnabled(true);
                    textInputLayoutTwo.setError("Can't be empty !");
                    linearLayout.setVisibility(View.INVISIBLE);
                }

                if (s.length() > 0) {
                    textInputLayoutTwo.setError(null);
                    textInputLayoutTwo.setErrorEnabled(false);
                    linearLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        compoundButtonGroup.setOnButtonSelectedListener(new CompoundButtonGroup.OnButtonSelectedListener() {
            @Override
            public void onButtonSelected(int position, String value, boolean isChecked) {
                if(position==0){isHr=true;}
                save.setVisibility(View.VISIBLE);
                department=value;

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewDialog ==null)
                { viewDialog=new ViewDialog(activity);}
                viewDialog.showDialog();
                String name=currentUser.getDisplayName();
                phone=number.getText().toString();
                post=designation.getText().toString();
                final int random = new Random().nextInt(1000) + 10;
                final int randomOne = new Random().nextInt(100) + 100;
                final int randomTwo = new Random().nextInt(1000) + 900;
                String id=""+random+randomOne+randomTwo;
                ems.setName(name);
                ems.setDepartment(department);
                ems.setDesignation(post);
                ems.setPhone(phone);
                ems.setLeaves(12);
                ems.setEmail(email);
           //     ems.setIsHr(isHr);
                Employee employee=new
                        Employee(id,"",name,email,phone,department,post,12,isHr);
                db.collection("employees").document(email).set(employee)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                viewDialog.hideDialog();
                                Intent loginIntent = new Intent(UserData.this, MainActivity.class);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(loginIntent);
                                Animatoo.animateSlideUp(UserData.this);
                                finish();
                            }
                        });
            }
        });

    }



    private void newUser()
    {


        nameString=new StringBuilder("Hey !  ");
        name=nameString.toString();
        typeWriterView.setDelay(100);
        typeWriterView.animateText(name);
        typeWriterView.setWithMusic(true);
        final int splash_length = 2000;
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        designation.setVisibility(View.VISIBLE);
                        textInputLayout.setVisibility(View.VISIBLE);
                    }
                }                , splash_length);



}

    private void checkUser()
    {


        DocumentReference docIdRef = db.collection("employees").document(email);

        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ems.setName(document.getString("name"));
                        ems.setDepartment(document.getString("department"));
                        ems.setDesignation(document.getString("designation"));
                        ems.setPhone(document.getString("phone"));
                        ems.setLeaves(document.getLong("leaves").intValue());
                        ems.setEmail(document.getString("email"));
                        ems.setIsHr(document.getBoolean("isHr"));
                        Intent loginIntent = new Intent(UserData.this,MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                        Animatoo.animateSlideUp(UserData.this);
                        finish();

                    } else {
                        if (viewDialog != null) { viewDialog.hideDialog(); viewDialog = null; }
                       newUser();

                    }
                } else {

                }
            }
        });

    }
    @Override public void onStop()
    {
        super.onStop();
        if (viewDialog != null) { viewDialog.hideDialog(); viewDialog = null; }
    }





}
