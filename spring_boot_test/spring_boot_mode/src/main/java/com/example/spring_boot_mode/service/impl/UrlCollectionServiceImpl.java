package com.example.spring_boot_mode.service.impl;

import com.example.spring_boot_mode.dao.mode.UrlCollectionDao;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.UrlCollection;
import com.example.spring_boot_mode.service.UrlCollectionService;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.UUidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UrlCollectionServiceImpl implements UrlCollectionService {
    @Autowired
    UrlCollectionDao urlCollectionDao;

    @Override
    public ResponseObjectEntity toadd(UrlCollection urlCollection, String userid) {
        urlCollection.setId(UUidUtil.getuuid());
        urlCollection.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
        urlCollection.setSscollector(userid);
        int count = urlCollectionDao.selectcountbytypeidanduserid(urlCollection.getSsurltypeid(),userid);
        urlCollection.setSort(count);
        int toaddflag = urlCollectionDao.toadd(urlCollection);
        if (toaddflag>0){
            return ResponseUtil.success("成功");
        }
        return ResponseUtil.error("失败");
    }

    @Override
    public ResponseObjectEntity toedit(UrlCollection urlCollection) {
        int toeditflag = urlCollectionDao.toedit(urlCollection);
        if (toeditflag>0){
            return ResponseUtil.success("成功");
        }
        return ResponseUtil.error("失败");
    }

    @Override
    public ResponseObjectEntity todelete(String[] ids) {
        int toeditflag =  urlCollectionDao.todelete(ids);
        if (toeditflag>0){
            return ResponseUtil.success("成功");
        }
        return ResponseUtil.error("失败");
    }

    @Override
    public ResponseObjectEntity tosavelogo(UrlCollection urlCollection) {
        int toeditflag = urlCollectionDao.toedit(urlCollection);
        if (toeditflag>0){
            return ResponseUtil.success(urlCollection);
        }
        return ResponseUtil.error("失败");
    }
}
