package com.chinafocus.stander;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author
 * @date 2019/11/22
 * description：
 */
public interface IActivityPlugin {

    void inject(Activity appActivity);

    void onCreate(Bundle savedInstanceState);

    void onDestroy();
}
