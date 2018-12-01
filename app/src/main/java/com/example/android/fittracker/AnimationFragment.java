package com.example.android.fittracker;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnimationFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = AboutFragment.class.getSimpleName();
    ImageView imgView;
    boolean isAnimation = false;
    public static AnimationFragment newInstant (int textIdToPassToFragment, String str, String data) {
        Log.i(TAG, "SangaeLee(147948186) newInstance");
        AnimationFragment m = new AnimationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("help_text", textIdToPassToFragment);
        bundle.putString("animation", str);
        bundle.putString("data_text", data);
        m.setArguments(bundle);

        return m;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(TAG, "SangaeLee(147948186) onCreateView");
        View view;
        if(getArguments().getString("animation").equalsIgnoreCase("yes")) {
            view = inflater.inflate(R.layout.fragment_animation, container, false);
            TextView textInDialog = (TextView) view.findViewById(R.id.helptextid);
            textInDialog.setText(getArguments().getInt("help_text"));
            imgView = (ImageView) view.findViewById(R.id.picture);
            initAnimation();
        }
        else {
            view = inflater.inflate(R.layout.fragment_about,container,false);
            TextView textInDialog =  (TextView)view.findViewById(R.id.helptextid);
            TextView detailInDialog =  (TextView)view.findViewById(R.id.detailtextid);
            int rInt = getArguments().getInt("help_text");
            textInDialog.setText(rInt);
            String rData = getArguments().getString("data_text");
            detailInDialog.setText(rData);
            Button button = (Button)view.findViewById(R.id.close_but_id);
            button.setOnClickListener(this);

        }
        return view;


    }

    void initAnimation() {

        imgView.setBackgroundResource(R.drawable.frame_animation_old2);
        AnimationDrawable frameAnimation = (AnimationDrawable)imgView.getBackground();
        frameAnimation.start();

    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
