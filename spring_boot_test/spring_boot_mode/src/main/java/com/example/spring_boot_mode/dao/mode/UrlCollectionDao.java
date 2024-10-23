package com.example.spring_boot_mode.dao.mode;

import com.example.spring_boot_mode.entity.mode.UrlCollection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UrlCollectionDao {
    List<UrlCollection> selectbytypeids(List<String> typeids);

    int toadd(UrlCollection urlCollection);

    int toedit(UrlCollection urlCollection);

    int todelete(String[] ids);

    int todeleteBytypeId(String[] ids);

    int selectcountbytypeidanduserid(@Param("ssurltypeid") String ssurltypeid,@Param("userid") String userid);

    List<UrlCollection> selectallbytwoid(@Param("dropid") String dropid,@Param("dragid") String dragid);

    int deletebytwoid(@Param("dropid") String dropid,@Param("dragid") String dragid);

    int insertList(List<UrlCollection> urlCollections);

    UrlCollection selectbyid(String id);
}
