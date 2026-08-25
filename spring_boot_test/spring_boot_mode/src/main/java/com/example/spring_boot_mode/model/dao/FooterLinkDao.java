package com.example.spring_boot_mode.model.dao;

import com.example.spring_boot_mode.model.entity.FooterLink;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 底部快速链接数据访问接口
 */
@Mapper
public interface FooterLinkDao {

    /** 新增链接 */
    int toadd(FooterLink link);

    /** 修改链接 */
    int toedit(FooterLink link);

    /** 根据ID删除链接 */
    int todelete(String id);

    /** 查询全部链接（按排序升序） */
    List<FooterLink> selectAll();

    /** 查询启用的链接 */
    List<FooterLink> selectEnabled();

    /** 根据ID查询链接 */
    FooterLink selectById(String id);
}
