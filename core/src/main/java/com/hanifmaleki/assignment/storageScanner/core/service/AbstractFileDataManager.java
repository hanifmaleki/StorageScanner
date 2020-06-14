package com.hanifmaleki.assignment.storageScanner.core.service;

import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileDataRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.hanifmaleki.assignment.storageScanner.core.model.FileType.FOLDER;

public abstract class AbstractFileDataManager implements FileDataManager {

    private final FileDataRepository fileDataRepository;

    protected AbstractFileDataManager(FileDataRepository fileDataRepository) {
        this.fileDataRepository = fileDataRepository;
    }


    @Override
    public List<FileData> getFolders() {
        return fileDataRepository.findByFileType(FOLDER, Sort.by("fileName"));
    }


    @Override
    public List<FileData> getSortedFilesOfType(String type) {
        if (type == null || type.isEmpty()) {
            return fileDataRepository.findByFileType(FOLDER, Sort.by("fileSize"));
        }
        return fileDataRepository.findByFileTypeOrExtension(FOLDER, type, Sort.by("fileSize"));
    }

}
