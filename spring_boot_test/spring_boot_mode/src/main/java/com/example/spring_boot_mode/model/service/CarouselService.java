package com.example.spring_boot_mode.model.service;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.Carousel;
import org.springframework.web.multipart.MultipartFile;

/**
 * 走马灯业务接口
 */
public interface CarouselService {

    /** 新增走马灯（含图片上传） */
    ResponseObjectEntity toadd(Carousel carousel, MultipartFile file);

    /** 修改走马灯（可选更新图片） */
    ResponseObjectEntity toedit(Carousel carousel, MultipartFile file);

    /** 删除走马灯 */
    ResponseObjectEntity todelete(String id);

    /** 查询全部走马灯 */
    ResponseObjectEntity querylist();

    /** 查询启用的走马灯（供前端首页调用） */
    ResponseObjectEntity queryenabled();

    /** 启用/禁用切换 */
    ResponseObjectEntity toggleEnabled(String id, Boolean enabled);
}
