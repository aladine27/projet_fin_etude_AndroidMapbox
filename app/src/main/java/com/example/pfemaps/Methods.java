package com.example.pfemaps;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Methods {
    @GET("posts")
    Call<Maps> getAllData();


}
