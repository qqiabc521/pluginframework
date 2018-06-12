package com.ljj.dynamic_proxy_hook.hook;

import android.app.Instrumentation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HookHelper {

    public static void attachContext() throws Exception {
        //获得当前ActivityThread对象
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);

        //currentActivityThreadMethod是一个static函数，所以可以直接invoke，不需要带实例参数
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);

        //拿到原始的mInstrumentation
        Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);

        //创建代理对象
        InstrumentionProxy instrumentionProxy = new InstrumentionProxy(mInstrumentation);

        //偷梁换柱
        mInstrumentationField.set(currentActivityThread, instrumentionProxy);

    }
}
