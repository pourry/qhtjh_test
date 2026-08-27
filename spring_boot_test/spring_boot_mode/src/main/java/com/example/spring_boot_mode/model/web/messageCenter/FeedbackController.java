package com.example.spring_boot_mode.model.web.messageCenter;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.dao.LoginDao;
import com.example.spring_boot_mode.model.service.messageCenter.FeedbackService;
import com.example.spring_boot_mode.model.entity.SysUser;
import com.example.spring_boot_mode.utils.TokenUtill;
import com.example.spring_boot_mode.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import com.example.spring_boot_mode.utils.DateUtil;

/**
 * 反馈控制器
 * 提供反馈相关的REST API接口
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    
    @Autowired
    private FeedbackService feedbackService;
    
    @Autowired
    private LoginDao loginDao;
    
    @org.springframework.beans.factory.annotation.Value("${picture.feedback.path:D:/picture/feedback}")
    private String feedbackImagePath;
    
    @org.springframework.beans.factory.annotation.Value("${picture.feedback.mappingPath:http://localhost:${server.port}/localPicture/feedback/}")
    private String feedbackImageMappingPath;
    
    /**
     * 提交反馈
     * POST /feedback/submit
     */
    @PostMapping("/submit")
    public ResponseObjectEntity submitFeedback(
            @RequestBody Map<String, String> data,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        
        // 获取用户完整信息
        SysUser userInfo = loginDao.selectById(user.getId());
        String nickname = "用户" + user.getId();
        if (userInfo != null) {
            if (userInfo.getNickName() != null && !userInfo.getNickName().isEmpty()) {
                nickname = userInfo.getNickName();
            } else if (userInfo.getUsername() != null) {
                nickname = userInfo.getUsername();
            }
        }
        
        String type = data.get("type");
        String title = data.get("title");
        String description = data.get("description");
        String contact = data.get("contact");
        String images = data.get("images");
        
        return feedbackService.submitFeedback(type, title, description, 
                contact, images, user.getId(), nickname);
    }
    
    /**
     * 获取我的反馈列表
     * GET /feedback/myList
     */
    @GetMapping("/myList")
    public ResponseObjectEntity getMyFeedbacks(HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return feedbackService.getMyFeedbacks(user.getId());
    }

    /**
     * 分页获取我的反馈列表
     * GET /feedback/myList/page?page=1&size=10
     */
    @GetMapping("/myList/page")
    public ResponseObjectEntity getMyFeedbacksPaged(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return feedbackService.getMyFeedbacksPaged(user.getId(), page, size);
    }

    /**
     * 最近 N 条反馈（Top 浮层预览用，不传 limit 默认 5）
     * GET /feedback/recentList?limit=5
     */
    @GetMapping("/recentList")
    public ResponseObjectEntity getRecentFeedbacks(
            @RequestParam(required = false, defaultValue = "5") int limit,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return feedbackService.getRecentFeedbacks(user.getId(), limit);
    }

    /**
     * 获取反馈详情
     * GET /feedback/detail?id=1
     */
    @GetMapping("/detail")
    public ResponseObjectEntity getFeedbackDetail(
            @RequestParam Long id,
            HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return feedbackService.getFeedbackDetail(id);
    }
    
    /**
     * 删除反馈（撤销）
     * POST /feedback/delete
     */
    @PostMapping("/delete")
    public ResponseObjectEntity deleteFeedback(
            @RequestParam Long id,
            HttpServletRequest request) {
        System.out.println("========== 删除反馈接口被调用 ==========");
        System.out.println("id: " + id);
        
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            System.out.println("用户未登录");
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        System.out.println("userId: " + user.getId());
        
        ResponseObjectEntity result = feedbackService.deleteFeedback(id, user.getId());
        System.out.println("删除结果: " + (result != null ? result.getResultValue() : "null"));
        return result;
    }
    
    /**
     * 更新反馈（编辑）
     * POST /feedback/update
     */
    @PostMapping("/update")
    public ResponseObjectEntity updateFeedback(
            @RequestBody Map<String, String> data,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        
        Long id = Long.parseLong(data.get("id"));
        String type = data.get("type");
        String title = data.get("title");
        String description = data.get("description");
        String contact = data.get("contact");
        String images = data.get("images");
        
        return feedbackService.updateFeedback(id, type, title, description, 
                contact, images, user.getId());
    }
    
    /**
     * 获取所有反馈列表（管理员，不分页，保留兼容）
     * GET /feedback/allList
     */
    @GetMapping("/allList")
    public ResponseObjectEntity getAllFeedbacks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }

        Map<String, Object> params = new HashMap<>();
        if (status != null && !status.isEmpty()) {
            params.put("status", status);
        }
        if (type != null && !type.isEmpty()) {
            params.put("type", type);
        }
        if (keyword != null && !keyword.isEmpty()) {
            params.put("keyword", keyword);
        }

        return feedbackService.getAllFeedbacks(params);
    }

    /**
     * 分页获取所有反馈列表（管理员）
     * GET /feedback/allList/page?page=1&size=20
     */
    @GetMapping("/allList/page")
    public ResponseObjectEntity getAllFeedbacksPaged(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }

        Map<String, Object> params = new HashMap<>();
        if (status != null && !status.isEmpty()) params.put("status", status);
        if (type != null && !type.isEmpty()) params.put("type", type);
        if (keyword != null && !keyword.isEmpty()) params.put("keyword", keyword);

        return feedbackService.getAllFeedbacksPaged(params, page, size);
    }
    
    /**
     * 处理反馈（更新状态）
     * POST /feedback/process
     */
    @PostMapping("/process")
    public ResponseObjectEntity processFeedback(
            @RequestBody Map<String, String> data,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        
        Long id = Long.parseLong(data.get("id"));
        String status = data.get("status");
        
        return feedbackService.processFeedback(id, status, user.getId());
    }
    
    /**
     * 回复反馈
     * POST /feedback/reply
     */
    @PostMapping("/reply")
    public ResponseObjectEntity replyFeedback(
            @RequestBody Map<String, String> data,
            HttpServletRequest request) {
        SysUser user = TokenUtill.getSysUser(request);
        if (Objects.isNull(user)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        
        Long id = Long.parseLong(data.get("id"));
        String reply = data.get("reply");
        
        return feedbackService.replyFeedback(id, reply, user.getId());
    }
    
    /**
     * 获取反馈统计数据
     * GET /feedback/stats
     */
    @GetMapping("/stats")
    public ResponseObjectEntity getFeedbackStats(HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return feedbackService.getFeedbackStats();
    }
    
    /**
     * 上传反馈图片
     * POST /feedback/uploadImage
     */
    @PostMapping("/uploadImage")
    public ResponseObjectEntity uploadImage(@RequestParam("file") MultipartFile file,
                                            HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        if (file == null || file.isEmpty()) {
            return ResponseUtil.error("请上传图片文件");
        }
        
        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseUtil.error("只能上传图片文件");
        }
        
        try {
            // 确保目录存在
            File dir = new File(feedbackImagePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = System.currentTimeMillis() + "_" + 
                    java.util.UUID.randomUUID().toString().substring(0, 8) + ext;
            
            // 保存文件
            File destFile = new File(feedbackImagePath, fileName);
            file.transferTo(destFile);
            
            // 返回访问路径 - 直接从请求构建完整 URL，确保格式正确
            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int serverPort = request.getServerPort();
            String url = scheme + "://" + serverName + ":" + serverPort + "/localPicture/feedback/" + fileName;
            
            Map<String, String> result = new HashMap<>();
            result.put("url", url);
            result.put("fileName", fileName);
            
            return ResponseUtil.success(result);
        } catch (Exception e) {
            return ResponseUtil.error("图片上传失败：" + e.getMessage());
        }
    }
}