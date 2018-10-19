package com.tks.gwa.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileUploadService {
    public String storeFile(MultipartFile file);
    public Resource loadFileAsResource(String fileName);

}
