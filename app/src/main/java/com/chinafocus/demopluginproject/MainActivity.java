package com.chinafocus.demopluginproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadPlugin(View view) {
        PluginManager.getInstance(this).loadPlugin();
    }

    public void jumpPlugin(View view) {
        // 插件包文件
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "TestPlugin"
                + File.separator
                + "p.apk");

        String path = file.getAbsolutePath();

        PackageManager manager = getPackageManager();
        // 加载插件包里面的activity，PackageInfo信息
        PackageInfo packageArchiveInfo = manager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        ActivityInfo activityInfo = packageArchiveInfo.activities[0];
        // 主要是获取插件apk里面入口类的全类名，以便做伪加载
        String pluginClazzName = activityInfo.name;

        Intent intent = new Intent(this, ProxyActivity.class);
        intent.putExtra("clazzName", pluginClazzName);

        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void loadApk(View view) {
        PluginManager.getInstance(this).parserApkAction();
    }
}
