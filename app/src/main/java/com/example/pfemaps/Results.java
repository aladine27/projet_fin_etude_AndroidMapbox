package com.example.pfemaps;

import com.google.gson.annotations.SerializedName;

public class Results {
    @SerializedName("lattitude")
    private String lattitude;
    @SerializedName("langitude")
    private String langitude;


    @SerializedName("police")
    private String police;


    public Results(String lattitude,String langitude,String police) {
        this.lattitude = lattitude;
        this.langitude = langitude;
        this.police = police;
    }

    public String getLattitudee() {
        return lattitude;
    }

    public String getLangitude() {
        return langitude;
    }

    public String getPolice() {
        return police;
    }
}
