package com.wangshuos.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Base64;

/**
 * @ClassName ImagesUtils
 * @Author wangshuo
 * @Date 2024/5/9 10:05
 * @Version 1.0
 **/
public class FileUtils {
    public static void main(String[] args) {
        String s = fileToBase64(new File("/Users/wangshuo/Desktop/未命名 2.txt"));
    }
    /**
     * 根据文件转换Base64
     */
    public static String fileToBase64(File file) {
        String base64File = "";
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] fileBytes = new byte[(int) file.length()];
            // 读取文件内容
            int read = fis.read(fileBytes);
            if (read != fileBytes.length) {
                throw new IOException("无法完全读取文件 " + file.getName());
            }
            // 检测文件的 MIME 类型
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // 默认类型
            }
            base64File = "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            System.err.println("文件转换为 Base64 时出错： " + e.getMessage());
        }
        return base64File;
    }

    /**
     * 根据文件转换Base64
     */
    public static String fileToBase64(MultipartFile multipartFile) {
        try {
            byte[] bytes = multipartFile.getBytes();
            String mimeType = multipartFile.getContentType();
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // 默认类型
            }
            return "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            return null;
        }
    }

}
