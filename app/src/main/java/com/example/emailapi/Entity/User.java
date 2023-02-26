package com.example.emailapi.Entity;

import androidx.annotation.Nullable;

import com.google.firebase.database.PropertyName;

public class User {
    private String name;
    private String dateOfBirth;
    private String lifestyle;
    private String freeTime;
    private String email;
    private String uid; // This is uid for some reason, it was like this before
    private Boolean organisation;

    public User() {
        this.name="";
        this.dateOfBirth="";
        this.lifestyle="";
        this.freeTime="";
        this.email="";
        this.uid=""; //make this into uid
        this.organisation=false;
    }

    public User(String name, String dateOfBirth, String lifestyle, String freeTime, String email, String uid, Boolean organisation) {
        this.name=name;
        this.dateOfBirth=dateOfBirth;
        this.lifestyle=lifestyle;
        this.freeTime=freeTime;
        this.email=email;
        this.uid=uid;
        this.organisation=organisation;
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

    public String getFreeTime() {
        return freeTime;
    }

    public String getLifestyle() {
        return lifestyle;
    }

    public String getAge() {
        return dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setFreeTime(String freeTime) {
        this.freeTime = freeTime;
    }

    public void setLifestyle(String lifestyle) {
        this.lifestyle = lifestyle;
    }



    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
