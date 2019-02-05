package com.prototype.project.testrun69;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prototype.project.testrun69.Model.AppData;
import com.prototype.project.testrun69.Model.ChildData;
import com.prototype.project.testrun69.Service.AppService;
import com.prototype.project.testrun69.Service.BackgroundService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Context mContext;

    private ArrayList<AppData> myApps = new ArrayList<AppData>();
    private ArrayList<ChildData> myApps1 = new ArrayList<ChildData>();

    private EditText editTextChildName;
    private Button btnSubmit;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference childRef = db.collection("Child");

    //Service
    Intent mServiceIntent;
    private AppService appService;
    Context ctx;

    public Context getCtx() {
        return ctx;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this;
        editTextChildName = (EditText) findViewById(R.id.childNameEdt);
        btnSubmit = (Button) findViewById(R.id.submitBtn);
        getInstalledApps();


        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            String manufacturer = "xiaomi";
            if (manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
                //this will open auto start screen where user can enable permission for your app
                Intent intent1 = new Intent();
                intent1.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                startActivity(intent1);
            }
            SharedPreferences prefs1 = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs1.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }

        if (Build.VERSION.SDK_INT >= 21) {
            Context context = getApplicationContext();
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 4000 * 10, time);

            if (stats == null || stats.isEmpty()) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }


        appService = new AppService(getCtx());
        mServiceIntent = new Intent(getCtx(), BackgroundService.class);
        mServiceIntent.putExtra("inputExtra", "HelloWorld");
        if (!isMyServiceRunning(appService.getClass())) {
            ContextCompat.startForegroundService(this, mServiceIntent);

        }
    }

    public void saveData(View v) {
        String childName = editTextChildName.getText().toString();
        String deviceName = android.os.Build.MODEL;
        List<AppData> appInfo;
        appInfo = this.getApps();

        ChildData childData = new ChildData(childName, deviceName);
        db.collection("Child").document(childName).set(childData);

        Map<String, Object> oof = new HashMap<>();
        for (int i = 0; i < appInfo.size(); i++) {
            oof.put("childName", childName);
            oof.put("childDeviceName", deviceName);
            oof.put("childAppName", appInfo.get(i).getAppName());
            oof.put("childPkgApps", appInfo.get(i).getPackageName());
            db.collection(childName).document(appInfo.get(i).getAppName()).set(oof);
        }
        //              Subcollection code
        //            childRef.document(name).collection(name + " Apps").document(appInfo.get(i).getAppName()).set(oof);
    }

    public ArrayList<AppData> getApps() {
        getInstalledApps();
        return myApps;
    }

    private void getInstalledApps() {
        List<ApplicationInfo> packages = getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0) {
                Log.d("System App", "App is System define");
            } else {
                AppData newApp = new AppData();
                newApp.setAppName(this.getApplicationLabelByPackageName(packageInfo.packageName));
                newApp.setPackageName(packageInfo.packageName);
                myApps.add(newApp);
            }
        }
        Collections.sort(myApps, new Comparator<AppData>() {
            @Override
            public int compare(AppData s1, AppData s2) {
                return s1.getAppName().compareToIgnoreCase(s2.getAppName());
            }
        });
    }

    // Custom method to get application label by package name
    private String getApplicationLabelByPackageName(String packageName) {
        PackageManager packageManager = this.getPackageManager();
        ApplicationInfo applicationInfo;
        String label = "Unknown";
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                label = (String) packageManager.getApplicationLabel(applicationInfo);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return label;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;

    }

    @Override
    protected void onStart() {
        super.onStart();
        db.collection("Block").document("fred").addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                    return;
                }
                if (documentSnapshot.exists()) {
                    String blockApp = documentSnapshot.getString("childPkgName");
                    Intent mIntent = new Intent("com.prototype.project.testrun69.AppService");
                    mIntent.putExtra("block", blockApp);
                    getApplication().startService(mIntent);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MAINACT", "onDestroy!");
        stopService(mServiceIntent);
    }

}

