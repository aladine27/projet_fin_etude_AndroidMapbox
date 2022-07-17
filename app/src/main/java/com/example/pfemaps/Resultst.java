package com.example.pfemaps;

import com.google.gson.annotations.SerializedName;

public class Resultst {
    @SerializedName("tournee")
    private String tournee;




    public Resultst(String tournee) {
        this.tournee = tournee;
    }

    public String getTournee() { return tournee;
    }



}
