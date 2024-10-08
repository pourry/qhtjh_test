package com.example.spring_boot_mode.service;

import com.example.spring_boot_mode.entity.Animation;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface AnimationService {
    ResponseObjectEntity toadd(MultipartFile image, Animation animation);
}
