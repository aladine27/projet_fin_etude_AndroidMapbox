package com.example.pfemaps;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    public static final String BASE_URL = "http:192.168.1.106:8000/api/";
    @GET("datamap")
    Call<List<Results>> getListLoactionFromBD();
}
