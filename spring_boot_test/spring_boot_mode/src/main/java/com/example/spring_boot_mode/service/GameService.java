package com.example.spring_boot_mode.service;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.Game;
import com.example.spring_boot_mode.entity.mode.Novel;
import org.springframework.web.multipart.MultipartFile;

public interface GameService {
    ResponseObjectEntity toadd(Game game, MultipartFile[] file);

    ResponseObjectEntity getList(int pageNumber,int passOver,int pageSize,Game game);

    ResponseObjectEntity toedit(Game game, MultipartFile[] file);

    ResponseObjectEntity getone(String id);

    ResponseObjectEntity todelet(String[] ids);
}
