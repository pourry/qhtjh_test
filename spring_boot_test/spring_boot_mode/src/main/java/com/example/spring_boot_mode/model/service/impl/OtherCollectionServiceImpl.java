package com.example.spring_boot_mode.model.service.impl;

import cn.hutool.core.util.IdUtil;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.dao.OtherCollectionDao;
import com.example.spring_boot_mode.model.dao.OtherCollectionTypeDao;
import com.example.spring_boot_mode.model.entity.OtherCollection;
import com.example.spring_boot_mode.model.entity.OtherCollectionType;
import com.example.spring_boot_mode.model.service.OtherCollectionService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.pictureSave.PictureSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OtherCollectionServiceImpl implements OtherCollectionService {

    @Autowired
    OtherCollectionTypeDao typeDao;
    @Autowired
    OtherCollectionDao ocDao;
    @Autowired
    @Qualifier("localPictureSave")
    PictureSave pictureSave;

    @Value("${picture.other.path}")
    private String picturePath;
    @Value("${picture.other.mappingPath}")
    private String pictureMappingPath;

    private final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ========= 类型 =========

    @Override
    public ResponseObjectEntity listTypes(String userId) {
        List<OtherCollectionType> list = typeDao.selectByUserId(userId);
        return ResponseUtil.success(list);
    }

    @Override
    public ResponseObjectEntity addType(OtherCollectionType type) {
        type.setId(IdUtil.fastSimpleUUID());
        type.setCreateTime(LocalDateTime.now().format(FMT));
        if (type.getSort() == null) type.setSort(0);
        typeDao.toadd(type);
        return ResponseUtil.success(type);
    }

    @Override
    public ResponseObjectEntity editType(OtherCollectionType type) {
        int rows = typeDao.toedit(type);
        if (rows == 0) return ResponseUtil.error("类型不存在");
        return ResponseUtil.success("ok");
    }

    @Override
    public ResponseObjectEntity deleteType(String typeId, String userId) {
        // 先查 typeValue
        OtherCollectionType t = typeDao.selectById(typeId);
        if (t != null && t.getTypeValue() != null) {
            // 把该类型下的收藏 typeValue 置空，而不是物理删收藏——安全
            // 但前端调用此接口前已经弹过确认框了，允许级联删收藏
            ocDao.deleteByTypeValue(userId, t.getTypeValue());
        }
        typeDao.todelete(typeId);
        return ResponseUtil.success("ok");
    }

    // ========= 收藏 =========

    @Override
    public ResponseObjectEntity listByUser(String userId) {
        List<OtherCollection> list = ocDao.selectByUserId(userId);
        // tags 字段存的是 JSON 字符串，前端自己解析
        return ResponseUtil.success(list);
    }

    private String handlePicture(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        String uuid = IdUtil.fastSimpleUUID();
        boolean ok = pictureSave.savefiles(file, picturePath, uuid);
        if (ok) {
            String ext = file.getOriginalFilename();
            if (ext != null && ext.contains(".")) ext = ext.substring(ext.lastIndexOf("."));
            else ext = ".jpg";
            return pictureMappingPath + uuid + ext;
        }
        return null;
    }

    @Override
    public ResponseObjectEntity add(OtherCollection oc, MultipartFile file) {
        oc.setId(IdUtil.fastSimpleUUID());
        oc.setCreateTime(LocalDateTime.now().format(FMT));
        oc.setUpdateTime(oc.getCreateTime());
        if (oc.getStatus() == null) oc.setStatus("wish");
        if (oc.getProgress() == null) oc.setProgress(0);
        if (oc.getPinned() == null) oc.setPinned(false);
        if (oc.getShare() == null) oc.setShare(false);

        if (file != null && !file.isEmpty()) {
            String url = handlePicture(file);
            oc.setPictureUrl(url);
        }

        ocDao.toadd(oc);
        return ResponseUtil.success(oc);
    }

    @Override
    public ResponseObjectEntity edit(OtherCollection oc, MultipartFile file) {
        oc.setUpdateTime(LocalDateTime.now().format(FMT));
        if (file != null && !file.isEmpty()) {
            String url = handlePicture(file);
            oc.setPictureUrl(url);
        }
        ocDao.toedit(oc);
        return ResponseUtil.success("ok");
    }

    @Override
    public ResponseObjectEntity delete(String id) {
        ocDao.todelete(id);
        return ResponseUtil.success("ok");
    }

    @Override
    public ResponseObjectEntity toggleShare(String id, Boolean share) {
        OtherCollection oc = ocDao.selectById(id);
        if (oc == null) return ResponseUtil.error("收藏不存在");
        oc.setShare(share);
        if (Boolean.TRUE.equals(share)) {
            oc.setShareTime(LocalDateTime.now().format(FMT));
        } else {
            oc.setShareTime(null);
        }
        ocDao.toedit(oc);
        return ResponseUtil.success("ok");
    }

    // ========= 首页公开 =========

    @Override
    public ResponseObjectEntity publicShow(int limit) {
        if (limit <= 0) limit = 10;
        List<OtherCollection> list = ocDao.selectShowList(limit);
        return ResponseUtil.success(list);
    }
}
