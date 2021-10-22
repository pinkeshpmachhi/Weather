package com.p2m.sweather;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.p2m.sweather.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity{
    String api = "566e7e0c3208750b42b82c39d93af343";
    String iconUrl="http://openweathermap.org/img/wn/10d@2x.png";

    String url="https://api.openweathermap.org/data/2.5/weather?q=Kolkata,India&appid=566e7e0c3208750b42b82c39d93af343";
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    String cityname, countryname;
    ActivityMainBinding binding;
    private String jsonResponse;
    private static String TAG = MainActivity.class.getSimpleName();
    String sunriseString=null, sunsetString=null, dt=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cityname= getIntent().getStringExtra("city");
        countryname= getIntent().getStringExtra("country");


        getSupportActionBar().hide();
        relativeLayout = findViewById(R.id.mainContainer);
        processData();
        binding.errorText.setVisibility(View.GONE);

    }

    private void processData() {
        binding.loader.setVisibility(View.VISIBLE);

        url= "https://api.openweathermap.org/data/2.5/weather?q="+cityname+","+countryname+"&appid=566e7e0c3208750b42b82c39d93af343";


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject array= response.getJSONObject("main");
                        String temp = array.getString("temp");
                        String pressure = array.getString("pressure");
                        String humidity = array.getString("humidity");
                        String temp_min = array.getString("temp_min");
                        String temp_max = array.getString("temp_max");
                    Log.d("PPPPP","82");

                    float  TEMPINCALCIUS= (float) (Float.parseFloat(temp)- 273.15);
                            String tempCelciuss= String.valueOf(TEMPINCALCIUS);
                            String tempCelcius= tempCelciuss.substring(0,2)+"°C";

                    float  MINTEMPINCALCIUS= (float) (Float.parseFloat(temp_min)- 273.15);
                    String MINtempCelciuss= String.valueOf(MINTEMPINCALCIUS);
                    String MINtempCelcius= MINtempCelciuss.substring(0,5)+"°C";

                    float  MAXTEMPINCALCIUS= (float) (Float.parseFloat(temp_max)- 273.15);
                    String MAXtempCelciuss= String.valueOf(MAXTEMPINCALCIUS);
                    String MAXtempCelcius= MAXtempCelciuss.substring(0,5)+"°C";


                    binding.temp.setText(tempCelcius);
                    binding.minTemp.setText("Minimum temperature: "+MINtempCelcius);
                    binding.maxTemp.setText("Maximum temperature: "+MAXtempCelcius);
                    binding.pressureTV.setText(pressure+" hPa");
                    binding.humidityTV.setText(humidity+"%");


                    JSONObject windOBJ= response.getJSONObject("wind");
                    String se= windOBJ.getString("speed");
                    String de= windOBJ.getString("deg");
                    String speed= se+"meter/sec";
                    String deg= de+"°";
                    Log.d("PPPPP","108");
                    binding.windspeedTV.setText(speed);
                    binding.winddegTV.setText(deg);




                    JSONObject coordOBJ= response.getJSONObject("coord");
                    String lon= coordOBJ.getString("lon");
                    String lat= coordOBJ.getString("lat");
                    Log.d("PPPPP","90");

                    JSONArray weatherarray= response.getJSONArray("weather");

                    String dscn= "";
                    for (int i=0;i<weatherarray.length();i++) {
                        dscn = weatherarray.getString(i);
                    }

//------------------------------------For description---------------------------------------------------------//
                    int descriptionstart= dscn.indexOf("description");
                    int descriptionend= dscn.indexOf("icon");
                    String dscription= dscn.substring(descriptionstart,descriptionend);
                    String dscnfinal= dscription.substring(0,dscription.length()-2);
                    String jjjj= dscnfinal.substring(14,dscnfinal.length()-1);

                    Log.d("PPPPP","99");
                    binding.discriprion.setText(jjjj);

//------------------------------------For icon----------------------------------------------------------------//
                    int iconstart= dscn.indexOf("icon");
                    String iconn= dscn.substring(iconstart);
                    String iconfinal= iconn.substring(7,iconn.length()-2);

                    String iconUrl = "http://openweathermap.org/img/w/" + iconfinal + ".png";
                    Log.d("IMAGE",iconUrl);

                    JSONObject mainOBJ= response.getJSONObject("main");
                    String tempstring= mainOBJ.getString("temp");
                    String pressurestring= mainOBJ.getString("pressure");
                    String humidityString= mainOBJ.getString("humidity");
                    String tempminString= mainOBJ.getString("temp_min");
                    String tempmaxString= mainOBJ.getString("temp_max");
                    Log.d("PPPPP","111");

                    String visibilityString= response.getString("visibility");
                    binding.visibilityTV.setText(visibilityString+" meter");

                    Log.d("PPPPP","114");

//                    JSONObject rainOBJ= response.getJSONObject("rain");
//                    String hour= rainOBJ.getString("1h");
//                    Log.d("PPPPP","123");

                    JSONObject cloudOBJ= response.getJSONObject("clouds");
                    String allString = cloudOBJ.getString("all");
                    Log.d("PPPPP","127");
                    binding.cloudnessTV.setText(allString+"%");

                    String dtString = response.getString("dt");
                    Log.d("PPPPP","130");
                    binding.updatedAta.setText("Last updated at "+getdate(dtString));

                    JSONObject sysOBJ= response.getJSONObject("sys");
                    String typeString= sysOBJ.getString("type");
                    String idString= sysOBJ.getString("id");
                    String country= sysOBJ.getString("country");
                    sunriseString= sysOBJ.getString("sunrise");
                    sunsetString= sysOBJ.getString("sunset");
                    Log.d("PPPPP","138");


                    binding.sunriseTV.setText(getdate(sunriseString));
                    binding.sunsetTV.setText(getdate(sunsetString));





                    String timezoneString= response.getString("timezone");
                    String lastID= response.getString("id");
                    String nameOfCountry= response.getString("name");
                    String codString= response.getString("cod");
                    Log.d("PPPPP","144");

                    binding.citytv.setText(cityname);
                    binding.Countrytv.setText(","+countryname);

                    binding.loader.setVisibility(View.GONE);
                    binding.mainContainer.setVisibility(View.VISIBLE);
                    binding.detailsContainer.setVisibility(View.VISIBLE);
                    binding.detailsContainer2.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: Check the Connection" + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    binding.loader.setVisibility(View.GONE);
                    binding.mainContainer.setVisibility(View.GONE);
                    binding.errorText.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("PPPP", error.toString());
                binding.loader.setVisibility(View.GONE);
                binding.mainContainer.setVisibility(View.GONE);
                binding.errorText.setVisibility(View.VISIBLE);
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjReq);


    }

    private String  getdate(String timestamp){
        try {
            long unixSeconds = Long.parseLong(timestamp);
// convert seconds to milliseconds
            Date date = new java.util.Date(unixSeconds*1000L);
// the format of your date
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
// give a timezone reference for formatting (see comment at the bottom)
            sdf.setTimeZone(java.util.TimeZone.getDefault());
            String formattedDate = sdf.format(date);
            Log.d("Last",formattedDate);
            return formattedDate;
        }catch (Exception e){
            return "";
        }

    }
}

