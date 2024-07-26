package com.wangshuos.cloudflare.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import static org.springframework.web.cors.CorsConfiguration.ALL;

/**
 * @ClassName WebMvcConfig
 * @Author wangshuo
 * @Date 2024/5/21 11:57
 * @Version 1.0
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 拦截器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new GlobalInterceptor()).addPathPatterns("/**");
        //super.addInterceptors(registry);
    }

    /**
     * 配置静态资源
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置会导致 knife4j 文档无法使用，不需要配置
        //registry.addResourceHandler("/**")
        //        .addResourceLocations("classpath:/static/");
    }

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //.allowedOrigins("http://localhost:8081")  // allowCredentials(true) 为ture时 要设置允许跨域 不能用户这个 * .allowedOrigins(ALL)
                .allowedOriginPatterns(ALL)  // 用这个
                .allowedMethods(ALL)
                .allowedHeaders(ALL)
                .allowCredentials(true) // 允许携带凭证
                .maxAge(3600);
    }

    /**
     * 配置视图控制器
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }


}
