package com.example.spring_boot_mode.model.web;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.Carousel;
import com.example.spring_boot_mode.model.service.CarouselService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.TokenUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * 走马灯控制器
 * 提供走马灯的增删改查接口
 */
@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    /** 查询全部走马灯（管理页使用） */
    @GetMapping("/querylist")
    public ResponseObjectEntity querylist(HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return carouselService.querylist();
    }

    /** 查询启用的走马灯（首页展示，无需登录） */
    @GetMapping("/public/queryenabled")
    public ResponseObjectEntity queryenabled() {
        return carouselService.queryenabled();
    }

    /** 新增走马灯（含图片上传） */
    @PostMapping("/toadd")
    public ResponseObjectEntity toadd(Carousel carousel,
                                     @RequestParam(value = "file", required = false) MultipartFile file,
                                     HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return carouselService.toadd(carousel, file);
    }

    /** 修改走马灯（可选更新图片） */
    @PostMapping("/toedit")
    public ResponseObjectEntity toedit(Carousel carousel,
                                      @RequestParam(value = "file", required = false) MultipartFile file,
                                      HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return carouselService.toedit(carousel, file);
    }

    /** 删除走马灯 */
    @PostMapping("/todelete/{id}")
    public ResponseObjectEntity todelete(@PathVariable String id, HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        return carouselService.todelete(id);
    }

    /** 启用/禁用切换 */
    @PostMapping("/toggleEnabled")
    public ResponseObjectEntity toggleEnabled(@RequestBody Map<String, String> params,
                                              HttpServletRequest request) {
        if (Objects.isNull(TokenUtill.getSysUser(request))) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        String id = params.get("id");
        Boolean enabled = Boolean.parseBoolean(params.get("enabled"));
        return carouselService.toggleEnabled(id, enabled);
    }
}
