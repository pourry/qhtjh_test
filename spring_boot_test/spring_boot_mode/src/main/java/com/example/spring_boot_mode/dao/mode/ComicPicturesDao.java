package com.example.spring_boot_mode.dao.mode;

import com.example.spring_boot_mode.entity.mode.AnimationPictures;
import com.example.spring_boot_mode.entity.mode.ComicPictures;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ComicPicturesDao {

    int insertpictures(List<ComicPictures> pictures);

    List<ComicPictures> selectByanimationIds(List<String> animationIds);

    List<String> selectPathByanimationIds(String[] ids);

    List<ComicPictures> selectIdByanimationId(String id);

    int deleteByids(List<String> ids);

    int deleteByanmationids(String[] ids);
}
