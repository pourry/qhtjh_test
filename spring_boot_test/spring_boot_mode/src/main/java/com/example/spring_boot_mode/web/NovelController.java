package com.example.spring_boot_mode.web;


import cn.hutool.json.JSONUtil;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.*;
import com.example.spring_boot_mode.entity.mode.Vo.NovelVo;
import com.example.spring_boot_mode.service.mode.NovelService;
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
@RequestMapping("/novel")
public class NovelController {
    @Autowired
    private NovelService novelService;

    @PostMapping("toadd")
    public ResponseObjectEntity toadd(Novel novel, @RequestParam(value = "file",required = false) MultipartFile[] file, HttpServletRequest request  ){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        novel.setSscollector(sysUser.getId());
        novel.setObject(null);
        ResponseObjectEntity responseObjectEntity = novelService.toadd(novel,file);
        return responseObjectEntity;
    }
    @PostMapping("toedit")
    public ResponseObjectEntity toedit(Novel novel ,@RequestParam(value = "file",required = false) MultipartFile[] file, HttpServletRequest request){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        novel.setSscollector(sysUser.getId());
        List<NovelPictures> list=JSONUtil.toList( JSONUtil.parseArray(novel.getObject()) ,NovelPictures.class);
        novel.setPictures(list);
        ResponseObjectEntity responseObjectEntity = novelService.toedit(novel,file);
        return responseObjectEntity;
    }
    @GetMapping("getList")
    public ResponseObjectEntity getList(NovelVo novelVo, HttpServletRequest request){
        Novel novel = new Novel();
        BeanUtils.copyProperties(novelVo,novel);
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        novelVo.setSscollector(sysUser.getId());
        ResponseObjectEntity responseObjectEntity = novelService.getList(novelVo.getPageNumber(),novelVo.getPassOver(), novelVo.getPageSize(),novel);
        return responseObjectEntity;
    }
    @GetMapping("getone/{id}")
    public ResponseObjectEntity getone(@PathVariable("id")String id){

        ResponseObjectEntity responseObjectEntity = novelService.getone(id);
        return responseObjectEntity;
    }
    @PostMapping("todelete/{ids}")
    public ResponseObjectEntity todelete(@PathVariable("ids")String[] ids, HttpServletRequest request){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        ResponseObjectEntity responseObjectEntity = novelService.todelet(ids);
        return responseObjectEntity;
    }
}
