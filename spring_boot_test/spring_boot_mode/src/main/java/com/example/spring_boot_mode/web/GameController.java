package com.example.spring_boot_mode.web;


import cn.hutool.json.JSONUtil;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.*;
import com.example.spring_boot_mode.entity.mode.Vo.GameVo;
import com.example.spring_boot_mode.entity.mode.Vo.NovelVo;
import com.example.spring_boot_mode.service.GameService;
import com.example.spring_boot_mode.service.NovelService;
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
@RequestMapping("/game")
public class GameController {
    @Autowired
    private GameService gameService;

    @PostMapping("toadd")
    public ResponseObjectEntity toadd(Game game, @RequestParam(value = "file",required = false) MultipartFile[] file, HttpServletRequest request  ){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        game.setSscollector(sysUser.getId());
        game.setObject(null);
        ResponseObjectEntity responseObjectEntity = gameService.toadd(game,file);
        return responseObjectEntity;
    }
    @PostMapping("toedit")
    public ResponseObjectEntity toedit(Game game ,@RequestParam(value = "file",required = false) MultipartFile[] file, HttpServletRequest request){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        game.setSscollector(sysUser.getId());
        List<GamePictures> list=JSONUtil.toList( JSONUtil.parseArray(game.getObject()) ,GamePictures.class);
        game.setPictures(list);
        ResponseObjectEntity responseObjectEntity = gameService.toedit(game,file);
        return responseObjectEntity;
    }
    @GetMapping("getList")
    public ResponseObjectEntity getList(GameVo gameVo, HttpServletRequest request){
        Game game = new Game();
        BeanUtils.copyProperties(gameVo,game);
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        gameVo.setSscollector(sysUser.getId());
        ResponseObjectEntity responseObjectEntity = gameService.getList(gameVo.getPageNumber(),gameVo.getPassOver(), gameVo.getPageSize(),game);
        return responseObjectEntity;
    }
    @GetMapping("getone/{id}")
    public ResponseObjectEntity getone(@PathVariable("id")String id){

        ResponseObjectEntity responseObjectEntity = gameService.getone(id);
        return responseObjectEntity;
    }
    @PostMapping("todelete/{ids}")
    public ResponseObjectEntity todelete(@PathVariable("ids")String[] ids){
        ResponseObjectEntity responseObjectEntity = gameService.todelet(ids);
        return responseObjectEntity;
    }
}
