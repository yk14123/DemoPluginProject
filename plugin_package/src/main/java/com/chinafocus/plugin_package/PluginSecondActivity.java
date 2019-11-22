package com.chinafocus.plugin_package;

import android.os.Bundle;
import android.widget.Toast;

public class PluginSecondActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_activity_second);

        Toast.makeText(mAppActivity, "我是插件PluginSecondActivity", Toast.LENGTH_SHORT).show();
    }
}
