package com.mall.constant;

/**
 * redis信息类
 */
public class RedisConstant {
    public static final String USER_LOGIN_STATE = "login:token";
    public static final String CACHE_CATEGORY_KEY = "cache:category";
    public static final String CACHE_CAROUSEL_KEY = "cache:carousel";
    public static final Long LOGIN_USER_TTL = 300000L;
    public static final String INCR_ORDER_KEY = "incr:order";
    public static final String APPLIANCE_CATEGORY_KEY = "category:appliance&2&3&4";
    public static final String ACCESSORY_CATEGORY_KEY = "category:accessory&5&6&7&8";
    public static final String INVENTORY_KEY = "inventory";
    public static final String SHOPPING_CART_KEY = "shoppingCart";
    public static final String LOCK_KEY_PREFIX = "lock:order";
}
