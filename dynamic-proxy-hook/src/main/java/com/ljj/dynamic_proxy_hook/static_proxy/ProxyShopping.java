package com.ljj.dynamic_proxy_hook.static_proxy;

import com.ljj.dynamic_proxy_hook.Shopping;

public class ProxyShopping implements Shopping {

    private Shopping base;

    public ProxyShopping(Shopping base) {
        this.base = base;
    }

    @Override
    public Object[] doShopping(long money) {
        //提高所花金额，黑钱
        long readCost = (long) (money * 1.5);
        System.out.println(String.format("实际金额%s，黑了%s块钱", money, readCost));
        Object[] things = base.doShopping(readCost);

        //偷梁换柱，调包商品
        if (things != null && things.length > 1) {
            things[0] = "该商品被调包";
        }

        return things;
    }
}
