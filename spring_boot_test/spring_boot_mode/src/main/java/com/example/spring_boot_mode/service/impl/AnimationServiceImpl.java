package com.example.spring_boot_mode.service.impl;

import com.example.spring_boot_mode.dao.mode.AnimationDao;
import com.example.spring_boot_mode.dao.mode.AnimationPicturesDao;
import com.example.spring_boot_mode.entity.mode.Animation;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.AnimationPictures;
import com.example.spring_boot_mode.service.AnimationService;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.PagingUtil;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.UUidUtil;
import com.example.spring_boot_mode.utils.pictureSave.PictureSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class AnimationServiceImpl implements AnimationService {
    @Autowired
    private AnimationDao animationDao;
    @Value("${picture.animation.path}")
    private String path;
    @Value("${picture.localorcloud}")
    private String localorcloud;
    @Value("${picture.animation.mappingPath}")
    private String mappingPath;
    @Autowired
    private Map<String,PictureSave> pictureSave;
    @Autowired
    private AnimationPicturesDao animationPicturesDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity toadd(Animation animation, MultipartFile[] file) {
        //添加
        animation.setId(UUidUtil.getuuid());
        animation.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
        if(animation.getShare()){
            animation.setShareTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
        }
        int reint = animationDao.toadd(animation);
        if (reint<=0){
            return ResponseUtil.success("失败");
        }
        //添加 文件
        //根据配置 获取上传方式bean
        PictureSave pictureSave =this.pictureSave.get(localorcloud);
        List<AnimationPictures> pictures = new ArrayList<AnimationPictures>();
        AnimationPictures animationPictures = null;
        if (file != null) {
            for (MultipartFile multipartFile : file) {
                animationPictures = new AnimationPictures();
                animationPictures.setId(UUidUtil.getuuid());
                animationPictures.setPictureName(multipartFile.getOriginalFilename());
                animationPictures.setPictureLogic(animationPictures.getId()+multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")));
                animationPictures.setPicturePath(path);
                animationPictures.setSsanimationid(animation.getId());
                animationPictures.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
                boolean hassave = pictureSave.savefiles(multipartFile, path, animationPictures.getId());
                if (hassave) {
                    pictures.add(animationPictures);
                }
            }
        }
        if (pictures.size() > 0) {
            int i = animationPicturesDao.insertpictures(pictures);
            if (i<0){
                return ResponseUtil.error("失败");
            }
        }


        return ResponseUtil.success("成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity getList(int pageNumber,int passOver,int pageSize,Animation animation) {
        int total = animationDao.gettotal(animation);
        List<Animation> animationList = animationDao.getList(passOver,pageSize,animation);
        List<String> animationIds = new ArrayList<>();
        animationList.stream().forEach(item ->{animationIds.add(item.getId());});
        if (!animationIds.isEmpty()) {
            List<AnimationPictures> pictures= animationPicturesDao.selectByanimationIds(animationIds);
            List<AnimationPictures> childs = null;
            for (Animation item : animationList) {
                childs = new ArrayList<>();
                for (AnimationPictures picture : pictures) {
                    if (item.getId().equals(picture.getSsanimationid())){
                        picture.setPictureUrl(mappingPath+ picture.getPictureLogic());
                        childs.add(picture);
                    }
                }
                item.setPictures(childs);
            }
        }

        return ResponseUtil.success(new PagingUtil(pageNumber,pageSize,animationList,total));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity toedit(Animation animation,MultipartFile[] file) {
        //如果 之前未被展示
        if (animation.getShare() ==null || !animation.getShare()){
            //当 现在展示
            if(animation.getShare()){
                animation.setShareTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
            }
        }
        int reint = animationDao.toedit(animation);
        if (reint<=0){
            return ResponseUtil.error("失败");
        }
        //判断animation.getPictures() 中 file 是否 需要已经删除
            List<AnimationPictures> pictures = animationPicturesDao.selectIdByanimationId(animation.getId());
            AtomicBoolean b = new AtomicBoolean(true);
            pictures = pictures.stream().filter( picture ->{
                           b.set(true);
                           animation.getPictures().forEach(item ->{
                               if (picture.getId().equals(item.getId())){
                                   b.set(false);
                               }

                           });
                           return b.get();
                       }).collect(Collectors.toList());
            for (AnimationPictures picture : pictures) {
                pictureSave.get(localorcloud).deletefiles(picture.getPicturePath() + File.separator + picture.getPictureLogic());
            }
            List<String> ids = pictures.stream().map(item -> {
                                       return item.getId();
                                   }).collect(Collectors.toList());
            if (!ids.isEmpty()) {
                int i = animationPicturesDao.deleteByids(ids);
                if (i <= 0) {
                    return ResponseUtil.error("失败");
                }
            }



        //存储 file
        if (file != null) {
            List<AnimationPictures> savepictures = new ArrayList<>();
            AnimationPictures animationPictures = null;
            for (MultipartFile multipartFile : file) {
                animationPictures = new AnimationPictures();
                animationPictures.setId(UUidUtil.getuuid());
                animationPictures.setPictureName(multipartFile.getOriginalFilename());
                animationPictures.setPictureLogic(animationPictures.getId()+multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")));
                animationPictures.setPicturePath(path);
                animationPictures.setSsanimationid(animation.getId());
                animationPictures.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
                boolean savefiles = pictureSave.get(localorcloud).savefiles(multipartFile, path, animationPictures.getId());
                if(savefiles){
                    savepictures.add(animationPictures);
                }
            }
            if (!savepictures.isEmpty()) {
                int insertpictures = animationPicturesDao.insertpictures(savepictures);
                if (insertpictures <=0){
                    return ResponseUtil.error("失败");
                }
            }


        }
        return ResponseUtil.success("成功");
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

    @Transactional
    @Override
    public ResponseObjectEntity todelet(String[] ids) {
        if (ids == null ||ids.length<=0){
            return ResponseUtil.error("删除失败");
        }
        //删除
        int reint = animationDao.todelet(ids);
        //删除文件
        List<String> paths = animationPicturesDao.selectPathByanimationIds(ids);
        if (paths.size() >0){
            for (String s : paths) {
                pictureSave.get(localorcloud).deletefiles(s);
            }
        }
        //删除数据库
        animationPicturesDao.deleteByanmationids(ids);
        if (reint >0){
            return ResponseUtil.success("成功");
        }else {
            return ResponseUtil.error("失败");
        }
    }
}
