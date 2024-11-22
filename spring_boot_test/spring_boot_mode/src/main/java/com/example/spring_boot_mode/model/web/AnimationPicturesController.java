package com.example.spring_boot_mode.model.web;

import com.example.spring_boot_mode.model.service.AnimationPicturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/animationPictures")
public class AnimationPicturesController {
    @Autowired
    AnimationPicturesService animationPicturesService;
}
