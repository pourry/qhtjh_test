package com.example.spring_boot_mode.model.web;


import cn.hutool.json.JSONUtil;
import com.example.spring_boot_mode.model.entity.Animation;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.AnimationPictures;
import com.example.spring_boot_mode.model.entity.SysUser;
import com.example.spring_boot_mode.model.entity.Vo.AnimationVo;
import com.example.spring_boot_mode.model.service.AnimationService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.TokenUtill;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/animation")
public class AnimationController {
    @Autowired
    private AnimationService animationService;


    @PostMapping("toadd")
    public ResponseObjectEntity toadd(Animation animation,@RequestParam(value = "file",required = false) MultipartFile[] file, HttpServletRequest request  ){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        animation.setSscollector(sysUser.getId());
        animation.setObject(null);
        ResponseObjectEntity responseObjectEntity = animationService.toadd(animation,file);
        return responseObjectEntity;
    }
    @PostMapping("toedit")
    public ResponseObjectEntity toedit(Animation animation ,@RequestParam(value = "file",required = false) MultipartFile[] file, HttpServletRequest request){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        animation.setSscollector(sysUser.getId());
        List<AnimationPictures> list=JSONUtil.toList( JSONUtil.parseArray(animation.getObject()) ,AnimationPictures.class);
        animation.setPictures(list);
        ResponseObjectEntity responseObjectEntity = animationService.toedit(animation,file);
        return responseObjectEntity;
    }
    @GetMapping("getList")
    public ResponseObjectEntity getList(AnimationVo animationVo, HttpServletRequest request){
        Animation animation = new Animation();
        BeanUtils.copyProperties(animationVo,animation);
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        animation.setSscollector(sysUser.getId());
        ResponseObjectEntity responseObjectEntity = animationService.getList(animationVo.getPageNumber(),animationVo.getPassOver(), animationVo.getPageSize(),animation);
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
