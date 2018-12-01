package com.example.android.fittracker;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fittracker.data.SharedPreference;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private String RADIO_METRIC = "Metric";
    private String RADIO_IMPERIAL = "Imperial";

    private RadioButton mMetric, mImperial, mRunning, mWaliking;
    private RadioGroup groupUnit, groupExecise;
    private EditText mChallenge, mWeight;
    private String saveUnit = RADIO_METRIC;
    private TextView unitWeight, unitChallenge;
    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mChallenge = view.findViewById(R.id.text_challenge);
        mWeight = view.findViewById(R.id.body_weight);
        groupUnit = view.findViewById(R.id.rg_unit);
        mMetric = view.findViewById(R.id.radio_metric);
        mImperial = view.findViewById(R.id.radio_imperial);
        unitWeight = view.findViewById(R.id.unit_weight);
        unitChallenge = view.findViewById(R.id.unit_challenge);

        initLoginForm(getContext());
        RadioGroup radioGroup1 = (RadioGroup) view.findViewById(R.id.rg_unit);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.radio_metric:
                        saveUnit = RADIO_METRIC;
                        unitWeight.setText("Weight(kg)");
                        break;
                    case R.id.radio_imperial:
                        saveUnit = RADIO_IMPERIAL;
                        unitWeight.setText("Weight(Lb)");
                        break;
                }
            }
        });


        Button saveButton = view.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToSharedPreferences(getContext());
                getActivity().getFragmentManager().popBackStack();

            }
        });

        return view;
    }



    private void initLoginForm(Context context)
    {
        saveUnit = readValueFromSharedPreferences(context, SharedPreference.SP_KEY_UNIT);
        String savedStepsize = readValueFromSharedPreferences(context, SharedPreference.SP_KEY_STEPSIZE);
        String savedWeight = readValueFromSharedPreferences(context, SharedPreference.SP_KEY_WEIGHT);

        if(saveUnit.equalsIgnoreCase(RADIO_METRIC)) {
            mMetric.setChecked(true);
            unitWeight.setText("Weight(kg)");
        }
        else if(saveUnit.equalsIgnoreCase(RADIO_IMPERIAL)) {
            mImperial.setChecked(true);
            unitWeight.setText("Weight(Lb)");
        }


        mChallenge.setText(savedStepsize);
        mWeight.setText(savedWeight);


    }

    // Write data to SharedPreferences object.
    private void writeToSharedPreferences(Context context)
    {
        // Get SharedPreferences object, the shared preferences file name is this activity class name.
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference.SP_FILE_NAME, Context.MODE_PRIVATE);
        String weight = mWeight.getText().toString();
        String stepsize = mChallenge.getText().toString();

        Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreference.SP_KEY_UNIT,saveUnit);
        editor.putString(SharedPreference.SP_KEY_STEPSIZE, stepsize);
        editor.putString(SharedPreference.SP_KEY_WEIGHT, weight);

        editor.apply();
    }

    private String readValueFromSharedPreferences(Context context, String key)
    {
        // Get SharedPreferences object, the shared preferences file name is this activity class name.
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference.SP_FILE_NAME, Context.MODE_PRIVATE);
        String ret = sharedPreferences.getString(key, "");
        return ret;
    }
}
