package com.example.wuyuhang.common.util;

import io.minio.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @Author lizehui
 * @Description:
 * @Date 2023-04-19  15:39
 */
@Repository
@Data
public class MinioUtils {

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.userName}")
    private String userName;

    @Value("${minio.passWord}")
    private String passWord;

    @Value("${minio.bucket}")
    private String bucketName;

    // 文件上传
    public String uploadFile(MultipartFile file) {
        try {
            // 创建MinIO客户端
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioUrl)
                    .credentials(userName, passWord)
                    .build();

            // 生成随机文件名
            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();

            // 使用putObject方法上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName) // 替换为你实际的存储桶名称
                    .object(filename)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            return filename;
        } catch (Exception e) {
            e.printStackTrace();
            return "文件上传失败：" + e.getMessage();
        }
    }

    // 文件下载
    public InputStream downloadFile(String bucketName, String filename) throws Exception {
        // 创建MinIO客户端
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(userName, passWord)
                .build();

        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .build());
    }
}