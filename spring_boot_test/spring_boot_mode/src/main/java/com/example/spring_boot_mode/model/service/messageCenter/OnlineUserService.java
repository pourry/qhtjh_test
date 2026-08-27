package com.example.spring_boot_mode.model.service.messageCenter;

import com.example.spring_boot_mode.model.dao.LoginDao;
import com.example.spring_boot_mode.model.dao.messageCenter.OnlineUserDao;
import com.example.spring_boot_mode.model.entity.SysUser;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.websocket.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 在线用户服务
 * 管理用户在线状态和在线用户列表
 */
@Service
public class OnlineUserService {

    @Autowired
    private OnlineUserDao onlineUserDao;

    @Autowired
    private LoginDao loginDao;

    /**
     * 设置用户在线状态
     *
     * @param userId 用户ID
     * @param online 在线状态
     */
    public void setOnlineStatus(String userId, boolean online) {
        try {
            SysUser user = new SysUser();
            user.setId(userId);
            user.setOnlineStatus(online);
            user.setUpdateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new Date()));
            loginDao.updateInfo(user);
        } catch (Exception e) {
            // 记录日志但不抛出异常，避免影响主流程
            e.printStackTrace();
        }
    }

    /**
     * 从数据库获取在线用户列表
     *
     * @return 在线用户列表
     */
    public List<Map<String, Object>> getOnlineUserList() {
        return onlineUserDao.selectOnlineUsers();
    }

    /**
     * 获取真正的在线用户列表
     * 以 WebSocket SessionManager 中的在线状态为准
     *
     * @return 在线用户列表
     */
    public List<Map<String, Object>> getRealOnlineUserList() {
        Set<String> onlineSessionUsers = SessionManager.getOnlineUserIds();
        List<Map<String, Object>> result = new ArrayList<>();

        for (String userId : onlineSessionUsers) {
            try {
                Map<String, Object> userInfo = onlineUserDao.selectOnlineUserById(userId);
                if (userInfo != null) {
                    result.add(userInfo);
                } else {
                    // 如果数据库查不到，创建基本信息
                    SysUser user = loginDao.selectById(userId);
                    if (user != null) {
                        Map<String, Object> info = new java.util.HashMap<>();
                        info.put("id", user.getId());
                        info.put("nickname", user.getNickName() != null ? user.getNickName() : user.getUsername());
                        info.put("avatar", user.getAvatar());
                        result.add(info);
                    }
                }
            } catch (Exception e) {
                // 忽略单个用户查询失败
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 检查用户是否在线
     *
     * @param userId 用户ID
     * @return true-在线，false-离线
     */
    public boolean isUserOnline(String userId) {
        return SessionManager.isOnline(userId);
    }

    /**
     * 获取当前在线用户数量
     *
     * @return 在线用户数量
     */
    public int getOnlineCount() {
        return SessionManager.getOnlineCount();
    }
}