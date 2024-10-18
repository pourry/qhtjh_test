package com.example.spring_boot_mode.dao.mode;

import com.example.spring_boot_mode.entity.mode.Game;
import com.example.spring_boot_mode.entity.mode.Novel;
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
}
