package com.project.springbox.controllers;

import com.project.springbox.dtos.FileDTO;
import com.project.springbox.message.ResponseFile;
import com.project.springbox.message.ResponseMessage;
import com.project.springbox.security.UserObject;
import com.project.springbox.services.FileService;
import com.project.springbox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

// RestController tar hand om requests till databasen
// och använder funktionerna som är skapade i Service.

@RestController
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    @Autowired
    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(
            @AuthenticationPrincipal UserObject user,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        var username = userService.getByUsername(user.getUsername()).orElseThrow(Exception::new);
        String message = "";
        fileService.uploadFile(username ,file);
        message = "Uploaded file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }

    @GetMapping("/all-files")
    public ResponseEntity<List<ResponseFile>> getAllFiles(
            @AuthenticationPrincipal UserObject user
    ) {
        if (!user.getUser().isAdmin()) {
            return ResponseEntity.badRequest().build();
        }

        if (user.getUsername() == null) {
            return ResponseEntity.notFound().build();
        }

        List<ResponseFile> files = fileService.getAllFiles().map(file -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/all-files")
                    .path(file.getFileId().toString())
                    .toUriString();

            return new ResponseFile(
                    file.getFileName(),
                    fileDownloadUri,
                    file.getContentType(),
                    file.getData().length,
                    file.getUser().getName());
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileDTO>> getUserFiles(
            @AuthenticationPrincipal UserObject user
            )  {
        var username = userService.getByUsername(user.getUsername());
        if (username.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(fileService
                .getUserFiles(user.getUsername())
                .stream()
                .map(file -> {
                    String fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/files")
                            .path(file.getFileId().toString())
                            .toUriString();
                    return new FileDTO(
                            file.getFileName(),
                            fileDownloadUri,
                            file.getContentType(),
                            file.getData().length
                    );
                }).collect(Collectors.toList()));
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<List<FileDTO>> getUserFiles(
            @AuthenticationPrincipal UserObject user,
            @PathVariable int id
    )  {
        var username = userService.getByUsername(user.getUsername());
        if (username.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var check = fileService.checkForFile(id, user.getUsername());
        if(check == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(fileService
                .findFileById(id)
                .stream()
                .map(file -> {
                    String fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/files")
                            .path(file.getFileId().toString())
                            .toUriString();
                    return new FileDTO(
                            file.getFileName(),
                            fileDownloadUri,
                            file.getContentType(),
                            file.getData().length
                    );
                }).collect(Collectors.toList()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteFile(
            @AuthenticationPrincipal UserObject user,
            @PathVariable int id
    ) {
        String message = "";

        var username = userService.getByUsername(user.getUsername());
        if (username.isEmpty()) {
            message = "User does not exist!";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
        }

        var file = fileService.findFileById(id);
        if (file.isEmpty()) {
            message = "File not found!";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
        }

        var check = fileService.checkForFile(id, user.getUsername());
        if(check == null) {
            message = "User does not have this file!";
            return ResponseEntity.status(HttpStatus.LOCKED).body(new ResponseMessage(message));
        }

        try {
            fileService.deleteFile(id);
            message = "Deleted file successfully!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not delete file!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

}
