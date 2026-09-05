package com.example.spring_boot_mode.model.service;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.OtherCollection;
import com.example.spring_boot_mode.model.entity.OtherCollectionType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OtherCollectionService {
    // 类型
    ResponseObjectEntity listTypes(String userId);
    ResponseObjectEntity addType(OtherCollectionType type);
    ResponseObjectEntity editType(OtherCollectionType type);
    ResponseObjectEntity deleteType(String typeId, String userId);

    // 收藏
    ResponseObjectEntity listByUser(String userId);
    ResponseObjectEntity add(OtherCollection oc, MultipartFile file);
    ResponseObjectEntity edit(OtherCollection oc, MultipartFile file);
    ResponseObjectEntity delete(String id);
    ResponseObjectEntity toggleShare(String id, Boolean share);

    // 首页公开接口
    ResponseObjectEntity publicShow(int limit);
}
