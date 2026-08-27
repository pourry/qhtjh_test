package com.example.spring_boot_mode.model.service.messageCenter.impl;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.dao.messageCenter.FeedbackDao;
import com.example.spring_boot_mode.model.entity.messageCenter.Feedback;
import com.example.spring_boot_mode.model.service.messageCenter.FeedbackService;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import com.example.spring_boot_mode.utils.pictureSave.PictureSave;

/**
 * 反馈服务实现类
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {
    
    @Autowired
    private FeedbackDao feedbackDao;
    
    @Value("${picture.feedback.path:D:/picture/feedback}")
    private String feedbackImagePath;
    
    @Value("${picture.localorcloud:localPictureSave}")
    private String localorcloud;
    
    @Autowired
    private Map<String, PictureSave> pictureSave;
    
    @Override
    public ResponseObjectEntity submitFeedback(String type, String title, String description,
                                               String contact, String images, String userId, String userName) {
        if (title == null || title.trim().isEmpty()) {
            return ResponseUtil.error("反馈标题不能为空");
        }
        if (description == null || description.trim().isEmpty()) {
            return ResponseUtil.error("反馈描述不能为空");
        }
        
        String now = DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new java.util.Date());
        
        Feedback feedback = new Feedback();
        feedback.setType(type != null ? type : "other");
        feedback.setTitle(title.trim());
        feedback.setDescription(description.trim());
        feedback.setContact(contact != null ? contact.trim() : "");
        feedback.setImages(images != null ? images : "");
        feedback.setStatus("pending");
        feedback.setUserId(userId);
        feedback.setUserName(userName);
        feedback.setCreateTime(now);
        feedback.setUpdateTime(now);
        
        int result = feedbackDao.insert(feedback);
        if (result > 0) {
            return ResponseUtil.success("反馈提交成功");
        }
        return ResponseUtil.error("提交失败");
    }
    
    @Override
    public ResponseObjectEntity getMyFeedbacks(String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseUtil.error("用户ID不能为空");
        }
        List<Feedback> feedbacks = feedbackDao.selectByUserId(userId);
        return ResponseUtil.success(feedbacks);
    }

    @Override
    public ResponseObjectEntity getMyFeedbacksPaged(String userId, int page, int size) {
        if (userId == null || userId.isEmpty()) {
            return ResponseUtil.error("用户ID不能为空");
        }
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        if (size > 100) size = 100;

        int offset = (page - 1) * size;
        long total = feedbackDao.countByUserId(userId);
        List<Feedback> list = feedbackDao.selectByUserIdPaged(userId, offset, size);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", total == 0 ? 0 : (int) ((total - 1) / size + 1));
        return ResponseUtil.success(result);
    }

    @Override
    public ResponseObjectEntity getRecentFeedbacks(String userId, int limit) {
        if (userId == null || userId.isEmpty()) {
            return ResponseUtil.success(java.util.Collections.emptyList());
        }
        if (limit <= 0) limit = 5;
        if (limit > 20) limit = 20;
        List<Feedback> list = feedbackDao.selectRecentByUserId(userId, limit);
        return ResponseUtil.success(list);
    }

    @Override
    public ResponseObjectEntity getFeedbackDetail(Long id) {
        if (id == null) {
            return ResponseUtil.error("反馈ID不能为空");
        }
        Feedback feedback = feedbackDao.selectDetailById(id);
        if (feedback != null) {
            return ResponseUtil.success(feedback);
        }
        return ResponseUtil.error("反馈不存在");
    }
    
    @Override
    public ResponseObjectEntity deleteFeedback(Long id, String userId) {
        if (id == null) {
            return ResponseUtil.error("反馈ID不能为空");
        }
        
        System.out.println("开始删除反馈, id: " + id + ", userId: " + userId);
        
        // 先查询反馈记录，获取图片列表以便删除物理文件
        Feedback feedback = feedbackDao.selectByIdAndUser(id, userId);
        if (feedback != null) {
            System.out.println("查询到反馈记录, images: " + feedback.getImages());
            if (feedback.getImages() != null && !feedback.getImages().isEmpty()) {
                deleteImageFiles(feedback.getImages());
            }
        } else {
            System.out.println("未查询到反馈记录，可能已被删除");
        }
        
        // 再删除数据库记录
        int result = feedbackDao.deleteByIdAndUser(id, userId);
        System.out.println("删除数据库记录结果: " + result);
        
        if (result > 0) {
            return ResponseUtil.success("撤销成功");
        }
        return ResponseUtil.error("撤销失败");
    }
    
    /**
     * 删除反馈关联的图片物理文件
     * 使用与上传代码完全一致的路径构建方式：new File(feedbackImagePath, fileName)
     */
    private void deleteImageFiles(String imagesJson) {
        if (imagesJson == null || imagesJson.isEmpty()) {
            System.out.println("[Feedback] 图片列表为空，跳过物理删除");
            return;
        }
        
        System.out.println("[Feedback] ========== 开始删除图片文件 ==========");
        System.out.println("[Feedback] imagesJson: " + imagesJson);
        System.out.println("[Feedback] feedbackImagePath: " + feedbackImagePath);
        
        List<String> imageUrls = parseImageUrls(imagesJson);
        System.out.println("[Feedback] 解析出 " + imageUrls.size() + " 个URL");
        
        // 逐个删除文件
        for (String url : imageUrls) {
            deleteSingleImage(url);
        }
        System.out.println("[Feedback] ========== 图片删除处理完成 ==========");
    }
    
    /**
     * 解析图片URL JSON字符串为URL列表
     */
    private List<String> parseImageUrls(String imagesJson) {
        List<String> imageUrls = new ArrayList<>();
        if (imagesJson == null || imagesJson.isEmpty()) {
            return imageUrls;
        }
        
        if (imagesJson.startsWith("[") && imagesJson.endsWith("]")) {
            // JSON数组格式: ["url1", "url2"]
            String content = imagesJson.substring(1, imagesJson.length() - 1);
            String[] parts = content.split(",");
            for (String part : parts) {
                String url = part.trim();
                // 移除可能的引号
                if (url.startsWith("\"") && url.endsWith("\"")) {
                    url = url.substring(1, url.length() - 1);
                }
                if (!url.isEmpty()) {
                    imageUrls.add(url);
                }
            }
        } else {
            // 单个URL格式
            imageUrls.add(imagesJson.trim());
        }
        return imageUrls;
    }
    
    /**
     * 删除单个图片文件
     */
    private void deleteSingleImage(String url) {
        try {
            // 从URL中提取文件名
            String fileName = url;
            int lastSlash = url.lastIndexOf('/');
            int lastBackslash = url.lastIndexOf('\\');
            int sepIndex = Math.max(lastSlash, lastBackslash);
            if (sepIndex >= 0 && sepIndex < url.length() - 1) {
                fileName = url.substring(sepIndex + 1);
            }
            
            System.out.println("[Feedback] 处理文件: url=" + url + ", fileName=" + fileName);
            
            // 使用与上传代码完全一致的路径构建方式
            File file = new File(feedbackImagePath, fileName);
            String absPath = file.getAbsolutePath();
            System.out.println("[Feedback] 构建的文件路径: " + absPath);
            System.out.println("[Feedback] 文件存在: " + file.exists() + ", isFile: " + file.isFile());
            
            if (!file.exists() || !file.isFile()) {
                System.out.println("[Feedback] 文件不存在或不是普通文件，跳过");
                return;
            }
            
            // 方式1: 使用 NIO Files.deleteIfExists (最可靠)
            boolean nioDeleted = java.nio.file.Files.deleteIfExists(file.toPath());
            System.out.println("[Feedback] NIO删除结果: " + (nioDeleted ? "成功" : "失败"));
            
            // 验证: 如果NIO未删除，尝试方式2
            if (file.exists()) {
                System.out.println("[Feedback] NIO删除后文件仍存在，尝试File.delete()...");
                boolean ioDeleted = file.delete();
                System.out.println("[Feedback] File.delete()结果: " + (ioDeleted ? "成功" : "失败"));
            }
            
            // 最终验证
            if (file.exists()) {
                System.out.println("[Feedback] *** 警告: 文件未能删除！***");
                System.out.println("[Feedback] 路径: " + absPath);
                // 尝试方式3: 使用 PictureSave 接口
                PictureSave ps = this.pictureSave.get(localorcloud);
                if (ps != null) {
                    boolean psDeleted = ps.deletefiles(absPath);
                    System.out.println("[Feedback] PictureSave删除结果: " + (psDeleted ? "成功" : "失败"));
                }
                // 最终验证
                if (!file.exists()) {
                    System.out.println("[Feedback] 文件最终已被删除");
                } else {
                    System.out.println("[Feedback] *** 文件删除失败，请检查权限或是否被占用 ***");
                }
            } else {
                System.out.println("[Feedback] 文件删除成功");
            }
        } catch (Exception e) {
            System.out.println("[Feedback] 删除文件异常: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public ResponseObjectEntity updateFeedback(Long id, String type, String title,
                                                String description, String contact, String images,
                                                String userId) {
        if (id == null) {
            return ResponseUtil.error("反馈ID不能为空");
        }
        if (title == null || title.trim().isEmpty()) {
            return ResponseUtil.error("反馈标题不能为空");
        }
        if (description == null || description.trim().isEmpty()) {
            return ResponseUtil.error("反馈描述不能为空");
        }
        
        // 检查反馈是否存在且属于该用户
        Feedback existing = feedbackDao.selectByIdAndUser(id, userId);
        if (existing == null) {
            return ResponseUtil.error("反馈不存在或无权编辑");
        }
        if (!"pending".equals(existing.getStatus())) {
            return ResponseUtil.error("仅待处理的反馈可编辑");
        }
        
        // 对比新旧图片，删除不再使用的旧图片
        String oldImages = existing.getImages();
        String newImages = images != null ? images : "";
        if (oldImages != null && !oldImages.isEmpty()) {
            // 解析旧图片列表和新图片列表
            List<String> oldUrls = parseImageUrls(oldImages);
            List<String> newUrls = parseImageUrls(newImages);
            
            // 找出需要删除的图片（在旧列表中但不在新列表中）
            List<String> toDelete = new ArrayList<>();
            for (String oldUrl : oldUrls) {
                if (!newUrls.contains(oldUrl)) {
                    toDelete.add(oldUrl);
                }
            }
            
            // 删除不再使用的旧图片
            if (!toDelete.isEmpty()) {
                System.out.println("[Feedback] 编辑时删除 " + toDelete.size() + " 个不再使用的旧图片");
                for (String url : toDelete) {
                    deleteSingleImage(url);
                }
            }
        }
        
        String now = DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new java.util.Date());
        
        int result = feedbackDao.updateFeedback(id, type, title.trim(), description.trim(), 
                contact != null ? contact.trim() : "", newImages, now);
        if (result > 0) {
            return ResponseUtil.success("更新成功");
        }
        return ResponseUtil.error("更新失败");
    }
    
    @Override
    public ResponseObjectEntity getAllFeedbacks(Map<String, Object> params) {
        List<Feedback> feedbacks = feedbackDao.selectAllWithFilter(params);
        return ResponseUtil.success(feedbacks);
    }

    @Override
    public ResponseObjectEntity getAllFeedbacksPaged(Map<String, Object> params, int page, int size) {
        if (page < 1) page = 1;
        if (size < 1) size = 20;
        if (size > 100) size = 100;
        int offset = (page - 1) * size;
        long total = feedbackDao.countAllWithFilter(params);
        List<Feedback> list = feedbackDao.selectAllWithFilterPaged(params, offset, size);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", total == 0 ? 0 : (int) ((total - 1) / size + 1));
        return ResponseUtil.success(result);
    }

    @Override
    public ResponseObjectEntity processFeedback(Long id, String status, String handlerId) {
        if (id == null) {
            return ResponseUtil.error("反馈ID不能为空");
        }
        if (status == null || status.isEmpty()) {
            return ResponseUtil.error("状态不能为空");
        }
        
        String now = DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new java.util.Date());
        int result = feedbackDao.updateStatus(id, status, handlerId, now);
        if (result > 0) {
            return ResponseUtil.success("状态更新成功");
        }
        return ResponseUtil.error("状态更新失败");
    }
    
    @Override
    public ResponseObjectEntity replyFeedback(Long id, String reply, String handlerId) {
        if (id == null) {
            return ResponseUtil.error("反馈ID不能为空");
        }
        if (reply == null || reply.trim().isEmpty()) {
            return ResponseUtil.error("回复内容不能为空");
        }
        
        String now = DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new java.util.Date());
        int result = feedbackDao.addReply(id, reply.trim(), handlerId, now);
        if (result > 0) {
            return ResponseUtil.success("回复成功");
        }
        return ResponseUtil.error("回复失败");
    }
    
    @Override
    public ResponseObjectEntity getFeedbackStats() {
        Map<String, Object> stats = feedbackDao.countStats();
        if (stats == null) {
            stats = new HashMap<>();
            stats.put("total", 0);
            stats.put("pending", 0);
            stats.put("processing", 0);
            stats.put("resolved", 0);
            stats.put("closed", 0);
        }
        return ResponseUtil.success(stats);
    }
}