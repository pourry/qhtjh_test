package com.example.spring_boot_mode.config.localPicutreMapping;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LocalPicuterMappingConfig implements WebMvcConfigurer {

    @Value("${picture.localoriginPath}")
    private String localoriginPath;
    @Value("${picture.mappinglocaloriginPath}")
    private String mappinglocaloriginPath;

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //文件磁盘图片url 映射
        //配置server虚拟路径，handler为前台访问的目录，locations为files相对应的本地路径
        registry.addResourceHandler(mappinglocaloriginPath).addResourceLocations(localoriginPath);
    }

}
