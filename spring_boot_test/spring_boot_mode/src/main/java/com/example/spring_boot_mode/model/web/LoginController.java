package com.example.spring_boot_mode.model.web;

import com.example.spring_boot_mode.config.encrypt.sm2.SM2Util;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.dao.LoginDao;
import com.example.spring_boot_mode.model.entity.SysUser;
import com.example.spring_boot_mode.exception.ThrowMsgException;
import com.example.spring_boot_mode.model.service.Loginservice;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.TokenUtill;
import com.example.spring_boot_mode.utils.UUidUtil;
import com.example.spring_boot_mode.utils.redis.RedisSafe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/login")
public class LoginController {

    private static final String GLOBAL_CAPTCHA_KEY = "global:captchaEnabled";

    static {
        System.out.println("静态语句块执行了");
    }
    @Autowired
    RedisSafe redisSafe;
    @Autowired
    private Loginservice loginservice;
    @Autowired
    private LoginDao loginDao;

    //获取sm2 秘钥
    @GetMapping("togetSm2")
    public ResponseObjectEntity togetSm2(){
        //每次重启都会自动生成新的sm2
        String publicKey = SM2Util.getPublicKey();
        return ResponseUtil.success(publicKey);
    }
    @GetMapping("getalltest")
    public ResponseObjectEntity getalltest(){
        List<SysUser> relist= loginservice.getSysUser();
        return ResponseUtil.success(relist);
    }

    /** 生成验证码：返回 base64 图片 + captchaKey，验证码存入 Redis 5 分钟有效 */
    @GetMapping("/captcha")
    public ResponseObjectEntity getCaptcha() {
        String captchaKey = "captcha:" + UUidUtil.getuuid();
        String captchaCode = generateRandomCode(4);
        try {
            BufferedImage image = generateCaptchaImage(captchaCode);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
            // 用 captchaKey 作为 token 存验证码值，5 分钟有效
            redisSafe.set(captchaKey, captchaCode, 300);

            Map<String, String> result = new HashMap<>();
            result.put("image", "data:image/png;base64," + base64Image);
            result.put("captchaKey", captchaKey);
            return ResponseUtil.success(result);
        } catch (Exception e) {
            return ResponseUtil.error("验证码生成失败：" + e.getMessage());
        }
    }

    /** 获取全局验证码开关（登录页获取配置用，无需登录） */
    @GetMapping("/globalCaptchaEnabled")
    public ResponseObjectEntity isCaptchaEnabled() {
        Object val = redisSafe.get(GLOBAL_CAPTCHA_KEY);
        boolean enabled = val != null && Boolean.parseBoolean(val.toString());
        return ResponseUtil.success(enabled);
    }

    /** 测试用：设置全局验证码开关（无需登录） */
    @GetMapping("/testSetCaptcha")
    public ResponseObjectEntity testSetCaptcha(@RequestParam Boolean enabled) {
        redisSafe.set(GLOBAL_CAPTCHA_KEY, String.valueOf(enabled), 365L * 24 * 3600);
        return ResponseUtil.success("已设置为: " + enabled);
    }

    @PostMapping("/login")
    public ResponseObjectEntity tologin(SysUser sysUser,
                                        @RequestParam(required = false) String captchaCode,
                                        @RequestParam(required = false) String captchaKey) {
        try {
            // 检查是否启用了验证码（全局开关 or 用户级别开关）
            Object globalVal = redisSafe.get(GLOBAL_CAPTCHA_KEY);
            boolean globalEnabled = globalVal != null && Boolean.parseBoolean(globalVal.toString());
            SysUser checkUser = loginDao.tologin(sysUser.getUsername());
            boolean userEnabled = checkUser != null && Boolean.TRUE.equals(checkUser.getCaptchaEnabled());
            if (globalEnabled || userEnabled) {
                // 验证码校验
                if (captchaCode == null || captchaCode.trim().isEmpty()) {
                    return ResponseUtil.error("请输入验证码");
                }
                if (captchaKey == null || captchaKey.trim().isEmpty()) {
                    return ResponseUtil.error("验证码已失效，请刷新");
                }
                // 从 Redis 获取验证码
                String storedCode = (String) redisSafe.get(captchaKey);
                if (storedCode == null) {
                    return ResponseUtil.error("验证码已过期，请刷新");
                }
                if (!captchaCode.equalsIgnoreCase(storedCode)) {
                    return ResponseUtil.error("验证码错误");
                }
                // 验证成功后立即删除验证码，防止重复使用
                redisSafe.remove(captchaKey);
            }

            Map<String,Object> remap = loginservice.tologin(sysUser);
            return ResponseUtil.success(remap);
        }catch (ThrowMsgException e){
            return ResponseUtil.error(e.getMessage());
        }catch (Exception e){
            return ResponseUtil.error("登录失败：" + e.getMessage());
        }

    }

