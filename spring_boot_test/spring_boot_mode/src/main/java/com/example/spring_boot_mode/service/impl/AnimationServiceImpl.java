package com.example.spring_boot_mode.service.impl;

import com.example.spring_boot_mode.entity.Animation;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.service.AnimationService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AnimationServiceImpl implements AnimationService {
    @Override
    public ResponseObjectEntity toadd(MultipartFile image, Animation animation) {
        System.out.println(animation);
        return ResponseUtil.success("新增成功");
    }
}
