package com.hanifmaleki.assignment.storageScanner.core.service;

import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileRepository;

import java.util.List;

public interface FetchContentService {
    List<FileData> getContent();

    String getSearchPath();

    void setSearchPath(String searchPath);


    FileRepository getFileRepository();

    void setFileRepository(FileRepository fileRepository);
}
