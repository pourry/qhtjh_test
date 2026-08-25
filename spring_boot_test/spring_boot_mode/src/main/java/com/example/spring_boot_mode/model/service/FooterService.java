package com.example.spring_boot_mode.model.service;

import com.example.spring_boot_mode.model.entity.FooterSection;
import com.example.spring_boot_mode.model.entity.FooterLink;

import java.util.List;

/**
 * 底部内容管理服务接口
 */
public interface FooterService {

    // ========== 内容板块操作 ==========

    /** 查询全部内容板块 */
    List<FooterSection> getAllSections();

    /** 根据类型查询板块 */
    List<FooterSection> getSectionsByType(String type);

    /** 新增内容板块 */
    void addSection(FooterSection section);

    /** 修改内容板块 */
    void updateSection(FooterSection section);

    /** 删除内容板块 */
    void deleteSection(String id);

    // ========== 快速链接操作 ==========

    /** 查询全部快速链接 */
    List<FooterLink> getAllLinks();

    /** 查询启用的快速链接 */
    List<FooterLink> getEnabledLinks();

    /** 新增快速链接 */
    void addLink(FooterLink link);

    /** 修改快速链接 */
    void updateLink(FooterLink link);

    /** 删除快速链接 */
    void deleteLink(String id);
}
