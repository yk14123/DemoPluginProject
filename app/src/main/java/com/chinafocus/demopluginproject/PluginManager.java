package com.chinafocus.demopluginproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

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

    // 反射系统源码，来解析apk文件里面的所有信息
    public void parserApkAction() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "TestPlugin"
                + File.separator
                + "p.apk");
        // PackageParser
//        public Package parsePackage(File packageFile, int flags) throws PackageParserException {
//            return parsePackage(packageFile, flags, false /* useCaches */);
//        }
        try {
            // PackageParser解析器，解析插件apk
            Class<?> clazz = Class.forName("android.content.pm.PackageParser");
            Object mPackageParser = clazz.newInstance();
            Method parsePackage = clazz.getDeclaredMethod("parsePackage", File.class, int.class);
            // 解析apk后，得到Package对象
            Object mPackage = parsePackage.invoke(mPackageParser, file, PackageManager.GET_ACTIVITIES);

            // Package对象里面，有很多activitys，services，receivers
            // 通过反射，拿到receivers的集合
            Class<?> packageClazz = Class.forName("android.content.pm.PackageParser$Package");
            Field packageNameField = packageClazz.getField("packageName");
            String packageName = (String) packageNameField.get(mPackage);
            Log.e("MyLog", "packageName >>> " + packageName);

            Field field = packageClazz.getDeclaredField("receivers");
            // 本质就是ArrayList<Activity>
            Object receivers = field.get(mPackage);
            ArrayList arrayList = (ArrayList) receivers;

            // receiver的类型是 ArrayList<Activity>
            // public final static class Activity extends Component<ActivityIntentInfo> implements Parcelable
            // Activity 又继承于 Component
            Class<?> componentClazz = Class.forName("android.content.pm.PackageParser$Component");
            Field intents = componentClazz.getField("intents");
            Field receiverNameField = componentClazz.getField("className");

            Class<?> activityClazz = Class.forName("android.content.pm.PackageParser$Activity");
            Field infoField = activityClazz.getField("info");

            // ArrayList<Activity> 一群广播 这里肯定有广播名
            for (Object mActivity : arrayList) {

                // 获取intent-filter intents == 很多的intent-filter
                // ArrayList<ActivityIntentInfo>
                // ActivityIntentInfo
                Object intentsArray = intents.get(mActivity);
                // receiverName 直接就是全类名！！
                String receiverName = (String) receiverNameField.get(mActivity);
//                Log.e("MyLog", "receiverName >>> " + receiverName);
                // activityInfo.name 和 receiverName 一模一样！
                ActivityInfo activityInfo = (ActivityInfo) infoField.get(mActivity);
                String name = activityInfo.name;
                Log.e("MyLog", "activityInfo.name >>> " + name);
                ArrayList activityIntentInfos = (ArrayList) intentsArray;

                for (int i = 0; i < activityIntentInfos.size(); i++) {
                    IntentFilter intentFilter = (IntentFilter) activityIntentInfos.get(i);
                    String action = intentFilter.getAction(0);
                    Log.e("MyLog", "action >>> " + action);

                    BroadcastReceiver receiver = (BroadcastReceiver) getDexClassLoader().loadClass(name).newInstance();
                    // 在宿主里面动态注册了一个广播
                    mContext.registerReceiver(receiver, intentFilter);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
