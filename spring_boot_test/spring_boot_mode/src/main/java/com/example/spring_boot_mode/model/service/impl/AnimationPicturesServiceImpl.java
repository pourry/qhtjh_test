package com.example.spring_boot_mode.model.service.impl;

import com.example.spring_boot_mode.model.dao.AnimationPicturesDao;
import com.example.spring_boot_mode.model.service.AnimationPicturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimationPicturesServiceImpl implements AnimationPicturesService {
    @Autowired
    private AnimationPicturesDao animationPicturesDao;
}
