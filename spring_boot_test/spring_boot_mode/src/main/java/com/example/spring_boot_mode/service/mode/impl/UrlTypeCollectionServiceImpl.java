package com.example.spring_boot_mode.service.mode.impl;

import com.example.spring_boot_mode.dao.mode.UrlCollectionDao;
import com.example.spring_boot_mode.dao.mode.UrlTypeCollectionDao;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.UrlCollection;
import com.example.spring_boot_mode.entity.mode.UrlTypeCollection;
import com.example.spring_boot_mode.service.mode.UrlTypeCollectionService;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.UUidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseObjectEntity tochange(String dropid,String dragid,String type) {
        if (!"inner".equals(type)){
            List<UrlCollection> urlCollections = urlCollectionDao.selectallbytwoid(dropid,dragid);
            if (urlCollections.isEmpty()){
                List<UrlTypeCollection> urlTypeCollections = urlTypeCollectionDao.selectallbytwoid(dropid,dragid);
                boolean b = urlTypeCollectionsort(dropid, dragid, urlTypeCollections, type);
                if (b) {
                    return ResponseUtil.success("成功");
                }
            }else {
                boolean b = urlCollectionsort(dropid, dragid, urlCollections, type);
                if (b) {
                    return ResponseUtil.success("成功");
                }
            }
        }else if("inner".equals(type)){
            UrlCollection urlCollection = urlCollectionDao.selectbyid(dragid);
            urlCollection.setSort(0);
            urlCollection.setSsurltypeid(dropid);
            int toedit = urlCollectionDao.toedit(urlCollection);
            if (toedit >0){
                return ResponseUtil.success("成功");
            }
        }

        return ResponseUtil.error("失败");
    }
    //收藏url排序存储
    public boolean urlCollectionsort(String dropid,String dragid,List<UrlCollection> urlCollections,String type){
        UrlCollection urlCollection = null;
        for (int i = 0; i < urlCollections.size(); i++) {
            if (urlCollections.get(i).getId().equals(dragid)) {
                urlCollection = urlCollections.get(i);
                urlCollections.remove(i);
                break;
            }
        }
        //升序拍 o1-o2
        urlCollections =urlCollections.stream().sorted((o1, o2) -> {return o1.getSort() - o2.getSort();}).collect(Collectors.toList());

        int sort = 0;
        //此时拖动节点未进入循环列表
        for (int i = 0; i < urlCollections.size(); i++) {
            //说明拖动节点在目标位置的后面
            if("after".equals(type)){
                urlCollections.get(i).setSort(sort);
                sort++;
            }
            if (urlCollections.get(i).getId().equals(dropid)) {
                urlCollection.setSsurltypeid(urlCollections.get(i).getSsurltypeid());
                urlCollection.setSort(sort);
                sort++;
            }
            //说明拖动节点在目标位置的前面
            if("before".equals(type)){
                urlCollections.get(i).setSort(sort);
                sort++;
            }

        }
        //设置好序号之后  加入到循环列表中
        urlCollections.add(urlCollection);
        int insert =urlCollectionDao.deletebytwoid(dropid, dragid);
        if(insert <=0){
            return false;
        }
        insert = urlCollectionDao.insertList(urlCollections);
        if (insert>0){
            return true;
        }
        return false;
    }
    //收藏urltype排序存储
    public boolean urlTypeCollectionsort(String dropid,String dragid,List<UrlTypeCollection> urlTypeCollections,String type){

        UrlTypeCollection urlTypeCollection = null;
        for (int i = 0; i < urlTypeCollections.size(); i++) {
            if (urlTypeCollections.get(i).getId().equals(dragid)) {
                urlTypeCollection = urlTypeCollections.get(i);
                urlTypeCollections.remove(i);
                break;
            }
        }
        //升序拍
        urlTypeCollections =urlTypeCollections.stream().sorted((o1, o2) -> {return o1.getSort() - o2.getSort();}).collect(Collectors.toList());

        int sort = 0;
        for (int i = 0; i < urlTypeCollections.size(); i++) {
            if("after".equals(type)){
                urlTypeCollections.get(i).setSort(sort);
                sort++;
            }
            if (urlTypeCollections.get(i).getId().equals(dropid)) {
                urlTypeCollection.setSort(sort);
                sort++;
            }
            if("before".equals(type)){
                urlTypeCollections.get(i).setSort(sort);
                sort++;
            }
        }
        urlTypeCollections.add(urlTypeCollection);
        int insert =urlTypeCollectionDao.deletebytwoid(dropid, dragid);
        if(insert <=0){
            return false;
        }
        insert = urlTypeCollectionDao.insertList(urlTypeCollections);
        if (insert>0){
            return true;
        }
        return false;
    }

}
