package com.example.spring_boot_mode.model.service;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.Novel;
import org.springframework.web.multipart.MultipartFile;

public interface NovelService {
    ResponseObjectEntity toadd(Novel novel, MultipartFile[] file);

    ResponseObjectEntity getList(int pageNumber,int passOver,int pageSize,Novel novel);

    ResponseObjectEntity toedit(Novel novel, MultipartFile[] file);

    ResponseObjectEntity getone(String id);

    ResponseObjectEntity todelet(String[] ids);
}
