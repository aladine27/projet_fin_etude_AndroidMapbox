package com.example.pfemaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Selection_Interface extends AppCompatActivity {

    ListView simpleList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_interface);

        this.getListLoactionFromBD();


    }


    private void getListLoactionFromBD() {


      //  String countryList[] = {"tourner 1", "tourner 2"};

       // String[] countryList = new String[10];


        Call<List<Resultst>> call = RetrofitClient.getInstance().getMyApit()
                .getListTourneFromBD();

        call.enqueue(new Callback<List<Resultst>>() {

            @Override
            public void onResponse(Call<List<Resultst>> call, Response<List<Resultst>> response) {
                List<Resultst> myheroList = response.body();
                simpleList = (ListView) findViewById(R.id.simpleListView);
                String[] countryList = new String[myheroList.size()];

                for (int i = 0; i < myheroList.size(); i++) {

                    String tour = myheroList.get(i).getTournee();

                    countryList[i] = tour;


                }



                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listview, R.id.textView, countryList);
                simpleList.setAdapter(arrayAdapter);

                simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.putExtra("tourner",countryList[i]);
                        startActivity(intent);
                    }
                });



  /* */




            }

            @Override
            public void onFailure(Call<List<Resultst>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }

        });
    }
}