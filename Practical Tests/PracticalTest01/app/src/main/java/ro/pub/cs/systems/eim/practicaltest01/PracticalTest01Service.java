package ro.pub.cs.systems.eim.practicaltest01;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class PracticalTest01Service extends Service {
    ProcessingThread processingThread;

    public PracticalTest01Service() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        processingThread.stopThread();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int firstButtonClicks = intent.getIntExtra(Constants.FIRST_BUTTON_CLICKS, -1);
        int secondButtonClicks = intent.getIntExtra(Constants.SECOND_BUTTON_CLICKS, -1);
        if (firstButtonClicks == -1 || secondButtonClicks == -1) {
            Toast.makeText(this, "Error receiving the number of clicks of each button", Toast.LENGTH_LONG).show();
        }

        processingThread = new ProcessingThread(this, firstButtonClicks, secondButtonClicks);
        processingThread.start();
        return START_REDELIVER_INTENT;
    }
}