package com.web.coloshop.Service.Interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/Login", "/SignUp","/logout","/Do_Login","/save_SignUp","/contacts","/Save_Contact","/ProductDetail","/shop",
                        "/Admin/Css/**", "/Admin/fonts/**", "/Admin/images/**",
                        "/Admin/JS/**", "/User/js/**", "/User/plugins/**",
                        "/User/styles/**"); // Các URL cần được bỏ qua
    }
}
