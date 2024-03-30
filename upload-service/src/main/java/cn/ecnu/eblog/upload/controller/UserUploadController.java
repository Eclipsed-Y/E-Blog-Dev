package cn.ecnu.eblog.upload.controller;

import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.upload.aliyun.AliyunOSS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/upload")
public class UserUploadController {
    @Autowired
    private AliyunOSS aliyunOSS;
    @PostMapping
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("上传图片：{}", file.getOriginalFilename());
        return Result.success(aliyunOSS.upload(file));
    }
}
