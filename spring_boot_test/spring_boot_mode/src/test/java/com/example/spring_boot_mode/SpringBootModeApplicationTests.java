package com.example.spring_boot_mode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Spring Boot 应用启动测试
 */
@SpringBootTest
@DisplayName("应用启动测试")
public class SpringBootModeApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("测试应用上下文加载")
    void contextLoads() {
        assertNotNull(applicationContext, "应用上下文不应为null");
        System.out.println("✅ Spring Boot 应用上下文加载成功");
        System.out.println("   Bean总数: " + applicationContext.getBeanDefinitionCount());
    }

    @Test
    @DisplayName("测试主要Bean是否加载")
    void testMainBeansLoaded() {
        // 测试数据源Bean
        assertTrue(applicationContext.containsBean("modeDataSource"), 
                "应该包含主数据源Bean");
        assertTrue(applicationContext.containsBean("flowableDataSource"), 
                "应该包含Flowable数据源Bean");
        
        // 测试Service Bean
        assertTrue(applicationContext.containsBean("databaseBackupServiceImpl"), 
                "应该包含数据库备份服务Bean");
        
        System.out.println("✅ 主要Bean加载测试通过");
    }

    @Test
    @DisplayName("测试应用配置属性")
    void testApplicationProperties() {
        String port = applicationContext.getEnvironment().getProperty("server.port");
        assertNotNull(port, "服务器端口配置不应为null");
        assertEquals("8001", port, "服务器端口应该是8001");
        
        String activeProfile = applicationContext.getEnvironment().getProperty("spring.profiles.active");
        assertNotNull(activeProfile, "激活的配置文件不应为null");
        assertEquals("dev", activeProfile, "应该激活dev配置");
        
        System.out.println("✅ 应用配置属性测试通过");
        System.out.println("   服务器端口: " + port);
        System.out.println("   激活配置: " + activeProfile);
    }
}
