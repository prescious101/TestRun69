package com.prototype.project.testrun69.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppRestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(AppRestartReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        //context.startService(new Intent(context, AppService.class));
        context.startService(new Intent(context, BackgroundService.class));
    }
}
