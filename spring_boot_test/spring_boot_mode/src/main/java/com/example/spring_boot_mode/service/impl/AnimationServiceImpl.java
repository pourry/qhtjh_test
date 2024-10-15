package com.example.spring_boot_mode.service.impl;

import com.example.spring_boot_mode.dao.mode.AnimationDao;
import com.example.spring_boot_mode.dao.mode.AnimationPicturesDao;
import com.example.spring_boot_mode.entity.mode.Animation;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.AnimationPictures;
import com.example.spring_boot_mode.service.AnimationService;
import com.example.spring_boot_mode.utils.PagingUtil;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.UUidUtil;
import com.example.spring_boot_mode.utils.pictureSave.PictureSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AnimationServiceImpl implements AnimationService {
    @Autowired
    private AnimationDao animationDao;
    @Value("${picture.animation.path}")
    private String path;
    @Value("${picture.localorcloud}")
    private String localorcloud;
    @Autowired
    private Map<String,PictureSave> pictureSave;
    @Autowired
    private AnimationPicturesDao animationPicturesDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity toadd(Animation animation, MultipartFile[] file) {
        //添加
        animation.setId(UUidUtil.getuuid());
        int reint = animationDao.toadd(animation);
        if (reint<=0){
            return ResponseUtil.success("失败");
        }
        //添加 文件
        //根据配置 获取上传方式bean
        PictureSave pictureSave =this.pictureSave.get(localorcloud);
        List<AnimationPictures> pictures = new ArrayList<AnimationPictures>();
        AnimationPictures animationPictures = null;
        for (MultipartFile multipartFile : file) {
            animationPictures = new AnimationPictures();
            animationPictures.setId(UUidUtil.getuuid());
            animationPictures.setPictureName(multipartFile.getOriginalFilename());
            animationPictures.setPictureLogic(animationPictures.getId()+multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")));
            animationPictures.setPicturePath(path);
            animationPictures.setSsanimationid(animation.getId());
            boolean hassave = pictureSave.savefiles(multipartFile, path, animationPictures.getId());
            if (hassave) {
                pictures.add(animationPictures);
            }
        }
        if (pictures.size() > 0) {
            int i = animationPicturesDao.insertpictures(pictures);
            if (i>0){
                return ResponseUtil.success("成功");
            }
        }


        return ResponseUtil.error("失败");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity getList(int pageNumber,int passOver,int pageSize,Animation animation) {
        int total = animationDao.gettotal(animation);
        List<Animation> animationList = animationDao.getList(passOver,pageSize,animation);
        List<String> animationIds = new ArrayList<>();
        animationList.stream().forEach(item ->{animationIds.add(item.getId());});
        List<AnimationPictures> pictures= animationPicturesDao.selectByanimationIds(animationIds);
        List<AnimationPictures> childs = null;
        for (Animation item : animationList) {
            childs = new ArrayList<>();
            for (AnimationPictures picture : pictures) {
                if (item.getId().equals(picture.getSsanimationid())){
                    childs.add(picture);
                }
            }
        }
        return ResponseUtil.success(new PagingUtil(pageNumber,pageSize,animationList,total));
    }

    @Override
    public ResponseObjectEntity toedit(Animation animation) {
        int reint = animationDao.toedit(animation);
        if (reint>0){
            return ResponseUtil.success("成功");
        }else {
            return ResponseUtil.error("失败");
        }
    }

    @Override
    public ResponseObjectEntity getone(String id) {
        Animation animation = animationDao.getone(id);
        if (!Objects.isNull(animation)){
            return ResponseUtil.success(animation);
        }else {
            return ResponseUtil.error("未查询到");
        }
    }

    @Override
    public ResponseObjectEntity todelet(String[] ids) {
        if (ids == null ||ids.length<=0){
            return ResponseUtil.error("删除失败");
        }
        int reint = animationDao.todelet(ids);
        if (reint >0){
            return ResponseUtil.success("成功");
        }else {
            return ResponseUtil.error("失败");
        }
    }
}
