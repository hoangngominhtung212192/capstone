package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.FileControllerWs;
import com.tks.gwa.dto.ResponseImage;
import com.tks.gwa.dto.UploadFileResponse;
import com.tks.gwa.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileControllerWsImpl implements FileControllerWs {

    private static final Logger logger = LoggerFactory.getLogger(FileControllerWs.class);

    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileUploadService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(fileName).toUriString();
        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @Override
    public List<UploadFileResponse> uploadMultipleFile(@RequestParam("files") MultipartFile[] files) {

        return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<Resource> downloadFile(@PathVariable  String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileUploadService.loadFileAsResource(fileName);
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }

    //ham cua khanh

    public static final Path rootLocation = Paths.get("uploads").toAbsolutePath().normalize();

    @Override
    public ResponseImage uploadImg(MultipartFile upload, HttpServletRequest request) {
        System.out.println("uploading pic");
        String sourceName = upload.getOriginalFilename();
        System.out.println("got pic namne "+sourceName);
        Calendar cal = Calendar.getInstance();
        Long timeInMillis = cal.getTimeInMillis();
        String finalFilename = timeInMillis.toString() + sourceName;
        System.out.println("final filename "+finalFilename);
        try {
            //root location: noi dat file hinh
            Files.createDirectories(rootLocation);
            Files.copy(upload.getInputStream(), rootLocation.resolve(finalFilename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = request.getScheme()
                .concat("://")
                .concat(request.getServerName())
                .concat(":8080")
                .concat("/gwa")
                .concat("/downloadFile/")
                .concat(finalFilename);
        return new ResponseImage(1, finalFilename, url);
//        return gson.toJson(new ResponseImage(1, fileName, url));
    }
}
