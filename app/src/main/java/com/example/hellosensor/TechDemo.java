package com.example.hellosensor;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;
import android.media.MediaPlayer;
import android.os.Vibrator;

import androidx.appcompat.app.AppCompatActivity;

public class TechDemo extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private long lastUpdate = 0;
    private static final int SHAKE_LIMIT = 30;
    private float last_x, last_y, last_z;
    private TextView diceTextView;
    private Random random;
    private MediaPlayer mp;
    private Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_demo);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        diceTextView = findViewById(R.id.dice_text_view);
        random = new Random();
        mp = MediaPlayer.create(this, R.raw.dice_sfx);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Button backButton = findViewById(R.id.sensor_values_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TechDemo.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long curTime = System.currentTimeMillis();
        if((curTime-lastUpdate) > 100){
            long diffTime = curTime-lastUpdate;
            lastUpdate = curTime;

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float deltaX = Math.abs(x-last_x);
            float deltaY = Math.abs(x-last_y);
            float deltaZ = Math.abs(x-last_z);

            if (deltaX > SHAKE_LIMIT || deltaY > SHAKE_LIMIT || deltaZ > SHAKE_LIMIT){
                mp.start();
                diceTextView.setText("Result: "+(random.nextInt(20)+1));
                v.vibrate(1000);
            }

            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, mSensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
