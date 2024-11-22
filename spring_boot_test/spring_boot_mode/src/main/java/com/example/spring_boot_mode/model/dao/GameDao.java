package com.example.spring_boot_mode.model.dao;

import com.example.spring_boot_mode.model.entity.Game;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GameDao {
    int toadd(Game game);

    List<Game> getList(@Param("passOver") int passOver, @Param("pageSize") int pageSize, @Param("animation")  Game game);

    int toedit(Game game);

    Game getone(String id);

    int todelet(String[] ids);

    int gettotal(Game game);

    List<Game> getGameShow();
}