    //验证 注册的用户名是否重复
    @PostMapping("/tocheckname")
    public ResponseObjectEntity tocheckname(String username){
        try{
            ResponseObjectEntity responseObjectEntity = loginservice.tocheckname(username);
            return responseObjectEntity;
        }catch (ThrowMsgException e){
            return ResponseUtil.error(e.getMessage());
        }

    }

    @PostMapping("/signUp")
    public ResponseObjectEntity tosignUp(SysUser sysUser){
        try{
            ResponseObjectEntity responseObjectEntity = loginservice.tosignUp(sysUser);
            return responseObjectEntity;
        }catch (ThrowMsgException e){
            return ResponseUtil.error(e.getMessage());
        }

    }

    /** 用户登出 - 设置用户为离线状态 */
    @PostMapping("/logout")
    public ResponseObjectEntity logout(HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.success("已退出登录");
        }
        // 设置用户为离线状态
        SysUser updateUser = new SysUser();
        updateUser.setId(user.getId());
        updateUser.setOnlineStatus(false);
        loginDao.updateInfo(updateUser);
        return ResponseUtil.success("已退出登录");
    }

    /** 更新验证码开关（登录后调用）- 同时更新全局Redis和用户数据库 */
    @PostMapping("/saveCaptchaEnabled")
    public ResponseObjectEntity saveCaptchaEnabled(@RequestParam Boolean captchaEnabled,
                                                    HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        // 保存到 Redis（全局开关）
        redisSafe.set(GLOBAL_CAPTCHA_KEY, String.valueOf(captchaEnabled), 365L * 24 * 3600);
        // 保存到数据库（用户级别）
        String userId = TokenUtill.getSysUser(request).getId();
        SysUser updateUser = new SysUser();
        updateUser.setId(userId);
        updateUser.setCaptchaEnabled(captchaEnabled);
        updateUser.setUpdateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new java.util.Date()));
        loginDao.updateInfo(updateUser);
        return ResponseUtil.success("保存成功");
    }

    // ========== 验证码工具方法 ==========

    /** 生成随机验证码文本 */
    private String generateRandomCode(int length) {
        String chars = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /** 生成验证码图片 */
    private BufferedImage generateCaptchaImage(String code) {
        int width = 120;
        int height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        Random random = new Random();

        // 背景
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, width, height);

        // 干扰线
        for (int i = 0; i < 6; i++) {
            g.setColor(new Color(150 + random.nextInt(80), 150 + random.nextInt(80), 150 + random.nextInt(80)));
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }

        // 干扰点
        for (int i = 0; i < 50; i++) {
            g.setColor(new Color(100 + random.nextInt(100), 100 + random.nextInt(100), 100 + random.nextInt(100)));
            g.fillOval(random.nextInt(width), random.nextInt(height), 2, 2);
        }

        // 绘制验证码字符
        g.setFont(new Font("Arial", Font.BOLD, 28));
        int charWidth = width / code.length();
        for (int i = 0; i < code.length(); i++) {
            g.setColor(new Color(30 + random.nextInt(50), 30 + random.nextInt(50), 30 + random.nextInt(50)));
            int degree = random.nextInt(30) - 15;
            double theta = Math.toRadians(degree);
            int x = charWidth * i + charWidth / 2;
            int y = height / 2 + 10;
            g.rotate(theta, x, y);
            g.drawString(String.valueOf(code.charAt(i)), x - 10, y);
            g.rotate(-theta, x, y);
        }

        g.dispose();
        return image;
    }

}
