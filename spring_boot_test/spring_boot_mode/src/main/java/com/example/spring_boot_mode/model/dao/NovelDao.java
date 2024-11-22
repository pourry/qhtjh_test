package com.example.spring_boot_mode.model.dao;

import com.example.spring_boot_mode.model.entity.Novel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NovelDao {
    int toadd(Novel novel);

    List<Novel> getList(@Param("passOver") int passOver, @Param("pageSize") int pageSize, @Param("animation")  Novel novel);

    int toedit(Novel novel);

    Novel getone(String id);

    int todelet(String[] ids);

    int gettotal(Novel novel);

    List<Novel> getNovelShow();
}
