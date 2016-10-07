package edu.ucla.derrickchang.weather;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager mSensorManager;
    private Sensor mPressure;
    private boolean isC = true;
    private double[] numbers = new double[6];

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Generate testing numbers

        Random rand = new Random();
        for (int i=0; i<numbers.length; i++) {
            numbers[i]=10.0 + (40.0 - 10.0) * rand.nextDouble();
        }

        // Example of a call to a native method
        TextView tv1 = (TextView) findViewById(R.id.textViewMon);
        tv1.setText("Mon: " + Double.toString(c2fJNI(numbers)[1]));

        TextView tv2 = (TextView) findViewById(R.id.textViewTue);
        tv2.setText("Tue: " + Double.toString(c2fJNI(numbers)[2]));

        TextView tv3 = (TextView) findViewById(R.id.textViewWed);
        tv3.setText("Wed: " + Double.toString(c2fJNI(numbers)[3]));

        TextView tv4 = (TextView) findViewById(R.id.textViewThu);
        tv4.setText("Thu: " + Double.toString(c2fJNI(numbers)[4]));

        TextView tv5 = (TextView) findViewById(R.id.textViewFri);
        tv5.setText("Fri: " + Double.toString(c2fJNI(numbers)[5]));

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float millibars_of_pressure = event.values[0];
        // Do something with this sensor data.
        numbers[0] = millibars_of_pressure;
        TextView tv = (TextView) findViewById(R.id.ambient_temp);

        if (isC) {
            tv.setText("Ambient temperature: " + Double.toString(numbers[0]));
        }
        else {
            tv.setText("Ambient temperature: " + Double.toString(c2fJNI(numbers)[0]));
        }

    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_UI);
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
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_convert:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                isC = !isC;

                TextView tv = (TextView) findViewById(R.id.ambient_temp);
                TextView tv1 = (TextView) findViewById(R.id.textViewMon);
                TextView tv2 = (TextView) findViewById(R.id.textViewTue);
                TextView tv3 = (TextView) findViewById(R.id.textViewWed);
                TextView tv4 = (TextView) findViewById(R.id.textViewThu);
                TextView tv5 = (TextView) findViewById(R.id.textViewFri);


                if (isC) {
                    tv.setText(": " + Double.toString(numbers[0]));
                    tv1.setText("Mon: " + Double.toString(numbers[1]));
                    tv2.setText("Tue: " + Double.toString(numbers[2]));
                    tv3.setText("Wed: " + Double.toString(numbers[3]));
                    tv4.setText("Thu: " + Double.toString(numbers[4]));
                    tv5.setText("Fri: " + Double.toString(numbers[5]));

                }
                else
                {
                    tv.setText(": " + Double.toString(c2fJNI(numbers)[0]));
                    tv1.setText("Mon: " + Double.toString(c2fJNI(numbers)[1]));
                    tv2.setText("Tue: " + Double.toString(c2fJNI(numbers)[2]));
                    tv3.setText("Wed: " + Double.toString(c2fJNI(numbers)[3]));
                    tv4.setText("Thu: " + Double.toString(c2fJNI(numbers)[4]));
                    tv5.setText("Fri: " + Double.toString(c2fJNI(numbers)[5]));
                }

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native double[] c2fJNI(double[] numbers);
}
