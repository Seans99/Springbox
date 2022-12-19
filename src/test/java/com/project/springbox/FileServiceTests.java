package com.project.springbox;

import com.project.springbox.repositories.FileRepository;
import com.project.springbox.services.FileService;
import com.project.springbox.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FileServiceTests {

    @Mock private FileRepository fileRepository;
    private UserService userService;
    private FileService underTest;

    @BeforeEach
    void setUp() {
        underTest = new FileService(fileRepository, userService);
    }

    @Test
    void getAllFiles() {
        // When
        underTest.getAllFiles();
        // Then
        verify(fileRepository).findAll();
    }

}
