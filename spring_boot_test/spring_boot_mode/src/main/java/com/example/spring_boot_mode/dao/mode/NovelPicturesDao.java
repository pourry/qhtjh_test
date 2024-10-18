package com.example.spring_boot_mode.dao.mode;

import com.example.spring_boot_mode.entity.mode.ComicPictures;
import com.example.spring_boot_mode.entity.mode.NovelPictures;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NovelPicturesDao {

    int insertpictures(List<NovelPictures> pictures);

    List<NovelPictures> selectByanimationIds(List<String> animationIds);

    List<String> selectPathByanimationIds(String[] ids);

    List<NovelPictures> selectIdByanimationId(String id);

    int deleteByids(List<String> ids);

    int deleteByanmationids(String[] ids);
}