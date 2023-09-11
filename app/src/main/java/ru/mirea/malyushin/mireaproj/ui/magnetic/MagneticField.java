package ru.mirea.malyushin.mireaproj.ui.magnetic;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mirea.malyushin.mireaproj.R;
import ru.mirea.malyushin.mireaproj.databinding.FragmentFashionBinding;
import ru.mirea.malyushin.mireaproj.databinding.FragmentMagneticFieldBinding;
import ru.mirea.malyushin.mireaproj.ui.fashion.FashionViewModel;

public class MagneticField extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor magneticSensor;
    private FragmentMagneticFieldBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MagneticViewModel magneticViewModel = new ViewModelProvider(this).get(MagneticViewModel.class);
        binding = FragmentMagneticFieldBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (magneticSensor != null) {
            sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (magneticSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float valuePitch = event.values[1];
            binding.text1.setText("");
            if (valuePitch < 2) {
                binding.text1.setText("Горизонтальный экран");
            } else {
                binding.text1.setText("Книжный экран");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
