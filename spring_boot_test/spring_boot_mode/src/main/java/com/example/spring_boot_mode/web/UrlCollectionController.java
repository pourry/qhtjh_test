package com.example.spring_boot_mode.web;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.SysUser;
import com.example.spring_boot_mode.entity.mode.UrlCollection;
import com.example.spring_boot_mode.service.mode.UrlCollectionService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.TokenUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/urlCollection")
public class UrlCollectionController {
    @Autowired
    UrlCollectionService urlCollectionService;

    //首页 url热度查询展示
    @GetMapping("/public/urlhot")
    public ResponseObjectEntity urlhot(){
        ResponseObjectEntity responseObjectEntity = urlCollectionService.urlhot();
        return responseObjectEntity;
    }
    //首页 url收藏展示
    @GetMapping("/public/urlshow")
    public ResponseObjectEntity urlshow(){
        ResponseObjectEntity responseObjectEntity = urlCollectionService.urlshow();
        return responseObjectEntity;
    }

    //存储url的logo
    @PostMapping("/tosavelogo")
    public ResponseObjectEntity tosavelogo(UrlCollection urlCollection, HttpServletRequest request){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        ResponseObjectEntity responseObjectEntity = urlCollectionService.tosavelogo(urlCollection);
        return responseObjectEntity;
    }

    @PostMapping("/toadd")
    public ResponseObjectEntity toadd(UrlCollection urlCollection, HttpServletRequest request){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        ResponseObjectEntity responseObjectEntity = urlCollectionService.toadd(urlCollection,sysUser.getId());
        return responseObjectEntity;
    }
    @PostMapping("/toedit")
    public ResponseObjectEntity toedit(UrlCollection urlCollection,HttpServletRequest request){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        ResponseObjectEntity responseObjectEntity = urlCollectionService.toedit(urlCollection);
        return responseObjectEntity;
    }
    @PostMapping("/todelete/{ids}")
    public ResponseObjectEntity todelete(@PathVariable("ids") String[] ids, HttpServletRequest request){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        ResponseObjectEntity responseObjectEntity = urlCollectionService.todelete(ids);
        return responseObjectEntity;
    }
}
