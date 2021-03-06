package com.ljj.binder_hook;

import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class BinderProxyHookHandler implements InvocationHandler{
    private static final String TAG = BinderProxyHookHandler.class.getSimpleName();

    private IBinder base;
    private Class<?> stub,iInterface;

    public BinderProxyHookHandler(IBinder base){
        this.base = base;
        try {
            stub = Class.forName("android.content.IClipboard$Stub");
            iInterface = Class.forName("android.content.IClipboard");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("queryLocalInterface".equals(method.getName())) {

            Log.d(TAG, "hook queryLocalInterface");

            // 这里直接返回真正被Hook掉的Service接口
            // 这里的 queryLocalInterface 就不是原本的意思了
            // 我们肯定不会真的返回一个本地接口, 因为我们接管了 asInterface方法的作用
            // 因此必须是一个完整的 asInterface 过的 IInterface对象, 既要处理本地对象,也要处理代理对象
            // 这只是一个Hook点而已, 它原始的含义已经被我们重定义了; 因为我们会永远确保这个方法不返回null
            // 让 IClipboard.Stub.asInterface 永远走到if语句的else分支里面
            return Proxy.newProxyInstance(proxy.getClass().getClassLoader(),

                    // asInterface 的时候会检测是否是特定类型的接口然后进行强制转换
                    // 因此这里的动态代理生成的类型信息的类型必须是正确的

                    // 这里面Hook的是一个BinderProxy对象(Binder代理) (代理Binder的queryLocalInterface正常情况下是返回null)
                    // 因此, 正常情况下 在asInterface里面会由于BinderProxy的queryLocalInterface返回null导致系统创建一个匿名的代理对象, 这样我们就无法控制了
                    // 所以我们要伪造一个对象, 瞒过这个if检测, 使得系统把这个queryLocalInterface返回的对象透传给asInterface的返回值;
                    // 检测有两个要求, 其一: 非空, 其二, IXXInterface类型。
                    // 所以, 其实返回的对象不需要是Binder对象, 我们把它当作普通的对象Hook掉就ok(拦截这个对象里面对于IXXInterface相关方法的调用)
                    // tks  jeremyhe_cn@qq.com
                    new Class[] { this.iInterface },
                    new BinderHookHandler(base, stub));
        }

        Log.d(TAG, "method:" + method.getName());

        return method.invoke(base,args);
    }
}
