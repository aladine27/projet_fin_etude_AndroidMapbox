package com.example.pfemaps;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.pfemaps.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;



import com.mapbox.api.directions.v5.DirectionsCriteria;

import com.mapbox.api.directions.v5.MapboxDirections;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.utils.PolylineUtils;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MainActivity extends AppCompatActivity {

    private MapView mapView;

    private DirectionsRoute currentRoute;
    private MapboxDirections client;
    private Point origin;
    private Point destination;

    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";



    FusedLocationProviderClient fusedLocationProviderClient;

    private ActivityMainBinding binding;

    
    @Override
    protected  void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        try {




            Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            mapView = findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);


            mapView.getMapAsync(new OnMapReadyCallback() { //load map

                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {
                    // Check if our result was valid.

                    Intent intent = getIntent();
                    String tourner = intent.getStringExtra("tourner").replace(' ','_');



                    Toast.makeText(getApplicationContext(),tourner,Toast.LENGTH_SHORT).show();



                    getListLoactionFromBD(mapboxMap,tourner);


                    mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {

                        @Override
                        public void onStyleLoaded(@NonNull Style style) {


                        }
                    });
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }


    }


    private void getListLoactionFromBD(MapboxMap mapboxMap, String tourner) {
         getLocation(mapboxMap);
        Call<List<Results>> call = RetrofitClient.getInstance().getMyApi().getListLoactionFromBD(tourner);

        call.enqueue(new Callback<List<Results>>() {

            @Override
            public void onResponse(Call<List<Results>> call, Response<List<Results>> response) {
                List<Results> myheroList = response.body();
                String[] oneHeroes = new String[myheroList.size()];

                LatLng[] latLng = new LatLng[myheroList.size()];



                for (int i = 0; i < myheroList.size(); i++) {

                    double latx = Double.parseDouble(myheroList.get(i).getLattitudee());
                    double langy = Double.parseDouble(myheroList.get(i).getLangitude());
                    String order = myheroList.get(i).getPolice();
                  //  String tournee = myheroList.get(i).getLangitude();

                    /* One way to add a marker view*/

                    latLng[i] = new LatLng(
                            latx,
                            langy);


                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latx, langy))
                            .title(order)
                            .snippet("Police")



                    );

                }

                // Draw Points on MapView
                mapboxMap.addPolyline(new PolylineOptions()
                        .add(latLng)
                        .color(Color.parseColor("#38afea"))
                        .width(5));




            }

            @Override
            public void onFailure(Call<List<Results>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }


    private void getLocation(MapboxMap mapboxMap) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //When permission denied
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            Toast.makeText(getApplicationContext(),"ddsd:1", Toast.LENGTH_LONG).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getApplicationContext(),"ddsd:2", Toast.LENGTH_LONG).show();
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //Initialize location
                Location location = task.getResult();

                if (location != null){

                    try {
                        //Initialize geoCoder
                        Geocoder geocoder= new Geocoder(MainActivity.this,
                                Locale.getDefault());
                        //Initialize address list
                        List<Address> address = geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1
                        );

                        double latx = address.get(0).getLatitude();

                        double langy = address.get(0).getLongitude();

                        /*
                        mapboxMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(36.862499, 10.195556))
                                        .title("Chicago")
                                        .snippet("Illinois")
                            */

                        mapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latx, langy))
                                .title("Position Actuel")

                        );



                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}