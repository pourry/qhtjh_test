package com.example.spring_boot_mode.model.service.impl;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.dao.CarouselDao;
import com.example.spring_boot_mode.model.entity.Carousel;
import com.example.spring_boot_mode.model.service.CarouselService;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.UUidUtil;
import com.example.spring_boot_mode.utils.pictureSave.PictureSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 走马灯业务实现类
 * 图片存储遵循项目全局策略：通过 picture.localorcloud 切换本地/OSS
 */
@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselDao carouselDao;

    /** 走马灯图片存储路径 */
    @Value("${picture.carousel.path}")
    private String path;

    /** 图片存储方式切换开关（localPictureSave / ossPictureSave） */
    @Value("${picture.localorcloud}")
    private String localorcloud;

    /** 图片访问映射路径 */
    @Value("${picture.carousel.mappingPath}")
    private String mappingPath;

    /** 图片存储策略映射（Spring 自动注入所有 PictureSave 实现） */
    @Autowired
    private Map<String, PictureSave> pictureSave;

    @Override
    public ResponseObjectEntity toadd(Carousel carousel, MultipartFile file) {
        // 生成主键
        carousel.setId(UUidUtil.getuuid());
        carousel.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new Date()));
        carousel.setEnabled(carousel.getEnabled() != null ? carousel.getEnabled() : true);
        carousel.setSort(carousel.getSort() > 0 ? carousel.getSort() : 0);

        // 保存图片（遵循全局图片存储策略）
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 逻辑文件名 = UUID + 扩展名
            carousel.setPictureLogic(carousel.getId() + ext);
            carousel.setPicturePath(path);
            // 根据配置选择存储方式
            PictureSave ps = this.pictureSave.get(localorcloud);
            boolean saved = ps.savefiles(file, path, carousel.getId());
            if (!saved) {
                return ResponseUtil.error("图片保存失败");
            }
        } else {
            return ResponseUtil.error("请上传走马灯图片");
        }

        int result = carouselDao.toadd(carousel);
        if (result <= 0) {
            return ResponseUtil.error("新增失败");
        }
        // 拼装完整访问URL（返回给前端）
        carousel.setPictureUrl(mappingPath + carousel.getPictureLogic());
        return ResponseUtil.success(carousel);
    }

    @Override
    public ResponseObjectEntity toedit(Carousel carousel, MultipartFile file) {
        Carousel existing = carouselDao.selectById(carousel.getId());
        if (existing == null) {
            return ResponseUtil.error("走马灯不存在");
        }

        // 如果传了新图片，替换上传
        if (file != null && !file.isEmpty()) {
            System.out.println("[Carousel] ========== 编辑走马灯，需要替换图片 ==========");
            System.out.println("[Carousel] 旧图片: picturePath=" + existing.getPicturePath() + ", pictureLogic=" + existing.getPictureLogic());
            
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            carousel.setPictureLogic(existing.getId() + ext);
            carousel.setPicturePath(path);
            PictureSave ps = this.pictureSave.get(localorcloud);
            
            // 构建旧图片的完整路径
            String oldFilePath = existing.getPicturePath() + File.separator + existing.getPictureLogic();
            System.out.println("[Carousel] 旧图片完整路径: " + oldFilePath);
            
            // 删除旧图片
            File oldFile = new File(oldFilePath);
            System.out.println("[Carousel] 旧文件存在: " + oldFile.exists() + ", isFile: " + oldFile.isFile());
            
            if (oldFile.exists() && oldFile.isFile()) {
                // 方式1: 使用 PictureSave 接口
                boolean deleted = ps.deletefiles(oldFilePath);
                System.out.println("[Carousel] PictureSave删除结果: " + (deleted ? "成功" : "失败"));
                
                // 方式2: 如果失败，使用 NIO 方式
                if (oldFile.exists()) {
                    System.out.println("[Carousel] PictureSave删除后文件仍存在，尝试NIO方式...");
                    try {
                        java.nio.file.Files.deleteIfExists(oldFile.toPath());
                        System.out.println("[Carousel] NIO方式删除完成");
                    } catch (Exception e) {
                        System.out.println("[Carousel] NIO删除失败: " + e.getMessage());
                    }
                }
                
                // 最终验证
                if (!oldFile.exists()) {
                    System.out.println("[Carousel] ✓ 旧图片已成功删除");
                } else {
                    System.out.println("[Carousel] ✗ 旧图片删除失败，请检查权限");
                }
            } else {
                System.out.println("[Carousel] 旧文件不存在，跳过删除");
            }
            
            // 保存新图片
            boolean saved = ps.savefiles(file, path, existing.getId());
            if (!saved) {
                return ResponseUtil.error("图片保存失败");
            }
            System.out.println("[Carousel] 新图片保存成功");
        } else {
            // 保留旧图片
            carousel.setPictureLogic(existing.getPictureLogic());
            carousel.setPicturePath(existing.getPicturePath());
        }

        int result = carouselDao.toedit(carousel);
        if (result <= 0) {
            return ResponseUtil.error("修改失败");
        }
        // 拼装完整访问URL（返回给前端）
        carousel.setPictureUrl(mappingPath + carousel.getPictureLogic());
        return ResponseUtil.success(carousel);
    }

    @Override
    public ResponseObjectEntity todelete(String id) {
        Carousel existing = carouselDao.selectById(id);
        if (existing == null) {
            return ResponseUtil.error("走马灯不存在");
        }
        // 删除图片文件
        PictureSave ps = this.pictureSave.get(localorcloud);
        ps.deletefiles(existing.getPicturePath() + File.separator + existing.getPictureLogic());
        // 删除数据库记录
        int result = carouselDao.todelete(id);
        if (result <= 0) {
            return ResponseUtil.error("删除失败");
        }
        return ResponseUtil.success("删除成功");
    }

    @Override
    public ResponseObjectEntity querylist() {
        List<Carousel> list = carouselDao.selectAll();
        // 为每条记录拼装 pictureUrl
        for (Carousel c : list) {
            c.setPictureUrl(mappingPath + c.getPictureLogic());
        }
        return ResponseUtil.success(list);
    }

    @Override
    public ResponseObjectEntity queryPage(int page, int size) {
        // 计算偏移量
        int offset = (page - 1) * size;
        
        // 查询总数
        int total = carouselDao.selectTotal();
        
        // 分页查询数据
        List<Carousel> list = carouselDao.selectPage(offset, size);
        
        // 为每条记录拼装 pictureUrl
        for (Carousel c : list) {
            c.setPictureUrl(mappingPath + c.getPictureLogic());
        }
        
        // 构建分页返回结果
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        
        return ResponseUtil.success(result);
    }

    @Override
    public ResponseObjectEntity queryenabled() {
        List<Carousel> list = carouselDao.selectEnabled();
        for (Carousel c : list) {
            c.setPictureUrl(mappingPath + c.getPictureLogic());
        }
        return ResponseUtil.success(list);
    }

    @Override
    public ResponseObjectEntity toggleEnabled(String id, Boolean enabled) {
        Carousel carousel = new Carousel();
        carousel.setId(id);
        carousel.setEnabled(enabled);
        int result = carouselDao.toedit(carousel);
        if (result <= 0) {
            return ResponseUtil.error("操作失败");
        }
        return ResponseUtil.success("操作成功");
    }
}
