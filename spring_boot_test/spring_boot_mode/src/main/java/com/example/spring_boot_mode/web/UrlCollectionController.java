package com.example.spring_boot_mode.web;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.SysUser;
import com.example.spring_boot_mode.entity.mode.UrlCollection;
import com.example.spring_boot_mode.entity.mode.UrlTypeCollection;
import com.example.spring_boot_mode.service.UrlCollectionService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.TokenUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/urlCollection")
public class UrlCollectionController {
    @Autowired
    UrlCollectionService urlCollectionService;

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
