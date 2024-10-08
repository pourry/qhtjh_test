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

@Service
public class AnimationServiceImpl implements AnimationService {
    @Autowired
    private AnimationDao animationDao;

    @Override
    public ResponseObjectEntity toadd(Animation animation) {
        animation.setId(UUidUtil.getuuid());
        int reint = animationDao.toadd(animation);
        if (reint>0){
            return ResponseUtil.success("新增成功");
        }
        return ResponseUtil.error("新增失败");
    }

    @Override
    public ResponseObjectEntity getList(Animation animation) {
        List<Animation> animationList = animationDao.getList(animation);
        return ResponseUtil.success(animationList);
    }
}
