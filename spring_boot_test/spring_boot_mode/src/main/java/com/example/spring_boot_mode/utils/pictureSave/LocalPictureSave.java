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
        // 处理路径：移除末尾的分隔符，统一使用 File.separator
        if (File.separator.equals(path.substring(path.length()-1,path.length()))){
            path = path.substring(0, path.length()-1);
        }
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 存储文件名: uuid + 扩展名
        String ext = multipartFile.getOriginalFilename()
                .substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        File outfile = new File(path + File.separator + uuid + ext);
        byte[] buffer = new byte[1024];
        try (InputStream ins = multipartFile.getInputStream();
             OutputStream os = new FileOutputStream(outfile)) {
            int bytesRead;
            while ((bytesRead = ins.read(buffer, 0, 1024)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
            log.info("图片保存成功: {}", outfile.getAbsolutePath());
            return true;
        } catch (IOException ioException) {
            log.error("图片保存失败: {}", ioException.getMessage(), ioException);
        }
        return false;
    }

    @Override
    public boolean deletefiles(String filePath) {
        File file = new File(filePath);
        log.info("尝试删除文件: {}, 存在: {}", filePath, file.exists());
        if(file.exists()) {
            boolean deleted = file.delete();
            log.info("文件删除结果: {}", deleted ? "成功" : "失败");
            // 如果删除失败，尝试 NIO 方式
            if (!deleted) {
                try {
                    java.nio.file.Files.deleteIfExists(file.toPath());
                    log.info("NIO方式删除完成");
                } catch (Exception e) {
                    log.error("NIO删除失败: {}", e.getMessage(), e);
                }
            }
            return !file.exists();
        }
        // 文件不存在也视为成功（幂等操作）
        return true;
    }
}
