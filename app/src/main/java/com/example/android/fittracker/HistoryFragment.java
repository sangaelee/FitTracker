package com.example.android.fittracker;


import android.database.Cursor;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.fittracker.data.FittrackerTableHandler;
import com.example.android.fittracker.data.HistoryData;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = HistoryFragment.class.getSimpleName();
    private final static int LOADER_ID = 0;
    Cursor mCursor;
    TextView today_wDuration, today_wCalorie, today_rDuration, today_rCalorie, today_tDuration, today_tCalorie;
    ArrayList<String> dayList;
    ArrayList<String> durationList;
    ArrayList<String> calorieList;
    FragmentManager fm;
    Button historyButton;
    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        today_wDuration = view.findViewById(R.id.today_wduration);
        today_wCalorie = view.findViewById(R.id.today_wcalorie);
        today_rDuration = view.findViewById(R.id.today_rduration);
        today_rCalorie = view.findViewById(R.id.today_rcalorie);
        today_tDuration = view.findViewById(R.id.today_tduration);
        today_tCalorie = view.findViewById(R.id.today_tcalorie);
        historyButton = view.findViewById(R.id.history_button);
        fm = getFragmentManager();

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putStringArrayList("his_day", dayList);
                arguments.putStringArrayList("his_duration", durationList);
                arguments.putStringArrayList("his_calorie", calorieList);
                Fragment fragment = null;
                fragment = new ChartFragment();
                fragment.setArguments(arguments);
                if (fragment != null) {
                    fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
                }

            }
        });
        return view;
    }





    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                FittrackerTableHandler.FittrackerID,
                FittrackerTableHandler.Date,
                FittrackerTableHandler.Type,
                FittrackerTableHandler.Distance,
                FittrackerTableHandler.Duration,
                FittrackerTableHandler.Calorie
        };

        return new CursorLoader(getContext(), MyContentProvider.CONTENT_URI, projection, null, null, FittrackerTableHandler.Date + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursor = cursor;
        mCursor.moveToFirst();
        ArrayList<HistoryData> result;
        if(restoreData(mCursor) != null) {
            result = restoreData(mCursor);

            dayList = new ArrayList<String>();
            durationList = new ArrayList<String>();
            calorieList = new ArrayList<String>();
            String current_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            for (int i = 0; i < result.size(); i++) {
                HistoryData d = new HistoryData();
                d = result.get(i);
                String date = d.getDate();
                double dwDuration = d.getDuration_walking();
                double dwCalorie = d.getCalorie_walking();
                double drDuration = d.getDuration_running();
                double drCalorie = d.getCalorie_running();
                double dtDuration = dwDuration + drDuration;
                double dtCalorie = dwCalorie + drCalorie;
                dayList.add(date);
                durationList.add(String.valueOf(dtDuration));
                calorieList.add(String.valueOf(dtCalorie));
                if (date.equalsIgnoreCase(current_date)) {
                    today_wDuration.setText(String.valueOf(dwDuration));
                    today_wCalorie.setText(String.valueOf(dwCalorie));
                    today_rDuration.setText(String.valueOf(drDuration));
                    today_rCalorie.setText(String.valueOf(drCalorie));
                    today_tDuration.setText(String.valueOf(dtDuration));
                    today_tCalorie.setText(String.valueOf(dtCalorie));
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public ArrayList<HistoryData> restoreData(Cursor cursor) {
        cursor.moveToFirst();
        double dtDuration  = 0;
        double dtCalorie = 0;
        String prevDate = "";
        double tempWalkDuration =0, tempWalkCalorie =0 ;
        double tempRunDuration = 0, tempRunCalorie = 0;
        double tempCycleDuration = 0, tempCycleCalorie = 0;
        boolean saveDone = false;

        ArrayList<HistoryData> historyList = new ArrayList<HistoryData>();
        if(mCursor.getCount()!=0) {
            for (int i = 0; i < mCursor.getCount(); i++) {
                String date = mCursor.getString(mCursor.getColumnIndex(FittrackerTableHandler.Date));
                String type = mCursor.getString(mCursor.getColumnIndex(FittrackerTableHandler.Type));
                String duration = mCursor.getString(mCursor.getColumnIndex(FittrackerTableHandler.Duration));
                dtDuration = Double.valueOf(duration);
                String calorie = mCursor.getString(mCursor.getColumnIndex(FittrackerTableHandler.Calorie));
                dtCalorie = Double.valueOf(calorie);

                if (!date.equalsIgnoreCase(prevDate) && !cursor.isFirst()) {

                    HistoryData history = new HistoryData();
                    history.setDate(prevDate);
                    history.setDuration_walking(tempWalkDuration);
                    history.setCalorie_walking(tempWalkCalorie);
                    history.setDuration_running(tempRunDuration);
                    history.setCalorie_running(tempRunCalorie);
                    history.setDuration_cycling(tempCycleDuration);
                    history.setCalorie_cycling(tempCycleCalorie);
                    historyList.add(history);
                    tempWalkDuration = 0;
                    tempWalkCalorie = 0;
                    tempRunDuration = 0;
                    tempRunCalorie = 0;
                    tempCycleDuration = 0;
                    tempCycleCalorie = 0;
                    saveDone = true;

                }
                if (type.equalsIgnoreCase("walking")) {
                    tempWalkDuration = tempWalkDuration + dtDuration;
                    tempWalkCalorie = tempWalkCalorie + dtCalorie;
                } else if (type.equalsIgnoreCase("running")) {
                    tempRunDuration = tempRunDuration + dtDuration;
                    tempRunCalorie = tempRunCalorie + dtCalorie;
                } else {
                    tempCycleDuration = tempCycleDuration + dtDuration;
                    tempCycleCalorie = tempCycleDuration + dtCalorie;
                }

                prevDate = date;
                cursor.moveToNext();
            }
            if (!saveDone) {
                HistoryData history = new HistoryData();
                history.setDate(prevDate);
                history.setDuration_walking(tempWalkDuration);
                history.setCalorie_walking(tempWalkCalorie);
                history.setDuration_running(tempRunDuration);
                history.setCalorie_running(tempRunCalorie);
                history.setDuration_cycling(tempCycleDuration);
                history.setCalorie_cycling(tempCycleCalorie);
                historyList.add(history);
                tempWalkDuration = 0;
                tempWalkCalorie = 0;
                tempRunDuration = 0;
                tempRunCalorie = 0;
                tempCycleDuration = 0;
                tempCycleCalorie = 0;
            }
            return historyList;
        }
        return null;
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
}
