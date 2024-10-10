package com.example.spring_boot_mode.web;


import com.example.spring_boot_mode.entity.mode.Animation;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.Vo.AnimationVo;
import com.example.spring_boot_mode.service.AnimationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animation")
public class AnimationController {
    @Autowired
    private AnimationService animationService;

    @PostMapping("toadd")
    public ResponseObjectEntity toadd(Animation animation){
        ResponseObjectEntity responseObjectEntity = animationService.toadd(animation);
        return responseObjectEntity;
    }
    @PostMapping("toedit")
    public ResponseObjectEntity toedit(Animation animation){
        ResponseObjectEntity responseObjectEntity = animationService.toedit(animation);
        return responseObjectEntity;
    }
    @GetMapping("getList")
    public ResponseObjectEntity getList(AnimationVo animationVo){
        Animation animation = new Animation();
        BeanUtils.copyProperties(animationVo,animation);
        ResponseObjectEntity responseObjectEntity = animationService.getList(animation);
        return responseObjectEntity;
    }
    @GetMapping("getone/{id}")
    public ResponseObjectEntity getone(@PathVariable("id")String id){
        ResponseObjectEntity responseObjectEntity = animationService.getone(id);
        return responseObjectEntity;
    }
    @PostMapping("todelete/{ids}")
    public ResponseObjectEntity todelete(@PathVariable("ids")String[] ids){
        ResponseObjectEntity responseObjectEntity = animationService.todelet(ids);
        return responseObjectEntity;
    }
}
