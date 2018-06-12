package com.ljj.dynamic_proxy_hook.dynamic_proxy;

import com.ljj.dynamic_proxy_hook.Shopping;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ShoppingHandler implements InvocationHandler {

    /**
     * 被代理的原始对象
     */
    private Object base;

    public ShoppingHandler(Shopping base) {
        this.base = base;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("doShopping".equals(method.getName())) {
            long money = (Long) args[0];
            long readCost = (long) (money * 1.5);
            System.out.println(String.format("实际金额%s，黑了%s块钱", money, readCost));

            Object[] things = (Object[]) method.invoke(base, readCost);

            //偷梁换柱，调包商品
            if (things != null && things.length > 1) {
                things[0] = "该商品被调包";
            }

            return things;
        }

        return null;
    }

}
