package com.example.android.fittracker;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class BmiFragment extends Fragment {
    private static final String TAG = BmiFragment.class.getSimpleName();
    RadioButton mMetric, mImperial;
    EditText mWeight, mHeight;
    TextView mBmi, mBmiResult, mTvWeight, mTvHeight;
    Button mCalculate;
    AnimationFragment newFragment;

    public BmiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bmi, container, false);
        mTvWeight = view.findViewById(R.id.tv_weight);
        mTvHeight = view.findViewById(R.id.tv_height);
        mMetric = view.findViewById(R.id.radio_metric);
        mMetric.setChecked(true);
        mMetric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvWeight.setText("Weight(kg)");
                mTvHeight.setText("Height(cm)");

            }
        });
        mImperial = view.findViewById(R.id.radio_imperial);
        mImperial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvWeight.setText("Weight(lb)");
                mTvHeight.setText("Height(inch)");

            }
        });
        mWeight = view.findViewById(R.id.weight);
        mHeight = view.findViewById(R.id.height);
        mCalculate = view.findViewById(R.id.calculate);
        mCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double bmiValue = calculateBMI();
                String bmiStatus = getBmiStatus(bmiValue);
                final String display = "BMI : " + String.valueOf(bmiValue) + "\nStatus : " + bmiStatus;
                newFragment = AnimationFragment.newInstant(R.string.save_animation,"yes","");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                newFragment.show(transaction, "Animation");

                final Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        newFragment.dismiss(); //disappear animationDialog

                        newFragment = AnimationFragment.newInstant(R.string.bmi_result, "no", display);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        newFragment.show(transaction, "fragment");

                        t.cancel(); // also just stop the timer thread
                    }
                }, 2000); // after 5 second (or 5000 miliseconds), the task will be active.

            }
        });
        return view;

    }

    private String getBmiStatus(double bmiValue) {
        String result = "";
        if(bmiValue < 18.5) {
            result = "under Weight";
        }
        else if(bmiValue >= 18.5 && bmiValue < 25) {
            result = "healthy Weight";
        }
        else {
            result = "over Weight";
        }
        return result;
        //mBmiResult.setText(result);
    }

    private double calculateBMI() {
        if((mWeight.getText().toString().isEmpty()) || ((mHeight.getText().toString().isEmpty()))){
            Toast.makeText(getActivity(), "input number!!", Toast.LENGTH_SHORT).show();
            return -1;
        }
        double weightValue = Double.valueOf(mWeight.getText().toString());
        double heightValue = Double.valueOf(mHeight.getText().toString());
        double bmi = 0;
        if(mMetric.isChecked()) {
            bmi = weightValue / (heightValue/100 * heightValue/100);
        }
        else {
            bmi = (weightValue * 703) /(heightValue * heightValue);

        }
        String bmiString = getNumberFormat(bmi);

        return Double.valueOf(bmiString);
    }
    public static String getNumberFormat(double number) {
        NumberFormat formatter;
        formatter = new DecimalFormat("0.00");
        return formatter.format(number);
    }

}
