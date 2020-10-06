package com.example.myweatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myweatherapplication.Api.WeatherApi;
import com.example.myweatherapplication.Model.WeatherDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private String TAG = "WEATHER";
    private TextView tvTemp;

    private ImageView tvImage;
    private TextView tvLocation;
    private Button buttonSearch;
    private TextView weather_degrees;
    private TextView tvDescription;
    private EditText etlocation_field;
    private WeatherApi.ApiInterface api;

    private Double lat = 0.0;
    private Double lng = 0.0;
    String units = "metric";
    String key = WeatherApi.KEY;

    Location gps_loc;
    Location network_loc;
    Location final_loc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


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
            lat = final_loc.getLatitude();
            lng = final_loc.getLongitude();
        } else if (network_loc != null) {
            final_loc = network_loc;
            lat = final_loc.getLatitude();
            lng = final_loc.getLongitude();
        } else {
            lat = 0.0;
            lng = 0.0;
        }


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);


        tvTemp = (TextView) findViewById(R.id.tvTemp);
        tvImage = (ImageView) findViewById(R.id.ivImage);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        etlocation_field = (EditText) findViewById(R.id.location_field);
        api = WeatherApi.getClient().create(WeatherApi.ApiInterface.class);

        getWeather();

    }

    public void getWeather() {

        // get weather for today
        Call<WeatherDay> callToday = api.getToday(lat, lng, units, key);
        callToday.enqueue(new Callback<WeatherDay>() {
            @Override
            public void onResponse(Call<WeatherDay> call, Response<WeatherDay> response) {
                Log.e(TAG, String.valueOf(R.string.OK));
                WeatherDay data = response.body();
                Log.d(TAG, response.toString());

                if (response.isSuccessful()) {
                    tvTemp.setText(data.getTempWithDegree());
                    tvDescription.setText(data.getDescription());
                    tvLocation.setText(data.getCity());
                    Glide.with(MainActivity.this).load(data.getIconUrl()).into(tvImage);


                }
            }

            @Override
            public void onFailure(Call<WeatherDay> call, Throwable t) {
                Log.e(TAG, String.valueOf(R.string.Failure));
                Log.e(TAG, t.toString());
            }
        });

    }

    public void searchWeather(View view) {

        String key = WeatherApi.KEY;
        String units = "metric";
        String keyword = etlocation_field.getText().toString();


        Call<WeatherDay> callSearch = api.getWeatherSearch(keyword, units, key);
        callSearch.enqueue(new Callback<WeatherDay>() {
            @Override
            public void onResponse(Call<WeatherDay> call, Response<WeatherDay> response) {

                Log.e(TAG, String.valueOf(R.string.OK));
                WeatherDay data = response.body();
                Log.d(TAG, response.toString());


                tvTemp.setText(data.getTempWithDegree());
                tvDescription.setText(data.getDescription());
                tvLocation.setText(data.getCity());
                Glide.with(MainActivity.this).load(data.getIconUrl()).into(tvImage);


            }

            @Override
            public void onFailure(Call<WeatherDay> call, Throwable t) {
                Log.e(TAG, String.valueOf(R.string.Failure));
                Log.e(TAG, t.toString());
            }
        });

    }

}

