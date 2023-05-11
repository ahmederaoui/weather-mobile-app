package ma.enset.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView date,city,temp,windSpeed,sunrise,sunset,pressure,tempMin,tempMax,humidity;
    ImageView searchBtn,main,localisation;
    EditText searchBar;
    WeatherEntity weatherEntity;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        date = findViewById(R.id.date);
        city = findViewById(R.id.city);
        temp = findViewById(R.id.temp);
        windSpeed = findViewById(R.id.windSpeed);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        pressure = findViewById(R.id.pressure);
        tempMin = findViewById(R.id.tempMin);
        tempMax = findViewById(R.id.tempMax);
        humidity = findViewById(R.id.humidity);
        searchBtn = findViewById(R.id.searchBtn);
        main = findViewById(R.id.main);
        localisation = findViewById(R.id.localisation);
        searchBar = findViewById(R.id.searchBar);
    }
    public void setSearchBtn(View v){
        String query = searchBar.getText().toString();
        if (query.length()!=0){
            RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
            String url="http://api.openweathermap.org/data/2.5/weather?q="
                    +query+"&appid=e457293228d5e1465f30bcbe1aea456b";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        Log.i("MyLog","----------------------------------------------");
                        Log.i("MyLog",response);
                        JSONObject jsonObject=new JSONObject(response);
                        WeatherMapper weatherMapper = new WeatherMapper();
                        weatherEntity = weatherMapper.mapper(jsonObject);
                        affectation(query);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Log.i("MyLog","-------Connection problem-------------------");
                            Toast.makeText(MainActivity.this,
                                    "City not fond",Toast.LENGTH_LONG).show();
                        }
                    });
            queue.add(stringRequest);
        }
    }
    /*
    TextView date,city,temp,windSpeed,sunrise,sunset,pressure,tempMin,tempMax,humidity;
    ImageView searchBtn,main,localisation;
    EditText searchBar;
    */
    public void affectation(String query){
        date.setText(weatherEntity.date);
        city.setText(query);
        temp.setText(String.valueOf(weatherEntity.temp));
        windSpeed.setText(String.valueOf(weatherEntity.wind_speed));
        sunrise.setText(weatherEntity.sunrise);
        sunset.setText(weatherEntity.sunset);
        pressure.setText(String.valueOf(weatherEntity.pressure));
        tempMin.setText(String.valueOf(weatherEntity.temp_min));
        tempMax.setText(String.valueOf(weatherEntity.temp_max));
        humidity.setText(String.valueOf(weatherEntity.humidity));
        date.setText(String.valueOf(weatherEntity.pressure));
        if(weatherEntity.main.equals("Rain")) {
            main.setImageResource(R.drawable.rainy);
        }
        else if(weatherEntity.main.equals("Clear")){
            main.setImageResource(R.drawable.sunny);}
        else if(weatherEntity.main.equals("Thunderstorm")) {
            main.setImageResource(R.drawable.storm);}
        else if(weatherEntity.main.equals("Clouds"))
        {
            main.setImageResource(R.drawable.cloudy);
        }

    }
    public void localisation(View v) {
        Intent intent=new Intent(MainActivity.this,MapsActivity.class);
        Bundle bundle= new Bundle();
        bundle.putFloat("lon",weatherEntity.lon);
        bundle.putFloat("lat",weatherEntity.lat);
        bundle.putString("city",city.getText().toString());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}