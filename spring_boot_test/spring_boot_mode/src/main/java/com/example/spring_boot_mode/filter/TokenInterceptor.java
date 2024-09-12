package com.example.spring_boot_mode.filter;

import com.example.spring_boot_mode.utils.TokenUtill;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("LoginInterceptor preHandle(目标方法执行前执行).....");
        //获取token
        String token = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(token)) {
            Claims claimsFromToken = TokenUtill.getClaimsFromToken(token);
            return true;
        }else {
            return false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("LoginInterceptor postHandle(目标方案执行后执行).....");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("LoginInterceptor afterCompletion(视图渲染完毕后执行,最后执行).....");
    }
}
