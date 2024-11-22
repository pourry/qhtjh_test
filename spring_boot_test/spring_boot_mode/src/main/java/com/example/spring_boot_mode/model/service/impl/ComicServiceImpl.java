package com.example.spring_boot_mode.model.service.impl;

import com.example.spring_boot_mode.model.dao.ComicDao;
import com.example.spring_boot_mode.model.dao.ComicPicturesDao;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.Comic;
import com.example.spring_boot_mode.model.entity.ComicPictures;
import com.example.spring_boot_mode.model.service.ComicService;
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
public class ComicServiceImpl implements ComicService {
    @Autowired
    private ComicDao comicDao;
    @Value("${picture.comic.path}")
    private String path;
    @Value("${picture.localorcloud}")
    private String localorcloud;
    @Value("${picture.comic.mappingPath}")
    private String mappingPath;
    @Autowired
    private Map<String, PictureSave> pictureSave;
    @Autowired
    private ComicPicturesDao comicPicturesDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity toadd(Comic comic, MultipartFile[] file) {
        //添加
        comic.setId(UUidUtil.getuuid());
        comic.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
        if(comic.getShare()){
            comic.setShareTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
        }
        int reint = comicDao.toadd(comic);
        if (reint<=0){
            return ResponseUtil.success("失败");
        }
        //添加 文件
        //根据配置 获取上传方式bean
        PictureSave pictureSave =this.pictureSave.get(localorcloud);
        List<ComicPictures> pictures = new ArrayList<ComicPictures>();
        ComicPictures comicPictures = null;
        if (file != null) {
            for (MultipartFile multipartFile : file) {
                comicPictures = new ComicPictures();
                comicPictures.setId(UUidUtil.getuuid());
                comicPictures.setPictureName(multipartFile.getOriginalFilename());
                comicPictures.setPictureLogic(comicPictures.getId()+multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")));
                comicPictures.setPicturePath(path);
                comicPictures.setSscomicid(comic.getId());
                comicPictures.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
                boolean hassave = pictureSave.savefiles(multipartFile, path, comicPictures.getId());
                if (hassave) {
                    pictures.add(comicPictures);
                }
            }
        }
        if (pictures.size() > 0) {
            int i = comicPicturesDao.insertpictures(pictures);
            if (i<0){
                return ResponseUtil.error("失败");
            }
        }


        return ResponseUtil.success("成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity getList(int pageNumber,int passOver,int pageSize,Comic comic) {
        int total = comicDao.gettotal(comic);
        List<Comic> comicList = comicDao.getList(passOver,pageSize,comic);
        List<String> comicIds = new ArrayList<>();
        comicList.stream().forEach(item ->{comicIds.add(item.getId());});
        if (!comicIds.isEmpty()) {
            List<ComicPictures> pictures= comicPicturesDao.selectByanimationIds(comicIds);
            List<ComicPictures> childs = null;
            for (Comic item : comicList) {
                childs = new ArrayList<>();
                for (ComicPictures picture : pictures) {
                    if (item.getId().equals(picture.getSscomicid())){
                        picture.setPictureUrl(mappingPath+ picture.getPictureLogic());
                        childs.add(picture);
                    }
                }
                item.setPictures(childs);
            }
        }

        return ResponseUtil.success(new PagingUtil(pageNumber,pageSize,comicList,total));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity toedit(Comic comic,MultipartFile[] file) {
        //如果 之前未被展示
        if (comic.getShare() ==null || !comic.getShare()){
            //当 现在展示
            if(comic.getShare()){
                comic.setShareTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
            }
        }
        int reint = comicDao.toedit(comic);
        if (reint<=0){
            return ResponseUtil.error("失败");
        }
        //判断animation.getPictures() 中 file 是否 需要已经删除
            List<ComicPictures> pictures = comicPicturesDao.selectIdByanimationId(comic.getId());
            AtomicBoolean b = new AtomicBoolean(true);
            pictures = pictures.stream().filter( picture ->{
                b.set(true);
                comic.getPictures().forEach(item ->{
                    if (picture.getId().equals(item.getId())){
                        b.set(false);
                    }

                });
                return b.get();
            }).collect(Collectors.toList());
            for (ComicPictures picture : pictures) {
                pictureSave.get(localorcloud).deletefiles(picture.getPicturePath() + File.separator + picture.getPictureLogic());
            }
            List<String> ids = pictures.stream().map(item -> {
                return item.getId();
            }).collect(Collectors.toList());
            if (!ids.isEmpty()) {
                int i = comicPicturesDao.deleteByids(ids);
                if (i <= 0) {
                    return ResponseUtil.error("失败");
                }
            }


        //存储 file
        if (file != null) {
            List<ComicPictures> savepictures = new ArrayList<>();
            ComicPictures comicPictures = null;
            for (MultipartFile multipartFile : file) {
                comicPictures = new ComicPictures();
                comicPictures.setId(UUidUtil.getuuid());
                comicPictures.setPictureName(multipartFile.getOriginalFilename());
                comicPictures.setPictureLogic(comicPictures.getId()+multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")));
                comicPictures.setPicturePath(path);
                comicPictures.setSscomicid(comic.getId());
                comicPictures.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
                boolean savefiles = pictureSave.get(localorcloud).savefiles(multipartFile, path, comicPictures.getId());
                if(savefiles){
                    savepictures.add(comicPictures);
                }
            }
            if (!savepictures.isEmpty()) {
                int insertpictures = comicPicturesDao.insertpictures(savepictures);
                if (insertpictures <=0){
                    return ResponseUtil.error("失败");
                }
            }


        }
        return ResponseUtil.success("成功");
    }

    @Override
    public ResponseObjectEntity getone(String id) {
        Comic comic = comicDao.getone(id);
        if (!Objects.isNull(comic)){
            return ResponseUtil.success(comic);
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
        int reint = comicDao.todelet(ids);
        //删除文件
        List<String> paths = comicPicturesDao.selectPathByanimationIds(ids);
        if (paths.size() >0){
            for (String s : paths) {
                pictureSave.get(localorcloud).deletefiles(s);
            }
        }
        //删除数据库
        comicPicturesDao.deleteByanmationids(ids);
        if (reint >0){
            return ResponseUtil.success("成功");
        }else {
            return ResponseUtil.error("失败");
        }
    }
}
