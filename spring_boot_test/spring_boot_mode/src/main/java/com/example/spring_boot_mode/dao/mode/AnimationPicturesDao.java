package com.example.spring_boot_mode.dao.mode;

import com.example.spring_boot_mode.entity.mode.AnimationPictures;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnimationPicturesDao {

    int insertpictures(List<AnimationPictures> pictures);

    List<AnimationPictures> selectByanimationIds(List<String> animationIds);

    List<String> selectPathByanimationIds(String[] ids);

    List<AnimationPictures> selectIdByanimationId(String id);

    int deleteByids(List<String> ids);

    int deleteByanmationids(String[] ids);
}
