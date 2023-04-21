package com.example.emailapi.Entity;

import androidx.annotation.Nullable;

import com.google.firebase.database.PropertyName;

public class User {
    private String name;
    private String dateOfBirth;
    private String phoneNo;
    private String eirCode;
    private String email;
    private String uid; // This is uid for some reason, it was like this before
    private String safetyIDfUser;
    private Boolean organisation;

    public User() {
        this.name="";
        this.dateOfBirth="";
        this.phoneNo="";
        this.eirCode="";
        this.email="";
        this.uid="";
        this.organisation=false;
        this.safetyIDfUser=safetyIDfUser;
    }

    public User(String name, String dateOfBirth, String phoneNo, String eirCode, String email, String uid, Boolean organisation, String safetyIDfUser) {
        this.name=name;
        this.dateOfBirth=dateOfBirth;
        this.phoneNo=phoneNo;
        this.eirCode=eirCode;
        this.email=email;
        this.uid=uid;
        this.organisation=organisation;
        this.safetyIDfUser = safetyIDfUser;
    }

    public String getSafetyIDfUser() {
        return safetyIDfUser;
    }

    public void setSafetyIDfUser(String safetyID) {
        this.safetyIDfUser = safetyID;
    }

    public Boolean getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Boolean organisation) {
        this.organisation = organisation;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEirCode() {
        return eirCode;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEirCode(String eirCode) {
        this.eirCode = eirCode;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }



    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
