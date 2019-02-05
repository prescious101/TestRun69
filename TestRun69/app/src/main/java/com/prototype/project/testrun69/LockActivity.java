package com.prototype.project.testrun69;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.prototype.project.testrun69.Service.AppService;
import com.prototype.project.testrun69.Service.BackgroundService;

public class LockActivity extends AppCompatActivity {

    //Service
    Intent mServiceIntent;
    private AppService appService;
    Context ctx;

    public Context getCtx() {
        return ctx;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockactivity);

        appService = new AppService(getCtx());
        mServiceIntent = new Intent(getCtx(), BackgroundService.class);
        mServiceIntent.putExtra("inputExtra", "HelloWorld");

    }
}
