package com.example.spring_boot_mode.service;

import com.example.spring_boot_mode.entity.mode.Animation;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;


public interface AnimationService {
    ResponseObjectEntity toadd(Animation animation);

    ResponseObjectEntity getList(Animation animationVo);
}
