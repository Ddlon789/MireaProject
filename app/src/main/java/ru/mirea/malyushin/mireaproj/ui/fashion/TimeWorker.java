package ru.mirea.malyushin.mireaproj.ui.fashion;

import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.os.Message;

import java.util.Date;
import java.util.Locale;

public class TimeWorker extends Thread {
    private Handler handler;

    public TimeWorker(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        Message message = Message.obtain();
        message.obj = getCurrentTime();
        handler.sendMessage(message);
    }

    private String getCurrentTime() {
        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedTime = dateFormat.format(currentTime);
        return formattedTime;
    }
}
