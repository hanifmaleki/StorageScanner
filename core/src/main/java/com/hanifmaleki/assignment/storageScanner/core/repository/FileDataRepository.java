package com.hanifmaleki.assignment.storageScanner.core.repository;

import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import com.hanifmaleki.assignment.storageScanner.core.model.FileType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, String>, CustomFileDataRepository {

    List<FileData> findByFileType(FileType fileType, Sort sort);

    List<FileData> findByFileTypeOrExtension(FileType fileType, String extension, Sort sort);

}
