package com.example.spring_boot_mode.model.web;


import cn.hutool.json.JSONUtil;
import com.example.spring_boot_mode.model.entity.Animation;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.AnimationPictures;
import com.example.spring_boot_mode.model.entity.SysUser;
import com.example.spring_boot_mode.model.entity.Vo.AnimationVo;
import com.example.spring_boot_mode.model.entity.reminder.Reminder;
import com.example.spring_boot_mode.model.service.AnimationService;
import com.example.spring_boot_mode.model.service.reminder.ReminderService;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.TokenUtill;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/animation")
public class AnimationController {
    @Autowired
    private AnimationService animationService;
    @Autowired
    private ReminderService reminderService;

    /**
     * 阻止Spring自动绑定pictures字段（因为该字段由前端以JSON字符串形式传递，需要手动解析）
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("pictures");
    }


    @PostMapping("toadd")
    public ResponseObjectEntity toadd(Animation animation,@RequestParam(value = "file",required = false) MultipartFile[] file, HttpServletRequest request  ){
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        animation.setSscollector(sysUser.getId());
        animation.setObject(null);
        ResponseObjectEntity responseObjectEntity = animationService.toadd(animation,file);
        return responseObjectEntity;
    }
    @PostMapping("toedit")
    public ResponseObjectEntity toedit(Animation animation,
                                       @RequestParam(value = "file", required = false) MultipartFile[] file,
                                       @RequestParam(value = "pictures", required = false) String picturesStr,
                                       HttpServletRequest request) {
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)) {
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        animation.setSscollector(sysUser.getId());
        // 手动解析pictures字符串为List
        if (picturesStr != null && !picturesStr.isEmpty()) {
            List<AnimationPictures> list = JSONUtil.toList(JSONUtil.parseArray(picturesStr), AnimationPictures.class);
            animation.setPictures(list);
        }
        // 清除object字段避免Spring绑定错误
        animation.setObject(null);
        ResponseObjectEntity responseObjectEntity = animationService.toedit(animation, file);
        return responseObjectEntity;
    }
    @GetMapping("getList")
    public ResponseObjectEntity getList(AnimationVo animationVo, HttpServletRequest request){
        Animation animation = new Animation();
        BeanUtils.copyProperties(animationVo,animation);
        SysUser sysUser = TokenUtill.getSysUser(request);
        if (Objects.isNull(sysUser)){
            return ResponseUtil.tokenExpire("token失效，请重新登录");
        }
        animation.setSscollector(sysUser.getId());
        ResponseObjectEntity responseObjectEntity = animationService.getList(animationVo.getPageNumber(),animationVo.getPassOver(), animationVo.getPageSize(),animation);
        return responseObjectEntity;
    }
    @GetMapping("getone/{id}")
    public ResponseObjectEntity getone(@PathVariable("id")String id, HttpServletRequest request){
        ResponseObjectEntity responseObjectEntity = animationService.getone(id);
        // 如果查询成功，补充提醒信息
        if (responseObjectEntity.getSuccessful() != null && responseObjectEntity.getSuccessful() && responseObjectEntity.getResultValue() != null) {
            Animation animation = (Animation) responseObjectEntity.getResultValue();
            SysUser sysUser = TokenUtill.getSysUser(request);
            String userId = sysUser != null ? sysUser.getId() : null;

            // 查询该动画是否有提醒
            Reminder reminder = reminderService.getActiveReminder(
                    Reminder.TYPE_ANIMATION, id, userId);

            // 构建返回数据，包含提醒信息
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("animation", animation);
            if (reminder != null) {
                resultMap.put("remindopen", true);
                resultMap.put("remindtime", reminder.getRemindTime() != null ?
                        reminder.getRemindTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);
                resultMap.put("remindmsg", reminder.getRemindMsg());
            } else {
                resultMap.put("remindopen", false);
                resultMap.put("remindtime", null);
                resultMap.put("remindmsg", null);
            }
            return ResponseUtil.success(resultMap);
        }
        return responseObjectEntity;
    }
    @PostMapping("todelete/{ids}")
    public ResponseObjectEntity todelete(@PathVariable("ids")String[] ids){
        ResponseObjectEntity responseObjectEntity = animationService.todelet(ids);
        return responseObjectEntity;
    }
}
