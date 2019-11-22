package com.chinafocus.plugin_package;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PluginActivity extends BaseActivity {

    private Intent mServiceIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_activity_main);

        Toast.makeText(mAppActivity, "我是插件", Toast.LENGTH_SHORT).show();

        findViewById(R.id.bt_plugin_jump_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mAppActivity, PluginSecondActivity.class));
            }
        });

        findViewById(R.id.bt_plugin_start_service).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mServiceIntent = new Intent(mAppActivity, PluginService.class);
                startService(mServiceIntent);
            }
        });

        findViewById(R.id.bt_plugin_stop_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(mServiceIntent);
            }
        });
    }
}
