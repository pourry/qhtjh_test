package com.example.spring_boot_mode.model.dao;

import com.example.spring_boot_mode.model.entity.OtherCollection;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OtherCollectionDao {
    List<OtherCollection> selectByUserId(@Param("userId") String userId);
    List<OtherCollection> selectShowList(int limit);
    int toadd(OtherCollection oc);
    int toedit(OtherCollection oc);
    int todelete(@Param("id") String id);
    OtherCollection selectById(@Param("id") String id);
    int deleteByTypeValue(@Param("userId") String userId, @Param("typeValue") String typeValue);
}
