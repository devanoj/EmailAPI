package com.example.emailapi.Entity;

import androidx.annotation.Nullable;

public class Animal {
    private String animalID;
    private String name;
    private String dob;
    private String breed;
    private String energyLevel;
    private String email;
    private String mImageUrl;
    private String uid;
    private String from;

    public Animal() {
        this.animalID="";
        this.name="";
        this.dob="";
        this.breed="";
        this.energyLevel="";
        this.email="";
        this.mImageUrl="";
        this.uid="";
        this.from="";
    }



    public Animal(String uid, String animalID, String name, String dob, String breed, String energyLevel, String email, String mImageUrl, String from) {
        this.animalID=animalID;
        this.name=name;
        this.dob=dob;
        this.breed=breed;
        this.energyLevel=energyLevel;
        this.email=email;
        this.mImageUrl=mImageUrl;
        this.uid=uid;
        this.from=from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUsid() {
        return uid;
    }

    public void setUsid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return animalID;
    }

    public void setId(String animalID) {
        this.animalID = animalID;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDOB() {
        return dob;
    }

    public void setDOB(String dob) {
        this.dob = dob;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(String energyLevel) {
        this.energyLevel = energyLevel;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }


}
