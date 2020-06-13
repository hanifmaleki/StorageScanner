package com.hanifmaleki.assignment.storageScanner.core.service;

import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import com.hanifmaleki.assignment.storageScanner.core.model.TheInputPathIsIncorrectException;

import java.util.List;

public interface FileDataManager {
    void getLatestFilesAndStore() throws TheInputPathIsIncorrectException;

    List<FileData> getFolders();

    List<FileData> getSortedFilesOfType(String type);
}
