package com.mall.config;

import com.mall.interceptor.LoginInterceptor;
import com.mall.interceptor.MyInterceptor;
import com.mall.interceptor.RefreshTokenInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${upload.path}")
    private  String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
         * 访问路径：http://localhost:8082/public/imgs/accessory/protectingShell-RedMi-K20&pro.png
         * http://localhost:8082/public/imgs/phone/picture/MI%20CC9%20Pro-2.jpg
         * "/public/**" 为前端URL访问路径
         * "file:" + uploadPath 是本地磁盘映射
         */
        registry.addResourceHandler("/public/**").addResourceLocations("file:" + uploadPath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有请求刷新token
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).
                    excludePathPatterns("/public/**").order(0);
        //拦截所有public开头的uri，并使用拦截器替换其中的特殊字符即空格
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/public/**").order(1);
        //登录拦截
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                    "/user/**",
                    "/product/getProductByCategory",
                    "/product/getCategory",
                    "/product/getHotProduct",
                    "/public/**",
                    "/resources/carousel",
                    "/product/getPromoProduct",
                    "/product/getAllProduct",
                    "/error"
        ).order(2);
    }
}

