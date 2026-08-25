package com.example.spring_boot_mode.model.service.impl;

import com.example.spring_boot_mode.model.dao.FooterLinkDao;
import com.example.spring_boot_mode.model.dao.FooterSectionDao;
import com.example.spring_boot_mode.model.entity.FooterLink;
import com.example.spring_boot_mode.model.entity.FooterSection;
import com.example.spring_boot_mode.model.service.FooterService;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.UUidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 底部内容管理服务实现类
 */
@Service
public class FooterServiceImpl implements FooterService {

    @Autowired
    private FooterSectionDao footerSectionDao;

    @Autowired
    private FooterLinkDao footerLinkDao;

    // ========== 内容板块操作 ==========

    @Override
    public List<FooterSection> getAllSections() {
        return footerSectionDao.selectAll();
    }

    @Override
    public List<FooterSection> getSectionsByType(String type) {
        return footerSectionDao.selectByType(type);
    }

    @Override
    public void addSection(FooterSection section) {
        section.setId(UUidUtil.getuuid());
        String now = DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new Date());
        section.setCreateTime(now);
        section.setUpdateTime(now);
        footerSectionDao.toadd(section);
    }

    @Override
    public void updateSection(FooterSection section) {
        section.setUpdateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new Date()));
        footerSectionDao.toedit(section);
    }

    @Override
    public void deleteSection(String id) {
        footerSectionDao.todelete(id);
    }

    // ========== 快速链接操作 ==========

    @Override
    public List<FooterLink> getAllLinks() {
        return footerLinkDao.selectAll();
    }

    @Override
    public List<FooterLink> getEnabledLinks() {
        return footerLinkDao.selectEnabled();
    }

    @Override
    public void addLink(FooterLink link) {
        link.setId(UUidUtil.getuuid());
        String now = DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new Date());
        link.setCreateTime(now);
        link.setUpdateTime(now);
        footerLinkDao.toadd(link);
    }

    @Override
    public void updateLink(FooterLink link) {
        link.setUpdateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss", new Date()));
        footerLinkDao.toedit(link);
    }

    @Override
    public void deleteLink(String id) {
        footerLinkDao.todelete(id);
    }
}
