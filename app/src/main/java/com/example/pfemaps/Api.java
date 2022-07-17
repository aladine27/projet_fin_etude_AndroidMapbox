package com.example.pfemaps;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    public static final String BASE_URL = "http:192.168.127.125:8000/api/";
    @GET("datamap")
    Call<List<Results>> getListLoactionFromBD(@Query("tourner") String tourner);



}
