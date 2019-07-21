package com.teamwaveassignment.ems.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.teamwaveassignment.ems.EMS;
import com.teamwaveassignment.ems.R;
import com.teamwaveassignment.ems.ViewDialog;
import com.teamwaveassignment.ems.models.Leave;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import sakout.mehdi.StateViews.StateView;

public class ApproveLeave extends AppCompatActivity {


    private FirebaseFirestore db;
    DocumentReference rRef,iRef;
    private FirestoreRecyclerAdapter adapter;
    Query query;
    FirebaseUser user;
    FirebaseAuth mAuth;

    Activity activity;

    ViewDialog viewDialog;

    EMS ems;
    @BindView(R.id.leaveHistory)
    RecyclerView recyclerView;
    @BindView(R.id.stateful)
    StateView mStatusPage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_leave);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        mStatusPage.displayLoadingState();
        activity=this;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ems = (EMS) getApplicationContext();
        query = db.collection("leaves").orderBy("department", Query.Direction.ASCENDING);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        FirestoreRecyclerOptions<Leave> leaveFireStoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Leave>()
                        .setQuery(query, Leave.class).build();
        adapter = new FirestoreRecyclerAdapter<Leave, LeaveHolder>(leaveFireStoreRecyclerOptions) {

            @NonNull
            @Override
            public LeaveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.leave_approve_item, parent, false);

                return new LeaveHolder(view);
            }
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                int i=getItemCount();
                if(i>0) mStatusPage.hideStates();
                else  mStatusPage.displayState("search");

            }

            @Override
            protected void onBindViewHolder(@NonNull LeaveHolder viewHolder, int i,@NonNull final Leave model) {


              /*  if(model.getEmail().equals(ems.getEmail()))
                {
                    viewHolder.itemView.setVisibility(View.INVISIBLE);
                    viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }*/
                viewHolder.name.setText(model.getName());
                viewHolder.designation.setText(model.getDesignation());
                viewHolder.department.setText(model.getDepartment());
                viewHolder.reason.setText(model.getReason());
                viewHolder.startDate.setText(model.getStartDate());
                viewHolder.endDate.setText(model.getEndDate());
                viewHolder.timeStamp.setText(model.getTimeStamp());
                viewHolder.dayCount.setText(model.getNoOfDays()+" Days");
                viewHolder.call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        String num = "+91" + model.getPhone();
                        intent.setData(Uri.parse("tel:" + num));
                        (activity).startActivity(intent);

                    }
                });

               final int noOfDays=model.getNoOfDays();
                viewHolder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewDialog=new ViewDialog(activity);
                        viewDialog.showDialog();
                        db.collection("employees").document(model.getEmail())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot document = task.getResult();
                                        int leaveBalance;
                                        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss Z", Locale.getDefault()).format(new Date());
                                        if (document.exists()) {
                                            leaveBalance=document.getDouble("leaves").intValue();
                                             if(leaveBalance>=noOfDays)
                                             {

                                               int finalBalance=leaveBalance-noOfDays;
                                               db.collection("employees").document(model.getEmail()).collection("myLeaves")
                                                   .document(model.getID()).update("approvedBy","Approved By "+ems.getName()+" on "+date
                                                        );
                                                 db.collection("employees").document(model.getEmail())
                                                         .update("leaves",finalBalance);

                                                 db.collection("employees").document(model.getEmail())
                                                         .collection("myLeaves")
                                                         .document(model.getID())
                                                         .update("status",1)
                                                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task) {
                                                                 viewDialog.hideDialog();
                                                                 db.collection("leaves").document(model.getID()).delete();
                                                             }
                                                         });


                                             }
                                             else
                                             {
                                                 viewDialog.hideDialog();
                                                 Toast.makeText(activity,"Not sufficient leave balance",Toast.LENGTH_LONG).show();

                                             }
                                        }
                                    }
                                });

                    }
                });

                viewHolder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewDialog=new ViewDialog(activity);
                        viewDialog.showDialog();
                        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss Z", Locale.getDefault()).format(new Date());
                        db.collection("employees").document(model.getEmail()).collection("myLeaves")
                                .document(model.getID()).update("approvedBy","Rejected By "+ems.getName()+" on "+date
                        );
                        db.collection("employees").document(model.getEmail())
                                .collection("myLeaves")
                                .document(model.getID())
                                .update("status",-1);
                        db.collection("leaves").document(model.getID()).delete();
                        viewDialog.hideDialog();
                    }
                });
            }

        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }


    class LeaveHolder extends RecyclerView.ViewHolder
    {


        @BindView(R.id.reason)
        TextView reason;
        @BindView(R.id.startDate)
        TextView startDate;
        @BindView(R.id.endDate)
        TextView endDate;
        @BindView(R.id.timeStamp)
        TextView timeStamp;
        @BindView(R.id.accept)
        FloatingActionButton accept;
        @BindView(R.id.reject)
        FloatingActionButton reject;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.designation)
        TextView designation;
        @BindView(R.id.department)
        TextView department;
        @BindView(R.id.call)
        Button call;
        @BindView(R.id.dayCount)
        TextView dayCount;


        private LeaveHolder(View view)
        {
            super(view);
            ButterKnife.bind(this,view);
        }


    }
















    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }

}
