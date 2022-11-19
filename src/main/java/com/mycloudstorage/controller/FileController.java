package com.mycloudstorage.controller;

import com.mycloudstorage.model.ErrorMessage;
import com.mycloudstorage.model.File;
import com.mycloudstorage.model.User;
import com.mycloudstorage.service.FileService;
import com.mycloudstorage.service.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/files")
public class FileController {

    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping
    public String upload(@RequestParam("fileUpload") MultipartFile multipartFile, Authentication authentication, Model model) {

        if (multipartFile.isEmpty()){
            return redirectError(ErrorMessage.NO_FILE);
        }

        String username = authentication.getName();
        User user = userService.getUser(username);
        Integer userId = user.getUserId();
        String fileName = multipartFile.getOriginalFilename();
        File fileWithUsedName = fileService.getByFilename(fileName);

        if(fileWithUsedName != null) {
            return redirectError(ErrorMessage.FILENAME_ALREADY_EXISTS);
        }

        try {
          String redirectSuccess = createFile(multipartFile, userId);
          if (redirectSuccess != null) return redirectSuccess;

        } catch(Exception e) {
            return redirectSuccess(false);
        }

        return redirectSuccess(true);
    }

    private String createFile(MultipartFile multipartFile, Integer userId) throws IOException {
      byte[] fileData = multipartFile.getBytes();

      File file = new File(null, multipartFile.getOriginalFilename(), multipartFile.getContentType(), String.valueOf(multipartFile.getSize()), userId, fileData);
      int created = fileService.create(file);

      if(created < 1) {
        return redirectSuccess(false);
      }
      return null;
    }

    private static String redirectError(ErrorMessage errorMessage) {
      return redirectSuccess(false) + "&error=" + errorMessage;
    }

    private static String redirectSuccess(boolean x) {
      return "redirect:/result?isSuccess=" + x;
    }

    @GetMapping("/download/{fileId}")
      public ResponseEntity<InputStreamResource> download(@PathVariable("fileId") Integer fileId) {
      return downloadFileById(fileId);
    }

    private ResponseEntity<InputStreamResource> downloadFileById(Integer fileId) {
      File file = retrieveFileById(fileId);
      return deliverFileData(file);
    }

    private static ResponseEntity<InputStreamResource> deliverFileData(File file) {
      String fileName = file.getFilename();
      InputStream inputStream = new ByteArrayInputStream(file.getFileData());
      InputStreamResource resource = new InputStreamResource(inputStream);
      return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
        .contentType(MediaType.parseMediaType(file.getContentType()))
        .body(resource);
    }

    private File retrieveFileById(Integer fileId) {
      File file = fileService.getById(fileId);
      return file;
    }

    @GetMapping("/delete/{fileId}")
      public String delete(@PathVariable("fileId") Integer fileId) {
      deleteFileById(fileId);
      return redirectHome();
      }

    private static String redirectHome() {
      return "redirect:/home";
    }

    private void deleteFileById(Integer fileId) {
      fileService.delete(fileId);
    }

}
