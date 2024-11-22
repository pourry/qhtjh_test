package com.example.spring_boot_mode.model.service.impl;

import com.example.spring_boot_mode.model.dao.GameDao;
import com.example.spring_boot_mode.model.dao.GamePicturesDao;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.Game;
import com.example.spring_boot_mode.model.entity.GamePictures;
import com.example.spring_boot_mode.model.service.GameService;
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
public class GameServiceImpl implements GameService {
    @Autowired
    private GameDao gameDao;
    @Value("${picture.game.path}")
    private String path;
    @Value("${picture.localorcloud}")
    private String localorcloud;
    @Value("${picture.game.mappingPath}")
    private String mappingPath;
    @Autowired
    private Map<String, PictureSave> pictureSave;
    @Autowired
    private GamePicturesDao gamePicturesDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity toadd(Game game, MultipartFile[] file) {
        //添加
        game.setId(UUidUtil.getuuid());
        game.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
        if(game.getShare()){
            game.setShareTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
        }
        int reint = gameDao.toadd(game);
        if (reint<=0){
            return ResponseUtil.success("失败");
        }
        //添加 文件
        //根据配置 获取上传方式bean
        PictureSave pictureSave =this.pictureSave.get(localorcloud);
        List<GamePictures> pictures = new ArrayList<GamePictures>();
        GamePictures gamePictures = null;
        if (file != null) {
            for (MultipartFile multipartFile : file) {
                gamePictures = new GamePictures();
                gamePictures.setId(UUidUtil.getuuid());
                gamePictures.setPictureName(multipartFile.getOriginalFilename());
                gamePictures.setPictureLogic(gamePictures.getId()+multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")));
                gamePictures.setPicturePath(path);
                gamePictures.setSsgameid(game.getId());
                gamePictures.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
                boolean hassave = pictureSave.savefiles(multipartFile, path, gamePictures.getId());
                if (hassave) {
                    pictures.add(gamePictures);
                }
            }
        }
        if (pictures.size() > 0) {
            int i = gamePicturesDao.insertpictures(pictures);
            if (i<0){
                return ResponseUtil.error("失败");
            }
        }


        return ResponseUtil.success("成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity getList(int pageNumber,int passOver,int pageSize,Game game) {
        int total = gameDao.gettotal(game);
        List<Game> novelList = gameDao.getList(passOver,pageSize,game);
        List<String> gameIds = new ArrayList<>();
        novelList.stream().forEach(item ->{gameIds.add(item.getId());});
        if (!gameIds.isEmpty()) {
            List<GamePictures> pictures= gamePicturesDao.selectByanimationIds(gameIds);
            List<GamePictures> childs = null;
            for (Game item : novelList) {
                childs = new ArrayList<>();
                for (GamePictures picture : pictures) {
                    if (item.getId().equals(picture.getSsgameid())){
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
    public ResponseObjectEntity toedit(Game game,MultipartFile[] file) {
        //如果 之前未被展示
        if (game.getShare() ==null || !game.getShare()){
            //当 现在展示
            if(game.getShare()){
                game.setShareTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
            }
        }
        int reint = gameDao.toedit(game);
        if (reint<=0){
            return ResponseUtil.error("失败");
        }
        //判断animation.getPictures() 中 file 是否 需要已经删除
        List<GamePictures> pictures = gamePicturesDao.selectIdByanimationId(game.getId());
        AtomicBoolean b = new AtomicBoolean(true);
        pictures = pictures.stream().filter( picture ->{
            b.set(true);
            game.getPictures().forEach(item ->{
                if (picture.getId().equals(item.getId())){
                    b.set(false);
                }

            });
            return b.get();
        }).collect(Collectors.toList());
        for (GamePictures picture : pictures) {
            pictureSave.get(localorcloud).deletefiles(picture.getPicturePath() + File.separator + picture.getPictureLogic());
        }
        List<String> ids = pictures.stream().map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        if (!ids.isEmpty()) {
            int i = gamePicturesDao.deleteByids(ids);
            if (i <= 0) {
                return ResponseUtil.error("失败");
            }
        }


        //存储 file
        if (file != null) {
            List<GamePictures> savepictures = new ArrayList<>();
            GamePictures gamePictures = null;
            for (MultipartFile multipartFile : file) {
                gamePictures = new GamePictures();
                gamePictures.setId(UUidUtil.getuuid());
                gamePictures.setPictureName(multipartFile.getOriginalFilename());
                gamePictures.setPictureLogic(gamePictures.getId()+multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")));
                gamePictures.setPicturePath(path);
                gamePictures.setSsgameid(game.getId());
                gamePictures.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
                boolean savefiles = pictureSave.get(localorcloud).savefiles(multipartFile, path, gamePictures.getId());
                if(savefiles){
                    savepictures.add(gamePictures);
                }
            }
            if (!savepictures.isEmpty()) {
                int insertpictures = gamePicturesDao.insertpictures(savepictures);
                if (insertpictures <=0){
                    return ResponseUtil.error("失败");
                }
            }


        }
        return ResponseUtil.success("成功");
    }

    @Override
    public ResponseObjectEntity getone(String id) {
        Game game = gameDao.getone(id);
        if (!Objects.isNull(game)){
            return ResponseUtil.success(game);
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
        int reint = gameDao.todelet(ids);
        //删除文件
        List<String> paths = gamePicturesDao.selectPathByanimationIds(ids);
        if (paths.size() >0){
            for (String s : paths) {
                pictureSave.get(localorcloud).deletefiles(s);
            }
        }
        //删除数据库
        gamePicturesDao.deleteByanmationids(ids);
        if (reint >0){
            return ResponseUtil.success("成功");
        }else {
            return ResponseUtil.error("失败");
        }
    }
}
