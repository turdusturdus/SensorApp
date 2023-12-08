package com.example.sensorapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SensorActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private RecyclerView recyclerView;
    private SensorAdapter sensorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if (sensorAdapter == null) {
            sensorAdapter = new SensorAdapter(sensorList, this);
            recyclerView.setAdapter(sensorAdapter);
        } else {
            sensorAdapter.notifyDataSetChanged();
        }

        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sensor_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem sensorsCountItem = menu.findItem(R.id.action_sensors_count);
        if (sensorsCountItem != null) {
            String title = getString(R.string.sensors_count, sensorList.size());
            sensorsCountItem.setTitle(title);
        }
        return true;
    }



    private class SensorViewHolder extends RecyclerView.ViewHolder {
        ImageView sensorIcon;
        TextView sensorName;
        View itemView;

        SensorViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            sensorIcon = itemView.findViewById(R.id.sensor_icon);
            sensorName = itemView.findViewById(R.id.sensor_name);
        }
    }

    private class SensorAdapter extends RecyclerView.Adapter<SensorViewHolder> {
        private List<Sensor> sensors;
        private Context context;
        private int selectedSensorType = -1;

        SensorAdapter(List<Sensor> sensors, Context context) {
            this.sensors = sensors;
            this.context = context;
        }

        @Override
        public SensorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_list_item, parent, false);
            return new SensorViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(SensorViewHolder holder, int position) {
            final Sensor sensor = sensors.get(position);
            holder.sensorName.setText(sensor.getName());
            holder.sensorIcon.setImageResource(android.R.drawable.ic_menu_camera);

            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER || sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                holder.itemView.setBackgroundColor(Color.GRAY);
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }

            holder.itemView.setOnClickListener(view -> {
                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER || sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    Intent intent = new Intent(context, SensorDetailsActivity.class);
                    intent.putExtra("sensorType", sensor.getType());
                    context.startActivity(intent);
                } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    Intent intent = new Intent(context, LocationActivity.class);
                    context.startActivity(intent);
                } else {
                    displaySensorDetails(sensor);
                }
            });
        }


        private void displaySensorDetails(Sensor sensor) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(sensor.getName())
                    .setMessage("Producent: " + sensor.getVendor() +
                            "\nMaksymalny zakres: " + sensor.getMaximumRange())
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public int getItemCount() {
            return sensors.size();
        }
    }

}
