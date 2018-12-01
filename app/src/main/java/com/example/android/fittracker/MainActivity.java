package com.example.android.fittracker;


import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();


    private static int FRAGMENT_INTRO = 0;
    private static int FRAGMENT_ABOUT = 1;
    private static int FRAGMENT_FITTRACKER = 2;
    private static int FRAGMENT_BMI = 3;
    private static int FRAGMENT_EDIT = 4;
    private static int FRAGMENT_CONTACT =5 ;
    private static int FRAGMENT_HISTORY =6 ;
    private static int FRAGMENT_SETTINGS = 7;
    private static int FRAGMENT_WEATHER = 8;
    private static int FRAGMENT_MAP = 9;
    private static int FRAGMENT_SENSOR = 10;



    Intent stepService;
    BroadcastReceiver receiver;

    boolean flag = true;
    String serviceData;
    TextView countText;
    Button startButton;
    FloatingActionButton fab;

    private static double METRIC_RUNNING_FACTOR = 1.02784823;   //Km
    private static double IMPERIAL_RUNNING_FACTOR = 0.75031498; //Miles

    private static double METRIC_WALKING_FACTOR = 0.708;
    private static double IMPERIAL_WALKING_FACTOR = 0.517;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem(MainActivity.FRAGMENT_SENSOR);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        selectItem(MainActivity.FRAGMENT_INTRO);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            selectItem(MainActivity.FRAGMENT_EDIT);
            return true;
        }
        if (id == R.id.action_exit) {
            selectItem(MainActivity.FRAGMENT_INTRO);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fit_tracker) {
            selectItem(FRAGMENT_FITTRACKER);
        } else if (id == R.id.nav_history) {
            selectItem(FRAGMENT_HISTORY);
        } else if (id == R.id.nav_bmi) {
            selectItem(FRAGMENT_BMI);
        } else if (id == R.id.nav_settings) {
            selectItem(FRAGMENT_SETTINGS);
        } else if (id == R.id.nav_about) {
            selectItem(FRAGMENT_ABOUT);
        } else if (id == R.id.nav_contact) {
            selectItem(FRAGMENT_CONTACT);
        } else if (id == R.id.nav_weather) {
            selectItem(FRAGMENT_WEATHER);
        } else if (id == R.id.nav_map) {
            selectItem(FRAGMENT_MAP);
        }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fab.show();
                fragment = new IntroFragment();
                break;
            case 1:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case 2:
                fab.show();
                fragment = new ListTrackerFragment();
                break;
            case 3:
                fab.show();
                fragment = new BmiFragment();
                break;
            case 4:
                fab.show();
                fragment = new EditTrackerFragment();
                break;
            case 5:
                fragment = new ContactFragment();
                break;
            case 6:
                fab.show();
                fragment = new HistoryFragment();
                break;
            case 7:
                fab.show();
                fragment = new SettingsFragment();
                break;
            case 8:
                fab.hide();
                fragment = new WeatherFragment();
                break;
            case 9:
                fab.show();
                fragment = new MapFragment();
                break;
            case 10:
                fab.hide();
                fragment = new FitTrackerFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    public void createDialog(int textId) {
        Log.i(TAG, "SangaeLee(147948186) createDialog");
        AboutFragment newDialog = AboutFragment.newInstant(textId);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        newDialog.show(transaction,"fragment");

    }

}
