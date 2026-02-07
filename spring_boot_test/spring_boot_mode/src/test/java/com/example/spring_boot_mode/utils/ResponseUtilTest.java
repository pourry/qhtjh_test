package com.example.spring_boot_mode.utils;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 响应工具类测试
 */
@DisplayName("响应工具类测试")
public class ResponseUtilTest {

    @Test
    @DisplayName("测试成功响应")
    void testSuccessResponse() {
        String data = "测试数据";
        ResponseObjectEntity response = ResponseUtil.success(data);
        
        assertNotNull(response, "响应对象不应为null");
        assertEquals(200, response.getCode(), "成功响应码应为200");
        assertTrue(response.getSuccessful(), "successful标志应为true");
        assertEquals(data, response.getResultValue(), "数据应该匹配");
        
        System.out.println("✅ 成功响应测试通过");
    }

    @Test
    @DisplayName("测试错误响应")
    void testErrorResponse() {
        String errorMsg = "错误信息";
        ResponseObjectEntity response = ResponseUtil.error(errorMsg);
        
        assertNotNull(response, "响应对象不应为null");
        assertEquals(403, response.getCode(), "错误响应码应为403");
        assertFalse(response.getSuccessful(), "successful标志应为false");
        assertEquals(errorMsg, response.getResultValue(), "错误信息应该匹配");
        
        System.out.println("✅ 错误响应测试通过");
    }

    @Test
    @DisplayName("测试Token过期响应")
    void testTokenExpireResponse() {
        String msg = "Token已过期";
        ResponseObjectEntity response = ResponseUtil.tokenExpire(msg);
        
        assertNotNull(response, "响应对象不应为null");
        assertEquals(410, response.getCode(), "Token过期响应码应为410");
        assertFalse(response.getSuccessful(), "successful标志应为false");
        assertEquals(msg, response.getResultValue(), "消息应该匹配");
        
        System.out.println("✅ Token过期响应测试通过");
    }

    @Test
    @DisplayName("测试空数据错误响应")
    void testErrorResponseWithNull() {
        ResponseObjectEntity response = ResponseUtil.error(null);
        
        assertNotNull(response, "响应对象不应为null");
        assertEquals(403, response.getCode(), "错误响应码应为403");
        assertNotNull(response.getResultValue(), "默认错误信息不应为null");
        
        System.out.println("✅ 空数据错误响应测试通过");
    }
}
