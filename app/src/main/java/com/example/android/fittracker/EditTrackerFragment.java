package com.example.android.fittracker;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fittracker.data.FitData;
import com.example.android.fittracker.data.FittrackerTableHandler;
import com.example.android.fittracker.data.SharedPreference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditTrackerFragment extends Fragment {
    private static final String TAG = EditTrackerFragment.class.getSimpleName();

    private Button dateButton, saveButton;
    private TextView mDate, mUnitDistance, mCalorie;
    private RadioGroup mRadioGroup;
    private RadioButton mWalking;
    private TextInputEditText mDistance, mDuration;
    private String execiseType = "walking";

    private long itemId = -1;
    private Uri itemUri;
    private boolean isUpdate;
    private FitData mFitData;
    private ContentResolver contentResolver;
    public static final String ARG_ID = "item_id";

    public EditTrackerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_tracker, container, false);
        dateButton =(Button) view.findViewById(R.id.btn);
        saveButton =(Button) view.findViewById(R.id.btn_save);
        mDate = view.findViewById(R.id.tv_date);
        mUnitDistance = view.findViewById(R.id.unit_distance);
        mDistance = view.findViewById(R.id.edit_distance);
        mDuration = view.findViewById(R.id.edit_duration);
        mCalorie = view.findViewById(R.id.tv_calorie);
        mRadioGroup = view.findViewById(R.id.radiogroup);
        mWalking = view.findViewById(R.id.rd_walking);
        mWalking.setChecked(true);


        Bundle arguments = getArguments();
        itemUri = (bundle == null) ? null : (Uri) bundle.getParcelable(MyContentProvider.CONTENT_ITEM_TYPE);



        if (arguments != null && arguments.containsKey(ARG_ID)) {
            itemUri = arguments.getParcelable(MyContentProvider.CONTENT_ITEM_TYPE);
            //itemId = getArguments().getLong(ARG_ID);
            //itemUri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + itemId);

            fillData(itemUri);
            isUpdate = true;
        } else {
            //fillData();
            isUpdate = false;
        }

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "Date Picker");
            }
        });

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
            @Override
            public void onClick(View v) {
                //Show animation DialogFragment
                saveState();

            }
        });

        if(isUpdate != true) {
            String current_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            mDate.setText(current_date);
        }
        mDistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEditText(s);
            }
        });

        mDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(validateEditText(s) == true) {
                    calculateCalorie();
                }

            }
        });


        return view;
    }

    private boolean validateEditText(Editable s) {
        Toast.makeText(getActivity(), "textchanged", Toast.LENGTH_SHORT).show();
        String valueDistance = mDistance.getText().toString();
        String valueDuration = mDuration.getText().toString();
        if(TextUtils.isEmpty(valueDistance) || TextUtils.isEmpty(valueDuration)){
            return false;
        }
        return true;

    }

    private void calculateCalorie() {
        double calorie = 0;
        String value = "";
        String savedWeight = readValueFromSharedPreferences(getContext(), SharedPreference.SP_KEY_WEIGHT);
        String saveUnit = readValueFromSharedPreferences(getContext(), SharedPreference.SP_KEY_UNIT);
        String valueDistance = mDistance.getText().toString();
        String valueDuration = mDuration.getText().toString();
        if(savedWeight != null) {
            if (execiseType.equalsIgnoreCase("walking")) {
                if (saveUnit.equalsIgnoreCase("Metric")) {
                    calorie = 0.03 * 2.2 * Double.valueOf(savedWeight) * Integer.valueOf(valueDuration) * Integer.valueOf(valueDistance);
                } else {
                    calorie = 0.03 * Double.valueOf(savedWeight) * 0.62 * Integer.valueOf(valueDuration) * Integer.valueOf(valueDistance);
                }
            } else if (execiseType.equalsIgnoreCase("running")) {
                if (saveUnit.equalsIgnoreCase("Metric")) {
                    calorie = 0.082 * 2.2 * Double.valueOf(savedWeight) * Integer.valueOf(valueDuration) * Integer.valueOf(valueDistance);
                } else {
                    calorie = 0.082 * Double.valueOf(savedWeight) * 0.62 * Integer.valueOf(valueDuration) * Integer.valueOf(valueDistance);
                }
            }
            value = String.valueOf(getNumberFormat(calorie));
        }

        mCalorie.setText(value);
    }
    private void fillData(Uri uri) {
        Log.i(TAG, "SangaeLee(147948186) fillData");
        String[] projection = {
                FittrackerTableHandler.FittrackerID,
                FittrackerTableHandler.Date,
                FittrackerTableHandler.Type,
                FittrackerTableHandler.Distance,
                FittrackerTableHandler.Duration,
                FittrackerTableHandler.Calorie
                };

        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            Log.i(TAG, "Sangae filldata....id="+cursor.getLong(cursor.getColumnIndex(FittrackerTableHandler.FittrackerID)));
            Log.i(TAG, "Sangae filldata....distance="+cursor.getString(cursor.getColumnIndexOrThrow(FittrackerTableHandler.Distance)));

            //for radio setting

            String distance = cursor.getString(cursor.getColumnIndex(FittrackerTableHandler.Distance));
            String duration = cursor.getString(cursor.getColumnIndex(FittrackerTableHandler.Duration));
            String date = cursor.getString(cursor.getColumnIndex(FittrackerTableHandler.Date));
            String calorie = cursor.getString(cursor.getColumnIndex(FittrackerTableHandler.Calorie));
            mDistance.setText(distance);
            mDuration.setText(duration);
            mCalorie.setText(calorie);
            mDate.setText(date);

            // Always close the cursor
            cursor.close();
        }
    }

    private void saveState() {
        Log.i(TAG, "SangaeLee(147948186) saveState");
        String date = mDate.getText().toString();
        String distance = mDistance.getText().toString();
        String duration = mDuration.getText().toString();
        String calorie = mCalorie.getText().toString();

        if(TextUtils.isEmpty(distance) || TextUtils.isEmpty(duration)){
            Toast.makeText(getActivity(), "empty ...", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(FittrackerTableHandler.Date, date);
        values.put(FittrackerTableHandler.Type, execiseType);
        values.put(FittrackerTableHandler.Distance, distance);
        values.put(FittrackerTableHandler.Duration, duration);
        values.put(FittrackerTableHandler.Calorie, calorie );

        if (isUpdate == true) {
            getContext().getContentResolver().update(itemUri, values, null, null);
        }
        else{
            itemUri = getContext().getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
        }
        Toast.makeText(getActivity(), "Saving ...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPause() {
        Log.i(TAG, "SangaeLee(147948186) onPause");
        super.onPause();
       // saveState();
    }

    private String readValueFromSharedPreferences(Context context, String key)
    {
        // Get SharedPreferences object, the shared preferences file name is this activity class name.
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference.SP_FILE_NAME, Context.MODE_PRIVATE);
        String ret = sharedPreferences.getString(key, "");
        return ret;
    }

    public static String getNumberFormat(double number) {
        NumberFormat formatter;
        formatter = new DecimalFormat("0.0");
        return formatter.format(number);
    }
}
