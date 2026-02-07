package com.example.spring_boot_mode.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据源配置测试
 */
@SpringBootTest
@DisplayName("数据源配置测试")
public class DataSourceConfigTest {

    @Autowired
    @Qualifier("modeDataSource")
    private DataSource modeDataSource;

    @Autowired
    @Qualifier("flowableDataSource")
    private DataSource flowableDataSource;

    @Test
    @DisplayName("测试主数据源配置")
    void testModeDataSource() {
        assertNotNull(modeDataSource, "主数据源不应为null");
        
        try (Connection conn = modeDataSource.getConnection()) {
            assertNotNull(conn, "主数据源连接不应为null");
            assertFalse(conn.isClosed(), "主数据源连接应该是打开的");
            
            DatabaseMetaData metaData = conn.getMetaData();
            String databaseName = conn.getCatalog();
            
            System.out.println("✅ 主数据源连接成功");
            System.out.println("   数据库名称: " + databaseName);
            System.out.println("   数据库产品: " + metaData.getDatabaseProductName());
            System.out.println("   数据库版本: " + metaData.getDatabaseProductVersion());
            
            assertTrue(databaseName.contains("test_all"), "应该连接到test_all数据库");
            
        } catch (Exception e) {
            fail("主数据源连接失败: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("测试Flowable数据源配置")
    void testFlowableDataSource() {
        assertNotNull(flowableDataSource, "Flowable数据源不应为null");
        
        try (Connection conn = flowableDataSource.getConnection()) {
            assertNotNull(conn, "Flowable数据源连接不应为null");
            assertFalse(conn.isClosed(), "Flowable数据源连接应该是打开的");
            
            DatabaseMetaData metaData = conn.getMetaData();
            String databaseName = conn.getCatalog();
            
            System.out.println("✅ Flowable数据源连接成功");
            System.out.println("   数据库名称: " + databaseName);
            System.out.println("   数据库产品: " + metaData.getDatabaseProductName());
            System.out.println("   数据库版本: " + metaData.getDatabaseProductVersion());
            
            assertTrue(databaseName.contains("flowable"), "应该连接到flowable数据库");
            
        } catch (Exception e) {
            fail("Flowable数据源连接失败: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("测试两个数据源是否独立")
    void testDataSourcesAreIndependent() {
        assertNotNull(modeDataSource, "主数据源不应为null");
        assertNotNull(flowableDataSource, "Flowable数据源不应为null");
        assertNotSame(modeDataSource, flowableDataSource, "两个数据源应该是不同的实例");
        
        try (Connection conn1 = modeDataSource.getConnection();
             Connection conn2 = flowableDataSource.getConnection()) {
            
            String db1 = conn1.getCatalog();
            String db2 = conn2.getCatalog();
            
            assertNotEquals(db1, db2, "两个数据源应该连接到不同的数据库");
            
            System.out.println("✅ 数据源独立性测试通过");
            System.out.println("   主数据源: " + db1);
            System.out.println("   Flowable数据源: " + db2);
            
        } catch (Exception e) {
            fail("数据源独立性测试失败: " + e.getMessage());
        }
    }
}
