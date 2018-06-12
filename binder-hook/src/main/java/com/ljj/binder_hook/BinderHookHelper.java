package com.ljj.binder_hook;

import android.os.IBinder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class BinderHookHelper {

    public static void hookClipboardService() throws Exception {
        String CLIPBOAED_SERVICE = "clipboard";

        //ServiceManager是隐藏类，所以通过反射获得ServiceManager.getService("clipboard");
        Class<?> serviceManager = Class.forName("android.os.ServiceManager");
        Method getService = serviceManager.getDeclaredMethod("getService", String.class);

        // ServiceManager里面管理的原始的Clipboard Binder对象
        // 一般来说这是一个Binder代理对象
        IBinder rawBinder = (IBinder) getService.invoke(null, CLIPBOAED_SERVICE);

        IBinder hookedBinder = (IBinder) Proxy.newProxyInstance(serviceManager.getClassLoader(), new Class<?>[]{IBinder.class}, new BinderProxyHookHandler(rawBinder));

        Field sCacheField = serviceManager.getDeclaredField("sCache");
        sCacheField.setAccessible(true);
        Map<String, IBinder> sCache = (Map<String, IBinder>) sCacheField.get(null);
        sCache.put(CLIPBOAED_SERVICE, hookedBinder);
    }
}
