package com.example.spring_boot_mode.model.service.impl;

import com.example.spring_boot_mode.model.dao.NovelDao;
import com.example.spring_boot_mode.model.dao.NovelPicturesDao;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.Novel;
import com.example.spring_boot_mode.model.entity.NovelPictures;
import com.example.spring_boot_mode.model.service.NovelService;
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
public class NovelServiceImpl implements NovelService {
    @Autowired
    private NovelDao novelDao;
    @Value("${picture.novel.path}")
    private String path;
    @Value("${picture.localorcloud}")
    private String localorcloud;
    @Value("${picture.novel.mappingPath}")
    private String mappingPath;
    @Autowired
    private Map<String, PictureSave> pictureSave;
    @Autowired
    private NovelPicturesDao novelPicturesDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity toadd(Novel novel, MultipartFile[] file) {
        //添加
        novel.setId(UUidUtil.getuuid());
        novel.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
        if(novel.getShare()){
            novel.setShareTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
        }
        int reint = novelDao.toadd(novel);
        if (reint<=0){
            return ResponseUtil.success("失败");
        }
        //添加 文件
        //根据配置 获取上传方式bean
        PictureSave pictureSave =this.pictureSave.get(localorcloud);
        List<NovelPictures> pictures = new ArrayList<NovelPictures>();
        NovelPictures novelPictures = null;
        if (file != null) {
            for (MultipartFile multipartFile : file) {
                novelPictures = new NovelPictures();
                novelPictures.setId(UUidUtil.getuuid());
                novelPictures.setPictureName(multipartFile.getOriginalFilename());
                novelPictures.setPictureLogic(novelPictures.getId()+multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")));
                novelPictures.setPicturePath(path);
                novelPictures.setSsnovelid(novel.getId());
                novelPictures.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
                boolean hassave = pictureSave.savefiles(multipartFile, path, novelPictures.getId());
                if (hassave) {
                    pictures.add(novelPictures);
                }
            }
        }
        if (pictures.size() > 0) {
            int i = novelPicturesDao.insertpictures(pictures);
            if (i<0){
                return ResponseUtil.error("失败");
            }
        }


        return ResponseUtil.success("成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity getList(int pageNumber,int passOver,int pageSize,Novel novel) {
        int total = novelDao.gettotal(novel);
        List<Novel> novelList = novelDao.getList(passOver,pageSize,novel);
        List<String> novelIds = new ArrayList<>();
        novelList.stream().forEach(item ->{novelIds.add(item.getId());});
        if (!novelIds.isEmpty()) {
            List<NovelPictures> pictures= novelPicturesDao.selectByanimationIds(novelIds);
            List<NovelPictures> childs = null;
            for (Novel item : novelList) {
                childs = new ArrayList<>();
                for (NovelPictures picture : pictures) {
                    if (item.getId().equals(picture.getSsnovelid())){
                        picture.setPictureUrl(mappingPath+ picture.getPictureLogic());
                        childs.add(picture);
                    }
                }
                item.setPictures(childs);
            }
        }

        return ResponseUtil.success(new PagingUtil(pageNumber,pageSize,novelList,total));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity toedit(Novel novel,MultipartFile[] file) {
        //如果 之前未被展示
        if (novel.getShare() ==null || !novel.getShare()){
            //当 现在展示
            if(novel.getShare()){
                novel.setShareTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
            }
        }
        int reint = novelDao.toedit(novel);
        if (reint<=0){
            return ResponseUtil.error("失败");
        }
        //判断animation.getPictures() 中 file 是否 需要已经删除
        List<NovelPictures> pictures = novelPicturesDao.selectIdByanimationId(novel.getId());
        AtomicBoolean b = new AtomicBoolean(true);
        pictures = pictures.stream().filter( picture ->{
            b.set(true);
            novel.getPictures().forEach(item ->{
                if (picture.getId().equals(item.getId())){
                    b.set(false);
                }

            });
            return b.get();
        }).collect(Collectors.toList());
        for (NovelPictures picture : pictures) {
            pictureSave.get(localorcloud).deletefiles(picture.getPicturePath() + File.separator + picture.getPictureLogic());
        }
        List<String> ids = pictures.stream().map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        if (!ids.isEmpty()) {
            int i = novelPicturesDao.deleteByids(ids);
            if (i <= 0) {
                return ResponseUtil.error("失败");
            }
        }


        //存储 file
        if (file != null) {
            List<NovelPictures> savepictures = new ArrayList<>();
            NovelPictures novelPictures = null;
            for (MultipartFile multipartFile : file) {
                novelPictures = new NovelPictures();
                novelPictures.setId(UUidUtil.getuuid());
                novelPictures.setPictureName(multipartFile.getOriginalFilename());
                novelPictures.setPictureLogic(novelPictures.getId()+multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")));
                novelPictures.setPicturePath(path);
                novelPictures.setSsnovelid(novel.getId());
                novelPictures.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
                boolean savefiles = pictureSave.get(localorcloud).savefiles(multipartFile, path, novelPictures.getId());
                if(savefiles){
                    savepictures.add(novelPictures);
                }
            }
            if (!savepictures.isEmpty()) {
                int insertpictures = novelPicturesDao.insertpictures(savepictures);
                if (insertpictures <=0){
                    return ResponseUtil.error("失败");
                }
            }


        }
        return ResponseUtil.success("成功");
    }

    @Override
    public ResponseObjectEntity getone(String id) {
        Novel novel = novelDao.getone(id);
        if (!Objects.isNull(novel)){
            return ResponseUtil.success(novel);
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
        int reint = novelDao.todelet(ids);
        //删除文件
        List<String> paths = novelPicturesDao.selectPathByanimationIds(ids);
        if (paths.size() >0){
            for (String s : paths) {
                pictureSave.get(localorcloud).deletefiles(s);
            }
        }
        //删除数据库
        novelPicturesDao.deleteByanmationids(ids);
        if (reint >0){
            return ResponseUtil.success("成功");
        }else {
            return ResponseUtil.error("失败");
        }
    }
}
