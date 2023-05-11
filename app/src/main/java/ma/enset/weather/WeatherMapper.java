package ma.enset.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherMapper {
    public WeatherEntity mapper(JSONObject jsonObject) throws JSONException {
        WeatherEntity weatherEntity = new WeatherEntity();
        Date dateD=new Date(jsonObject.getLong("" +
                "")*1000);
        SimpleDateFormat simpleDateFormat=
                new SimpleDateFormat("dd-MMM-yyyy' T 'HH:mm");
        weatherEntity.date=simpleDateFormat.format(dateD);

        JSONObject main=jsonObject.getJSONObject("main");
        weatherEntity.temp=(float)(main.getDouble("temp")-273.15);
        weatherEntity.temp_min=(float) (main.getDouble("temp_min")-273.15);
        weatherEntity.temp_max=(float) (main.getDouble("temp_max")-273.15);
        weatherEntity.pressure=(float) (main.getDouble("pressure"));
        weatherEntity.humidity=(int)(main.getDouble("humidity"));

        JSONArray weather=jsonObject.getJSONArray("weather");
        weatherEntity.main=weather.getJSONObject(0).getString("main");
        JSONObject sys=jsonObject.getJSONObject("sys");
        weatherEntity.sunset=simpleDateFormat.format(new Date(sys.getLong("sunset")*1000));
        weatherEntity.sunrise=simpleDateFormat.format(new Date(sys.getLong("sunrise")*1000));
        JSONObject wind=jsonObject.getJSONObject("wind");
        weatherEntity.wind_speed=wind.getLong("speed");
        JSONObject coord=jsonObject.getJSONObject("coord");
        weatherEntity.lon=(float)coord.getDouble("lon");
        weatherEntity.lat=(float)coord.getDouble("lat");
        return weatherEntity;

    }
}
