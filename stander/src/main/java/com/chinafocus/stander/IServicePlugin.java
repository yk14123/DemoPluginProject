package com.chinafocus.stander;

import android.app.Service;
import android.content.Intent;

/**
 * @author
 * @date 2019/11/22
 * description：
 */
public interface IServicePlugin {

    void inject(Service appService);

    void onCreate();

    int onStartCommand(Intent intent, int flags, int startId);

    void onDestroy();

}
