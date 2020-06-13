package com.hanifmaleki.assignment.storageScanner.core.service;

import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import com.hanifmaleki.assignment.storageScanner.core.model.TheInputPathIsIncorrectException;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileRepository;

public interface FetchContentService {
    FileData getContent() throws TheInputPathIsIncorrectException;

    String getSearchPath();

    void setSearchPath(String searchPath);


    FileRepository getFileRepository();

    void setFileRepository(FileRepository fileRepository);
}
