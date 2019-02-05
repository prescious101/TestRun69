package com.prototype.project.testrun69.Service;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.prototype.project.testrun69.LockActivity;
import com.prototype.project.testrun69.Model.ChildData;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class AppService extends Service {

    private static final String TAG = "AppService";
    public int counter = 0;
    private ChildData childData;
    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    public AppService(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public AppService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String passBlck = intent.getStringExtra("block");
        startTimer(passBlck);

        return START_STICKY;
    }

//    public void stoptimertask() {
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//    }

    public void getTopActivityFromLolipopOnwards(String toBlock) {
        String topPackageName = null;
        PackageManager pm = getPackageManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            }
            long time = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            List<UsageStats> stats = null;
            if (mUsageStatsManager != null) {
                stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 5, time);
            }
            // Sort the stats by the last time used
            if (stats != null) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (!mySortedMap.isEmpty()) {
                    topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    if (topPackageName != null) {
                        topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                        childData.setChildPkgApps(topPackageName);
                        Log.e("TopPackage Name", topPackageName);
                        {
                            Log.d(TAG, "getTopActivityFromLolipopOnwards: " + childData);
                        }
                        if (toBlock.contains(topPackageName)) {
                            Toast.makeText(this, "App BlockApp SARRY", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AppService.this, LockActivity.class);
                            intent.putExtra("Block", topPackageName);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }
            }
        }
    }

    public void startTimer(String block) {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();
        getTopActivityFromLolipopOnwards(block);
        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + (counter++));
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent(this, AppRestartReceiver.class);

        sendBroadcast(broadcastIntent);
//        stoptimertask();
    }
}

