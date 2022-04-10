package com.example.pfemaps;

import com.google.gson.annotations.SerializedName;

public class Results {
    @SerializedName("lattitude")
    private String lattitude;
    @SerializedName("langitude")
    private String langitude;


    public Results(String lattitude,String langitude) {
        this.lattitude = lattitude;
        this.langitude = langitude;
    }

    public String getLattitudee() {
        return lattitude;
    }

    public String getLangitude() {
        return langitude;
    }
}
