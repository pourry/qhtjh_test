package com.example.spring_boot_mode.model.dao;

import com.example.spring_boot_mode.model.entity.FooterSection;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 底部内容板块数据访问接口
 */
@Mapper
public interface FooterSectionDao {

    /** 新增板块 */
    int toadd(FooterSection section);

    /** 修改板块 */
    int toedit(FooterSection section);

    /** 根据ID删除板块 */
    int todelete(String id);

    /** 查询全部板块（按排序升序） */
    List<FooterSection> selectAll();

    /** 根据类型查询板块 */
    List<FooterSection> selectByType(String type);

    /** 根据ID查询板块 */
    FooterSection selectById(String id);
}
