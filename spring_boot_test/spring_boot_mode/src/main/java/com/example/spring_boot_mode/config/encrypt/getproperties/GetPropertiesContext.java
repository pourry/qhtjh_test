package com.example.spring_boot_mode.config.encrypt.getproperties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

//读取配置文件的类
@Slf4j
@Component
public class GetPropertiesContext {
    private static Properties properties = null;

    static {
        ClassPathResource resource = new ClassPathResource("ConfigAPI.properties");
        try {
            properties= PropertiesLoaderUtils.loadProperties(resource);
            log.info("读取配置文件配置----------------success-----------------");

        } catch (IOException e) {
            log.info("读取配置失败----------------failed---------------------");
            e.printStackTrace();
        }
    }

    public static String getProperties(String key){
        return properties.getProperty(key);
    }
}
