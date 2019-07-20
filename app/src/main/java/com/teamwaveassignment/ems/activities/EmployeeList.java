package com.teamwaveassignment.ems.activities;

import android.os.Bundle;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.teamwaveassignment.ems.EMS;
import com.teamwaveassignment.ems.R;
import com.teamwaveassignment.ems.ViewDialog;
import com.teamwaveassignment.ems.models.Employee;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class EmployeeList extends AppCompatActivity {

    private FirebaseFirestore db;
    DocumentReference rRef,iRef;
    private FirestoreRecyclerAdapter adapter;
    Query query;
    FirebaseUser user;
    FirebaseAuth mAuth;

    @BindView(R.id.employee_list)
    RecyclerView recyclerView;
    @BindView(R.id.filter)
    FloatingActionButton filter;

    ViewDialog viewDialog;

    EMS ems;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeelist);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        query = db.collection("employees");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        setEmployeeList(query);

    }

    private void setEmployeeList(Query query)
    {
        FirestoreRecyclerOptions<Employee> employeeFireStoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Employee>()
                        .setQuery(query, Employee.class).build();
        adapter = new FirestoreRecyclerAdapter<Employee, EmployeeHolder>(employeeFireStoreRecyclerOptions) {

            @NonNull
            @Override
            public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.employeelist_item, parent, false);

                return new EmployeeHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull EmployeeHolder viewHolder, int i, final Employee model) {
                viewHolder.name.setText(model.getName());
                viewHolder.department.setText(model.getDepartment());
                viewHolder.designation.setText(model.getDesignation());
                viewHolder.profilePhoto.setImageResource(R.drawable.me);
            }

        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }




  public class EmployeeHolder extends RecyclerView.ViewHolder
  {
      @BindView(R.id.name)
      TextView name;
      @BindView(R.id.designation)
      TextView designation;
      @BindView(R.id.department)
      TextView department;
      @BindView(R.id.call)
      MaterialButton call;
      @BindView(R.id.email)
      MaterialButton email;
      @BindView(R.id.whatsApp)
      MaterialButton whatsApp;


      @BindView(R.id.profilePhoto)
      CircularImageView profilePhoto;
      public View view;

      private EmployeeHolder(View view)
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
