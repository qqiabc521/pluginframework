package com.ljj.dynamic_proxy_hook.static_proxy;

import com.ljj.dynamic_proxy_hook.Shopping;
import com.ljj.dynamic_proxy_hook.ShoppingImpl;

import java.util.Arrays;

public class TestStaticProxy {

    public static void main(String[] args) {
        Shopping my = new ShoppingImpl();

        System.out.println(Arrays.toString(my.doShopping(100)));

        System.out.println("===================================");

        my = new ProxyShopping(my);
        System.out.println(Arrays.toString(my.doShopping(100)));
    }
}
