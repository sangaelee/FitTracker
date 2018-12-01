package com.example.android.fittracker;


import android.app.DialogFragment;
        import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TextView;


public class AboutFragment extends DialogFragment implements View.OnClickListener{
    private static final String TAG = AboutFragment.class.getSimpleName();
    public static AboutFragment newInstant (int textIdToPassToFragment){
        Log.i(TAG, "SangaeLee(147948186) newInstance");
        AboutFragment m = new AboutFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("help_text",textIdToPassToFragment);
        m.setArguments(bundle);

        return m;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "SangaeLee(147948186) onCreateView");
        View view = inflater.inflate(R.layout.fragment_about,container,false);
        TextView textInDialog =  (TextView)view.findViewById(R.id.helptextid);
        textInDialog.setText(getArguments().getInt("help_text"));
        Button button = (Button)view.findViewById(R.id.close_but_id);
        button.setOnClickListener(this);

        return view;


    }

    @Override
    public void onClick(View v) {
        dismiss();
    }


}

