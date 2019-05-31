package com.example.homework3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    static final public String SENSOR_TYPE = "sensorType";
    static public SensorManager mSensorManager;
    static Sensor sensorLight; //zamiast SensorList

    static String[] listOfComments = new String[] {
            "It is certain.", "It is decidedly so.", "Without a doubt.", "Yes - definitely.",
            "You may rely on it.", "As I see it, yes.","Most likely.","Outlook good.",
            " Yes.","Signs point to yes.","Reply hazy, try again.","Ask again later.",
            " Better not tell you now.","Cannot predict now."," Concentrate and ask again.",
            "Don't count on it.","My reply is no."," My sources say no.","Outlook not so good.","Very doubtful."};

    private long lastUpdate = -1;
    private TextView answerBall;
    private long answerNumber = -1;
    private long[] arrayOfTime = new long[10000000];
    private int counter = 0;
    private int tempCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        answerBall = findViewById(R.id.answer);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) { // Success!.
            sensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        } else {
            System.out.println("No light sensor.");
        }
//        @Override
//        public void onGlobalLayout(){
//        }
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
            } else {
                counter = 0;
                answerNumber = ((arrayOfTime[tempCounter] - arrayOfTime[0])/1000) % 21 ;
            }
        }

      //  if (counter == 0 && tempCounter == 0) to jest poczatkowa sytuacja, tu moze byc rysnek kuli z '8'
        if (answerNumber > 0) {
            answerBall.setText(listOfComments[(int)answerNumber]);
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


 //   private void handleLightSensor() {
//        if (!animFlag && (sensorValue < 100)) {
//            animFlag = true;
//            moonImgView.setAlpha(0f);
//            sunImgView.setAlpha(1f);
//            lightSensorAnimation(true);
//        } else if (animFlag && (sensorType == Sensor.TYPE_LIGHT && sensorValue >= 100)) {
//            animFlag = false;
//            lightSensorAnimation(false);
//        }
//    }
}
