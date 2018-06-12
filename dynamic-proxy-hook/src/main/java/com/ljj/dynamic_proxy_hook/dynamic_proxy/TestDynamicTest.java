package com.ljj.dynamic_proxy_hook.dynamic_proxy;

import com.ljj.dynamic_proxy_hook.Shopping;
import com.ljj.dynamic_proxy_hook.ShoppingImpl;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class TestDynamicTest {

    public static void main(String[] args) {

        Shopping my = new ShoppingImpl();
        System.out.println(Arrays.toString(my.doShopping(100)));

        System.out.println("===================================");

        my = (Shopping) Proxy.newProxyInstance(Shopping.class.getClassLoader(), new Class[]{Shopping.class}, new ShoppingHandler(my));
        System.out.println(Arrays.toString(my.doShopping(100)));

    }

}
