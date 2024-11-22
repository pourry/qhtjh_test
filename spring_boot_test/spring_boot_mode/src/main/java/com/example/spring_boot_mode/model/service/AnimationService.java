package com.example.spring_boot_mode.model.service;

import com.example.spring_boot_mode.model.entity.Animation;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import org.springframework.web.multipart.MultipartFile;


public interface AnimationService {
    ResponseObjectEntity toadd(Animation animation, MultipartFile[] file);

    ResponseObjectEntity getList(int pageNumber,int passOver,int pageSize,Animation animation);

    ResponseObjectEntity toedit(Animation animation,MultipartFile[] file);

    ResponseObjectEntity getone(String id);

    ResponseObjectEntity todelet(String[] ids);

}
