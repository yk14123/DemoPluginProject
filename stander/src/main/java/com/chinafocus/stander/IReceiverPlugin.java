package com.chinafocus.stander;

import android.content.Context;
import android.content.Intent;

/**
 * @author
 * @date 2019/11/23
 * description：
 */
public interface IReceiverPlugin {
    void onReceive(Context context, Intent intent);
}
