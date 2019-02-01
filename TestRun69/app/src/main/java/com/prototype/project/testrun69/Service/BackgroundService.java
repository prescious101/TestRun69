package com.prototype.project.testrun69.Service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.prototype.project.testrun69.MainActivity;
import com.prototype.project.testrun69.R;

import static com.prototype.project.testrun69.App.CHANNEL_ID;

public class BackgroundService extends IntentService {

    private static final String TAG = "ExampleIntentService";
    private PowerManager.WakeLock wakeLock;

    public BackgroundService() {
        super("BackgroundService");
        setIntentRedelivery(true);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");


        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "ExampleApp:Wakelock");
        wakeLock.acquire();
        Log.d(TAG, "Wakelock acquired");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("ExampleIntentService")
                    .setContentText("Running...")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .build();

            startForeground(1, notification);
        }else {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("ExampleIntentService")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);
        }



    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent");

        String input = intent.getStringExtra("inputExtra");

        for (int i = 0; i < 5000; i++) {
            Log.d(TAG, input + " - " + i);
            SystemClock.sleep(1000);
        }




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
//        wakeLock.release();
//        Intent broadcastIntent = new Intent(this, AppRestartReceiver.class);
//        sendBroadcast(broadcastIntent);
        Log.d(TAG, "Wakelock released");
    }

}
