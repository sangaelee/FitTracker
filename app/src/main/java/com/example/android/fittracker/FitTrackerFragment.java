package com.example.android.fittracker;


import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.DynamicLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fittracker.data.FittrackerTableHandler;
import com.example.android.fittracker.data.SharedPreference;

import java.text.DecimalFormat;
import java.util.Date;

import static com.example.android.fittracker.EditTrackerFragment.getNumberFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class FitTrackerFragment extends Fragment {
    private static final String TAG = FitTrackerFragment.class.getSimpleName();
    private String execiseType = "walking";

    private Uri addressUri;

    Context mContext;
    Intent stepService;
    BroadcastReceiver receiver;

    boolean flag = true;
    String serviceData;
    TextView countText, carlText, distanceText, timeText, disunitText;
    LinearLayout fitLayout;
    Button startButton, saveButton;
    RadioGroup mRadioGroup;
    RadioButton mWalking;
    Double mCalorie = 0.0;
    Double mDistance = 0.0;
    Double mDuration = 0.0;
    long starttime = 0;
    String savedUnit = "Metric";
    View view;
    //   FloatingActionButton fab;

    private static double METRIC_RUNNING_FACTOR = 1.02784823;   //Km
    private static double IMPERIAL_RUNNING_FACTOR = 0.75031498; //Miles

    private static double METRIC_WALKING_FACTOR = 0.708;
    private static double IMPERIAL_WALKING_FACTOR = 0.517;

    public FitTrackerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stepService = new Intent(getActivity(), StepService.class);
        receiver = new StepReceiver();
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fit_tracker, container, false);

        countText = (TextView) view.findViewById(R.id.display_step);
        carlText = (TextView) view.findViewById(R.id.tv_calorie);
        distanceText = (TextView) view.findViewById(R.id.tv_distance);
        timeText = (TextView) view.findViewById(R.id.tv_time);
        disunitText = (TextView) view.findViewById(R.id.tv_unit_distance);
        fitLayout =view.findViewById(R.id.fit_linearlayout);
        saveButton = view.findViewById(R.id.save_button);
        mRadioGroup = view.findViewById(R.id.radiogroup_fit);
        mWalking = view.findViewById(R.id.radio_setwalking);
        mWalking.setChecked(true);

        savedUnit = readValueFromSharedPreferences(mContext, SharedPreference.SP_KEY_UNIT);
        Boolean isMetric = savedUnit.equalsIgnoreCase("Metric");
        if(isMetric){
            disunitText.setText("Km");
        }
        else{
            disunitText.setText("Mile(s)");
        }

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i(TAG, "SangaeLee(147948186) onCheckedChanged");
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                Toast.makeText(getActivity(), rb.getText(), Toast.LENGTH_SHORT).show();
                execiseType = rb.getText().toString();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saveData();
                initView();
                saveButton.setVisibility(view.INVISIBLE);
            }
        });

        startButton =(Button) view.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag == true) {
                    try {

                        IntentFilter mainFilter = new IntentFilter("com.example.android.fittracker");

                        getActivity().registerReceiver(receiver, mainFilter);
                        getActivity().startService(stepService);
                        starttime = System.currentTimeMillis();

                    } catch (Exception e) {

                    }
                }
                else {
                    try {

                        getActivity().unregisterReceiver(receiver);

                        getActivity().stopService(stepService);
                        saveData();
                        initView();
                        //saveButton.setVisibility(view.VISIBLE);

                    } catch (Exception e) {
                        // TODO: handle exception

                    }

                }
                flag = !flag;
                if(flag == true) {
                    startButton.setText("Start");
                }
                else{
                    startButton.setText("Stop");
                }
            }
        });
        if(flag == true) {
            startButton.setText("Start");
        }
        return view;
    }


    class StepReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Broadcast onReceiver");
            serviceData = intent.getStringExtra("stepService");
            Log.i(TAG, "SangaeLee receiverData="+serviceData);
            countText.setText(serviceData);

            long millis = System.currentTimeMillis() - starttime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds     = seconds % 60;
            mDuration = Double.valueOf(minutes +"."+seconds);
            updateParameter(serviceData);

            DecimalFormat numberFormat = new DecimalFormat("0.0");
            String str = numberFormat.format(mCalorie);
            carlText.setText(str);

            DecimalFormat number2Format = new DecimalFormat("0.0");
            String str2 = numberFormat.format(mDistance);
            distanceText.setText(str2);


            timeText.setText(String.format("%d:%02d", minutes, seconds));

        }
    }

    String readValueFromSharedPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference.SP_FILE_NAME, Context.MODE_PRIVATE);
        String ret = sharedPreferences.getString(key, "");
        return ret;
    }

    void initView(){
        countText.setText("");
        carlText.setText("");
        distanceText.setText("");
        timeText.setText("");
        mCalorie = 0.0;
        mDistance = 0.0;
        mDuration = 0.0;
        starttime = 0;

    }

    void updateParameter(String stepData) {
        String saveUnit = readValueFromSharedPreferences(mContext, SharedPreference.SP_KEY_UNIT);
        Boolean isMetric = saveUnit.equalsIgnoreCase("Metric");
       // String savedStepsize = readValueFromSharedPreferences(mContext, SharedPreference.SP_KEY_STEPSIZE);
       // int stepSize = Integer.valueOf(savedStepsize);
        int stepSize = 20;
        String savedWeight = readValueFromSharedPreferences(mContext, SharedPreference.SP_KEY_WEIGHT);
        int steps = Integer.valueOf(stepData);

        if(savedWeight != null) {
            if (execiseType.equalsIgnoreCase("walking")) {
                if (saveUnit.equalsIgnoreCase("Metric")) {
                    mCalorie = 0.03 * 2.2 * Double.valueOf(savedWeight) * mDuration * mDistance;
                } else {
                    mCalorie = 0.03 * Double.valueOf(savedWeight) * 0.62 * mDuration * mDistance;
                }
            } else if (execiseType.equalsIgnoreCase("running")) {
                if (saveUnit.equalsIgnoreCase("Metric")) {
                    mCalorie = 0.082 * 2.2 * Double.valueOf(savedWeight) * mDuration * mDistance;
                } else {
                    mCalorie = 0.082 * Double.valueOf(savedWeight) * 0.62 * mDuration * mDistance;
                }
            }
        }


        if (isMetric) {
            mDistance += stepSize * steps/ 100000.0; // centimeters/kilometer
        }
        else {
            mDistance += stepSize * steps / 63360.0; // inches/mile
        }

    }

    private void saveData() {
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);

        String step = countText.getText().toString();
        String calorie = carlText.getText().toString();
        String distance = distanceText.getText().toString();
        String time = timeText.getText().toString();

//To Save SQLite
        ContentValues values = new ContentValues();
        values.put(FittrackerTableHandler.Date, thisDate);
        values.put(FittrackerTableHandler.Type, execiseType);
        values.put(FittrackerTableHandler.Calorie, calorie);
        values.put(FittrackerTableHandler.Distance, distance);
        values.put(FittrackerTableHandler.Duration, mDuration);


        if (addressUri == null) {
            addressUri = getContext().getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
        }

    }


}
