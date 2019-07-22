package com.teamwaveassignment.ems.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.marcoscg.dialogsheet.DialogSheet;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.teamwaveassignment.ems.EMS;
import com.teamwaveassignment.ems.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.profilePhoto)
    ImageView profilePhoto;
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
    CircularImageView bottomProfilePhoto;
    EMS ems;
    DialogSheet dialogSheet;
    EditText bottomName, bottomDesignation, bottomPhone;
    static final int REQUEST_TAKE_PHOTO = 1;
    FirebaseStorage firebaseStorage;
    private UploadTask uploadTask;
    private Uri uri = null;


    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        ems=(EMS)getApplicationContext();
        db = FirebaseFirestore.getInstance();
        userName.setText(ems.getName());
        designation.setText(ems.getDesignation());
        department.setText(ems.getDepartment());


        Glide.with(MainActivity.this)
                .load(ems.getPhotoUrl()).placeholder(R.drawable.placeholder).into(profilePhoto);


        approveLeave.setVisibility(View.INVISIBLE);
        if(ems.getIsHr()) {
            approveLeave.setVisibility(View.VISIBLE);

        }

        firebaseStorage = FirebaseStorage.getInstance();

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
                        if (uri != null)
                            sendDataToServer(uri);
                        String bottomNameText = bottomName.getText().toString();
                        String bottomDesignationText = bottomDesignation.getText().toString();
                        String bottomPhoneText = bottomPhone.getText().toString();

                        if (!bottomNameText.equals(ems.getName())) {
                            ems.setName(bottomNameText);
                            db.collection("employees").document(ems.getEmail())
                                    .update("name", bottomNameText);


                        }
                        if (!bottomDesignationText.equals(ems.getDesignation())) {

                            ems.setDesignation(bottomDesignationText);
                            db.collection("employees").document(ems.getEmail())
                                    .update("designation", bottomDesignationText);

                        }
                        if (!bottomPhoneText.equals(ems.getPhone())) {
                            ems.setPhone(bottomPhoneText);
                            db.collection("employees").document(ems.getEmail())
                                    .update("phone", bottomPhoneText);
                        }
                        userName.setText(ems.getName());
                        designation.setText(ems.getDesignation());
                        dialogSheet.dismiss();
                    }
                });

        dialogSheet.setView(R.layout.bottomsheet_profile);
        View inflatedView = dialogSheet.getInflatedView();
        bottomName = inflatedView.findViewById(R.id.bottomName);
        bottomDesignation = inflatedView.findViewById(R.id.bottomDesignation);
        bottomPhone = inflatedView.findViewById(R.id.phone_number);
        bottomProfilePhoto = inflatedView.findViewById(R.id.bottomPhoto);
        Glide.with(MainActivity.this)
                .load(ems.getPhotoUrl()).placeholder(R.drawable.placeholder).into(bottomProfilePhoto);
        bottomName.setText(ems.getName());
        bottomDesignation.setText(ems.getDesignation());
        bottomPhone.setText(ems.getPhone());
        bottomProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickPhoto();
            }
        });


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
                                Animatoo.animateSplit(MainActivity.this);
                                finish();
                            }
                        });
            }
        });

        approveLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ApproveLeave.class));
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            uri = data.getData();
            Glide.with(MainActivity.this)
                    .load(uri).placeholder(R.drawable.placeholder).into(profilePhoto);
            Glide.with(MainActivity.this)
                    .load(uri).placeholder(R.drawable.placeholder).into(bottomProfilePhoto);


        }

    }


    private void clickPhoto() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_TAKE_PHOTO);
    }


    private void sendDataToServer(Uri uri) {
        StorageReference storageRef = firebaseStorage.getReference();


        StorageReference profilePhotoRef = storageRef.child(ems.getName() + "profilePhoto.jpg");


        uploadTask = profilePhotoRef.putFile(uri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();

                }

                // Continue with the task to get the download URL
                return profilePhotoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    db.collection("employees").document(ems.getEmail())
                            .update("photoUrl", downloadUri.toString());

                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }


}

