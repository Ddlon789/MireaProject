package ru.mirea.malyushin.mireaproj.ui.audio;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;

import ru.mirea.malyushin.mireaproj.databinding.FragmentAudioBinding;

public class AudioFragment extends Fragment {

    private FragmentAudioBinding binding;
    private AudioViewModel mViewModel;
    private static final int REQUEST_CODE_PERMISSION = 200;
    private boolean isWork;

    private long startTime;
    private MediaRecorder mediaRecorder;
    private String recordFilePath;

    private boolean isRecording = false;


    public static AudioFragment newInstance() {
        return new AudioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(AudioViewModel.class);
        binding = FragmentAudioBinding.inflate(inflater, container, false);
        binding.button2.setText("Start");

        recordFilePath = (new File(getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "/audiorecord.3gp")).getAbsolutePath();

        int audioRecordPermissionStatus = ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.RECORD_AUDIO);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.
                WRITE_EXTERNAL_STORAGE);
        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus
                == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            isWork = true;
        }

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(isWork);
                if (isWork) {
                    binding.textView2.setText("Запись идет ...");
                    binding.button2.setText("Stop");
                    startRecording();
                } else {
                    binding.button2.setText("Start");
                    stopRecording();
                }
            }
        });

        return binding.getRoot();
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordFilePath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            startTime = SystemClock.elapsedRealtime();
            isWork = false;
        } catch (IOException e) {
            Log.e(getActivity().getClass().getSimpleName(), "prepare() failed");
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            long endTime = SystemClock.elapsedRealtime();
            long recordingTime = endTime - startTime;
            mediaRecorder.release();
            binding.textView2.setText("время записи: "  + (float) recordingTime/1000 + " sec");
            mediaRecorder = null;
        }
        isWork = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_PERMISSION:
                isWork = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!isWork) getActivity().finish();
    }
}