package com.example.spring_boot_mode.web;


import com.example.spring_boot_mode.entity.Animation;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.service.AnimationService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/animation")
public class AnimationController {
    @Autowired
    private AnimationService animationService;

    @PostMapping("toadd")                                          // 默认true
    public ResponseObjectEntity toadd(
                                      @RequestParam("animation")Animation animation){
        ResponseObjectEntity responseObjectEntity = animationService.toadd(null,animation);
        return responseObjectEntity;
    }
}
