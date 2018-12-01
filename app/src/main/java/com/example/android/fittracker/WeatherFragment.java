package com.example.android.fittracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.fittracker.data.SharedPreference;
import com.example.android.fittracker.weather.JsonUtil;
import com.example.android.fittracker.weather.NetworkHandler;
import com.example.android.fittracker.weather.WeatherData;
import com.example.android.fittracker.weather.WeatherUtil;

import java.net.URL;
import java.util.ArrayList;


public class WeatherFragment extends Fragment {
    private ArrayList<WeatherData> weatherList;
    WeatherData mWeatherData;
    private RecyclerView mRecyclerView;
    private ForecastAdapter mAdapter;
    ImageView cardview_image;
    TextView cardview_description, cardview_date, cardview_city, cardview_temperature,
            cardview_humidity, cardview_pressure;

    String cityname = "toronto";
    String countryname = "canada";

    public WeatherFragment() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onResume() {
        super.onResume();


        FetchWeatherTask mFetchWeatherTask = new FetchWeatherTask();
        mFetchWeatherTask.execute(cityname, countryname);

        FetchForeCastTask mFetchForeCastTask = new FetchForeCastTask();
        mFetchForeCastTask.execute(cityname, countryname);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 5);
        mRecyclerView.setLayoutManager(mLayoutManager);


        cardview_image = view.findViewById(R.id.cardview_image);
        cardview_date = view.findViewById(R.id.cardview_date);
        cardview_city = view.findViewById(R.id.cardview_city);
        cardview_description = view.findViewById(R.id.cardview_description);
        cardview_temperature = view.findViewById(R.id.cardview_temperature);
        cardview_humidity = view.findViewById(R.id.cardview_humidityvalue);
        cardview_pressure = view.findViewById(R.id.cardview_pressurevalue);

        return view;

    }

    public class FetchWeatherTask extends AsyncTask<String, Void, WeatherData> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected WeatherData doInBackground(String... params) {

            String location = params[0];
            String country = params[1];
            URL weatherRequestUrl = NetworkHandler.buildUrl(location, country, true);

            try {
                String jsonWeatherResponse = NetworkHandler
                        .getResponseFromHttpUrl(weatherRequestUrl);

                WeatherData wData = JsonUtil.getWeatherValueFromJson(location, jsonWeatherResponse);


                return wData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(WeatherData wData) {
            cardview_date.setText(wData.getWeather_date());
            cardview_city.setText(wData.getCity());
            cardview_description.setText(wData.getDescription());
            String temString = WeatherUtil.getNumberFormat(wData.getTemp()) + "°C";
            cardview_temperature.setText(temString);
            cardview_humidity.setText(wData.getHumidity() + "%");
            String pString = WeatherUtil.getNumberFormat(wData.getPressure()) + "mph";
            cardview_pressure.setText(pString);
            int sId = WeatherUtil.getTheIconID(wData.getIcon());
            cardview_image.setImageResource(sId);

        }
    }


    public class FetchForeCastTask extends AsyncTask<String, Void, ArrayList<WeatherData>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<WeatherData> doInBackground(String... params) {

            String location = params[0];
            String country = params[1];
            URL weatherRequestUrl = NetworkHandler.buildUrl(location, country, false);

            try {
                String jsonWeatherResponse = NetworkHandler
                        .getResponseFromHttpUrl(weatherRequestUrl);

                ArrayList<WeatherData> wList = JsonUtil.getForecastValueFromJson(location, jsonWeatherResponse);
                return wList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<WeatherData> wList) {
            weatherList = wList;
            mAdapter = new ForecastAdapter(weatherList);
            mRecyclerView.setAdapter(mAdapter);

        }
    }

    private String readValueFromSharedPreferences(Context context, String key)
    {
        // Get SharedPreferences object, the shared preferences file name is this activity class name.
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference.SP_FILE_NAME, Context.MODE_PRIVATE);
        String ret = sharedPreferences.getString(key, "");
        return ret;
    }


}
