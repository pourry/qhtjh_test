package com.example.spring_boot_mode.model.dao;

import com.example.spring_boot_mode.model.entity.Animation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnimationDao {
    int toadd(Animation animation);

    List<Animation> getList(@Param("passOver") int passOver,@Param("pageSize") int pageSize,@Param("animation")  Animation animation);

    int toedit(Animation animation);

    Animation getone(String id);

    int todelet(String[] ids);

    int gettotal(Animation animation);

    List<Animation> getAnimationShow();
}
