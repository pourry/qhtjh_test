package com.example.spring_boot_mode.web;


import com.example.spring_boot_mode.entity.mode.Animation;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.SysUser;
import com.example.spring_boot_mode.entity.mode.Vo.AnimationVo;
import com.example.spring_boot_mode.service.AnimationService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.TokenUtill;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/animation")
public class AnimationController {
    @Autowired
    private AnimationService animationService;

    @PostMapping("toadd")
    public ResponseObjectEntity toadd(Animation animation, HttpServletRequest request  ){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        animation.setSscollector(sysUser.getId());
        ResponseObjectEntity responseObjectEntity = animationService.toadd(animation);
        return responseObjectEntity;
    }
    @PostMapping("toedit")
    public ResponseObjectEntity toedit(Animation animation){
        ResponseObjectEntity responseObjectEntity = animationService.toedit(animation);
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
        ResponseObjectEntity responseObjectEntity = animationService.getList(animationVo.getPassOver(), animationVo.getPageSize(),animation);
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
