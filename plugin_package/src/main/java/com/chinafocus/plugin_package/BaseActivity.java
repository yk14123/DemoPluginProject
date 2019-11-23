package com.chinafocus.plugin_package;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.chinafocus.stander.IActivityPlugin;

/**
 * @author
 * @date 2019/11/22
 * description：
 */
public class BaseActivity extends AppCompatActivity implements IActivityPlugin {

    protected Activity mAppActivity;

    @Override
    public void inject(Activity appActivity) {
        mAppActivity = appActivity;
    }

    @Override
    public void setContentView(int layoutResID) {
        mAppActivity.setContentView(layoutResID);
    }

    @Override
    public void startActivity(Intent intent) {
        Intent intentNew = new Intent();
        intentNew.putExtra("clazzName", intent.getComponent().getClassName());
        mAppActivity.startActivity(intentNew);
    }

    @Override
    public ComponentName startService(Intent service) {
//        return super.startService(service);
        Intent intentNew = new Intent();
        intentNew.putExtra("clazzName", service.getComponent().getClassName());
        return mAppActivity.startService(intentNew);
    }

    @Override
    public boolean stopService(Intent name) {
        return mAppActivity.stopService(name);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return mAppActivity.registerReceiver(receiver, filter);
    }

    @Override
    public void sendBroadcast(Intent intent) {
        mAppActivity.sendBroadcast(intent);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return mAppActivity.findViewById(id);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {

    }
}
