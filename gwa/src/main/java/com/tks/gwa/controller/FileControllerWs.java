package com.tks.gwa.controller;

import com.tks.gwa.dto.ResponseImage;
import com.tks.gwa.dto.UploadFileResponse;
import com.tks.gwa.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public interface FileControllerWs {
    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file")MultipartFile file);

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFile(@RequestParam("files") MultipartFile[] files);

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request);

    @PostMapping("/image/upload")
    public ResponseImage uploadImg(@RequestPart  MultipartFile upload, HttpServletRequest request);
}
