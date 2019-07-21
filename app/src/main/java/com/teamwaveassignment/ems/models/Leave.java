package com.teamwaveassignment.ems.models;

public class Leave {
  private   String ID,approvedBy,reason,endDate,startDate,timeStamp,email,name,phone,status;
  private   int balanceLeave;
    public Leave(){}

    public Leave(String ID,String approvedBy,String reason,String endDate,String startDate,
    String timeStamp,String name,String email,String phone, String status,int balanceLeave )
    {
        this.ID=ID;
        this.approvedBy=approvedBy;
        this.reason=reason;
        this.endDate=endDate;
        this.startDate=startDate;
        this.timeStamp=timeStamp;
        this.email=email;
        this.phone=phone;
        this.status=status;
        this.name=name;
        this.balanceLeave=balanceLeave;
    }

    public String getID() {
        return ID;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getReason() {
        return reason;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBalanceLeave() {
        return balanceLeave;
    }

    public void setBalanceLeave(int balanceLeave) {
        this.balanceLeave = balanceLeave;
    }
}
