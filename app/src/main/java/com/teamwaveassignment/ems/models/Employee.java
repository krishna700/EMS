package com.teamwaveassignment.ems.models;

public class Employee {
   private String name,email,phone,designation,department;
   private int leaves;

    public Employee(){}

    public Employee(String name,String email,String phone,String department,String designation,int leaves)
    {
        this.name=name;
        this.email=email;
        this.designation=designation;
        this.phone=phone;
        this.department=department;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getLeaves() {
        return leaves;
    }

    public String getDepartment() {
        return department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setLeaves(int leaves) {
        this.leaves = leaves;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
