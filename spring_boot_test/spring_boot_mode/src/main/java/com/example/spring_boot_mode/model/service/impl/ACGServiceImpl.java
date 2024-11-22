package com.example.spring_boot_mode.model.service.impl;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.dao.*;
import com.example.spring_boot_mode.model.entity.*;
import com.example.spring_boot_mode.model.service.ACGService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ACGServiceImpl implements ACGService {
    @Autowired
    AnimationDao animationDao;
    @Autowired
    AnimationPicturesDao animationPicturesDao;
    @Value("${picture.animation.mappingPath}")
    private String animationMappingPath;
    @Autowired
    ComicDao comicDao;
    @Autowired
    ComicPicturesDao comicPicturesDao;
    @Value("${picture.comic.mappingPath}")
    private String comicMappingPath;
    @Autowired
    NovelDao novelDao;
    @Autowired
    NovelPicturesDao novelPicturesDao;
    @Value("${picture.novel.mappingPath}")
    private String novelMappingPath;
    @Autowired
    GameDao gameDao;
    @Autowired
    GamePicturesDao gamePicturesDao;
    @Value("${picture.game.mappingPath}")
    private String gameMappingPath;
    @Override
    public ResponseObjectEntity getshowAce() {
        List<Animation> animationList = animationDao.getAnimationShow();
        List<String> animationids = animationList.stream().map(item->{
            return item.getId();
        }).collect(Collectors.toList());
        if(animationids.size()>0){
            List<AnimationPictures> animationPictures = animationPicturesDao.selectByanimationIds(animationids);
            animationList = animationList.stream().map(item -> {
                item.setPictures(new ArrayList<>());
                animationPictures.stream().forEach(picture ->{
                    if(picture.getPictureUrl() == null){
                        picture.setPictureUrl(animationMappingPath + picture.getPictureLogic());
                    }
                    if(item.getId().equals(picture.getSsanimationid())){
                        item.getPictures().add(picture);
                    }
                });
                return  item;
            }).collect(Collectors.toList());
        }

        List<Comic> comicList = comicDao.getComicShow();

        List<String> comicids = comicList.stream().map(item->{
            return item.getId();
        }).collect(Collectors.toList());
        if (comicids.size()>0){
            List<ComicPictures> comicPictures = comicPicturesDao.selectByanimationIds(comicids);
            comicList = comicList.stream().map(item -> {
                item.setPictures(new ArrayList<>());
                comicPictures.stream().forEach(picture ->{
                    if(picture.getPictureUrl() == null){
                        picture.setPictureUrl(comicMappingPath + picture.getPictureLogic());
                    }
                    if(item.getId().equals(picture.getSscomicid())){
                        item.getPictures().add(picture);
                    }
                });
                return item;
            }).collect(Collectors.toList());
        }
        List<Novel> novelList = novelDao.getNovelShow();
        List<String> novelids = novelList.stream().map(item->{
            return item.getId();
        }).collect(Collectors.toList());
        if(novelids.size()>0){
            List<NovelPictures> novelPictures = novelPicturesDao.selectByanimationIds(novelids);
            novelList = novelList.stream().map(item -> {
                item.setPictures(new ArrayList<>());
                novelPictures.stream().forEach(picture ->{
                    if(picture.getPictureUrl() == null){
                        picture.setPictureUrl(novelMappingPath+ picture.getPictureLogic());
                    }
                    if(item.getId().equals(picture.getSsnovelid())){
                        item.getPictures().add(picture);
                    }
                });
                return  item;
            }).collect(Collectors.toList());

        }


        List<Game> gameList = gameDao.getGameShow();

        List<String> gameids = gameList.stream().map(item->{
            return item.getId();
        }).collect(Collectors.toList());
        if(gameids.size()>0){
            List<GamePictures> gamePictures = gamePicturesDao.selectByanimationIds(gameids);
            gameList = gameList.stream().map(item -> {
                item.setPictures(new ArrayList<>());
                gamePictures.stream().forEach(picture ->{
                    if(picture.getPictureUrl() == null){
                        picture.setPictureUrl(gameMappingPath + picture.getPictureLogic());
                    }
                    if(item.getId().equals(picture.getSsgameid())){
                        item.getPictures().add(picture);
                    }
                });
                return  item;
            }).collect(Collectors.toList());
        }

        Map<String, Object> remap = new HashMap<>();
        remap.put("animations", animationList);
        remap.put("comics", comicList);
        remap.put("novels", novelList);
        remap.put("games", gameList);

        return ResponseUtil.success(remap);
    }
}
