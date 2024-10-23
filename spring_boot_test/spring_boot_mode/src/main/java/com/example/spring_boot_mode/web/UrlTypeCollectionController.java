package com.example.spring_boot_mode.web;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.SysUser;
import com.example.spring_boot_mode.entity.mode.UrlTypeCollection;
import com.example.spring_boot_mode.service.UrlTypeCollectionService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.TokenUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/urlTypeCollection")
public class UrlTypeCollectionController {
    @Autowired
    UrlTypeCollectionService urlTypeCollectionService;

    @GetMapping("/geturltree")
    public ResponseObjectEntity  geturltree(HttpServletRequest request) {
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        ResponseObjectEntity responseObjectEntity = urlTypeCollectionService.geturltree(sysUser.getId());
        return responseObjectEntity;
    }
    @PostMapping("/toadd")
    public ResponseObjectEntity toadd(UrlTypeCollection urlTypeCollection,HttpServletRequest request){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        ResponseObjectEntity responseObjectEntity = urlTypeCollectionService.toadd(urlTypeCollection,sysUser.getId());
        return responseObjectEntity;
    }
    @PostMapping("/toedit")
    public ResponseObjectEntity toedit(UrlTypeCollection urlTypeCollection,HttpServletRequest request){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        ResponseObjectEntity responseObjectEntity = urlTypeCollectionService.toedit(urlTypeCollection);
        return responseObjectEntity;
    }
    @PostMapping("/todelete/{ids}")
    public ResponseObjectEntity todelete(@PathVariable("ids") String[] ids, HttpServletRequest request){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        ResponseObjectEntity responseObjectEntity = urlTypeCollectionService.todelete(ids);
        return responseObjectEntity;
    }

    @PostMapping("/tochange")                                 //     锚点节点id           被拖拽的节点id
    public ResponseObjectEntity tochange( String dropid,  String dragid, String dropType,HttpServletRequest request){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        ResponseObjectEntity responseObjectEntity = urlTypeCollectionService.tochange(dropid,dragid,dropType);
        return responseObjectEntity;
    }

}
