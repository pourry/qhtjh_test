package com.example.spring_boot_mode.dao.mode;

import com.example.spring_boot_mode.entity.mode.Comic;
import com.example.spring_boot_mode.entity.mode.Novel;
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
}
