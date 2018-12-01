package com.example.android.fittracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.fittracker.weather.WeatherData;
import com.example.android.fittracker.weather.WeatherUtil;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyViewHolder> {
    private List<WeatherData> wList;


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mDate, mTemp;
        ImageView mImage;


        public MyViewHolder(View view) {
            super(view);
            mDate = (TextView) view.findViewById(R.id.cardview_forecastdate);
            mTemp = (TextView) view.findViewById(R.id.cardview_forcasttemp);
            mImage = (ImageView) view.findViewById(R.id.cardview_forecastimage);

        }

    }

    public ForecastAdapter(List<WeatherData> weatherList) {
        this.wList = weatherList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_forecast, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

        if(wList == null) return;
        else {
            WeatherData weather = wList.get(position);
            String fDate = weather.getWeather_date();
            myViewHolder.mDate.setText(WeatherUtil.getDayFromDateString(fDate));
            String tempString = WeatherUtil.getNumberFormat(weather.getTemp()) + "°C";
            myViewHolder.mTemp.setText(tempString);
            String imageName = weather.getIcon();
            int sId = WeatherUtil.getTheSmallIconID(imageName);
            //int id=2131099750;
            //String resource = "R.drawable.w" + imageName.substring(0,2) + "small";
            //myViewHolder.mImage.setImageResource(Integer.valueOf(resource));
            myViewHolder.mImage.setImageResource(sId);
        }
    }

    @Override
    public int getItemCount() {
        return wList.size();
    }

}
