package com.example.spring_boot_mode.utils.pictureSave;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PictureSave {
    public boolean savefiles(MultipartFile multipartFile,String path,String uuid);

    public boolean deletefiles(String filePath);
}
