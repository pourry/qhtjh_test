package com.example.spring_boot_mode.utils;

import cn.hutool.core.date.DateUtil;
import com.example.spring_boot_mode.config.encrypt.sm2.SM2Util;
import com.example.spring_boot_mode.model.entity.SysUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class TokenUtill {

    //token 超时时间
    private static Integer expiration;
    @Value("${token.expiration-s}")
    public void setTokenExpiration(Integer param){
        expiration = param;
    }

    //根据用户信息生成token
    public static String generateToken(SysUser sysUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("CLAIM_KEY_USER_ID", sysUser.getId());
        claims.put("CLAIM_KEY_USERNAME", sysUser.getUsername());
        claims.put("CLAIM_KEY_CREATED", new Date());
        return generateToken(claims);
    }

    //从数据声明生成令牌
    private static String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration * 1000);
        return Jwts.builder()
                //注入参数
                .setClaims(claims)
                //设置过期时间
                .setExpiration(expirationDate)
                //设置算法及密钥
                .signWith(SignatureAlgorithm.HS512, SM2Util.getPrivateKey())
                .compact();
    }

    //获取Token中的主体
    public static Claims getClaimsFromToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SM2Util.getPrivateKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.warn("JWT格式验证失败: {}", e.getMessage());
        }
        return claims;
    }
    
    //获取用户信息
    public static SysUser getSysUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return null;
        }
        SysUser sysUser = null;
        try {
           Claims claims = Jwts.parser()
                    .setSigningKey(SM2Util.getPrivateKey())
                    .parseClaimsJws(token)
                    .getBody();
            if (!Objects.isNull(claims)) {
                sysUser = new SysUser();
                sysUser.setId(claims.get("CLAIM_KEY_USER_ID").toString());
            }
        } catch (Exception e) {
            log.warn("JWT格式验证失败: {}", e.getMessage());
        }
        return sysUser;
    }

    /**
     * 从Token字符串解析用户信息
     * 用于WebSocket连接时验证Token
     *
     * @param token JWT Token字符串
     * @return 用户信息，如果Token无效返回null
     */
    public static SysUser getSysUserFromToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SM2Util.getPrivateKey())
                    .parseClaimsJws(token)
                    .getBody();
            if (!Objects.isNull(claims)) {
                SysUser sysUser = new SysUser();
                sysUser.setId(claims.get("CLAIM_KEY_USER_ID").toString());
                sysUser.setUsername(claims.get("CLAIM_KEY_USERNAME").toString());
                return sysUser;
            }
        } catch (Exception e) {
            log.warn("JWT格式验证失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 从token中获取登录用户名
     */
    public static String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.get("CLAIM_KEY_USERNAME").toString();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 验证token是否还有效
     *
     * @param token    客户端传入的token
     * @param username 从数据库中查询出来的用户名
     */
    public boolean validateToken(String token, String username) {
        String usernameToken = getUserNameFromToken(token);
        return usernameToken.equals(username) && !isTokenExpired(token);
    }

    /**
     * 判断token是否已经失效
     */
    public static boolean isTokenExpired(String token) {
        Date expiredDate = getClaimsFromToken(token).getExpiration();
        return expiredDate.before(new Date());
    }

    /**
     * 判断token在指定时间内是否刚刚刷新过
     *
     * @param token 原token
     * @param time  指定时间（秒）
     */
    private boolean tokenRefreshJustBefore(String token, int time) {
        Claims claims = getClaimsFromToken(token);
        Date created = claims.get("CLAIM_KEY_CREATED", Date.class);
        Date refreshDate = new Date();
        //刷新时间在创建时间的指定时间内
        return refreshDate.after(created) && refreshDate.before(DateUtil.offsetSecond(created, time));
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("tokenHeader");
        return token;
    }
}