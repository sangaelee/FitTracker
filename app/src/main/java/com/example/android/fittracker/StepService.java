package com.example.android.fittracker;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import com.example.android.fittracker.data.StepModel;

public class StepService extends Service implements SensorEventListener{
    private static final String TAG = StepService.class.getSimpleName();
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private StepModel sModel;
    int count = 0;
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;

    private float x, y, z;
    private static final int SHAKE_THRESHOLD = 10;

    public StepService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sModel = new StepModel();
        sModel.Step = 0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "onStartCommand");
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        } // end of if

        return START_STICKY;
    } // end of onStartCommand

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestory");
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            sModel.Step = 0;
        } // end of if
    } // end of onDestroy

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);

            if (gabOfTime > 100) { //  gap of time of step count
                Log.i(TAG, "onSensorChanged FIRST_IF_IN");
                lastTime = currentTime;

                x = event.values[0];
                y = event.values[1];
                z = event.values[2];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Log.i(TAG,"onSensorChanged_IFSECOND_IF_IN");
                    Intent myFilteredResponse = new Intent("com.example.android.fittracker");

                    sModel.Step = count++;

                    String msg = sModel.Step / 2 + "";
                    myFilteredResponse.putExtra("stepService", msg);

                    sendBroadcast(myFilteredResponse);
                } // end of if

                lastX = event.values[0];
                lastY = event.values[1];
                lastZ = event.values[2];
            } // end of if
        } // end of if

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
