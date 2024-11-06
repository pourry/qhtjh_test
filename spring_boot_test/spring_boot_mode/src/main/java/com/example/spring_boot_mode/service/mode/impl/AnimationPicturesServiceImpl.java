package com.example.spring_boot_mode.service.mode.impl;

import com.example.spring_boot_mode.dao.mode.AnimationPicturesDao;
import com.example.spring_boot_mode.service.mode.AnimationPicturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimationPicturesServiceImpl implements AnimationPicturesService {
    @Autowired
    private AnimationPicturesDao animationPicturesDao;
}
