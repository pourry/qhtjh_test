package com.example.spring_boot_mode.service;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.Animation;
import com.example.spring_boot_mode.entity.mode.Comic;
import org.springframework.web.multipart.MultipartFile;

public interface ComicService {
    ResponseObjectEntity toadd(Comic comic, MultipartFile[] file);

    ResponseObjectEntity getList(int pageNumber,int passOver,int pageSize,Comic comic);

    ResponseObjectEntity toedit(Comic comic, MultipartFile[] file);

    ResponseObjectEntity getone(String id);

    ResponseObjectEntity todelet(String[] ids);
}
