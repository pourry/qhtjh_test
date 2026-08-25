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
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            carousel.setPictureLogic(existing.getId() + ext);
            carousel.setPicturePath(path);
            PictureSave ps = this.pictureSave.get(localorcloud);
            // 删除旧图片
            ps.deletefiles(existing.getPicturePath() + File.separator + existing.getPictureLogic());
            // 保存新图片
            boolean saved = ps.savefiles(file, path, existing.getId());
            if (!saved) {
                return ResponseUtil.error("图片保存失败");
            }
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
