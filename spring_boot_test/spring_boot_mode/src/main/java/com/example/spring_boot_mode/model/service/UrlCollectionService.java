package com.example.spring_boot_mode.model.service;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.model.entity.UrlCollection;

import java.util.List;
import java.util.Map;

public interface UrlCollectionService {
    ResponseObjectEntity toadd(UrlCollection urlCollection, String id);

    ResponseObjectEntity toedit(UrlCollection urlCollection);

    ResponseObjectEntity todelete(String[] ids);

    ResponseObjectEntity tosavelogo(UrlCollection urlCollection);

    ResponseObjectEntity urlshow();

    ResponseObjectEntity urlhot();

    /**
     * 批量验证网址可用性
     * @param urls 网址列表
     * @return 验证结果 Map
     */
    ResponseObjectEntity validateUrls(List<Map<String, String>> urls);
}
