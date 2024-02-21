package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText etCity, etCountry;
    TextView tvResult, tempDisplay, cityDisplay, windSpeedDisplay, humidityDisplay;
    ImageView currentWeather;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "b6cbe5bc960e6e4e4eb428096a1c9ee6";
    DecimalFormat df = new DecimalFormat("#.##");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.city);
        tvResult = findViewById(R.id.tvResult);
        tempDisplay = findViewById(R.id.tempDisplay);
        cityDisplay = findViewById(R.id.cityDisplay);
        currentWeather = findViewById(R.id.currentWeather);
        windSpeedDisplay = findViewById(R.id.windSpeedDisplay);
        humidityDisplay = findViewById(R.id.humidityDisplay);
    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();
        if(city.equals("")){
            tvResult.setText("City field can not be empty!");
        }else{
            tempUrl = url + "?q=" + city + "&appid=" + appid + "&units=metric";
        }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("main");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp");
                        double feelsLike = jsonObjectMain.getDouble("feels_like");
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");
                        //.setTextColor(color.rgb(68,134,199));
                        /*output += "Current weather of " + cityName + " (" + countryName + ")"
                                + "\n Temp: " + df.format(temp) + " °C"
                                + "\n Feels Like: " + df.format(feelsLike) + " °C"
                                + "\n Humidity: " + humidity + "%"
                                + "\n Description: " + description
                                + "\n Wind Speed: " + wind + "m/s (meters per second)"
                                + "\n Cloudiness: " + clouds + "%"
                                + "\n Pressure: " + pressure + " hPa"; */
                        tvResult.setText(output);
                        tempDisplay.setText(Math.round(temp) +  " °C");
                        cityDisplay.setText(cityName + ", " + countryName);
                        windSpeedDisplay.setText("Wind Speed\n" + wind +"m/s");
                        humidityDisplay.setText("Humidity \n" + humidity + "%");
                        if (description.equals("Clear")) {
                            currentWeather.setImageResource(R.drawable.clear);
                        } else if (description.equals("Rain")) {
                            currentWeather.setImageResource(R.drawable.rain);
                        } else if (description.equals("Drizzle")) {
                            currentWeather.setImageResource(R.drawable.drizzle);
                        } else if (description.equals("Snow")) {
                            currentWeather.setImageResource(R.drawable.snow);
                        } else if (description.equals("Atmosphere")) {
                            currentWeather.setImageResource(R.drawable.mist);
                        } else if (description.equals("Clouds")) {
                            currentWeather.setImageResource(R.drawable.clouds);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }


