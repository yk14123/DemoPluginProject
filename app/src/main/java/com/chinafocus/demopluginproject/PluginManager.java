package com.chinafocus.demopluginproject;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * @author
 * @date 2019/11/22
 * description：
 */
public class PluginManager {

    private Resources mResourcesPlugin;
    private DexClassLoader mDexClassLoader;

    private PluginManager() {
    }

    private Context mContext;

    private PluginManager(Context appActivity) {
        mContext = appActivity;
    }

    private static PluginManager sPluginManager;

    public static PluginManager getInstance(Context appActivity) {
        if (sPluginManager == null) {
            synchronized (PluginManager.class) {
                if (sPluginManager == null) {
                    sPluginManager = new PluginManager(appActivity);
                }
            }
        }
        return sPluginManager;
    }

    public DexClassLoader getDexClassLoader() {
        return mDexClassLoader;
    }

    public Resources getResourcesPlugin() {
        return mResourcesPlugin;
    }

    public void loadPlugin() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "TestPlugin"
                + File.separator
                + "p.apk");

        Log.e("PluginManager", file.getAbsolutePath());

        String cachePath = mContext.getDir("pDir", Context.MODE_PRIVATE).getAbsolutePath();

        if (!file.exists()) return;

        // 加载插件里面的activity
        mDexClassLoader = new DexClassLoader(file.getAbsolutePath(), cachePath, null, mContext.getClassLoader());

        // 加载插件里面的layout
        try {
            // 资源文件管理器指向插件apk包
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = assetManager.getClass().getMethod("addAssetPath", String.class);
            method.invoke(assetManager, file.getAbsolutePath());

            Resources resources = mContext.getResources();
            // new一个新的资源文件加载指向新的apk
            mResourcesPlugin = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
