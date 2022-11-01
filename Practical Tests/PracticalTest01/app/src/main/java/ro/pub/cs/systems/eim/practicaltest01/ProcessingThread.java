package ro.pub.cs.systems.eim.practicaltest01;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Random;


public class ProcessingThread extends Thread {
    private Context context;
    private int firstButtonClicks;
    private int secondButtonClicks;
    private Random rand;
    private boolean isRunning;

    public ProcessingThread(Context context, int firstButtonClicks, int secondButtonClicks) {
        this.context = context;
        this.firstButtonClicks = firstButtonClicks;
        this.secondButtonClicks = secondButtonClicks;
    }

    @Override
    public void run() {
        isRunning = true;
        rand = new Random();
        while(isRunning) {
            sendMessage();
            sleep();
        }
    }

    private void sendMessage() {
        Intent intent = new Intent();
        int actionType = rand.nextInt() % 3;
        switch (actionType) {
            case 0:
                intent.setAction(Constants.ACTION_1);
                break;
            case 1:
                intent.setAction(Constants.ACTION_2);
                break;
            case 2:
                intent.setAction(Constants.ACTION_3);
                break;
        }
        double mArith = (double) (firstButtonClicks + secondButtonClicks) / 2.0;
        double mGeom = Math.sqrt((double)(firstButtonClicks * secondButtonClicks));
        intent.putExtra(Constants.DATA, Calendar.getInstance().getTime().toString() + " " + mArith + " " + mGeom);
        context.sendBroadcast(intent);
    }

    private void sleep() {
        try {
            Thread.sleep(Constants.SLEEP_TIME);
        } catch (InterruptedException interruptedException) {
            Log.e(Constants.SERVICE_TAG, interruptedException.getMessage());
            interruptedException.printStackTrace();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
