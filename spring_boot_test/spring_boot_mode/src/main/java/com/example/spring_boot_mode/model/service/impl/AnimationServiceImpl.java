package com.example.spring_boot_mode.model.service.impl;

import com.example.spring_boot_mode.model.dao.AnimationDao;
import com.example.spring_boot_mode.model.dao.AnimationPicturesDao;
import com.example.spring_boot_mode.model.entity.Animation;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.AnimationPictures;
import com.example.spring_boot_mode.model.entity.reminder.Reminder;
import com.example.spring_boot_mode.model.service.AnimationService;
import com.example.spring_boot_mode.model.service.reminder.ReminderService;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.PagingUtil;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.UUidUtil;
import com.example.spring_boot_mode.utils.pictureSave.PictureSave;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
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
    @Autowired
    private ReminderService reminderService;

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
        // 处理提醒
        handleReminder(animation);
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
                // 查询每个动画的提醒信息
                loadReminderInfo(item);
            }
        }

        return ResponseUtil.success(new PagingUtil(pageNumber,pageSize,animationList,total));
    }

    /**
     * 加载动画的提醒信息
     */
    private void loadReminderInfo(Animation animation) {
        try {
            Reminder reminder = reminderService.getActiveReminder(
                    Reminder.TYPE_ANIMATION, animation.getId(), animation.getSscollector());
            if (reminder != null) {
                animation.setRemindopen(true);
                animation.setRemindtime(reminder.getRemindTime() != null ?
                        reminder.getRemindTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);
                animation.setRemindmsg(reminder.getRemindMsg());
            } else {
                animation.setRemindopen(false);
                animation.setRemindtime(null);
                animation.setRemindmsg(null);
            }
        } catch (Exception e) {
            // 查询失败不影响主流程
            animation.setRemindopen(false);
            animation.setRemindtime(null);
            animation.setRemindmsg(null);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity toedit(Animation animation,MultipartFile[] file) {
        //如果 之前未被展示
        if (animation.getShare() ==null || !animation.getShare()){
            //当 现在展示
            if(animation.getShare() != null && animation.getShare()){
                animation.setShareTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
            }
        }
        int reint = animationDao.toedit(animation);
        if (reint<=0){
            return ResponseUtil.error("失败");
        }
        //判断animation.getPictures() 中 file 是否 需要已经删除
            List<AnimationPictures> pictures = animationPicturesDao.selectIdByanimationId(animation.getId());
            List<AnimationPictures> currentPictures = animation.getPictures() != null ? animation.getPictures() : new ArrayList<>();
            AtomicBoolean b = new AtomicBoolean(true);
            pictures = pictures.stream().filter( picture ->{
                           b.set(true);
                           for (AnimationPictures item : currentPictures) {
                               if (picture.getId().equals(item.getId())){
                                   b.set(false);
                                   break;
                               }
                           }
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
        // 处理提醒
        handleReminder(animation);
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
        //删除关联的提醒
        for (String id : ids) {
            Reminder reminder = reminderService.getActiveReminder(Reminder.TYPE_ANIMATION, id, null);
            if (reminder != null) {
                reminderService.deleteReminder(reminder.getId());
            }
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

    /**
     * 处理提醒的创建、更新或删除
     * 根据Animation中的remindopen字段判断是否需要创建/更新提醒
     */
    private void handleReminder(Animation animation) {
        String targetId = animation.getId();
        String userId = animation.getSscollector();

        // 如果userId为空，跳过提醒处理
        if (userId == null || userId.isEmpty()) {
            log.warn("跳过提醒处理: userId为空, animationId={}", targetId);
            return;
        }

        try {
            // 查询现有的提醒
            Reminder existingReminder = reminderService.getActiveReminder(
                    Reminder.TYPE_ANIMATION, targetId, userId);

            // 判断是否开启了提醒
            Boolean remindopen = animation.getRemindopen();
            boolean isOpen = remindopen != null && remindopen;

            if (isOpen) {
                // 开启了提醒
                Reminder reminder;
                if (existingReminder != null) {
                    // 更新现有提醒
                    reminder = existingReminder;
                    log.info("更新已有提醒: id={}", reminder.getId());
                } else {
                    // 创建新提醒
                    reminder = new Reminder();
                    reminder.setTargetType(Reminder.TYPE_ANIMATION);
                    reminder.setTargetId(targetId);
                    reminder.setUserId(userId);
                    log.info("创建新提醒: targetId={}", targetId);
                }
                // 设置提醒信息
                reminder.setTargetName(animation.getName());
                reminder.setStatus(Reminder.STATUS_PENDING);
                reminder.setIsOpen(1);
                // 提醒消息默认为空字符串
                reminder.setRemindMsg(animation.getRemindmsg() != null ? animation.getRemindmsg() : "");

                // 解析提醒时间，如果为空则使用当前时间
                if (animation.getRemindtime() != null && !animation.getRemindtime().isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    reminder.setRemindTime(LocalDateTime.parse(animation.getRemindtime(), formatter));
                } else {
                    // 默认使用当前时间
                    reminder.setRemindTime(LocalDateTime.now());
                    log.warn("提醒时间为空，使用当前时间: {}", reminder.getRemindTime());
                }

                reminderService.saveReminder(reminder);
                log.info("提醒保存成功: targetId={}, remindTime={}", targetId, reminder.getRemindTime());
            } else {
                // 关闭了提醒，删除已有的提醒
                if (existingReminder != null) {
                    reminderService.deleteReminder(existingReminder.getId());
                    log.info("提醒已删除: id={}", existingReminder.getId());
                }
            }
        } catch (Exception e) {
            log.error("提醒处理失败: animationId={}, error={}", targetId, e.getMessage(), e);
        }
    }


}
