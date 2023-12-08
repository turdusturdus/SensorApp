package com.example.sensorapp;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sensorAdapter = new SensorAdapter(sensorList);
        recyclerView.setAdapter(sensorAdapter);
    }

    private class SensorViewHolder extends RecyclerView.ViewHolder {
        ImageView sensorIcon;
        TextView sensorName;

        SensorViewHolder(View itemView) {
            super(itemView);
            sensorIcon = itemView.findViewById(R.id.sensor_icon);
            sensorName = itemView.findViewById(R.id.sensor_name);
        }
    }

    private class SensorAdapter extends RecyclerView.Adapter<SensorViewHolder> {
        private List<Sensor> sensors;

        SensorAdapter(List<Sensor> sensors) {
            this.sensors = sensors;
        }

        @Override
        public SensorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_list_item, parent, false);
            return new SensorViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(SensorViewHolder holder, int position) {
            Sensor sensor = sensors.get(position);
            holder.sensorName.setText(sensor.getName());
            holder.sensorIcon.setImageResource(android.R.drawable.ic_menu_camera); // Zastąp 'ic_sensor_default' odpowiednim ID zasobu.
        }

        @Override
        public int getItemCount() {
            return sensors.size();
        }
    }
}
