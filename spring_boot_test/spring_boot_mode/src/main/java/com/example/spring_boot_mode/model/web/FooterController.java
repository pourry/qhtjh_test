package com.example.spring_boot_mode.model.web;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.FooterLink;
import com.example.spring_boot_mode.model.entity.FooterSection;
import com.example.spring_boot_mode.model.service.FooterService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 底部内容管理控制器
 * 提供底部简介、联系方式、备案、快速链接的增删改查接口
 */
@RestController
@RequestMapping("/footer")
public class FooterController {

    @Autowired
    private FooterService footerService;

    // ==================== 内容板块接口 ====================

    /** 查询全部内容板块（管理端使用） */
    @GetMapping("/section/list")
    public ResponseObjectEntity getAllSections() {
        List<FooterSection> list = footerService.getAllSections();
        return ResponseUtil.success(list);
    }

    /** 根据类型查询板块 */
    @GetMapping("/section/type/{type}")
    public ResponseObjectEntity getSectionsByType(@PathVariable String type) {
        List<FooterSection> list = footerService.getSectionsByType(type);
        return ResponseUtil.success(list);
    }

    /** 新增内容板块 */
    @PostMapping("/section/add")
    public ResponseObjectEntity addSection(FooterSection section) {
        if (section.getType() == null || section.getType().isEmpty()) {
            return ResponseUtil.error("板块类型不能为空");
        }
        footerService.addSection(section);
        return ResponseUtil.success("新增成功");
    }

    /** 修改内容板块 */
    @PostMapping("/section/update")
    public ResponseObjectEntity updateSection(FooterSection section) {
        if (section.getId() == null || section.getId().isEmpty()) {
            return ResponseUtil.error("板块ID不能为空");
        }
        footerService.updateSection(section);
        return ResponseUtil.success("修改成功");
    }

    /** 删除内容板块 */
    @PostMapping("/section/delete")
    public ResponseObjectEntity deleteSection(@RequestParam String id) {
        if (id == null || id.isEmpty()) {
            return ResponseUtil.error("板块ID不能为空");
        }
        footerService.deleteSection(id);
        return ResponseUtil.success("删除成功");
    }

    // ==================== 快速链接接口 ====================

    /** 查询全部快速链接（管理端使用） */
    @GetMapping("/link/list")
    public ResponseObjectEntity getAllLinks() {
        List<FooterLink> list = footerService.getAllLinks();
        return ResponseUtil.success(list);
    }

    /** 新增快速链接 */
    @PostMapping("/link/add")
    public ResponseObjectEntity addLink(FooterLink link) {
        if (link.getName() == null || link.getName().isEmpty()) {
            return ResponseUtil.error("链接名称不能为空");
        }
        footerService.addLink(link);
        return ResponseUtil.success("新增成功");
    }

    /** 修改快速链接 */
    @PostMapping("/link/update")
    public ResponseObjectEntity updateLink(FooterLink link) {
        if (link.getId() == null || link.getId().isEmpty()) {
            return ResponseUtil.error("链接ID不能为空");
        }
        footerService.updateLink(link);
        return ResponseUtil.success("修改成功");
    }

    /** 删除快速链接 */
    @PostMapping("/link/delete")
    public ResponseObjectEntity deleteLink(@RequestParam String id) {
        if (id == null || id.isEmpty()) {
            return ResponseUtil.error("链接ID不能为空");
        }
        footerService.deleteLink(id);
        return ResponseUtil.success("删除成功");
    }

    // ==================== 前端展示接口 ====================

    /** 获取底部完整配置（前端展示使用，返回所有启用的板块和链接） */
    @GetMapping("/config")
    public ResponseObjectEntity getFooterConfig() {
        Map<String, Object> config = new HashMap<>();

        // 查询所有启用的内容板块
        List<FooterSection> sections = footerService.getAllSections();
        config.put("sections", sections);

        // 查询所有启用的快速链接
        List<FooterLink> links = footerService.getEnabledLinks();
        config.put("links", links);

        return ResponseUtil.success(config);
    }
}
