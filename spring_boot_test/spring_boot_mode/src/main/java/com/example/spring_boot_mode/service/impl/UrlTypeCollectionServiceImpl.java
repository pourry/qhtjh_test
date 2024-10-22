package com.example.spring_boot_mode.service.impl;

import com.example.spring_boot_mode.dao.mode.UrlCollectionDao;
import com.example.spring_boot_mode.dao.mode.UrlTypeCollectionDao;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.UrlCollection;
import com.example.spring_boot_mode.entity.mode.UrlTypeCollection;
import com.example.spring_boot_mode.service.UrlTypeCollectionService;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.UUidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UrlTypeCollectionServiceImpl implements UrlTypeCollectionService {
    @Autowired
    UrlTypeCollectionDao urlTypeCollectionDao;
    @Autowired
    UrlCollectionDao urlCollectionDao;

    @Override
    public ResponseObjectEntity geturltree(String userid) {
        //查询类别
        List<UrlTypeCollection> urlTypeCollectionList =urlTypeCollectionDao.selectbyuserId(userid);
        //查询具体信息
        List<String> typeids = new ArrayList<>();
        urlTypeCollectionList.forEach(item ->{
            typeids.add(item.getId());
        });
        if (!typeids.isEmpty()) {
            List<UrlCollection> urlCollectionList = urlCollectionDao.selectbytypeids(typeids);
            urlTypeCollectionList.forEach(item ->{
                List<UrlCollection>  urlCollections = new ArrayList<>();
                urlCollectionList.forEach(collect ->{
                    if (item.getId().equals(collect.getSsurltypeid())) {
                        urlCollections.add(collect);
                    }
                });
                item.setChildren(urlCollections);
            });
        }

        return ResponseUtil.success(urlTypeCollectionList);

    }

    @Override
    public ResponseObjectEntity toadd(UrlTypeCollection urlTypeCollection, String userid) {
        urlTypeCollection.setId(UUidUtil.getuuid());
        urlTypeCollection.setCreateTime(DateUtil.getStrYmd("yyyy-MM-dd HH:mm:ss",new Date()));
        urlTypeCollection.setSscollector(userid);
        int count = urlTypeCollectionDao.selectcountbyuserId(userid);
        urlTypeCollection.setSort(count);
        int toaddflag = urlTypeCollectionDao.toadd(urlTypeCollection);
        if (toaddflag>0){
            return ResponseUtil.success("成功");
        }
        return ResponseUtil.error("失败");
    }

    @Override
    public ResponseObjectEntity toedit(UrlTypeCollection urlTypeCollection) {
        int toeditflag =  urlTypeCollectionDao.toedit(urlTypeCollection);
        if (toeditflag>0){
            return ResponseUtil.success("成功");
        }
        return ResponseUtil.error("失败");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity todelete(String[] ids) {
        //删除其下的所有元素
        //
        urlCollectionDao.todeleteBytypeId(ids);
        int toeditflag =  urlTypeCollectionDao.todelete(ids);
        if (toeditflag>0){
            return ResponseUtil.success("成功");
        }
        return ResponseUtil.error("失败");
    }
}
