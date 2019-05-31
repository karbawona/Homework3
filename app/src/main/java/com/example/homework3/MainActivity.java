package com.example.homework3;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    static public SensorManager mSensorManager;
    static Sensor sensorLight;

    static String[] listOfComments = new String[] {
            "It is certain.", "It is decidedly so.", "Without a doubt.", "Yes - definitely.",
            "You may rely on it.", "As I see it, yes.","Most likely.","Outlook good.",
            " Yes.","Signs point to yes.","Reply hazy, try again.","Ask again later.",
            " Better not tell you now.","Cannot predict now."," Concentrate and ask again.",
            "Don't count on it.","My reply is no."," My sources say no.","Outlook not so good.","Very doubtful."};

    private long lastUpdate = -1;
    private TextView answerBall;
    private ImageView imageBall;
    private int answerNumber = -1;
    private long tempanswerNumber = -1;
    private long[] arrayOfTime = new long[10000000];
    private int counter = 0;
    private int tempCounter = 0;
    private ConstraintLayout mainContainer;
    private Animation shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         shake = AnimationUtils.loadAnimation(this, R.anim.vibration);
        imageBall = findViewById(R.id.imageView);
        answerBall = findViewById(R.id.answer);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        imageBall.setImageResource(R.drawable.hw3ball_front);
        answerBall.setText(" ");

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            sensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        } else {
            System.out.println("No light sensor.");
        }

        mainContainer = findViewById(R.id.sensor_container);
        mainContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (answerNumber > 0) {
                    imageBall.setImageResource(R.drawable.hw3ball_empty);

                }
                if (counter != 0){
                    imageBall.setImageResource(R.drawable.hw3ball_front);
                    answerBall.setText(" ");
                }
            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (lastUpdate == -1) {
            lastUpdate = event.timestamp;
        } else {
            lastUpdate = event.timestamp;
        }

        for (int i = 0; i < event.values.length; i++) {
            if (event.values[i] == 0) {
                arrayOfTime[counter] = lastUpdate;
                tempCounter = counter;
                counter++;
                findViewById(R.id.imageView).startAnimation(shake);
            } else {
                counter = 0;
                tempanswerNumber = (arrayOfTime[tempCounter] - arrayOfTime[0])/1000;
                answerNumber = (int)(tempanswerNumber % 20);
            }
        }

        if (tempanswerNumber > 0) {
            answerBall.setText(listOfComments[answerNumber]);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sensorLight != null) {
            MainActivity.mSensorManager.registerListener(this, sensorLight, 10000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (sensorLight != null) {
            MainActivity.mSensorManager.unregisterListener(this, sensorLight);
        }
    }


}
