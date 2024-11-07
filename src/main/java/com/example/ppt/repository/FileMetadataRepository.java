package com.example.ppt.repository;

import com.example.ppt.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Integer> {
    Optional<FileMetadata> findByFileName(String fileName);
}
