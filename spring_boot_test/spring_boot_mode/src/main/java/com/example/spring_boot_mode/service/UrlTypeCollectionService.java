package com.example.spring_boot_mode.service;

import com.example.spring_boot_mode.entity.ResponseObjectEntity;
import com.example.spring_boot_mode.entity.mode.UrlTypeCollection;

public interface UrlTypeCollectionService {
    ResponseObjectEntity geturltree(String id);

    ResponseObjectEntity toadd(UrlTypeCollection urlTypeCollection, String id);

    ResponseObjectEntity toedit(UrlTypeCollection urlTypeCollection);

    ResponseObjectEntity todelete(String[] ids);

    ResponseObjectEntity tochange( String dropid,String dragid,String type);
}
