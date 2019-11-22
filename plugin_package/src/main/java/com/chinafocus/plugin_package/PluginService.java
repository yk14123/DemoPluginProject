package com.chinafocus.plugin_package;

import android.content.Intent;
import android.util.Log;

/**
 * @author
 * @date 2019/11/22
 * description：
 */
public class PluginService extends BaseService {
    private static final String TAG = PluginService.class.getSimpleName();
    private boolean isStopService;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "PluginService >>> onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStopService) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "PluginService >>> onStartCommand");
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isStopService = true;
        super.onDestroy();
    }
}
