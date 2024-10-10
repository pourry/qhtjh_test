package com.example.spring_boot_mode.dao.mode;

import com.example.spring_boot_mode.entity.mode.Animation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnimationDao {
    int toadd(Animation animation);

    List<Animation> getList(Animation animation);

    int toedit(Animation animation);

    Animation getone(String id);

    int todelet(String[] ids);
}
