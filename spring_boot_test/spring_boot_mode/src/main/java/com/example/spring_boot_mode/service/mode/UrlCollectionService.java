package com.example.spring_boot_mode.service.mode;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.UrlCollection;

public interface UrlCollectionService {
    ResponseObjectEntity toadd(UrlCollection urlCollection, String id);

    ResponseObjectEntity toedit(UrlCollection urlCollection);

    ResponseObjectEntity todelete(String[] ids);

    ResponseObjectEntity tosavelogo(UrlCollection urlCollection);

    ResponseObjectEntity urlshow();

    ResponseObjectEntity urlhot();
}