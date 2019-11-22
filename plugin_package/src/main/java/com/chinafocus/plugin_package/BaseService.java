package com.chinafocus.plugin_package;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.chinafocus.stander.IServicePlugin;

/**
 * @author
 * @date 2019/11/22
 * description：
 */
public class BaseService extends Service implements IServicePlugin {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Service mAppService;

    @Override
    public void inject(Service appService) {
        mAppService = appService;
    }

    @Override
    public void onCreate() {
//        super.onCreate();
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return -1;
    }

    @Override
    public void onDestroy() {
//        super.onDestroy();
    }
}
