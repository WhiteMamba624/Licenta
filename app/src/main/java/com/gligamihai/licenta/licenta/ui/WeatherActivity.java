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
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gligamihai.licenta.R;
import com.gligamihai.licenta.licenta.utils.ApiClient;
import com.gligamihai.licenta.licenta.utils.ApiInterface;
import com.gligamihai.licenta.licenta.utils.WeatherData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    DrawerLayout drawerLayout;
    TextView cityName;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location gps_loc;
    Location network_loc;
    Location final_loc;
    double longitude;
    double latitude;
    TextView cityTemperature;
    TextView cityWeatherDescription;
    TextView cityWindSpeed;
    TextView cityPressure;
    TextView cityHumidity;
    TextView cityVisibility;
    TextView cityTemperatureMin;
    TextView cityTemperatureMax;
    TextView cityCountry;
    ImageView weatherImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initApp();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(WeatherActivity.this);
        // btngetlocation=findViewById(R.id.buttonGetLocation);
        //String currentCity=getCurrentCity();
        cityName.setText(getCurrentCity());
        //String cityy="Brasov";
        getWeatherData(cityName.getText().toString().trim());

    }

    public String getCurrentCity() {
        String city = "";
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
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
        } else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        } else {
            latitude = 0.0;
            longitude = 0.0;
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        try {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                city = addresses.get(0).getLocality();
                cityCountry.setText(addresses.get(0).getCountryName());
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return city;
    }

    private void getWeatherData(String city) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<WeatherData> call = apiInterface.getWeatherData(city);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                try {
                    if (response.body() != null) {
                        // Log.w("TAG",response.body().getMain().getMainTemp());
                        cityTemperature.setText(String.valueOf(Math.round(Double.parseDouble(response.body().getWeather().getTemp()))));
                        cityWindSpeed.setText(response.body().getWind().getWindSpeed() + " m/s");
                        cityWeatherDescription.setText(response.body().getWeatherList().get(0).getWeatherMain());
                        cityPressure.setText(response.body().getWeather().getTempPressure() + "hPa");
                        cityHumidity.setText(response.body().getWeather().getTempHumidity() + " %");
                        cityVisibility.setText(response.body().getVisibility() / 1000 + " km");
                        cityTemperatureMin.setText(String.valueOf(Math.round(Double.parseDouble(response.body().getWeather().getTempMin()))));
                        cityTemperatureMax.setText(String.valueOf(Math.round(Double.parseDouble(response.body().getWeather().getTempMax()))));
                        if (response.body().getWeatherList().get(0).getWeatherId() >= 200 && response.body().getWeatherList().get(0).getWeatherId() <= 232) {
                            weatherImage.setBackgroundResource(R.drawable.ic_thunderstorm);
                        } else if (response.body().getWeatherList().get(0).getWeatherId() >= 300 && response.body().getWeatherList().get(0).getWeatherId() <= 531) {
                            weatherImage.setBackgroundResource(R.drawable.ic_raining);
                        } else if (response.body().getWeatherList().get(0).getWeatherId() >= 600 && response.body().getWeatherList().get(0).getWeatherId() <= 622) {
                            weatherImage.setBackgroundResource(R.drawable.ic_snow);
                        } else if (response.body().getWeatherList().get(0).getWeatherId() >= 701 && response.body().getWeatherList().get(0).getWeatherId() <= 781) {
                            weatherImage.setBackgroundResource(R.drawable.ic_atmosphere);
                        } else if (response.body().getWeatherList().get(0).getWeatherId() == 800) {
                            if (response.body().getWeatherList().get(0).getWeatherIcon().equalsIgnoreCase("01d")) {
                                weatherImage.setBackgroundResource(R.drawable.ic_sunny);
                            } else if (response.body().getWeatherList().get(0).getWeatherIcon().equalsIgnoreCase("01n")) {
                                weatherImage.setBackgroundResource(R.drawable.ic_clear_night);
                            }
                        } else if (response.body().getWeatherList().get(0).getWeatherId() > 800) {
                            if (response.body().getWeatherList().get(0).getWeatherIcon().endsWith("n")) {
                                weatherImage.setBackgroundResource(R.drawable.ic_clouds_night);
                            } else if (response.body().getWeatherList().get(0).getWeatherIcon().endsWith("d")) {
                                weatherImage.setBackgroundResource(R.drawable.ic_clouds_day);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //   Log.d("DATA","Temperature in "+city+" "+String.valueOf(Math.round(Double.parseDouble(response.body().getWeather().getTemp()))));
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {

            }
        });
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
        startActivity(new Intent(WeatherActivity.this, MainActivity.class));
    }

    public void clickWeather(View view) {
        recreate();
    }

    public void clickLogout(View view) {
        AlertDialog.Builder alertLogout = new AlertDialog.Builder(view.getContext());
        alertLogout.setTitle("Logout");
        alertLogout.setMessage("Are you sure you want to log out?");
        alertLogout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO make a method for this code
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(WeatherActivity.this, MainActivity.class));
    }

    public void clickPlaces(View view) {
        startActivity(new Intent(WeatherActivity.this, PlacesActivity.class));
    }

    private void initApp(){
        drawerLayout = findViewById(R.id.drawer_layout);
        cityName = findViewById(R.id.textViewCity);
        cityTemperature = findViewById(R.id.textViewTemperature);
        cityWeatherDescription = findViewById(R.id.textViewWeatherDescription);
        cityWindSpeed = findViewById(R.id.textViewWindValue);
        cityPressure = findViewById(R.id.textViewPressureValue);
        cityHumidity = findViewById(R.id.textViewHumidityValue);
        cityVisibility = findViewById(R.id.textViewVisibilityValue);
        cityTemperatureMin = findViewById(R.id.textViewTempLowValue);
        cityTemperatureMax = findViewById(R.id.textViewTempMaxValue);
        cityCountry = findViewById(R.id.textViewCountry);
        weatherImage = findViewById(R.id.imageViewWeatherType);
    }

    //    public void getLocation(){
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull @NotNull Task<Location> task) {
//                Location location=task.getResult();
//                if(location!=null){
//                    try {
//                        Geocoder geocoder=new Geocoder(WeatherActivity.this,
//                                Locale.getDefault());
//                        List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//                            cityName.setText(addresses.get(0).getLocality());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }


}