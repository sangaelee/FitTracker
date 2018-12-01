package com.example.android.fittracker.weather;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkHandler {
    private static final String APP_ID = "&APPID=63860fabc3208db4454276ee154fea32";
    private static final String OPENWEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private static final String OPENFORCAST_URL = "https://api.openweathermap.org/data/2.5/forecast?q=";
    private static final String UNIT_METIC ="&units=metric";
    private static final String UNIT_IMPERIAL ="&units=imperial";
    private static final String FIND_CITY_URL = "https://pkgstore.datahub.io/core/world-cities/world-cities_json/data/5b3dd46ad10990bca47b04b4739a02ba/world-cities_json.json";

    public static URL buildUrl(String city, String country, boolean Today) {
        String wUrl = null;

        if(Today) {
            wUrl = OPENWEATHER_URL + city +","+ country + UNIT_METIC + APP_ID ;
        }
        else {
            wUrl =  OPENFORCAST_URL + city +","+ country + UNIT_METIC + APP_ID;
        }

        try {
            URL weatherUrl = new URL(wUrl);
            return weatherUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildCityUrl() {
        try {
            URL weatherUrl = new URL(FIND_CITY_URL);
            return weatherUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

}
