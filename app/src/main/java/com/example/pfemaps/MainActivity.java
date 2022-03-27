package com.example.pfemaps;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.TextView;


import android.view.LayoutInflater;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pfemaps.databinding.ActivityMainBinding;
import android.widget.FrameLayout;

import android.view.Menu;
import android.view.MenuItem;
//import com.mapbox.maps.MapView;
//import com.mapbox.maps.Style;
//import com.mapbox.maps.MapboxMap;

import android.graphics.PointF;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.Projection;

import androidx.annotation.NonNull;

//import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import com.mapbox.maps.plugin.annotation.Annotation;
//import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
//import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager;

public class MainActivity extends AppCompatActivity {
    //private MapView mapView;
   // private MapboxMap Mapbox;
    private MapView mapView;
    private MarkerView markerView;
    private MarkerViewManager markerViewManager;
    DBHelper DB;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DB= new DBHelper(this,"pfeapp",1);
/*
        try{
            DB.checkdb();
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            DB.OpenDatabase();
        }catch(Exception e){
            e.printStackTrace();
        }
*/


      /* Cursor mydata = DB.getdata();
        while (mydata.moveToNext()){
            System.out.println(mydata.getString(1));
        }
        */

       // Mapbox.get
       // Mapbox.getInstance(this, getString(R.string.mapbox_key));
       // setContentView(R.layout.activity_main);
        //mapView = (MapView) findViewById(R.id.mapView);
        //mapView.onCreate(savedInstanceState);


        try {
            SQLiteDatabase myDB = this.openOrCreateDatabase("pfeapp", MODE_PRIVATE, null);

            /* Create a Table in the Database. */
            myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                    + "releve"
                    + " (latx VARCHAR, langy VARCHAR);");

            /* Insert data to a Table*/
            myDB.execSQL("INSERT INTO "
                    + "releve"
                    + " (latx, langy)"
                    + " VALUES ('33.892166', '9.561555499999997');");

            /* Insert data to a Table*/
            myDB.execSQL("INSERT INTO "
                    + "releve"
                    + " (latx, langy)"
                    + " VALUES ('35.821430' , '10.634422');");

            /* Insert data to a Table*/
            myDB.execSQL("INSERT INTO "
                    + "releve"
                    + " (latx, langy)"
                    + " VALUES ('34.73423', '10.761');");

            /*retrieve data from database */
            Cursor c = myDB.rawQuery("SELECT * FROM " + "releve" , null);

            int Column1 = c.getColumnIndex("latx");
            int Column2 = c.getColumnIndex("langy");



            Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            mapView = findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);


            mapView.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {
                    // Check if our result was valid.
                    c.moveToFirst();
                    if (c != null) {
                        // Loop through all Results
                        do {
                            double latx = Double.parseDouble(c.getString(Column1));
                            double langy = Double.parseDouble(c.getString(Column2));

                            // One way to add a marker view
                            mapboxMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latx, langy))
                                    .title("Chicago")
                                    .snippet("Illinois")
                            );


                            // Toast.makeText(this,"latx:"+latx,Toast.LENGTH_SHORT).show();
                        }while(c.moveToNext());
                    }

                    mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {

                        @Override
                        public void onStyleLoaded(@NonNull Style style) {
                            // Initialize the MarkerViewManager
                            //   markerViewManager = new MarkerViewManager(mapView, mapboxMap);


                            // Use an XML layout to create a View object
                            //View customView = LayoutInflater.from(MainActivity.this).inflate(
                            //     R.layout.marker_view_bubble, null);
                            //  customView.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

                            //ImageView imgExample = (ImageView) customView.findViewById(R.id.imageView);


                            // Use the View to create a MarkerView which will eventually be given to
                            // the plugin's MarkerViewManager class
                            // markerView = new MarkerView(new LatLng(33.892166, 9.561555499999997), customView);
                            //   markerViewManager.addMarker(markerView);


                        }
                    });
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
        //first initialize Mapbox

       // mapView.getMapboxMap().loadStyleUri(  Style.MAPBOX_STREETS   );

       // mapView.getLocationInSurface();

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

}