package com.ljj.dynamic_proxy_hook.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Method;

public class InstrumentionProxy extends Instrumentation {
    private static final String TAG = InstrumentionProxy.class.getSimpleName();

    Instrumentation base;

    public InstrumentionProxy(Instrumentation base){
        this.base = base;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {


        // Hook之前, XXX到此一游!
        Log.i(TAG, "\n执行了startActivity, 参数如下: \n" + "who = [" + who + "], " +
                "\ncontextThread = [" + contextThread + "], \ntoken = [" + token + "], " +
                "\ntarget = [" + target + "], \nintent = [" + intent +
                "], \nrequestCode = [" + requestCode + "], \noptions = [" + options + "]");

        try {
            Method execStartActivityMethod = Instrumentation.class.getDeclaredMethod("execStartActivity",Context.class,
                    IBinder.class,IBinder.class, Activity.class,Intent.class,int.class,Bundle.class);
            execStartActivityMethod.setAccessible(true);
            return (ActivityResult) execStartActivityMethod.invoke(base,who,contextThread,token,target,intent,requestCode,options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
