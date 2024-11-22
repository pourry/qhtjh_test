package com.example.spring_boot_mode.model.dao;

import com.example.spring_boot_mode.model.entity.UrlTypeCollection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UrlTypeCollectionDao {
    List<UrlTypeCollection> selectbyuserId(String id);

    int toadd(UrlTypeCollection urlTypeCollection);

    int toedit(UrlTypeCollection urlTypeCollection);

    int todelete(String[] ids);

    int selectcountbyuserId(String userid);

    List<UrlTypeCollection> selectallbytwoid(@Param("dropid") String dropid,@Param("dragid") String dragid);

    int deletebytwoid(@Param("dropid") String dropid,@Param("dragid") String dragid);

    int insertList(List<UrlTypeCollection> urlTypeCollections);

    UrlTypeCollection selectbyid(String id);
}
