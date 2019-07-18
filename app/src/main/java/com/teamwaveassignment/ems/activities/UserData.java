package com.teamwaveassignment.ems.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teamwaveassignment.ems.R;
import com.teamwaveassignment.ems.ViewDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.codeshuffle.typewriterview.TypeWriterView;

public class UserData extends AppCompatActivity {


    @BindView(R.id.typeWriterView)
    TypeWriterView typeWriterView;
    @BindView(R.id.designation)
    EditText designation;
    @BindView(R.id.textInputLayout)
    TextInputLayout textInputLayout;
    @BindView(R.id.designations)
        LinearLayout linearLayout;

    StringBuilder nameString;
    String name;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private FirebaseFirestore db;

    ViewDialog viewDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdata);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        viewDialog=new ViewDialog(this);
        viewDialog.showDialog();
        designation.setVisibility(View.INVISIBLE);
        textInputLayout.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
        db = FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        nameString=new StringBuilder("Hey ! ");
        nameString.append(currentUser.getDisplayName()+".");
        name=nameString.toString();

        designation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Cant be empty !");
                    linearLayout.setVisibility(View.INVISIBLE);
                }

                if (s.length() > 0) {
                    textInputLayout.setError(null);
                    textInputLayout.setErrorEnabled(false);
                    linearLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
       // checkUser();
        newUser();

    }

    private void newUser()
    {

        viewDialog.hideDialog();
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


                }, splash_length);
        textInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              linearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkUser()
    {
        final String email=currentUser.getEmail();
        DocumentReference docIdRef = db.collection("users").document(email);

        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String name=document.getString("name");
                        String email=document.getString("email");
                        String designation=document.getString("location");
                        Intent loginIntent = new Intent(UserData.this,MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                        finish();

                    } else {
                       viewDialog.hideDialog();
                       newUser();

                    }
                } else {

                }
            }
        });

    }
}
