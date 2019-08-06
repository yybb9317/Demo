package com.example.springboot01.controller;

import static com.example.springboot01.response.TResponse.success;

import com.example.springboot01.Service.FileService;
import com.example.springboot01.constant.DataBaseEnum.DocType;
import com.example.springboot01.entity.Doc;
import com.example.springboot01.response.TResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @title:  文件接口
 * @author: Yuanbo
 * @date: 2019/8/2  17:49
 */
@RestController
@RequestMapping("/files")
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * @description:   上传文件 图片支持jpg,jpeg,png,gif,webp,视频支持mp4,flv,avi,mkv,wmv
     * @Author Bob
     * @date 2019/8/5 11:05
     */
    @PostMapping("/upload")
    @CrossOrigin
    public ResponseEntity upload(
            @RequestParam(defaultValue = "other") String type,  //文件类型,IMAGE,VIDEO,OTHER
            @RequestParam MultipartFile file
    ) {
        return success(fileService.upload(file, DocType.valueOf(type)));
    }

    /**
     * @description:   下载文件
     * @Author Bob
     * @date 2019/8/5 11:08
     */
    @GetMapping("download/{id}")
    @CrossOrigin
    public ResponseEntity download(
            @PathVariable String id,         //文件id
            HttpServletRequest request,
            HttpServletResponse response
    ) {
//        Doc file = fileService.findFile(id);
//        if (file == null) {
//            return;
//        }
        Doc file = new Doc("8ecd461ea51445b2a68b2efa0b262673", DocType.valueOf("IMAGE"), "QQ截图20170511122910.png",
                "\\2019-08\\7a0c4146da548b05ddcc5ed408cbdd09.png", 606482L, "c1fe145f2983f7ad357918cd1ee429d1",
                "image/png", LocalDateTime.now());
        fileService.download(id, file, request, response);
        return TResponse.success();
    }
}
