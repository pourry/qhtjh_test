package com.example.spring_boot_mode.service.impl;

import com.example.spring_boot_mode.dao.mode.AnimationDao;
import com.example.spring_boot_mode.dao.mode.ComicDao;
import com.example.spring_boot_mode.dao.mode.GameDao;
import com.example.spring_boot_mode.dao.mode.NovelDao;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.Animation;
import com.example.spring_boot_mode.entity.mode.Comic;
import com.example.spring_boot_mode.entity.mode.Game;
import com.example.spring_boot_mode.entity.mode.Novel;
import com.example.spring_boot_mode.service.ACGService;
import com.example.spring_boot_mode.service.ComicService;
import com.example.spring_boot_mode.service.GameService;
import com.example.spring_boot_mode.service.NovelService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ACGServiceImpl implements ACGService {
    @Autowired
    AnimationDao animationDao;
    @Autowired
    ComicDao comicDao;
    @Autowired
    NovelDao novelDao;
    @Autowired
    GameDao gameDao;
    @Override
    public ResponseObjectEntity getshowAce() {
        List<Animation> animationList = animationDao.getAnimationShow();
        List<Comic> comicList = comicDao.getComicShow();
        List<Novel> novelList = novelDao.getNovelShow();
        List<Game> gameList = gameDao.getGameShow();
        Map<String, Object> remap = new HashMap<>();
        remap.put("animations", animationList);
        remap.put("comics", comicList);
        remap.put("novels", novelList);
        remap.put("games", gameList);

        return ResponseUtil.success(remap);
    }
}
