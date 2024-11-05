package com.example.spring_boot_mode.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "picture")
public class PictureProperties {

    
    static class animation{
        private String path;
        private String mappingPath;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getMappingPath() {
            return mappingPath;
        }

        public void setMappingPath(String mappingPath) {
            this.mappingPath = mappingPath;
        }
    }
}

