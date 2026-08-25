package com.example.spring_boot_mode.model.service.impl;

import com.example.spring_boot_mode.model.dao.UrlCollectionDao;
import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.UrlCollection;
import com.example.spring_boot_mode.model.service.UrlCollectionService;
import com.example.spring_boot_mode.utils.DateUtil;
import com.example.spring_boot_mode.utils.ResponseUtil;
import com.example.spring_boot_mode.utils.UUidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
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

    /**
     * 批量验证网址可用性
     * 使用 HEAD 请求检测网址是否可访问
     * @param urls 待验证的网址列表，每项包含 id 和 url
     * @return 验证结果列表
     */
    @Override
    public ResponseObjectEntity validateUrls(List<Map<String, String>> urls) {
        List<Map<String, Object>> results = new ArrayList<>();
        int timeout = 5000; // 超时时间 5 秒

        for (Map<String, String> item : urls) {
            String id = item.get("id");
            String url = item.get("url");
            Map<String, Object> result = new HashMap<>();
            result.put("id", id);
            result.put("url", url);

            if (url == null || url.trim().isEmpty()) {
                result.put("available", false);
                result.put("message", "网址为空");
                results.add(result);
                continue;
            }

            HttpURLConnection connection = null;
            try {
                URL urlObj = new URL(url);
                connection = (HttpURLConnection) urlObj.openConnection();
                connection.setRequestMethod("HEAD");
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);
                connection.setInstanceFollowRedirects(true);
                // 设置请求头，模拟浏览器
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; URLValidator/1.0)");

                int responseCode = connection.getResponseCode();
                // 2xx 和 3xx 状态码都认为可用（3xx 为重定向）
                boolean available = responseCode >= 200 && responseCode < 400;
                result.put("available", available);
                result.put("statusCode", responseCode);
                result.put("message", available ? "可用 (HTTP " + responseCode + ")" : "不可用 (HTTP " + responseCode + ")");
            } catch (Exception e) {
                result.put("available", false);
                result.put("message", "无法访问: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            results.add(result);
        }

        // 统计结果
        long availableCount = results.stream().filter(r -> (Boolean) r.get("available")).count();
        Map<String, Object> response = new HashMap<>();
        response.put("total", results.size());
        response.put("availableCount", availableCount);
        response.put("unavailableCount", results.size() - availableCount);
        response.put("results", results);

        return ResponseUtil.success(response);
    }
}
