package com.hanifmaleki.assignment.storageScanner.core.service;

import com.hanifmaleki.assignment.storageScanner.core.model.FileData;

import java.util.List;

public interface FileDataManager {
    void getLatestFilesAndStore();

    List<FileData> getFolders();

    List<FileData> getSortedFilesOfType(String type);
}
