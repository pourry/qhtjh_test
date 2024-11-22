package com.example.spring_boot_mode.model.dao;

import com.example.spring_boot_mode.model.entity.Comic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ComicDao {
    int toadd(Comic comic);

    List<Comic> getList(@Param("passOver") int passOver, @Param("pageSize") int pageSize, @Param("animation")  Comic comic);

    int toedit(Comic comic);

    Comic getone(String id);

    int todelet(String[] ids);

    int gettotal(Comic comic);

    List<Comic> getComicShow();
}
