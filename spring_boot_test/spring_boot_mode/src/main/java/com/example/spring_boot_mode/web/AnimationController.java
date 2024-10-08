package com.example.spring_boot_mode.web;


import com.example.spring_boot_mode.entity.mode.Animation;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.service.AnimationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animation")
public class AnimationController {
    @Autowired
    private AnimationService animationService;

    @PostMapping("toadd")                                          // 默认true
    public ResponseObjectEntity toadd(Animation animation){
        ResponseObjectEntity responseObjectEntity = animationService.toadd(animation);
        return responseObjectEntity;
    }
    @GetMapping("getList")                                          // 默认true
    public ResponseObjectEntity getList(Animation animation){
        ResponseObjectEntity responseObjectEntity = animationService.getList(animation);
        return responseObjectEntity;
    }
}
