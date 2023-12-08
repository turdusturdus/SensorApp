package com.example.sensorapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView tvSensorName, tvSensorValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        tvSensorName = findViewById(R.id.tvSensorName);
        tvSensorValues = findViewById(R.id.tvSensorValues);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Get sensor type from the intent
        int sensorType = getIntent().getIntExtra("sensorType", -1);
        if (sensorType != -1) {
            sensor = sensorManager.getDefaultSensor(sensorType);

            if (sensor != null) {
                tvSensorName.setText(sensor.getName());
            } else {
                tvSensorName.setText("Brak czujnika");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        StringBuilder currentValue = new StringBuilder();

        for (float value : sensorEvent.values) {
            currentValue.append(value).append(" ");
        }

        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                tvSensorValues.setText(getString(R.string.accelerometer_values, currentValue.toString().trim()));
                break;
            case Sensor.TYPE_GYROSCOPE:
                tvSensorValues.setText(getString(R.string.gyroscope_values, currentValue.toString().trim()));
                break;
            default:
                tvSensorValues.setText(getString(R.string.unknown_sensor, currentValue.toString().trim()));
                break;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle sensor accuracy changes if needed
    }
}
