package com.example.spring_boot_mode.model.dao;

import com.example.spring_boot_mode.model.entity.GamePictures;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GamePicturesDao {

    int insertpictures(List<GamePictures> pictures);

    List<GamePictures> selectByanimationIds(List<String> animationIds);

    List<String> selectPathByanimationIds(String[] ids);

    List<GamePictures> selectIdByanimationId(String id);

    int deleteByids(List<String> ids);

    int deleteByanmationids(String[] ids);
}
