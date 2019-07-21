package com.teamwaveassignment.ems.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.teamwaveassignment.ems.EMS;
import com.teamwaveassignment.ems.R;
import com.teamwaveassignment.ems.models.Employee;
import com.teamwaveassignment.ems.models.Leave;

import butterknife.BindView;
import butterknife.ButterKnife;
import sakout.mehdi.StateViews.StateView;

public class LeaveHistory extends AppCompatActivity {

    private FirebaseFirestore db;
    DocumentReference rRef,iRef;
    private FirestoreRecyclerAdapter adapter;
    Query query;
    FirebaseUser user;
    FirebaseAuth mAuth;

    EMS ems;



    @BindView(R.id.leaveHistory)
    RecyclerView recyclerView;
    @BindView(R.id.stateful)
    StateView mStatusPage;



    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_history);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        mStatusPage.displayLoadingState();

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        ems=(EMS) getApplicationContext();
        query = db.collection("employees").document(ems.getEmail()).collection("myLeaves")
                .orderBy("timeStamp", Query.Direction.ASCENDING);
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
                        .inflate(R.layout.leave_history_item, parent, false);

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
            protected void onBindViewHolder(@NonNull LeaveHolder viewHolder, int i, final Leave model) {


                viewHolder.approvedBy.setText(model.getApprovedBy());
                viewHolder.reason.setText(model.getReason());
                viewHolder.startDate.setText(model.getStartDate());
                viewHolder.endDate.setText(model.getEndDate());
                viewHolder.status.setText(model.getStatus());
                viewHolder.timeStamp.setText(model.getTimeStamp());
            }

        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }


    public class LeaveHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.approvedBy)
        TextView approvedBy;
        @BindView(R.id.reason)
        TextView reason;
        @BindView(R.id.startDate)
        TextView startDate;
        @BindView(R.id.endDate)
        TextView endDate;
        @BindView(R.id.timeStamp)
        TextView timeStamp;
        @BindView(R.id.status)
        TextView status;

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
