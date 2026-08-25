package com.example.spring_boot_mode.model.web;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.dao.LoginDao;
import com.example.spring_boot_mode.model.entity.SysUser;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.TokenUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.File;
import java.util.Map;
import java.util.Objects;

/**
 * 用户信息控制器
 * 提供个人信息的查询、更新、密码修改等接口
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    private LoginDao loginDao;

    /** 头像存储路径 */
    @org.springframework.beans.factory.annotation.Value("${picture.user.path:D:/picture/user}")
    private String userAvatarPath;

    /** 头像访问映射路径 */
    @org.springframework.beans.factory.annotation.Value("${picture.user.mappingPath:http://localhost:8001/localPicture/user/}")
    private String userAvatarMappingPath;

    /** 查询当前登录用户信息 */
    @GetMapping("/query")
    public ResponseObjectEntity queryUserInfo(HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        String userId = TokenUtill.getSysUser(request).getId();
        SysUser user = loginDao.selectById(userId);
        if (user != null) {
            // 清除密码字段，不返回给前端
            user.setPassword(null);
        }
        return ResponseUtil.success(user);
    }

    /** 更新用户基本信息 */
    @PostMapping("/updateInfo")
    public ResponseObjectEntity updateInfo(SysUser sysUser, HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        String userId = TokenUtill.getSysUser(request).getId();
        sysUser.setId(userId);
        // 设置更新时间
        sysUser.setUpdateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new java.util.Date()));

        int result = loginDao.updateInfo(sysUser);
        if (result <= 0) {
            return ResponseUtil.error("更新失败");
        }
        return ResponseUtil.success("更新成功");
    }

    /** 修改密码 */
    @PostMapping("/changePassword")
    public ResponseObjectEntity changePassword(@RequestParam String currentPassword,
                                               @RequestParam String newPassword,
                                               HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        String userId = TokenUtill.getSysUser(request).getId();

        // 查询当前用户
        SysUser user = loginDao.selectById(userId);
        if (user == null) {
            return ResponseUtil.error("用户不存在");
        }

        // 验证当前密码
        if (!currentPassword.equals(user.getPassword())) {
            return ResponseUtil.error("当前密码不正确");
        }

        // 验证新密码长度
        if (newPassword == null || newPassword.length() < 6) {
            return ResponseUtil.error("新密码长度至少6位");
        }

        String updateTime = DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new java.util.Date());
        int result = loginDao.updatePassword(userId, currentPassword, newPassword, updateTime);
        if (result <= 0) {
            return ResponseUtil.error("密码修改失败");
        }
        return ResponseUtil.success("密码修改成功");
    }

    /** 上传头像 */
    @PostMapping("/uploadAvatar")
    public ResponseObjectEntity uploadAvatar(@RequestParam("file") MultipartFile file,
                                             HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        if (file == null || file.isEmpty()) {
            return ResponseUtil.error("请上传头像文件");
        }

        String userId = TokenUtill.getSysUser(request).getId();

        // 确保目录存在
        File dir = new File(userAvatarPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = userId + ext;

        try {
            // 保存文件
            File destFile = new File(userAvatarPath, fileName);
            file.transferTo(destFile);

            // 更新数据库
            SysUser updateUser = new SysUser();
            updateUser.setId(userId);
            updateUser.setAvatar(userAvatarMappingPath + fileName);
            updateUser.setUpdateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new java.util.Date()));
            loginDao.updateInfo(updateUser);

            return ResponseUtil.success(userAvatarMappingPath + fileName);
        } catch (Exception e) {
            return ResponseUtil.error("头像上传失败：" + e.getMessage());
        }
    }
}
