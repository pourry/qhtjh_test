package com.example.spring_boot_mode.service.mode.impl;

import com.example.spring_boot_mode.dao.mode.UrlCollectionDao;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.UrlCollection;
import com.example.spring_boot_mode.service.mode.UrlCollectionService;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.UUidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class UrlCollectionServiceImpl implements UrlCollectionService {
    @Autowired
    UrlCollectionDao urlCollectionDao;

    @Override
    public ResponseObjectEntity toadd(UrlCollection urlCollection, String userid) {
        urlCollection.setId(UUidUtil.getuuid());
        urlCollection.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
        urlCollection.setSscollector(userid);
        if(urlCollection.getShare()){
            urlCollection.setShareTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
        }
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
        UrlCollection urlCollectionold = urlCollectionDao.selectbyid(urlCollection.getId());
        //如果 之前未被展示
        if (urlCollectionold.getShare() ==null || !urlCollectionold.getShare()){
            //当 现在展示
            if(urlCollection.getShare()){
                urlCollection.setShareTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
            }
        }
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

    @Override
    public ResponseObjectEntity urlshow() {
        List<UrlCollection> urlCollections = urlCollectionDao.urlshow();
        return ResponseUtil.success(urlCollections);
    }

    @Override
    public ResponseObjectEntity urlhot() {
        List<Map<String,Object>> urlCollections = urlCollectionDao.urlhot();
        AtomicInteger i = new AtomicInteger(1);
        urlCollections = urlCollections.stream().sorted((o1, o2) -> {
            return Integer.valueOf(o2.get("countpath").toString()) - Integer.valueOf(o1.get("countpath").toString());
        }).map(item ->{
            item.put("index", i.getAndIncrement());

            return item;
        }).collect(Collectors.toList());
        return ResponseUtil.success(urlCollections);
    }
}
