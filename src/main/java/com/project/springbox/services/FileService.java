package com.project.springbox.services;

import com.project.springbox.data.File;
import com.project.springbox.data.User;
import com.project.springbox.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

// En service innehåller alla funktioner för en applikation

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final UserService userService;

    @Autowired
    public FileService(FileRepository fileRepository, UserService userService) {
        this.fileRepository = fileRepository;
        this.userService = userService;
    }

    public File uploadFile(User user, MultipartFile file) throws IOException {
        var check = fileRepository.checkIfUserHasFileByName(file.getOriginalFilename(), user.getName());
        if (check != null) {
            System.out.println("File" + file.getOriginalFilename() + "already exists!");
            throw new HttpServerErrorException(HttpStatus.CONFLICT);
        }
        String fileName = file.getOriginalFilename();
        File fileUpload = new File(user, fileName, file.getContentType(), file.getBytes());
        return fileRepository.save(fileUpload);
    }

    public Stream<File> getAllFiles() {
        return fileRepository.findAll().stream();
    }

    public List<File> getUserFiles(String user) {
        return fileRepository.findFilesByName(user);
    }

    public List<File> findFileById(int id) {
        return fileRepository.findFilesById(id);
    }

    public String checkForFile(int id, String name) {
        return fileRepository.checkIfUserHasFile(id, name);
    }

    public void deleteFile(int id) {
        fileRepository.deleteFileById(id);
    }

}
