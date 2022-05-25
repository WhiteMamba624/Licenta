package com.gligamihai.licenta.licenta.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.gligamihai.licenta.BuildConfig;
import com.gligamihai.licenta.R;
import com.gligamihai.licenta.licenta.utils.JsonParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PlacesActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Spinner spinner;
    Button buttonFind;
    SupportMapFragment supportMapFragment;
    GoogleMap map;
    Location gps_loc;
    Location network_loc;
    Location final_loc;
    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        drawerLayout = findViewById(R.id.drawer_layout);
        spinner = findViewById(R.id.spinnerType);
        buttonFind = findViewById(R.id.buttonFind);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMapFragment);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String[] placeTypeList = {"car_repair", "gas_station","insurance_agency"};
        String[] placeNameList = {"ITP", "Rovinietta","RCA"};
        spinner.setAdapter(new ArrayAdapter<>(PlacesActivity.this, android.R.layout.simple_spinner_dropdown_item, placeNameList));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        try {

            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gps_loc != null) {
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));
                }
            });
        } else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));
                }
            });
        } else {
            latitude = 0.0;
            longitude = 0.0;
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);

        buttonFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=spinner.getSelectedItemPosition();

                    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                            "?location=" + latitude + "," + longitude +
                            "&radius=5000" +
                            "&types=" + placeTypeList[i] +
                            "&sensor=true" +
                            "&key=" + BuildConfig.MAPS_API_KEY;
                          //  getResources().getString(R.string.google_map_key);

                    new PlaceTask().execute(url);
            }
        });

    }

    private class PlaceTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {

            String data=null;
            try {
                data=downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string)throws IOException {

        URL url=new URL(string);

        HttpURLConnection connection=(HttpURLConnection) url.openConnection();

        connection.connect();

        InputStream stream=connection.getInputStream();
        BufferedReader reader =new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder=new StringBuilder();
        String line="";

        while((line=reader.readLine())!=null){
            builder.append(line);
        }

        String data=builder.toString();
        reader.close();
        return data;
    }

    private class ParserTask extends AsyncTask<String,Integer,List<HashMap<String,String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {

            JsonParser jsonParser=new JsonParser();

            List<HashMap<String,String>> mapList=null;

            JSONObject object=null;
            try {
                object=new JSONObject(strings[0]);
                mapList=jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            map.clear();

            for(int i=0;i<hashMaps.size();i++){
                HashMap<String,String>hashMapList= hashMaps.get(i);

                double lat=Double.parseDouble(hashMapList.get("lat"));
                double lng=Double.parseDouble(hashMapList.get("lng"));
                String name=hashMapList.get("name");
                LatLng latLng=new LatLng(lat,lng);
                MarkerOptions options=new MarkerOptions();
                options.position(latLng);
                options.title(name);
                map.addMarker(options);
            }
        }
    }
    public void clickMenu(View view) {
        //Open drawer layout
        MainActivity.openDrawer(drawerLayout);
    }

    public void clickLogo(View view) {
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }

    public void clickHome(View view) {
        startActivity(new Intent(PlacesActivity.this, MainActivity.class));
    }

    public void clickPlaces(View view){
        recreate();
    }

    public void clickLogout(View view) {
        AlertDialog.Builder alertLogout = new AlertDialog.Builder(view.getContext());
        alertLogout.setTitle("Logout");
        alertLogout.setMessage("Are you sure you want to log out?");
        alertLogout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        alertLogout.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertLogout.show();
    }

    public void clickWeather(View view){
        startActivity(new Intent(PlacesActivity.this,WeatherActivity.class));
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(PlacesActivity.this, MainActivity.class));
    }


}