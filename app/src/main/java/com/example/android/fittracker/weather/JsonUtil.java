package com.example.android.fittracker.weather;

import android.icu.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class JsonUtil {

    private static String JSON_CODE = "cod";
    private static int HTTP_OK = 200;

    private static String JSON_CITY = "city";
    private static String JSON_COORD = "coord";

    private static String JSON_LATITUDE = "lat";
    private static String JSON_LONGITUDE = "lon";

    private static String JSON_PRESSURE = "pressure";
    private static String JSON_HUMIDITY = "humidity";
    private static String JSON_WINDSPEED = "speed";

    private static String JSON_WEATHER = "weather";
    private static String JSON_DESCRIPTION = "description";
    private static String JSON_ICON = "icon";

    private static String JSON_LIST = "list";
    private static String JSON_MAIN = "main";
    private static String JSON_DATE = "dt_txt";
    private static String JSON_TEMPERATURE = "temp";
    private static String JSON_TEMPMAX = "temp_max";
    private static String JSON_TEMPMIN = "temp_min";


    private static String JSON_COUNTRY = "country";
    private static String JSON_CITYNAME = "name";
    private static String JSON_GEOID = "geonameid";



    // public static ContentValues[] getWeatherContentValuesFromJson(Context context, String forecastJsonStr)
    //         throws JSONException {
    public static ArrayList<WeatherData> getForecastValueFromJson(String city,  String forecastJsonStr) throws JSONException {
        String description, fdescription = null, icon, ficon = null;
        WeatherData mWeather = null;
        ArrayList<WeatherData> weatherList;
        weatherList = new ArrayList<WeatherData>();
        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        /* Is there an error? */
        if (forecastJson.has(JSON_CODE)) {
            int errorCode = forecastJson.getInt(JSON_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }


        if (forecastJson != null && forecastJson.getInt(JSON_CODE) == HttpURLConnection.HTTP_OK) {

            // Getting JSON Object
            JSONArray list = forecastJson.getJSONArray(JSON_LIST);
            for (int i = 0; i < list.length(); i++) {

                WeatherData fWeather = new WeatherData();

                JSONObject jb = list.getJSONObject(i);
                String date = jb.getString(JSON_DATE);
                String current_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                if (!WeatherUtil.haveSameString(date, current_date)
                        && WeatherUtil.haveSameString(date, "12:00:00")) {
                    JSONObject main = jb.getJSONObject(JSON_MAIN);

                    Double temp = main.getDouble(JSON_TEMPERATURE);
                    Double pressure = main.getDouble(JSON_PRESSURE);
                    int humidity = main.getInt(JSON_HUMIDITY);
                    Double mintemp = main.getDouble(JSON_TEMPMIN);
                    Double maxtemp = main.getDouble(JSON_TEMPMAX);

                    JSONArray weather = jb.getJSONArray(JSON_WEATHER);
                    for (int j = 0; j < weather.length(); j++) {
                        JSONObject eWeather = weather.getJSONObject(j);
                        fdescription = eWeather.getString(JSON_DESCRIPTION);
                        ficon = eWeather.getString(JSON_ICON);
                    }

                    fWeather.setCity(city);
                    fWeather.setWeather_date(date);
                    fWeather.setDescription(fdescription);
                    fWeather.setIcon(ficon);
                    fWeather.setTemp(temp);
                    fWeather.setMintemp(mintemp);
                    fWeather.setMaxtemp(maxtemp);
                    fWeather.setHumidity(humidity);
                    fWeather.setPressure(pressure);
                    weatherList.add(fWeather);
                }
            }
        }
        return weatherList;
    }

/*
            ContentValues weatherValues = new ContentValues();
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, dateTimeMillis);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, humidity);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, pressure);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, windDirection);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, high);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, low);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weatherId);

            weatherContentValues[i] = weatherValues;
        }

        return weatherContentValues;
    }
*/

    // json parser for current weather
    public static WeatherData getWeatherValueFromJson(String city,  String forecastJsonStr) throws JSONException {
        String description = null, icon = null;
        WeatherData mWeather = null;
        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        /* Is there an error? */
        if (forecastJson.has(JSON_CODE)) {
            int errorCode = forecastJson.getInt(JSON_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }


        if (forecastJson != null && forecastJson.getInt(JSON_CODE) == HttpURLConnection.HTTP_OK) {

            // Getting JSON Object
            JSONObject main = forecastJson.getJSONObject(JSON_MAIN);

            Double temp = main.getDouble(JSON_TEMPERATURE);
            Double pressure = main.getDouble(JSON_PRESSURE);
            int humidity = main.getInt(JSON_HUMIDITY);
            Double mintemp = main.getDouble(JSON_TEMPMIN);
            Double maxtemp = main.getDouble(JSON_TEMPMAX);


            JSONArray weather = forecastJson.getJSONArray(JSON_WEATHER);
            mWeather = new WeatherData();
            for (int i = 0; i < weather.length(); i++) {
                JSONObject eWeather = weather.getJSONObject(i);

                description = eWeather.getString(JSON_DESCRIPTION);
                icon = eWeather.getString(JSON_ICON);
            }
            mWeather.setCity(city);
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            mWeather.setWeather_date(date);
            mWeather.setDescription(description);
            mWeather.setIcon(icon);
            mWeather.setTemp(temp);
            mWeather.setMintemp(mintemp);
            mWeather.setMaxtemp(maxtemp);
            mWeather.setHumidity(humidity);
            mWeather.setPressure(pressure);

        }
        return mWeather;
    }

    public static ArrayList<LocationData> getLocationValueFromJson(String locationJsonStr) throws JSONException {
        String description, fdescription = null, icon, ficon = null;
        LocationData mLocation = null;
        ArrayList<LocationData> locationList;
        locationList = new ArrayList<LocationData>();
        JSONArray jsonarray = new JSONArray(locationJsonStr);

        for (int i = 0; i < jsonarray.length(); i++) {
            mLocation = new LocationData();
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            String cityname = jsonobject.getString(JSON_CITYNAME);
            String country = jsonobject.getString(JSON_COUNTRY);
            Double geoid = jsonobject.getDouble(JSON_GEOID);

            mLocation.setCity(cityname);
            mLocation.setCountry(country);
            //Todo
            mLocation.setLatitude(geoid);
            mLocation.setLongitude(geoid);

            locationList.add(mLocation);
        }
        return locationList;
    }

}