package com.chinafocus.plugin_package;

import android.content.Intent;
import android.content.IntentFilter;
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

        findViewById(R.id.bt_plugin_register_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("com.chinafocus.plugin_package.ACTION");
                registerReceiver(new PluginReceiver(), intentFilter);
            }
        });

        findViewById(R.id.bt_plugin_send_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.chinafocus.plugin_package.ACTION");
                sendBroadcast(intent);
            }
        });

        findViewById(R.id.bt_plugin_send_static_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.chinafocus.plugin_package.STATICReceiver");
                sendBroadcast(intent);
            }
        });
    }
}
