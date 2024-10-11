package com.example.spring_boot_mode.service.impl;

import com.example.spring_boot_mode.dao.mode.AnimationDao;
import com.example.spring_boot_mode.entity.mode.Animation;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.service.AnimationService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.UUidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AnimationServiceImpl implements AnimationService {
    @Autowired
    private AnimationDao animationDao;

    @Override
    public ResponseObjectEntity toadd(Animation animation) {
        animation.setId(UUidUtil.getuuid());
        int reint = animationDao.toadd(animation);
        if (reint>0){
            return ResponseUtil.success("成功");
        }
        return ResponseUtil.error("失败");
    }

    @Override
    public ResponseObjectEntity getList(int passOver,int pageSize,Animation animation) {
        List<Animation> animationList = animationDao.getList(passOver,pageSize,animation);
        return ResponseUtil.success(animationList);
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
