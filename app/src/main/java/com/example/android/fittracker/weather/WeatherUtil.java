package com.example.android.fittracker.weather;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;

import com.example.android.fittracker.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

public class WeatherUtil {

    public static int getTheIconID(String name) {
        int id = 0;
        //input 01d-> output->R.drawable.w01
        switch(name){
            case "01d":
            case "01n":
                id = R.drawable.w01;
                break;
            case "02d":
            case "02n":
                id = R.drawable.w02;
                break;
            case "03d":
            case "03n":
                id = R.drawable.w03;
                break;
            case "04d":
            case "04n":
                id = R.drawable.w04;
                break;
            case "09d":
            case "09n":
                id = R.drawable.w09;
                break;
            case "010d":
            case "010n":
                id = R.drawable.w10;
                break;
            case "011d":
            case "011n":
                id = R.drawable.w11;
                break;
            case "013d":
            case "013n":
                id = R.drawable.w13;
                break;
            case "050d":
            case "050n":
                id = R.drawable.w50;
                break;
            default:
                id = R.drawable.w01;
                break;
        }
        return id;
    }

    public static int getTheSmallIconID(String name) {
        int id = 0;
        //input 01d-> output->R.drawable.w01
        switch(name){
            case "01d":
            case "01n":
                id = R.drawable.w01small;
                break;
            case "02d":
            case "02n":
                id = R.drawable.w02small;
                break;
            case "03d":
            case "03n":
                id = R.drawable.w03small;
                break;
            case "04d":
            case "04n":
                id = R.drawable.w04small;
                break;
            case "09d":
            case "09n":
                id = R.drawable.w09small;
                break;
            case "010d":
            case "010n":
                id = R.drawable.w10small;
                break;
            case "011d":
            case "011n":
                id = R.drawable.w11small;
                break;
            case "013d":
            case "013n":
                id = R.drawable.w13small;
                break;
            case "050d":
            case "050n":
                id = R.drawable.w50small;
                break;
            default:
                id = R.drawable.w01small;
                break;
        }
        return id;
    }

    public static double celsiusToFahrenheit(double temperature) {
        double fTemp = (temperature * 1.8) + 32;
        return fTemp;
    }

    public static double fahrenheitToCelsius(double temperature) {
        double cTemp = (temperature -32)*(0.5556);
        return cTemp;
    }

    public static boolean haveSameString(String date, String current_date){
        if(date.toUpperCase().contains(current_date.toUpperCase())) {
            return true;
        }
        return false;
    }

    public static String getDayFromDateString(String input_date)
    {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        try {
            dt1 = format1.parse(input_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format2 = new SimpleDateFormat("EE");
        return format2.format(dt1);

    }

    public static String getNewDateFormat(String input_date)
    {
        SimpleDateFormat format1 = new SimpleDateFormat("MMM dd,yyyy");
        return format1.format(input_date);

    }

    public static String getNumberFormat(double number) {
        NumberFormat formatter;
        formatter = new DecimalFormat("0");
        return formatter.format(number);
    }
}

