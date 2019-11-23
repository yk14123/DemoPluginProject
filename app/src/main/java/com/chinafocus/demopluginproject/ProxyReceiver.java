package com.chinafocus.demopluginproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chinafocus.stander.IReceiverPlugin;

/**
 * @author
 * @date 2019/11/23
 * description：
 */
public class ProxyReceiver extends BroadcastReceiver {

    private String mReceiverName;

    public ProxyReceiver(String receiverName) {
        mReceiverName = receiverName;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Object o = PluginManager.getInstance(context).getDexClassLoader().loadClass(mReceiverName).newInstance();
            IReceiverPlugin iReceiverPlugin = (IReceiverPlugin) o;
            iReceiverPlugin.onReceive(context, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
