package com.chinafocus.plugin_package;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author
 * @date 2019/11/23
 * description：
 */
public class StaticReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "我是静态注册的广播", Toast.LENGTH_SHORT).show();
    }
}
