package com.ljj.dynamic_proxy_hook;

public class ShoppingImpl implements Shopping {

    @Override
    public Object[] doShopping(long money) {
        System.out.println("逛海淘，买买买");
        System.out.println(String.format("花了%s钱", money));
        return new Object[]{"衣服", "鞋子", "帽子"};
    }

}
