package com.mall.config;

import com.mall.interceptor.MyInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private  String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 访问路径：http://localhost:8082/public/imgs/accessory/protectingShell-RedMi-K20&pro.png
         * http://localhost:8082/public/imgs/phone/picture/MI%20CC9%20Pro-2.jpg
         * "/public/**" 为前端URL访问路径
         * "file:" + uploadPath 是本地磁盘映射
         */
        registry.addResourceHandler("/public/**").addResourceLocations("file:" + uploadPath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有public开头的uri，并使用拦截器替换其中的特殊字符即空格
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/public/**");
    }
}

