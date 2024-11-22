package com.example.spring_boot_mode.model.web;


import cn.hutool.json.JSONUtil;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.Comic;
import com.example.spring_boot_mode.model.entity.ComicPictures;
import com.example.spring_boot_mode.model.entity.SysUser;
import com.example.spring_boot_mode.model.entity.Vo.ComicVo;
import com.example.spring_boot_mode.model.service.ComicService;
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
@RequestMapping("/comic")
public class ComicController {
    @Autowired
    private ComicService comicService;

    @PostMapping("toadd")
    public ResponseObjectEntity toadd(Comic comic, @RequestParam(value = "file",required = false) MultipartFile[] file, HttpServletRequest request  ){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        comic.setSscollector(sysUser.getId());
        comic.setObject(null);
        ResponseObjectEntity responseObjectEntity = comicService.toadd(comic,file);
        return responseObjectEntity;
    }
    @PostMapping("toedit")
    public ResponseObjectEntity toedit(Comic comic ,@RequestParam(value = "file",required = false) MultipartFile[] file, HttpServletRequest request){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        comic.setSscollector(sysUser.getId());
        List<ComicPictures> list=JSONUtil.toList( JSONUtil.parseArray(comic.getObject()) ,ComicPictures.class);
        comic.setPictures(list);
        ResponseObjectEntity responseObjectEntity = comicService.toedit(comic,file);
        return responseObjectEntity;
    }
    @GetMapping("getList")
    public ResponseObjectEntity getList(ComicVo comicVo, HttpServletRequest request){
        Comic comic = new Comic();
        BeanUtils.copyProperties(comicVo,comic);
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        comic.setSscollector(sysUser.getId());
        ResponseObjectEntity responseObjectEntity = comicService.getList(comicVo.getPageNumber(),comicVo.getPassOver(), comicVo.getPageSize(),comic);
        return responseObjectEntity;
    }
    @GetMapping("getone/{id}")
    public ResponseObjectEntity getone(@PathVariable("id")String id){

        ResponseObjectEntity responseObjectEntity = comicService.getone(id);
        return responseObjectEntity;
    }
    @PostMapping("todelete/{ids}")
    public ResponseObjectEntity todelete(@PathVariable("ids")String[] ids){
        ResponseObjectEntity responseObjectEntity = comicService.todelet(ids);
        return responseObjectEntity;
    }
}
