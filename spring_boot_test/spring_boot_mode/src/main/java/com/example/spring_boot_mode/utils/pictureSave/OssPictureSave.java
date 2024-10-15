package com.example.spring_boot_mode.utils.pictureSave;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class OssPictureSave implements PictureSave{
    @Override
    public boolean savefiles(MultipartFile multipartFile,String path,String uuid) {
        return false;
    }
}
