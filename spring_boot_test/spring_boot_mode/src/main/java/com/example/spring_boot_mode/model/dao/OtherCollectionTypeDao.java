package com.example.spring_boot_mode.model.dao;

import com.example.spring_boot_mode.model.entity.OtherCollectionType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OtherCollectionTypeDao {
    List<OtherCollectionType> selectByUserId(@Param("userId") String userId);
    int toadd(OtherCollectionType type);
    int toedit(OtherCollectionType type);
    int todelete(@Param("id") String id);
    OtherCollectionType selectById(@Param("id") String id);
    int countByUserId(@Param("userId") String userId);
}
