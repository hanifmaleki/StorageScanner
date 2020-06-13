package com.hanifmaleki.assignment.storageScanner.core.repository;

import com.hanifmaleki.assignment.storageScanner.core.model.FileData;

public interface CustomFileDataRepository {

    FileData saveHierarchy(FileData fileData);
}
