package com.example.wuyuhang.modules.api.controller;

import com.example.wuyuhang.common.util.MinioUtils;
import com.example.wuyuhang.modules.api.util.IException;
import com.example.wuyuhang.modules.api.util.Result;
import com.example.wuyuhang.modules.api.vo.StudentPushPlanInfoVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/api/minio")
@Api("minio上传文件")
public class MinioController {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.bucket}")
    private String bucket;

    @Autowired
    private MinioUtils minIOUtils;

    // 文件上传接口
    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        return new Result<String>().ok(minIOUtils.uploadFile(file));
    }

    // 文件下载接口
    @GetMapping("/download/{fileName}")
    public Result<String> downloadFile(@PathVariable String fileName) {
        return new Result<String>().ok(url + "/" + bucket + "/" + fileName);
    }

    // 文件上传接口
    @PostMapping("/userPlanUpload")
    public Result<String> userPlanUpload(@RequestParam("file") MultipartFile file) {
        return new Result<String>().ok(minIOUtils.uploadFile(file));
    }
}



