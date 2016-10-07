package edu.ucla.derrickchang.weather;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager mSensorManager;
    private Sensor mAmbientTemperature;
    private boolean isC = true;
    private double[] rawTemps = new double[6];
    private double[] showTemps = new double[6];
    private final static String NOT_SUPPORTED_MESSAGE = "n/a";//"!Sorry, sensor not available for this device!";
    private TextView tv, tv1, tv2, tv3, tv4, tv5;


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native double[] c2fJNI(double[] numbers);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_weather);

        tv = (TextView) findViewById(R.id.ambient_temp);
        tv1 = (TextView) findViewById(R.id.textViewMon);
        tv2 = (TextView) findViewById(R.id.textViewTue);
        tv3 = (TextView) findViewById(R.id.textViewWed);
        tv4 = (TextView) findViewById(R.id.textViewThu);
        tv5 = (TextView) findViewById(R.id.textViewFri);

        // Generate testing numbers
        Random rand = new Random();
        for (int i=0; i<rawTemps.length; i++) {
            rawTemps[i]=10.0 + (40.0 - 10.0) * rand.nextDouble();
        }


        showTemps = rawTemps.clone();
        updateUI();

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAmbientTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        // TYPE_AMBIENT_TEMPERATURE won't work
        if (mAmbientTemperature == null) {
            tv.setText(NOT_SUPPORTED_MESSAGE);
        }

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.`
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // Do something with this sensor data.
        rawTemps[0] = event.values[0];
        showTemps = isC? rawTemps.clone() : c2fJNI(rawTemps);

        tv.setText(String.format("%1.0f ",showTemps[0])+ (isC? "\u2103" : "\u2109"));
        Log.d("myTag", "New sensor reading:" + Double.toString(rawTemps[0]));

    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mAmbientTemperature, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.my_toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_convertCF:
                // User chose the "Settings" item, show the app settings UI...
                isC = !isC;
                // Example of a call to a native method
                showTemps = isC? rawTemps.clone() : c2fJNI(rawTemps);
                updateUI();
                return true;

        /*    case R.id.action_convert:
                // User chose the "Common Access" action
                isC = !isC;
                showTemps = isC? rawTemps.clone() : c2fJNI(rawTemps);
                updateUI();
                return true;*/

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void updateUI(){

        if (mAmbientTemperature != null) {
            tv.setText(String.format("%1.0f ",showTemps[0]) + (isC? "\u2103" : "\u2109"));
        }
        tv1.setText(String.format("%1.0f ",showTemps[1]) + (isC? "\u2103" : "\u2109"));
        tv2.setText(String.format("%1.0f ",showTemps[2]) + (isC? "\u2103" : "\u2109"));
        tv3.setText(String.format("%1.0f ",showTemps[3]) + (isC? "\u2103" : "\u2109"));
        tv4.setText(String.format("%1.0f ",showTemps[4]) + (isC? "\u2103" : "\u2109"));
        tv5.setText(String.format("%1.0f ",showTemps[5]) + (isC? "\u2103" : "\u2109"));
    }


}
