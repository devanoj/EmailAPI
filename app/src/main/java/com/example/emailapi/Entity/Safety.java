package com.example.emailapi.Entity;

import androidx.annotation.Nullable;

public class Safety {
    private String SafetyId;
    private String adult;
    private String garden;
    private String hoursAlone;
    private String property;
    private String ussid;

    public Safety() {
        this.SafetyId="";
        this.adult="";
        this.garden="";
        this.hoursAlone="";
        this.property="";
        this.ussid="";
    }

    public Safety(String SafetyId, String ussid, String adult, String garden, String hoursAlone, String property) {
        this.SafetyId=SafetyId;
        this.adult=adult;
        this.garden=garden;
        this.hoursAlone=hoursAlone;
        this.property=property;
        this.ussid=ussid;
    }

    public String getSafetyId() {
        return SafetyId;
    }

    public void setSafetyId(String safetyId) {
        SafetyId = safetyId;
    }

    public String getUssid() {
        return ussid;
    }

    public void setUssid(String ussid) {
        this.ussid = ussid;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getGarden() {
        return garden;
    }

    public void setGarden(String garden) {
        this.garden = garden;
    }

    public String getHoursAlone() {
        return hoursAlone;
    }

    public void setHoursAlone(String hoursAlone) {
        this.hoursAlone = hoursAlone;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

}
