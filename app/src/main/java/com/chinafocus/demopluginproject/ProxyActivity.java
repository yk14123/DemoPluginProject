package com.chinafocus.demopluginproject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.chinafocus.stander.IActivityPlugin;

import java.lang.reflect.Constructor;

/**
 * @author
 * @date 2019/11/22
 * description：
 */
public class ProxyActivity extends Activity {

    private Intent mIntentNew;

    // 必须重写该方法，因为加载资源文件，是从插件包里面去寻找resId
    @Override
    public Resources getResources() {
//        return super.getResources();
        return PluginManager.getInstance(this).getResourcesPlugin();
    }

    @Override
    public ClassLoader getClassLoader() {
//        return super.getClassLoader();
        return PluginManager.getInstance(this).getDexClassLoader();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String className = getIntent().getStringExtra("clazzName");
        try {
//            Object clazz = getClassLoader().loadClass(clazzName).newInstance();
            // 通过反射构造方法的形式，初始化对象。调用对象的方法
            Class<?> loadClass = getClassLoader().loadClass(className);
            Constructor<?> constructor = loadClass.getConstructor(new Class[]{});
            Object newInstance = constructor.newInstance(new Object[]{});
//            IActivityPlugin IActivityPlugin = (IActivityPlugin) clazz;
            IActivityPlugin iActivityPlugin = (IActivityPlugin) newInstance;

            // 把宿主的context注入给插件
            iActivityPlugin.inject(this);
            // 加载插件的activity
            // 其实本质，还是调用ProxyActivity，只是这里内部调用了接口的实现方法
            iActivityPlugin.onCreate(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        // 这里的clazzPluginName，是插件内部跳转的全类名
        String className = intent.getStringExtra("clazzName");
        Intent intentNew = new Intent(this, ProxyActivity.class);
        // 传递给ProxyActivity内部onCreate，以便调用全类接口方法
        intentNew.putExtra("clazzName", className);
        super.startActivity(intentNew);
    }

    @Override
    public ComponentName startService(Intent service) {
        // 跳转插件service
        String className = service.getStringExtra("clazzName");
        mIntentNew = new Intent(this, ProxyService.class);
        // 传递给ProxyActivity内部onCreate，以便调用全类接口方法
        mIntentNew.putExtra("clazzName", className);
        return super.startService(mIntentNew);
    }

    @Override
    public boolean stopService(Intent name) {
        // 这里会有问题！一旦结束服务，所有插件里面的伪服务调用会全部全局停止！
        return super.stopService(mIntentNew);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        String receiverName = receiver.getClass().getName();
        return super.registerReceiver(new ProxyReceiver(receiverName), filter);
    }

    @Override
    public void sendBroadcast(Intent intent) {
        String action = intent.getAction();
        super.sendBroadcast(intent);
    }

}
