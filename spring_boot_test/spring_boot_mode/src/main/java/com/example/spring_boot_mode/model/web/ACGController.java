package com.example.spring_boot_mode.model.web;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.service.ACGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/acg")
public class ACGController {
    @Autowired
    private ACGService acgService;

    //获取acg 包括动画 漫画 小说 游戏 每个10条（首页展示用）
    @GetMapping("/public/getshowAce")
    public ResponseObjectEntity getshowAce(){
        ResponseObjectEntity responseObjectEntity = acgService.getshowAce();
        return responseObjectEntity;
    }

    //获取acg热度排行 包括动画 漫画 小说 游戏（首页热度排行用）
    @GetMapping("/public/gethotAce")
    public ResponseObjectEntity gethotAce(){
        ResponseObjectEntity responseObjectEntity = acgService.gethotAce();
        return responseObjectEntity;
    }

}
