package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public class WeatherAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return HttpRequest.excuteGet(
                    "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                            "&units=metric&appid=" + API_KEY);
        }

        // is called after doInBackground is finished
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObj = new JSONObject(s);
                Log.d("WeatherAsyncTask", "Received response:\n" + jsonObj);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                JSONObject sys = jsonObj.getJSONObject("sys");

                // Get all data from response
                String city_name = jsonObj.getString("name");
                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Last Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a"
                        , Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temperature = main.getString("temp");
                String cast = weather.getString("description");
                String humidity = main.getString("humidity");
                String temp_min = main.getString("temp_min");
                String temp_max = main.getString("temp_max");
                Long s_rise = sys.getLong("sunrise");
                String sunrise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                        .format(new Date(s_rise * 1000));
                Long s_set = sys.getLong("sunset");
                String sunset = new SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                        .format(new Date(s_set * 1000));
                // fill views
                v_city.setText(city_name);
                v_time.setText(updatedAtText);
                v_temp.setText(temperature + "°C");
                v_forecast.setText(cast);
                v_humidity.setText(humidity);
                v_min_temp.setText(temp_min);
                v_max_temp.setText(temp_max);
                v_sunrise.setText(sunrise);
                v_sunset.setText(sunset);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,
                        "Error! Check your input. Maybe service unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    String API_KEY;
    String city;
    EditText city_input;
    ImageView search_button;
    TextView v_city, v_time, v_temp, v_forecast,
            v_humidity, v_min_temp, v_max_temp, v_sunrise, v_sunset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        API_KEY = BuildConfig.weatherMapAPIKey;
        search_button = findViewById(R.id.search);
        city_input = findViewById(R.id.city_input_field);
        v_city = findViewById(R.id.city);
        v_time = findViewById(R.id.time);
        v_temp = findViewById(R.id.temp);
        v_forecast = findViewById(R.id.forecast);
        v_humidity = findViewById(R.id.humidity_value);
        v_min_temp = findViewById(R.id.min_temp_value);
        v_max_temp = findViewById(R.id.max_temp_value);
        v_sunrise = findViewById(R.id.sunrise_value);
        v_sunset = findViewById(R.id.sunset_value);
    }

    @Override
    protected void onStart() {
        super.onStart();
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city = city_input.getText().toString();
                new WeatherAsyncTask().execute();
            }
        });
    }

}