package com.project.springbox.repositories;

import com.project.springbox.data.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
    @Query(value = "SELECT * FROM file, user WHERE user_id = id AND name = :name", nativeQuery = true)
    List<File> findFilesByName(String name);

    @Query(value = "SELECT * FROM file WHERE file_id = :id", nativeQuery = true)
    List<File> findFilesById(int id);

    @Query(value = "SELECT file_id, name FROM file, user WHERE name = :name AND file_id = :id AND user_id = id", nativeQuery = true)
    String checkIfUserHasFile(int id, String name);

    @Query(value = "SELECT file_name FROM file, user WHERE name = :name AND file_name = :fileName AND user_id = id", nativeQuery = true)
    String checkIfUserHasFileByName(String fileName, String name);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM file WHERE file_id = :id", nativeQuery = true)
    void deleteFileById(int id);
}
