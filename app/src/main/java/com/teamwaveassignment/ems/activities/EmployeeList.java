package com.teamwaveassignment.ems.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.teamwaveassignment.ems.EMS;
import com.teamwaveassignment.ems.R;
import com.teamwaveassignment.ems.ViewDialog;
import com.teamwaveassignment.ems.models.Employee;

import butterknife.BindView;
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
    @BindView(R.id.clear)
    Chip department;
    ViewDialog viewDialog;
    PowerMenu powerMenu;
    Activity activity;
    EMS ems;
    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
           addFilter(item.getTitle());
            department.setVisibility(View.VISIBLE);
            department.setText(item.getTitle());
            powerMenu.setSelectedPosition(position);
            powerMenu.dismiss();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeelist);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        ems=(EMS)getApplicationContext();
        activity=this;
        department.setVisibility(View.INVISIBLE);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        query = db.collection("employees").orderBy("name", Query.Direction.ASCENDING);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        setEmployeeList(query);
        department.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                             adapter.stopListening();
                                              query = db.collection("employees").orderBy("name", Query.Direction.ASCENDING);
                                              setEmployeeList(query);
                                             adapter.startListening();
                                             department.setVisibility(View.INVISIBLE);
                                             powerMenu.setSelectedPosition(-1);
                                          }
                                      });

                powerMenu = new PowerMenu.Builder(EmployeeList.this)
                        .addItem(new PowerMenuItem("HR", false))
                        .setAnimation(MenuAnimation.ELASTIC_TOP_RIGHT)
                        .setShowBackground(true)
                        .setMenuRadius(10f)
                        .setMenuShadow(10f) // sets the shadow.
                        .setTextColor(getBaseContext().getResources().getColor(R.color.textBlack))
                        .setSelectedTextColor(getColor(R.color.textBlack))
                        .setMenuColor(Color.WHITE)
                        .setSelectedMenuColor(getBaseContext().getResources().getColor(R.color.colorAccent))
                        .setOnMenuItemClickListener(onMenuItemClickListener)
                        .build();
        powerMenu.addItem(new PowerMenuItem("Development",false));
        powerMenu.addItem(new PowerMenuItem("IT services",false));
        powerMenu.addItem(new PowerMenuItem("Sales",false));
        powerMenu.addItem(new PowerMenuItem("Finance",false));
        powerMenu.addItem(new PowerMenuItem("QA",false));


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              powerMenu.showAsAnchorLeftBottom(view);
            }
        });


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

                if(model.getEmail().equals(ems.getEmail()))
                {
                    viewHolder.itemView.setVisibility(View.INVISIBLE);
                    viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }


                viewHolder.name.setText(model.getName());
                viewHolder.department.setText(model.getDepartment());
                viewHolder.designation.setText(model.getDesignation());
                Glide.with(getBaseContext())
                        .load(model.getPhotoUrl()).placeholder(R.drawable.placeholder).into(viewHolder.profilePhoto);

                viewHolder.call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            String num = "+91" + model.getPhone();
                            intent.setData(Uri.parse("tel:" + num));
                            (activity).startActivity(intent);

                    }
                });
                viewHolder.email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.setPackage("com.google.android.gm");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{model.getEmail()});
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                viewHolder.whatsApp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        whatsApp(activity,"+91"+model.getPhone());
                    }
                });
            }

        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }

    @SuppressLint("NewApi")
    public  void whatsApp(Activity activity, String phone) {


        String url = "https://api.whatsapp.com/send?phone=" + phone;
        try {
            PackageManager pm = activity.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(EmployeeList.this, "WhatsApp is not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void addFilter(String department)
    {

        adapter.stopListening();

            query = db.collection("employees")
                    .orderBy("name", Query.Direction.ASCENDING)
                    .whereEqualTo("department",  department);
            setEmployeeList(query);

           adapter.startListening();
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

      private EmployeeHolder(View itemView)
      {
          super(itemView);
          ButterKnife.bind(this,itemView);
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
