package com.example.spring_boot_mode.model.service.impl;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.dao.*;
import com.example.spring_boot_mode.model.entity.*;
import com.example.spring_boot_mode.model.service.ACGService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;
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
        Map<String, Object> result = loadAllAceData(false);
        return ResponseUtil.success(result);
    }

    @Override
    public ResponseObjectEntity gethotAce() {
        Map<String, Object> result = loadAllAceData(true);
        return ResponseUtil.success(result);
    }

    /**
     * 加载所有ACG数据（动画、漫画、小说、游戏）
     * @param withIndex 是否添加排行索引
     */
    private Map<String, Object> loadAllAceData(boolean withIndex) {
        Map<String, Object> result = new HashMap<>();

        // 动画
        List<Animation> animationList = animationDao.getAnimationShow();
        animationList = fillAnimationPictures(animationList);
        if (withIndex) {
            result.put("animations", toHotList(animationList, Animation::getName, Animation::getAddress, Animation::getPictures, "pictureUrl"));
        } else {
            result.put("animations", animationList);
        }

        // 漫画
        List<Comic> comicList = comicDao.getComicShow();
        comicList = fillComicPictures(comicList);
        if (withIndex) {
            result.put("comics", toHotList(comicList, Comic::getName, Comic::getAddress, Comic::getPictures, "pictureUrl"));
        } else {
            result.put("comics", comicList);
        }

        // 小说
        List<Novel> novelList = novelDao.getNovelShow();
        novelList = fillNovelPictures(novelList);
        if (withIndex) {
            result.put("novels", toHotList(novelList, Novel::getName, Novel::getAddress, Novel::getPictures, "pictureUrl"));
        } else {
            result.put("novels", novelList);
        }

        // 游戏
        List<Game> gameList = gameDao.getGameShow();
        gameList = fillGamePictures(gameList);
        if (withIndex) {
            result.put("games", toHotList(gameList, Game::getName, Game::getAddress, Game::getPictures, "pictureUrl"));
        } else {
            result.put("games", gameList);
        }

        return result;
    }

    /**
     * 将实体列表转换为排行Map列表（带索引、名称、图片、地址）
     */
    private <T, P> List<Map<String, Object>> toHotList(List<T> itemList,
                                                       java.util.function.Function<T, String> nameGetter,
                                                       java.util.function.Function<T, String> addressGetter,
                                                       java.util.function.Function<T, List<P>> picturesGetter,
                                                       String pictureUrlKey) {
        List<Map<String, Object>> result = new ArrayList<>();
        int index = 1;
        for (T item : itemList) {
            Map<String, Object> map = new HashMap<>();
            map.put("index", index++);
            map.put("name", nameGetter.apply(item));
            map.put("address", addressGetter.apply(item));
            // 获取第一张图片URL
            List<P> pictures = picturesGetter.apply(item);
            if (pictures != null && !pictures.isEmpty()) {
                Object firstPic = pictures.get(0);
                try {
                    Method method = firstPic.getClass().getMethod("getPictureUrl");
                    map.put("pictureUrl", method.invoke(firstPic));
                } catch (Exception e) {
                    map.put("pictureUrl", null);
                }
            } else {
                map.put("pictureUrl", null);
            }
            result.add(map);
        }
        return result;
    }

    // ========== 动画图片填充 ==========
    private List<Animation> fillAnimationPictures(List<Animation> animationList) {
        if (animationList == null || animationList.isEmpty()) return animationList;
        List<String> ids = animationList.stream().map(Animation::getId).collect(Collectors.toList());
        List<AnimationPictures> pictures = animationPicturesDao.selectByanimationIds(ids);
        return animationList.stream().map(item -> {
            item.setPictures(new ArrayList<>());
            pictures.forEach(pic -> {
                if (pic.getPictureUrl() == null) {
                    pic.setPictureUrl(animationMappingPath + pic.getPictureLogic());
                }
                if (item.getId().equals(pic.getSsanimationid())) {
                    item.getPictures().add(pic);
                }
            });
            return item;
        }).collect(Collectors.toList());
    }

    // ========== 漫画图片填充 ==========
    private List<Comic> fillComicPictures(List<Comic> comicList) {
        if (comicList == null || comicList.isEmpty()) return comicList;
        List<String> ids = comicList.stream().map(Comic::getId).collect(Collectors.toList());
        List<ComicPictures> pictures = comicPicturesDao.selectByanimationIds(ids);
        return comicList.stream().map(item -> {
            item.setPictures(new ArrayList<>());
            pictures.forEach(pic -> {
                if (pic.getPictureUrl() == null) {
                    pic.setPictureUrl(comicMappingPath + pic.getPictureLogic());
                }
                if (item.getId().equals(pic.getSscomicid())) {
                    item.getPictures().add(pic);
                }
            });
            return item;
        }).collect(Collectors.toList());
    }

    // ========== 小说图片填充 ==========
    private List<Novel> fillNovelPictures(List<Novel> novelList) {
        if (novelList == null || novelList.isEmpty()) return novelList;
        List<String> ids = novelList.stream().map(Novel::getId).collect(Collectors.toList());
        List<NovelPictures> pictures = novelPicturesDao.selectByanimationIds(ids);
        return novelList.stream().map(item -> {
            item.setPictures(new ArrayList<>());
            pictures.forEach(pic -> {
                if (pic.getPictureUrl() == null) {
                    pic.setPictureUrl(novelMappingPath + pic.getPictureLogic());
                }
                if (item.getId().equals(pic.getSsnovelid())) {
                    item.getPictures().add(pic);
                }
            });
            return item;
        }).collect(Collectors.toList());
    }

    // ========== 游戏图片填充 ==========
    private List<Game> fillGamePictures(List<Game> gameList) {
        if (gameList == null || gameList.isEmpty()) return gameList;
        List<String> ids = gameList.stream().map(Game::getId).collect(Collectors.toList());
        List<GamePictures> pictures = gamePicturesDao.selectByanimationIds(ids);
        return gameList.stream().map(item -> {
            item.setPictures(new ArrayList<>());
            pictures.forEach(pic -> {
                if (pic.getPictureUrl() == null) {
                    pic.setPictureUrl(gameMappingPath + pic.getPictureLogic());
                }
                if (item.getId().equals(pic.getSsgameid())) {
                    item.getPictures().add(pic);
                }
            });
            return item;
        }).collect(Collectors.toList());
    }
}
