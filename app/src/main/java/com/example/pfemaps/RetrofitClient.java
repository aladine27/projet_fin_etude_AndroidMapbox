package com.example.pfemaps;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {

    private static RetrofitClient instance = null;
    private Api myApi;
    private  static String BASE_URL = "https://jsonplaceholder.typicode.com/";

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(Api.class);
    }
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }
    public Api getMyApi() {
        return myApi;
    }
    /*
    private  static Retrofit retrofit;
    private  static String BASE_URL = "https://jsonplaceholder.typicode.com/";
    public static  Retrofit getRetrofitInstance(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit;
    }*/
}
