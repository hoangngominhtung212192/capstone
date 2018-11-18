package com.tks.gwa.controller;

import com.tks.gwa.dto.ResponseImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;


@RestController
public class ImageController {
    private final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

    public static final Path rootLocation = Paths.get("src/main/resources/static/images").toAbsolutePath().normalize();

    //up hinh
    @PostMapping("/image/upload")
    public ResponseImage upload(@RequestPart  MultipartFile upload, HttpServletRequest request) {
        System.out.println("uploading pic");
        String sourceName = upload.getOriginalFilename();
        System.out.println("got pic namne "+sourceName);
//        RandomStringUtils.randomAlphanumeric(20).toUpperCase();
//        String src =
//        String sourceFileName = FilenameUtils.getBaseName(sourceName);
//        String sourceExt = FilenameUtils.getExtension(sourceName).toLowerCase();
//
//        String fileName = RandomStringUtils.randomAlphabetic(8)
//                .concat(sourceFileName)
//                .concat(".")
//                .concat(sourceExt);
        try {
            //root location: noi dat file hinh
            Files.createDirectories(rootLocation);
            Files.copy(upload.getInputStream(), rootLocation.resolve(sourceName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = request.getScheme()
                .concat("://")
                .concat(request.getServerName())
                .concat(":8080")
                .concat("/gwa")
                .concat("/images/")
                .concat(sourceName);
        return new ResponseImage(1, sourceName, url);
//        return gson.toJson(new ResponseImage(1, fileName, url));
    }

    //load hinh
    @GetMapping("/gwa/images/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        // Load file as Resource
        Resource resource = loadFileAsResource(fileName);
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            LOGGER.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    //load hinh
    public Resource loadFileAsResource(String fileName) throws Exception {
        try {
            Path filePath = rootLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new Exception("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new Exception("File not found " + fileName, ex);
        }
    }
}
