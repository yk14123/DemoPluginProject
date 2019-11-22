package com.chinafocus.demopluginproject;

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
public class ProxyService extends Service {

    private IServicePlugin mIServicePlugin;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return super.getClassLoader();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String className = intent.getStringExtra("clazzName");
        try {

            Class<?> loadClass = PluginManager.getInstance(this).getDexClassLoader().loadClass(className);
            Object newInstance = loadClass.newInstance();

            mIServicePlugin = (IServicePlugin) newInstance;

            // 把宿主的context注入给插件
            mIServicePlugin.inject(this);
            // 加载插件的Service
            // 其实本质，还是调用ProxyService，只是这里内部调用了接口的实现方法
            mIServicePlugin.onCreate();

            // 模拟先调用onCreate，再调用onStartCommand
            mIServicePlugin.onStartCommand(intent, flags, startId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mIServicePlugin.onDestroy();
        super.onDestroy();
    }
}
