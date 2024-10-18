package com.example.spring_boot_mode.utils.pictureSave;

import com.example.spring_boot_mode.utils.UUidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Slf4j
@Component("localPictureSave")
public class LocalPictureSave implements PictureSave {

    @Override
    public boolean savefiles(MultipartFile multipartFile, String path,String uuid) {
        if (multipartFile == null){
            return false;
        }
            //验证 文件夹是否存在 没有则创建
//            String path = "D:\\";
            if (File.separator.equals(path.substring(path.length()-1,path.length()))){
                path = path.substring(0, path.length()-1);
            }
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //存储
            InputStream ins = null;
            OutputStream os = null;
            File outfile = new File(path+File.separator+
                                                        uuid+
                                                        multipartFile.getOriginalFilename()
                                                                .substring(multipartFile.getOriginalFilename().lastIndexOf(".")));
            byte[] buffer = new byte[1024];
        try{
                 ins = multipartFile.getInputStream();

                 os = new FileOutputStream(outfile);
                 int bytesRead = 0;
                 while ((bytesRead = ins.read(buffer, 0, 1024)) != -1) {
                     os.write(buffer, 0, bytesRead);
                 }
                 os.flush();
             return true;
            }catch (IOException ioException){
               log.error(ioException.getMessage());
            }finally {
              if (ins != null) {
                  try {
                      ins.close();
                  }catch (IOException e){
                      log.error(e.getMessage());
                  }
              }
              if (os != null) {
                  try {
                      os.close();
                  }catch (IOException e){
                      log.error(e.getMessage());
                  }
              }
        }

        return false;
    }

    @Override
    public boolean deletefiles(String filePath) {
        File file = new File(filePath);

        if(file.exists()) {
            return file.delete();
        }
        return true;
    }
}
