package com.chinafocus.plugin_package;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.chinafocus.stander.IReceiverPlugin;

/**
 * @author
 * @date 2019/11/23
 * description：
 */
public class PluginReceiver extends BroadcastReceiver implements IReceiverPlugin {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "我是插件里面的广播", Toast.LENGTH_SHORT).show();
    }
}
